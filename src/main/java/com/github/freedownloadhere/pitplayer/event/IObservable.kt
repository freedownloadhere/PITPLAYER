package com.github.freedownloadhere.pitplayer.event

interface IObservable {
    val observers : ArrayList<IObserver>
    fun dispatch(e : IEvent) { for(o in observers) o.receiveEvent(e) }
}