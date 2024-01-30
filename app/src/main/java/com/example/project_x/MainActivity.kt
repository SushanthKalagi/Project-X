package com.example.project_x

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.project_x.MainActivity.Companion.FILE_PICKER_REQUEST_CODE
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class Folder(val id: String = "", val name: String = "")
private fun retrieveFoldersFromFirestore(): Task<List<Folder>> {
    val db = Firebase.firestore
    val foldersCollection = db.collection("folders")
    return foldersCollection.get()
        .continueWith { task ->
            if (task.isSuccessful) {
                val result = task.result?.documents?.map { document ->
                    val folderId = document.id
                    val folderName = document.getString("name") ?: ""
                    Folder(folderId, folderName)
                }
                result ?: emptyList()
            } else {
                // Handle errors
                emptyList()
            }
        }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val container = findViewById<LinearLayout>(R.id.folderContainer)

        retrieveFoldersFromFirestore().addOnSuccessListener { folders ->
            folders.forEach { folder ->
                val folderView = ImageView(this)
                folderView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                folderView.setImageResource(R.drawable.baseline_folder_24)
                folderView.isClickable = true
                folderView.setOnClickListener { onFolderClick(folder) }

                container.addView(folderView)
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the selected file here
            val uri = data?.data
            // Perform file upload logic using the selected file URI
        }
    }

    companion object {
        const val FILE_PICKER_REQUEST_CODE = 123
    }
    private fun onFolderClick(folder: Folder) {
        // Handle folder click
        // You can use folder.id or folder.name as needed
        // Implement file picker logic for the selected folder

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"  // You can specify the desired file types here

        ActivityCompat.startActivityForResult(this, intent, 123, null )
    }
}

