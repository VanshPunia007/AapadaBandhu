package com.priyavansh.aapadabandhu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.priyavansh.aapadabandhu.adapters.ReportAdapter
import com.priyavansh.aapadabandhu.databinding.FragmentProfileBinding
import com.priyavansh.aapadabandhu.models.Report
import com.priyavansh.aapadabandhu.models.User
import com.priyavansh.aapadabandhu.utils.REPORTS
import com.priyavansh.aapadabandhu.utils.USER_NODE

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var reportList = ArrayList<Report>()
    lateinit var reportAdapter : ReportAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        Firebase.firestore.collection(USER_NODE)
            .document(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                val user =it.toObject<User>()!!
                binding.name.text = user.name
                binding.emailId.text = user.email
                Glide.with(this@ProfileFragment)
                    .load(user.image)
                    .circleCrop()
                    .into(binding.profileImage)
            }

        reportAdapter = ReportAdapter(requireContext(), reportList)
        binding.reportRv.layoutManager = LinearLayoutManager(requireContext())
        binding.reportRv.adapter = reportAdapter

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
            .collection(REPORTS).get().addOnSuccessListener {
                var tempList = ArrayList<Report>()
                reportList.clear()
                for (i in it.documents) {
                    var report: Report = i.toObject<Report>()!!
                    tempList.add(report)
                }
                reportList.addAll(tempList)
                reportAdapter.notifyDataSetChanged()
            }

        return binding.root
    }

    companion object {
    }
}