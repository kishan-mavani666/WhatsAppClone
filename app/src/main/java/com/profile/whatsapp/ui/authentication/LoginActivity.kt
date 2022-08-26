package com.profile.whatsapp.ui.authentication

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.profile.whatsapp.MainActivity
import com.profile.whatsapp.R
import com.profile.whatsapp.databinding.ActivityLoginBinding
import com.profile.whatsapp.model.UserModel
import com.profile.whatsapp.ui.base.BaseActivity

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog
    private val RC_SIGN_IN = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initBind()
        setData()
        initObserver()
        initListener()
        binding.executePendingBindings()
    }

    private fun initBind() {
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Login")
        progressDialog.setMessage("Login to your account")
    }

    private fun setData() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun initObserver() {

    }

    private fun initListener() {
        binding.btnSignIn.setOnClickListener {
            progressDialog.show()
            auth.signInWithEmailAndPassword(binding.etEmail.text.toString(),
                binding.etPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        openActivity(MainActivity::class.java)

                    } else {
                        Toast.makeText(this, "" + task.exception?.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
        }
        binding.tvSignUp.setOnClickListener { openActivity(SignUpActivity::class.java) }
        binding.btnGoogle.setOnClickListener {
            progressDialog.show()
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    if (task.isSuccessful) {
                        val account = task.result
                        if (account != null) {
                            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                            auth.signInWithCredential(credential)
                                .addOnCompleteListener { task ->
                                    progressDialog.dismiss()
                                    if (task.isSuccessful) {
                                        val currentUser = auth.currentUser
                                        val userModel =UserModel()
                                        userModel.userId=currentUser?.uid
                                        userModel.userName=currentUser?.displayName
                                        userModel.userProfile=currentUser?.photoUrl.toString()
                                        database.reference.child("Users").child(currentUser!!.uid)
                                            .setValue(userModel)
                                        openActivity(MainActivity::class.java)
                                    } else {
                                        showToast(task.exception?.message.toString())
                                    }
                                }
                        }


                    } else {
                        showToast(task.exception?.message.toString())
                    }

                } catch (e: ApiException) {
                }
            }
        }
    }
}