package com.capstone.urskripsi.ui.content.profile.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityChangePasswordBinding
import com.capstone.urskripsi.utils.Utility.hide
import com.capstone.urskripsi.utils.Utility.show
import com.capstone.urskripsi.utils.Utility.showToast
import com.capstone.urskripsi.utils.Utility.simpleToolbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        simpleToolbar(getString(R.string.change_password), binding.toolbar.root, true)

        mAuth = Firebase.auth

        changePassword()
    }

    private fun changePassword() {
        binding.apply {
            btnChangePassword.setOnClickListener {
                val oldPassword = edtOldPassword.text.toString().trim()
                val newPassword = edtNewPassword.text.toString().trim()
                val confirmPassword = edtConfirmNewPassword.text.toString().trim()

                when {
                    oldPassword.isEmpty() -> {
                        edtOldPassword.error = getString(R.string.password_empty)
                    }
                    newPassword.isEmpty() -> {
                        edtNewPassword.error = getString(R.string.password_empty)
                    }
                    confirmPassword.isEmpty() -> {
                        edtConfirmNewPassword.error = getString(R.string.password_empty)
                    }
                    else -> {
                        when (newPassword) {
                            confirmPassword -> {
                                val user = mAuth.currentUser
                                val userEmail = user?.email

                                if (user != null && userEmail != null) {
                                    val credential =
                                        EmailAuthProvider.getCredential(userEmail, oldPassword)

                                    user.reauthenticate(credential).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            binding.progressBarDialog.root.show()
                                            user.updatePassword(newPassword)
                                                .addOnCompleteListener { update ->
                                                    if (update.isSuccessful) {
                                                        binding.apply {
                                                            progressBarDialog.root.hide()
                                                            edtOldPassword.text.clear()
                                                            edtNewPassword.text.clear()
                                                            edtConfirmNewPassword.text.clear()
                                                            
                                                            edtOldPassword.clearFocus()
                                                            edtNewPassword.clearFocus()
                                                            edtConfirmNewPassword.clearFocus()
                                                        }
                                                        showToast(
                                                            getString(R.string.change_password_success),
                                                            this@ChangePasswordActivity
                                                        )
                                                    }
                                                }
                                        } else {
                                            binding.progressBarDialog.root.hide()
                                            showToast(
                                                getString(
                                                    R.string.change_password_failed,
                                                    task.exception
                                                ),
                                                this@ChangePasswordActivity
                                            )
                                        }
                                    }
                                }
                            }
                            else -> showToast(
                                getString(R.string.password_is_not_the_same),
                                this@ChangePasswordActivity
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}