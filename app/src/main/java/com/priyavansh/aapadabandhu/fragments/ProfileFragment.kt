package com.priyavansh.aapadabandhu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.priyavansh.aapadabandhu.databinding.FragmentProfileBinding
import com.priyavansh.aapadabandhu.models.User
import com.priyavansh.aapadabandhu.utils.USER_NODE

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
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

            }

        return binding.root
    }

    companion object {
    }
}