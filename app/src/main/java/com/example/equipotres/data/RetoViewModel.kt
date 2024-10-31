import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equipotres.repository.PokemonRepository
import kotlinx.coroutines.launch

class RetoViewModel(private val repository: PokemonRepository) : ViewModel() {

    private val _pokemonImageUrl = MutableLiveData<String?>() // Cambia a nullable
    val pokemonImageUrl: LiveData<String?> get() = _pokemonImageUrl // Cambia a LiveData<String?>

    init {
        loadPokemonImage()
    }

    private fun loadPokemonImage() {
        viewModelScope.launch {
            val imageUrl = repository.getRandomPokemonImage()
            _pokemonImageUrl.value = imageUrl // Esto puede ser null
        }
    }
}