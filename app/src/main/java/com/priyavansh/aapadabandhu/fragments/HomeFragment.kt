package com.priyavansh.aapadabandhu.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.priyavansh.aapadabandhu.ChatBotActivity
import com.priyavansh.aapadabandhu.R
import com.priyavansh.aapadabandhu.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
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
        return binding.root
    }

    companion object {
    }
}