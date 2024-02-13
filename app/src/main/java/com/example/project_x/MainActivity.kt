package com.example.project_x

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.project_x.R.color.white
//import com.google.android.gms.tasks.Task
//import com.google.firebase.Firebase
//import com.google.firebase.firestore.firestore
//import android.os.Parcelable
//import android.os.Parcel
//import com.example.project_x.fragments.FolderContentsAdapter
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage

data class Folder(val id: String = "", val name: String = ""): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Folder> {
        override fun createFromParcel(parcel: Parcel): Folder {
            return Folder(parcel)
        }

        override fun newArray(size: Int): Array<Folder?> {
            return arrayOfNulls(size)
        }
    }
}
/* private fun retrieveFoldersFromFirestore(): Task<List<Folder>> {
    val db = Firebase.firestore
    val foldersCollection = db.collection("Folders")

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
} */

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val container = findViewById<LinearLayout>(R.id.folderContainer)
//
//        retrieveFoldersFromFirestore().addOnSuccessListener { folders ->
//            folders.forEach { folder ->
//
//                displayFolder(folder, container)
//            }
//        }
//
//
//        var upload:ImageView = findViewById(R.id.uploadbtn)
//        upload.setOnClickListener{
//
//        }
        webView = findViewById(R.id.webview)
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()

        // this will load the url of the website
        webView.loadUrl("https://drive.google.com/drive/folders/16z2KT8-GPTuvjTSyeVaIkolnfcsfO8gm?usp=drive_link")

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)
    }

    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (webView.canGoBack())
            webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }


