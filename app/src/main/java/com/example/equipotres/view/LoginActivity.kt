package com.example.equipotres.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.equipotres.R
import com.example.equipotres.databinding.ActivityLoginBinding
import com.example.equipotres.model.UserRequest
import com.example.equipotres.viewmodel.LoginViewModel
import androidx.core.content.ContextCompat
import com.google.android.play.integrity.internal.s
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        sesion()
        setup()
        viewModelObservers()

        // validación en tiempo real de la contraseña
        val passwordEditText = binding.etPass
        val passwordTextInputLayout = binding.tilPass

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()

                // Validación de longitud de la contraseña
                if (password.length < 6) {
                    passwordTextInputLayout.isErrorEnabled = true
                    passwordTextInputLayout.error = "Mínimo 6 dígitos"
                    passwordTextInputLayout.boxStrokeColor = ContextCompat.getColor(this@LoginActivity, R.color.red)
                } else if (password.length in 6..10) {
                    passwordTextInputLayout.isErrorEnabled = false
                    passwordTextInputLayout.boxStrokeColor = ContextCompat.getColor(this@LoginActivity, R.color.white)
                } else {
                    passwordTextInputLayout.isErrorEnabled = false
                    passwordTextInputLayout.boxStrokeColor = ContextCompat.getColor(this@LoginActivity, R.color.white)
                }
                // aqui se llama al metodo ValidateFields para habilitar o deshabilitar el boton
                validateFields()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding .etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateFields()
            }

            override fun beforeTextChanged(s: CharSequence? ,start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { val emailInput = s.toString().trim()


            }
        })
    }

    private fun viewModelObservers() {
        observerIsRegister()
    }

    private fun observerIsRegister() {
        loginViewModel.isRegister.observe(this) { userResponse ->
            if (userResponse.isRegister) {
                Toast.makeText(this, userResponse.message, Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putString("email", userResponse.email).apply()
                goToHome()
            } else {
                Toast.makeText(this, userResponse.message, Toast.LENGTH_SHORT).show()

                binding.tvRegister.isEnabled =true
                binding.tvRegister.setTextColor(ContextCompat.getColor(this , R.color.colorEnabledText))
            }
        }
    }

    private fun setup() {
        binding.tvRegister.setOnClickListener {
            registerUser()
            it.isEnabled = false
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
            //deshabilitar el boton para evitar multiples clics
            it.isEnabled = false
        }
        // el boton queda  inactivo hasta que se ingrese un email y una contraseña
        binding.btnLogin.isEnabled = false
        binding.btnLogin.setTextColor(ContextCompat.getColor(this, R.color.colorDisabledText))

        binding.tvRegister.isEnabled = false
        binding.tvRegister.setTextColor(ContextCompat.getColor(this, R.color.colorDisabledText))
    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        val userRequest = UserRequest(email, pass)

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            Log.d("RegisterUser", "Email: $email, Password: $pass")
            loginViewModel.registerUser(userRequest)
        } else {
            Toast.makeText(this, "Campos Vacíos", Toast.LENGTH_SHORT).show()
        }
        binding.tvRegister.isEnabled = true
        binding.tvRegister.setTextColor(ContextCompat.getColor(this, R.color.colorEnabledText))

    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        Log.d("LoginUser", "Email: $email, Password: $pass")
        loginViewModel.loginUser(email, pass) { isLogin ->
            if (isLogin) {
                // Guardar sesión activa
                sharedPreferences.edit().apply {
                    putString("email", email)
                    putBoolean("isLoggedIn", true)
                    apply()
                }
                goToHome()
            } else {
                Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
                binding.btnLogin.isEnabled = true
                binding.btnLogin.setTextColor(ContextCompat.getColor(this, R.color.colorEnabledText))
            }
        }
    }

    /*private fun sesion() {
        val email = sharedPreferences.getString("email", null)
        loginViewModel.sesion(email) { isEnableView ->
            if (isEnableView) {
                binding.clContenedor.visibility = View.INVISIBLE
                goToHome()
            }
        }
    }*/
    private fun sesion() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            goToHome() // Ir directamente a la pantalla principal si la sesión está activa
        }
    }
    private fun validateFields() {
        val email = binding.etEmail.text.toString().trim()
        val pass = binding.etPass.text.toString().trim()
        //el boton se activara si el email contiene un @ y la contraseña tiene almenos  de 6 caracteres
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = pass.isNotEmpty() && pass.length >= 6

        // El botón se habilitará si el email es válido y la contraseña tiene al menos 6 caracteres
        val isValid = isEmailValid && isPasswordValid

        // Habilitar o deshabilitar el botón de login y registrar
        binding.btnLogin.isEnabled = isValid
        binding.btnLogin.setTextColor(ContextCompat.getColor(this, if (isValid) R.color.colorEnabledText else R.color.colorDisabledText))

        binding.tvRegister.isEnabled = isValid
        binding.tvRegister.setTextColor(ContextCompat.getColor(this, if (isValid) R.color.colorEnabledText else R.color.colorDisabledText))

    }
}