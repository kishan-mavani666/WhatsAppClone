package com.profile.whatsapp.ui.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.profile.whatsapp.R
import com.profile.whatsapp.databinding.ActivitySettingsBinding
import com.profile.whatsapp.model.UserModel
import com.profile.whatsapp.ui.base.BaseActivity
import java.util.*
import kotlin.collections.HashMap

class SettingsActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage:FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        initBind()
        setData()
        initObserver()
        initListener()
        binding.executePendingBindings()
    }

    private fun initBind() {
        supportActionBar?.hide()
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        storage=FirebaseStorage.getInstance()
    }

    private fun setData() {
        database.reference.child("Users").child(auth.uid.toString())
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userModel=snapshot.getValue(UserModel::class.java)
                    Glide
                        .with(this@SettingsActivity)
                        .load(userModel?.userProfile)
                        .centerCrop()
                        .placeholder(R.drawable.img_user_profile)
                        .into(binding.profileImage)

                    binding.etUserName.setText(userModel?.userName.toString())
                    binding.etAbout.setText(userModel?.about.toString())
                }


                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun initObserver() {

    }

    private fun initListener() {
        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.ivPlus.setOnClickListener {
            val intent=Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,201)
        }
        binding.btnSave.setOnClickListener {
            val userName=binding.etUserName.text.toString()
            val about=binding.etAbout.text.toString()

            val hashMap=HashMap<String,Any>()
            hashMap["userName"] = userName
            hashMap["about"]=about
            database.reference.child("Users").child(auth.uid.toString())
                .updateChildren(hashMap)
            showToast(resources.getString(R.string.profile_updated))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.data!=null){
            when(requestCode){
                201 ->{
                    val selectedImage=data.data
                    binding.profileImage.setImageURI(selectedImage)
                    showToast(getString(R.string.profile_updated))

                    val storageReference=storage.reference.child("ProfilePic").child(auth.uid.toString())
                    storageReference.putFile(selectedImage!!)
                        .addOnSuccessListener { onSuccess ->
                            storageReference.downloadUrl.addOnSuccessListener { uri ->
                                database.reference.child("Users").child(auth.uid!!).child("userProfile")
                                    .setValue(uri.toString())
                            }

                        }
                }
            }
        }
    }
}