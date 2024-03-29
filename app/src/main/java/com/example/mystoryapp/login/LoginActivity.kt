package com.example.mystoryapp.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.login.LoginViewModel
import com.example.mystoryapp.MainActivity
import com.example.mystoryapp.R.string
import com.example.mystoryapp.remote.ApiResponse
import com.example.mystoryapp.remote.auth.LoginBody
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.login.LoginActivity.Companion.start
import com.example.mystoryapp.register.RegisterActivity
import com.example.mystoryapp.register.RegisterActivity.Companion.start
import com.example.mystoryapp.utils.ConstVal.KEY_EMAIL
import com.example.mystoryapp.utils.ConstVal.KEY_IS_LOGIN
import com.example.mystoryapp.utils.ConstVal.KEY_TOKEN
import com.example.mystoryapp.utils.ConstVal.KEY_USER_ID
import com.example.mystoryapp.utils.ConstVal.KEY_USER_NAME
import com.example.mystoryapp.utils.SessionManager
import com.example.mystoryapp.utils.ext.gone
import com.example.mystoryapp.utils.ext.show
import com.example.mystoryapp.utils.ext.showOKDialog
import com.example.mystoryapp.utils.ext.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable.start

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    private var _activityLoginBinding: ActivityLoginBinding? = null
    private val binding get() = _activityLoginBinding!!

    private lateinit var pref: SessionManager

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_activityLoginBinding?.root)

        pref = SessionManager(this)

        initAction()
    }

    private fun initAction() {
        binding.btnLogin.setOnClickListener {
            val userEmail = binding.edtEmail.text.toString()
            val userPassword = binding.edtPassword.text.toString()

            when {
                userEmail.isBlank() -> {
                    binding.edtEmail.requestFocus()
                    binding.edtEmail.error = getString(string.error_empty_password)
                }
                userPassword.isBlank() -> {
                    binding.edtPassword.requestFocus()
                    binding.edtPassword.error = getString(string.error_empty_password)
                }
                else -> {
                    val request = LoginBody(
                        userEmail, userPassword
                    )

                    loginUser(request, userEmail)
                }
            }
        }
        binding.tvToRegister.setOnClickListener {
            RegisterActivity.start(this)
        }
    }

    private fun loginUser(loginBody: LoginBody, email: String) {
        loginViewModel.loginUser(loginBody).observe(this) { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    showLoading(true)
                }
                is ApiResponse.Success -> {
                    try {
                        showLoading(false)
                        val userData = response.data.loginResult
                        pref.apply {
                            setStringPreference(KEY_USER_ID, userData.userId)
                            setStringPreference(KEY_TOKEN, userData.token)
                            setStringPreference(KEY_USER_NAME, userData.name)
                            setStringPreference(KEY_EMAIL, email)
                            setBooleanPreference(KEY_IS_LOGIN, true)
                        }
                    } finally {
                        MainActivity.start(this)
                    }
                }
                is ApiResponse.Error -> {
                    showLoading(false)
                    showOKDialog(getString(string.title_dialog_error), getString(string.message_incorrect_auth))
                }
                else -> {
                    showToast(getString(string.message_unknown_state))
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        if (isLoading) binding.bgDim.show() else binding.bgDim.gone()
        binding.edtEmail.isClickable = !isLoading
        binding.edtEmail.isEnabled = !isLoading
        binding.edtPassword.isClickable = !isLoading
        binding.edtPassword.isEnabled = !isLoading
        binding.btnLogin.isClickable = !isLoading
    }
}