package com.example.equipotres.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.equipotres.model.ApiPoke
import com.example.equipotres.repository.PokemonRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class PokemonsViewModelTest {

   @get:Rule
   val rule = InstantTaskExecutorRule()
    private lateinit var pokemonsViewModel: PokemonsViewModel
    private lateinit var pokemonRepository: PokemonRepository


    @Before
    fun setUp() {
        pokemonRepository = mock(PokemonRepository::class.java)
        pokemonsViewModel = PokemonsViewModel(pokemonRepository)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test getPokemons` () = runBlocking{
        Dispatchers.setMain(UnconfinedTestDispatcher())

        val mockPokemonRepository = mutableListOf(
            ApiPoke(1, "Pikachu"),

        )
        `when`(pokemonRepository.getPokemons()).thenReturn(mockPokemonRepository)

        pokemonsViewModel.getPokemons()


        assertEquals(mockPokemonRepository, pokemonsViewModel.pokemonsList.value)



    }
}