package com.priyavansh.aapadabandhu.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.priyavansh.aapadabandhu.ChatBotActivity
import com.priyavansh.aapadabandhu.MainActivity
import com.priyavansh.aapadabandhu.NewsViewModel
import com.priyavansh.aapadabandhu.NewsViewModelProviderFactory
import com.priyavansh.aapadabandhu.R
import com.priyavansh.aapadabandhu.ReportActivity
import com.priyavansh.aapadabandhu.adapters.NewsAdapter
import com.priyavansh.aapadabandhu.databinding.FragmentHomeBinding
import com.priyavansh.aapadabandhu.db.ArticleDatabase
import com.priyavansh.aapadabandhu.repository.NewsRepository
import com.priyavansh.aapadabandhu.utils.QUERY_PAGE_SIZE
import com.priyavansh.aapadabandhu.utils.Resource

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemHeadlinesError: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        binding.ChatbotButton.setOnClickListener {
            startActivity(Intent(requireContext(), ChatBotActivity::class.java))
        }
        binding.ReportButton.setOnClickListener {
            startActivity(Intent(requireContext(), ReportActivity::class.java))
        }

        binding.sos.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:01124363260")  // The number you want to dial
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel // Ensure this is called after onCreate in MainActivity

        setupHeadlinesRecyclerView()
        binding= FragmentHomeBinding.bind(view)

        itemHeadlinesError=view.findViewById(R.id.itemHeadlinesError)

        val inflater= requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View=inflater.inflate(R.layout.item_error,null)

        retryButton=view.findViewById(R.id.retryButton)
        errorText=view.findViewById(R.id.errorText)

        newsAdapter.setItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            if (article.url != null) {
                findNavController().navigate(R.id.nav_host_fragment_activity_main, bundle)
            } else {
                Toast.makeText(activity, "Article URL is missing!", Toast.LENGTH_SHORT).show()
            }
        }

        newsViewModel.headlines.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Sucess<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        // Filter articles to remove ones with null URLs or titles
                        val filteredArticles = newsResponse.articles.filter { it.url != null && it.title != null }
                        // Submit the filtered list to the adapter
                        newsAdapter.differ.submitList(filteredArticles.toList())

                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.headlinesPage == totalPages
                        if (isLastPage) {
                            binding.recyclerHeadlines.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }

                is Resource.Loading<*> -> {
                    showProgressBar()
                }
            }
        })


        retryButton.setOnClickListener {
            newsViewModel.getHeadlines("us")
        }
    }

    var isError=false
    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    private fun hideErrorMessage(){
        itemHeadlinesError.visibility=View.INVISIBLE
        isError=false
    }

    private fun showErrorMessage(message:String){
        itemHeadlinesError.visibility=View.VISIBLE
        errorText.text=message
        isError=true
    }

    val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors=!isError
            val isNotLoadingAndNotLastPage= !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible = totalItemCount>=QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotAtBeginning && isNotLoadingAndNotLastPage && isAtLastItem && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                newsViewModel.getHeadlines("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }
    }

    private fun setupHeadlinesRecyclerView(){
        newsAdapter=NewsAdapter()
        binding.recyclerHeadlines.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scrollListener)
        }
    }

    companion object {
    }
}