//    private fun retrieveFoldersFromFirestore(): Task<List<Folder>> {
//        // Implement logic to retrieve top-level folders from Firestore
//        val db = FirebaseFirestore.getInstance()
//        val foldersCollection = db.collection("Folders")
//
//        return foldersCollection.get()
//            .continueWith { task ->
//                if (task.isSuccessful) {
//                    val result = task.result?.documents?.map { document ->
//                        val folderId = document.id
//                        val folderName = document.getString("name") ?: ""
//                        Folder(folderId, folderName)
//                    }
//                    result ?: emptyList()
//                } else {
//                    // Handle errors
//                    emptyList()
//                }
//            }
//    }
//    private fun launchFilePicker() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "*/*"
//        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE)
//    }
//    private fun uploadFileToStorage(fileUri: Uri, folderId: String) {
//        val storageRef = FirebaseStorage.getInstance().reference
//        val fileName = fileUri.lastPathSegment ?: "file"
//        val fileRef = storageRef.child("files/$fileName")
//
//        val uploadTask = fileRef.putFile(fileUri)
//        uploadTask.continueWithTask { task ->
//            if (!task.isSuccessful) {
//                task.exception?.let {
//                    throw it
//                }
//            }
//            fileRef.downloadUrl
//        }.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val downloadUri = task.result
//                // Once upload is complete, store the file metadata in Firestore
//                storeFileMetadata(folderId, fileName, downloadUri.toString())
//            } else {
//                // Handle errors
//                Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//    private fun storeFileMetadata(folderId: String, fileName: String, fileUrl: String) {
//        val db = Firebase.firestore
//        val folderRef = db.collection("folders").document(folderId)
//        val filesCollection = folderRef.collection("files")
//
//        val fileData = hashMapOf(
//            "name" to fileName,
//            "url" to fileUrl
//        )
//
//        filesCollection.add(fileData)
//            .addOnSuccessListener { documentReference ->
//                // File metadata stored successfully
//                Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                // Handle errors
//                Toast.makeText(this, "Failed to store file metadata", Toast.LENGTH_SHORT).show()
//                Log.e("TAG", "Error adding file metadata", e)
//            }
//    }
//
//
//    private fun retrieveSubfoldersFromFirestore(parentFolder: Folder): Task<List<Folder>> {
//        // Implement logic to retrieve subfolders for the given parentFolder from Firestore
//        val db = FirebaseFirestore.getInstance()
//        val folderRef = db.collection("subFolders").document(parentFolder.id).collection("subfolders")
//
//        return folderRef.get()
//            .continueWith { task ->
//                if (task.isSuccessful) {
//                    val result = task.result?.documents?.map { document ->
//                        val subFolderId = document.id
//                        val subFolderName = document.getString("name") ?: ""
//                        Folder(subFolderId, subFolderName)
//                    }
//                    result ?: emptyList()
//                } else {
//                    // Handle errors
//                    emptyList()
//                }
//            }
//    }
//    private fun displayFolder(folder: Folder, container: LinearLayout) {
//        // Create a layout to hold the folder icon and text label
//        val folderLayout = LinearLayout(this)
//        folderLayout.orientation = LinearLayout.HORIZONTAL
//        folderLayout.gravity = Gravity.CENTER
//        folderLayout.layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//
//        // Create and configure the folder icon ImageView
//        val folderView = ImageView(this)
//
//        folderView.layoutParams = ViewGroup.LayoutParams(
//            200, 200
//        )
//        folderView.setImageResource(R.drawable.baseline_folder_24)
//        folderView.isClickable = true
//        folderView.setOnClickListener { onFolderClick(folder) }
//
//        // Create and configure the text label TextView
//        val textLabel = TextView(this)
//        textLabel.setTextColor(getColor(R.color.white))
//        textLabel.text = folder.name
//        textLabel.gravity = Gravity.CENTER
//
//        // Add folder icon and text label to the folder layout
//        folderLayout.addView(folderView)
//        folderLayout.addView(textLabel)
//
//        // Add the folder layout to the container layout
//        container.addView(folderLayout)
//
//        // Fetch subfolders from Firestore and display them recursively
//        retrieveSubfoldersFromFirestore(folder).addOnSuccessListener { subfolders ->
//            subfolders.forEach { subfolder ->
//                displayFolder(subfolder, container)
//            }
//        }
//    }
//
//
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            data?.data?.let { fileUri ->
//                var db = FirebaseFirestore.getInstance()
//                val foldersCollection = db.collection("Folders")
//
//                val folderId = foldersCollection.id
//
//                // Replace folderId with the ID of the folder where the file should be stored
//                uploadFileToStorage(fileUri, folderId)
//            }
//        }
//    }
//
//
//    companion object {
//        const val FILE_PICKER_REQUEST_CODE = 123
//    }
//    private fun onFolderClick(parentFolder: Folder) {
//        // Fetch subfolders of the selected folder from Firestore
//        Log.w("tag", "fucntion working")
//        retrieveSubfoldersFromFirestore(parentFolder).addOnSuccessListener { subFoldersList ->
//            // Find the instance of the FolderContentsFragment
//            Log.w("tag", "fucntion working34")
//            val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? FolderContentsFragment
//            fragment?.folderContentsAdapter?.setFolders(subFoldersList)
//        }.addOnFailureListener { exception ->
//            // Handle failure
//            Toast.makeText(this, "Failed to retrieve folder contents", Toast.LENGTH_SHORT).show()
//            Log.e("TAG", "Error getting folder contents", exception)
//        }
//    }

    /* private fun onFolderClick(parentFolder: Folder) {

        Log.w("tag", "fucntion working")
        // Fetch subfolders of the selected folder from Firestore
        retrieveSubfoldersFromFirestore(parentFolder).addOnSuccessListener { subFoldersList ->
            // Update the adapter with the selected folder's contents
            Log.w("tag", "fucntion working 123")
            FolderContentsAdapter.setFolders(subFoldersList)
        }.addOnFailureListener { exception ->
            // Handle failure
            Toast.makeText(this, "Failed to retrieve folder contents", Toast.LENGTH_SHORT).show()
            Log.e("TAG", "Error getting folder contents", exception)
        }
    }*/


    /*
    private fun fetchSubfolders(folderId: String) {
        val db = Firebase.firestore
        val folderRef = db.collection("folders").document(folderId)
        folderRef.collection("subFolders").get()
            .addOnSuccessListener { documents ->
                val subFoldersList = mutableListOf<Folder>()
                for (document in documents) {
                    val subFolderId = document.id
                    val subFolderName = document.getString("name") ?: ""
                    val subFolder = Folder(subFolderId, subFolderName)
                    subFoldersList.add(subFolder)
                    // Fetch the sub-subfolders recursively
                    fetchSubfolders(subFolderId)
                }
                // Replace the current fragment with a new fragment to display subfolders
                val fragment = FolderContentsFragment.newInstance(folderId, subFoldersList)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)  // Add the fragment to the back stack
                    .commit()
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Toast.makeText(this, "Failed to retrieve subfolders", Toast.LENGTH_SHORT).show()
                Log.e("tag", "Error getting subfolders", exception)
            }
    }*/
}




