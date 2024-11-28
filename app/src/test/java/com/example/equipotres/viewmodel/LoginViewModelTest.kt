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
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.mockk.every
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
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
        // Arrange: Configuramos el comportamiento del mock para un registro exitoso
        val userRequest = UserRequest(email = "test@example.com", password = "123456")
        val userResponse = UserResponse(email = "test@example.com", isRegister = true, message = "Registro Exitoso")

        // Mocking la llamada a createUserWithEmailAndPassword para simular éxito
        every { loginRepository.registerUser(eq(userRequest), any()) } answers {
            val callback = it.invocation.args[1] as (UserResponse) -> Unit
            callback(userResponse) // Simulamos la respuesta de éxito
        }

        // Act: Observamos el LiveData y llamamos al método registerUser
        val observer = mockk<Observer<UserResponse>>(relaxed = true)
        loginViewModel.isRegister.observeForever(observer)

        loginViewModel.registerUser(userRequest)

        // Assert: Verificamos que el LiveData se actualice correctamente
        verify { observer.onChanged(userResponse) } // Verifica que el observer haya sido notificado con la respuesta esperada
    }

    @Test
    fun `loginUser should call isLoginResponse with false on failed login`() {
        // Arrange
        val email = "test@example.com"
        val password = "wrongPassword"

        // Tarea mockeada (Task)
        val task = mockk<Task<AuthResult>>()
        every { task.isSuccessful } returns false

        // Mockeamos FirebaseAuth
        val firebaseAuth = mockk<FirebaseAuth>()
        every { firebaseAuth.signInWithEmailAndPassword(eq(email), eq(password)) } returns task

        // Mockeamos el LoginRepository con el FirebaseAuth simulado
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



}



