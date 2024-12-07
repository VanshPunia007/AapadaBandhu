package com.priyavansh.aapadabandhu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.priyavansh.aapadabandhu.R
import com.priyavansh.aapadabandhu.databinding.UserReportBinding
import com.priyavansh.aapadabandhu.models.Report
import com.priyavansh.aapadabandhu.utils.USER_NODE

class ReportAdapter(var context: Context, var reportList: ArrayList<Report>):
    RecyclerView.Adapter<ReportAdapter.MyHolder>()
{
    inner class MyHolder(var binding: UserReportBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = UserReportBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val report = reportList[position]

        // Binding data to the views
        holder.binding.apply {
             date.text = "07/12/2024"  // Assuming `report.time` contains the formatted date string
            try {
                val text = TimeAgo.using(report.time!!.toLong())
                time.text = text

            }catch (e : Exception){
                time.text = "0 hrs"
            }
            location.text = "${report.location} (${String.format("%.3f", report.lat)} , ${String.format("%.3f", report.long)})"
            description.text = report.description

            // Set an image if provided, else use a placeholder
            if (!report.image.isNullOrEmpty()) {
                Glide.with(context)
                    .load(report.image)
                    .placeholder(R.drawable.flood) // Replace with your placeholder image
                    .into(imageView3)
            } else {
                imageView3.setImageResource(R.drawable.earthquake) // Replace with your placeholder image
            }

            // Optional: Add click listener for the card if needed
            root.setOnClickListener {
                Toast.makeText(context, "Clicked on ${report.location}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}