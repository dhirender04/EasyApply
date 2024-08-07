package com.example.easyapply.ui.mailtempalte.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.easyapply.R
import com.example.easyapply.databinding.ItemApplicationListBinding

class ApplicationListAdapter(val onClick:(Int)->Unit) :
    RecyclerView.Adapter<ApplicationListAdapter.ApplicationListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationListViewHolder {
        val binding = DataBindingUtil.inflate<ItemApplicationListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_application_list,
            parent,
            false
        )
        return ApplicationListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10

    }

    override fun onBindViewHolder(holder: ApplicationListViewHolder, position: Int) {
        holder.binding.apply {
            mainContainer.setOnClickListener {
                onClick(position)
            }
        }
    }

    inner class ApplicationListViewHolder(val binding: ItemApplicationListBinding) :
        RecyclerView.ViewHolder(binding.root)

}