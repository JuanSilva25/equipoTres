package com.example.equipotres.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.equipotres.model.UserRequest
import com.example.equipotres.model.UserResponse
import com.example.equipotres.repository.LoginRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.verify
import io.mockk.every
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel

    @Mock
    private lateinit var loginRepository: LoginRepository

    @Mock
    private lateinit var userResponseObserver: Observer<UserResponse>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        MockitoAnnotations.openMocks(this)
        loginRepository = mock(LoginRepository::class.java)
        loginViewModel = LoginViewModel(loginRepository)

    }

    @Test
    fun `registerUser should update isRegister LiveData with success response on successful registration`() {
        // Arrange: mock para un registro exitoso
        val userRequest = UserRequest(email = "test@example.com", password = "123456")
        val userResponse = UserResponse(email = "test@example.com", isRegister = true, message = "Registro Exitoso")

        // Mocking createUserWithEmailAndPassword para caso éxito
        every { loginRepository.registerUser(eq(userRequest), any()) } answers {
            val callback = it.invocation.args[1] as (UserResponse) -> Unit
            callback(userResponse)
        }

        // Act: Observar LiveData y llamamar método registerUser
        val observer = mockk<Observer<UserResponse>>(relaxed = true)
        loginViewModel.isRegister.observeForever(observer)

        loginViewModel.registerUser(userRequest)


        verify { observer.onChanged(userResponse) }
    }

    @Test
    fun `método loginUser `() {
        // Arrange
        val email = "test@example.com"
        val password = "wrongPassword"

        // Tarea mockeada
        val task = mockk<Task<AuthResult>>()
        every { task.isSuccessful } returns false

        // Mock FirebaseAuth
        val firebaseAuth = mockk<FirebaseAuth>()
        every { firebaseAuth.signInWithEmailAndPassword(eq(email), eq(password)) } returns task

        // Mock LoginRepository con el FirebaseAuth simulado
        val loginRepository = mockk<LoginRepository>()
        every { loginRepository.loginUser(eq(email), eq(password), any()) } answers {
            val callback = it.invocation.args[2] as (Boolean) -> Unit
            callback(false) // Simulamos que el login falló y llamamos al callback con 'false'
        }

        // ViewModel con el repository mockeado
        loginViewModel = LoginViewModel(loginRepository)

        // Act
        var loginResult: Boolean? = null
        loginViewModel.loginUser(email, password) { result ->
            loginResult = result
        }

        // Assert
        assertEquals(false, loginResult)
    }


    @Test
    fun `sesion should update isEnableView LiveData with true when email is not null`() {
        // Arrange
        val scenarios = listOf(
            "test@example.com" to true,  // Email no es null, debe retornar true
            null to false               //Email es null, retorna false
        )
        val isEnableView = mockk<(Boolean) -> Unit>(relaxed = true)


        scenarios.forEach { (email, expectedValue) ->
            loginViewModel.sesion(email, isEnableView)
            verify { isEnableView(expectedValue) }
        }


    }



}



