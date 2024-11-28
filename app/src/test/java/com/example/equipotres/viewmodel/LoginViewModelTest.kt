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
        MockitoAnnotations.openMocks(this)
        loginRepository = mock(LoginRepository::class.java)
        loginViewModel = LoginViewModel(loginRepository)

    }

    @Test
    fun `registerUser should update isRegister LiveData when registration is successful`() {
        // Arrange: Datos de entrada
        val email = "test@example.com"
        val password = "123456"
        val userRequest = UserRequest(email, password)

        // Creamos la respuesta esperada
        val expectedResponse = UserResponse(
            email = email,
            isRegister = true,
            message = "Registro Exitoso"
        )

        // Simulamos el comportamiento del repository para la respuesta exitosa
        every { loginRepository.registerUser(any(), any()) } answers {
            val callback = arg<(UserResponse) -> Unit>(1)
            callback(expectedResponse) // Llamamos al callback con la respuesta exitosa
        }

        // Act: Llamamos al método registerUser en el ViewModel
        loginViewModel.registerUser(userRequest)

        // Assert: Verificamos que el LiveData isRegister haya sido actualizado correctamente
        verify { userResponseObserver.onChanged(expectedResponse) }
    }

    @Test
    fun `loginUser  should call isLoginResponse with false on failed login`() {
        // Arrange
        val email = "test@example.com"
        val password = "wrongPassword"

        // Simulando el comportamiento de FirebaseAuth
        val authResultSlot = slot<(Task<AuthResult>) -> Unit>()
        val firebaseAuth = mockk<FirebaseAuth>()

        // Creamos una tarea mockeada (Task)
        val task = mockk<Task<AuthResult>>()
        every { task.isSuccessful } returns false

        // Simulamos el comportamiento de `signInWithEmailAndPassword`
        every { firebaseAuth.signInWithEmailAndPassword(eq(email), eq(password)) } returns task

        // Creamos un ViewModel mockeado con este FirebaseAuth simulado
        loginViewModel = LoginViewModel(loginRepository)

        // Act: Llamamos al método `loginUser` y verificamos la respuesta
        var loginResult: Boolean? = null
        loginViewModel.loginUser(email, password) { result ->
            loginResult = result
        }

        // Assert: Verificamos que el resultado sea falso
        assertEquals(false, loginResult)
    }


}



