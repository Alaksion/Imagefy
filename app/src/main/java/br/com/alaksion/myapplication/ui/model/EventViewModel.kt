package br.com.alaksion.myapplication.ui.model

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class EventViewModel<Event : ViewModelEvent> : BaseViewModel() {

    private val _eventHandler = Channel<Event> { Channel.BUFFERED }
    val eventHandler = _eventHandler.receiveAsFlow()

    protected open suspend fun sendEvent(event: Event) {
        _eventHandler.send(event)
    }

}