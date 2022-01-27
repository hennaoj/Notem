package com.example.notem.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notem.data.entity.Reminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())

    val state: StateFlow<HomeViewState>
        get() = _state

    init {

        val list = mutableListOf(
            Reminder(1, Date(),"This is the first reminder!"),
            Reminder(2, Date(),"Finish exercise 1 by 6.2."),
            Reminder(3, Date(),"Call the dentist office"),
            Reminder(4, Date(),"Remember to do something you forgot about earlier!"),
            Reminder(5, Date(), "Lorem ipsum dolor sit amet, consectetur " +
                    "adipiscing elit. Nunc pharetra et est et ullamcorper. Proin sem lorem, vulputate" +
                    " eu venenatis at, lacinia ac dui. Sed sit amet mi ut est molestie consectetur " +
                    "ut a metus. Vestibulum faucibus tincidunt enim, et varius magna commodo sit amet." +
                    " Etiam vel consectetur sem, sit amet mollis diam."),
            Reminder(6, Date(), "Lorem ipsum dolor sit amet, consectetur " +
                    "adipiscing elit. Nunc pharetra et est et ullamcorper."),
            Reminder(7, Date(),"This is the seventh reminder!"),
            Reminder(6, Date(), "Lorem ipsum dolor sit amet, consectetur " +
                    "adipiscing elit. Nunc pharetra et est et ullamcorper."),
        )

        viewModelScope.launch {
            _state.value = HomeViewState(
                reminders = list
            )
        }
    }
}

data class HomeViewState(
    val reminders: List<Reminder> = emptyList()
)