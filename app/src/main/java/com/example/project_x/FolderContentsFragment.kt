package com.example.project_x

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_x.fragments.FolderContentsAdapter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FolderContentsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var folderContentsAdapter: FolderContentsAdapter
    private lateinit var folderContentsList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_folder_contents, container, false)
        folderContentsList = view.findViewById(R.id.recycler_view_folders)
        folderContentsAdapter = FolderContentsAdapter()
        folderContentsList.adapter = folderContentsAdapter
        folderContentsList.layoutManager = LinearLayoutManager(activity)
        return view
    }



    companion object {
        fun newInstance(param1: String, param2: List<Folder>): FolderContentsFragment {
            val fragment = FolderContentsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putParcelableArrayList(ARG_PARAM2, ArrayList(param2))
            fragment.arguments = args
            return fragment
        }
    }
}