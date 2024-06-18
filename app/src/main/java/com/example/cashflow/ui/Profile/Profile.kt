package com.example.cashflow.ui.Profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cashflow.User
import com.example.cashflow.databinding.FragmentProfileBinding
import com.example.cashflow.ui.Search.SearchRecyclerAdapter
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileImageView: ImageView
    private lateinit var selectImageButton: Button
    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var user:User

    companion object {
        private const val REQUEST_IMAGE_PICKER = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileImageView = binding.profileImageView
        selectImageButton = binding.selectImageButton

        user = User( FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
        updateProfilePic(user,profileImageView)

        selectImageButton.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(REQUEST_IMAGE_PICKER)
        }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                uploadImageToFirebase(imageUri)
            }
        } else {
            Toast.makeText(requireContext(), "Image selection canceled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageReference = storage.reference.child("profile_images/${user.getUserPhoneNum()}")
        storageReference.putFile(imageUri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    saveImageUrlToFirestore(uri.toString())
                }
            } else {
                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageUrlToFirestore(imageUrl: String) {
        val phoneNum = user.getUserPhoneNum()
        val documentReference = firestore.collection("users").document(user.getUserPhoneNum())
        val updates = mapOf("profileImageUrl" to imageUrl)

        documentReference.update(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateProfilePic(user,profileImageView)
            } else {
                Toast.makeText(requireContext(), "Failed to update profile picture", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun updateProfilePic(user:User,imageView: ImageView){
        user.getProfileImageUrl {
            if (it != null) {
                Glide.with(this)
                    .load(it)
                    .into(imageView)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}