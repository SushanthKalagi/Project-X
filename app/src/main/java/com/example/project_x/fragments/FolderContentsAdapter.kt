package com.example.project_x.fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_x.Folder
import com.example.project_x.R

class FolderContentsAdapter : RecyclerView.Adapter<FolderContentsAdapter.FolderViewHolder>() {
    private var folders: List<Folder> = listOf()

    inner class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text_folder_content)

        fun bind(folder: Folder) {
            textView.text = folder.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_folder_content, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(folders[position])
    }

    override fun getItemCount(): Int {
        return folders.size
    }
    fun setFolders(folders: List<Folder>) {
        this.folders = folders

    }
    

    companion object {

        private var folders: List<Folder> = listOf()

        fun setFolders(folders: List<Folder>) {
            this.folders = folders

        }
    }
}
