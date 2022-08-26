package com.profile.whatsapp.ui.authentication

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.profile.whatsapp.MainActivity
import com.profile.whatsapp.R
import com.profile.whatsapp.databinding.ActivitySignUpBinding
import com.profile.whatsapp.model.UserModel
import com.profile.whatsapp.ui.base.BaseActivity

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dataBase: FirebaseDatabase
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        initBind()
        setData()
        initObserver()
        initListener()
        binding.executePendingBindings()
    }

    private fun initBind() {
        supportActionBar?.hide()
        auth = Firebase.auth
        dataBase = FirebaseDatabase.getInstance()
        progressDialog = ProgressDialog(this)
    }

    private fun setData() {
        progressDialog.setTitle("Creating Account")
        progressDialog.setMessage("We're creating your account")

    }

    private fun initObserver() {
    }

    private fun initListener() {
        binding.btnSignUp.setOnClickListener {
            if (binding.etUserName.text.toString().isNullOrEmpty()){
                binding.etUserName.error=resources.getString(R.string.enter_your_name)
                return@setOnClickListener
            }
            if (binding.etEmail.text.toString().isNullOrEmpty()){
                binding.etEmail.error="Enter Email"
                return@setOnClickListener
            }
            if (binding.etPassword.text.toString().isNullOrEmpty()){
                binding.etPassword.error="Enter Password"
                return@setOnClickListener
            }
            progressDialog.show()
            auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(),
                binding.etPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        val signUpDetails = UserModel(
                            binding.etUserName.text.toString(),
                            binding.etEmail.text.toString(),
                            binding.etPassword.text.toString(),
                        )
                        val user = auth.currentUser
                        dataBase.reference.child("Users").child(user!!.uid).setValue(signUpDetails)
                        openActivity(MainActivity::class.java)

                    } else {
                        Toast.makeText(this, "" + task.exception?.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.tvLogin.setOnClickListener {
            openActivity(LoginActivity::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            openActivity(MainActivity::class.java)
        }
    }

}