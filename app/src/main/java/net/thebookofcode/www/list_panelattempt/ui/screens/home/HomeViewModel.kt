package net.thebookofcode.www.list_panelattempt.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.thebookofcode.www.list_panelattempt.R
import net.thebookofcode.www.list_panelattempt.room.entity.Word
import net.thebookofcode.www.list_panelattempt.room.repository.TheBOCRepository
import net.thebookofcode.www.list_panelattempt.util.ErrorMessage
import net.thebookofcode.www.list_panelattempt.util.cppListToWordList
import net.thebookofcode.www.list_panelattempt.util.javaListToWordList
import net.thebookofcode.www.list_panelattempt.util.pythonListToWordList
import java.util.*
import javax.inject.Inject
import net.thebookofcode.www.list_panelattempt.util.Result



/**
 * An internal representation of the Home route state, in a raw form
 */
private data class HomeViewModelState(
    val words: List<Word>? = null,
    val selectedLanguageCode: Int? = null,
    val selectedWordKey: String? = null, // TODO back selectedPostId in a SavedStateHandle
    val isDetailsOpen: Boolean = false,
    val isBookmarked: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val searchInput: String = "",
)
{

    /**
     * Converts this [HomeViewModelState] into a more strongly typed [HomeUiState] for driving
     * the ui.
     */
    fun toUiState(): HomeUiState =
        if (words == null) {
            HomeUiState.NoWords(
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        } else {
            HomeUiState.HasWords(
                words = words,
                // Determine the selected post. This will be the post the user last selected.
                // If there is none (or that post isn't in the current feed), default to the
                // highlighted post

                selectedWord = words.find {
                    it.key == selectedWordKey && it.code == selectedLanguageCode
                } ?: words[0],
                isDetailsOpen = isDetailsOpen,
                isBookmarked = isBookmarked,
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        }
}

/**
 * ViewModel that handles the business logic of the Home screen
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TheBOCRepository
) : ViewModel() {

    private val preSelectedLanguageCode: Int? = null
    private val preSelectedWordKey: String? = null
    private var words: Flow<List<Word>>? = null
    var isFavorite:Boolean? = null

    private val viewModelState = MutableStateFlow(
        HomeViewModelState(
            isLoading = true,
            selectedLanguageCode = preSelectedLanguageCode,
            selectedWordKey = preSelectedWordKey,
            isDetailsOpen = preSelectedWordKey != null
        )
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        words = getWords(1)
        refreshWords()
    }

    private fun getWords(preSelectedLanguageCode:Int):Flow<List<Word>> {
        var words: Flow<List<Word>>? = null
        when (preSelectedLanguageCode){
            1 ->{
                words = repository.getAllCppWords().map {
                    cppListToWordList(it)
                }
            }
            2 -> {
                words = repository.getAllPythonWords().map {
                    pythonListToWordList(it)
                }
            }
            3 -> {
                words = repository.getAllJavaWords().map {
                    javaListToWordList(it)
                }
            }
        }
        return words!!
    }

    fun ifInBookmark(word: Word) {
         viewModelScope.launch {
             isFavorite = repository.ifInBookmark(word.code, word.key)
        }
    }

    /**
     * Refresh posts and update the UI state accordingly
     */
    fun refreshWords() {
        // Ui state is refreshing
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = repository.getWords(preSelectedLanguageCode)
            viewModelState.update {
                when (result) {
                    is Result.Success -> it.copy(words = result.data, isLoading = false)
                    is Result.Error -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
        }
    }

    fun toggleBookmark(word: Word) {
        viewModelScope.launch {
            repository.toggleBookmark(word)
        }
    }

    /**
     * Selects the given article to view more information about it.
     */
    fun selectWord(selectedWord: Word) {
        // Treat selecting a detail as simply interacting with it
        interactedWithWordDetails(selectedWord)
    }

    /**
     * Notify that an error was displayed on the screen
     */
    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    /**
     * Notify that the user interacted with the feed
     */
    fun interactedWithList() {
        viewModelState.update {
            it.copy(isDetailsOpen = false)
        }
    }

    /**
     * Notify that the user interacted with the article details
     */
    fun interactedWithWordDetails(selectedWord: Word) {
        viewModelState.update {
            it.copy(
                selectedWordKey = selectedWord.key,
                selectedLanguageCode = selectedWord.code,
                isDetailsOpen = true
            )
        }
    }

    /**
     * Notify that the user updated the search query
     */
    fun onSearchInputChanged(searchInput: String) {
        viewModelState.update {
            it.copy(searchInput = searchInput)
        }
    }

}
