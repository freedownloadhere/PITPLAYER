package com.github.freedownloadhere.pitplayer.event

interface IObserver {
    fun receiveEvent(e : IEvent)
}