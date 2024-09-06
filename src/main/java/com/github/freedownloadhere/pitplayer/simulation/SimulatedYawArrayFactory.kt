package com.github.freedownloadhere.pitplayer.simulation

object SimulatedYawArrayFactory {
    fun make(initialYaw : Float, size : Int = 8) : ArrayList<SimulatedYaw> {
        val arr = arrayListOf<SimulatedYaw>()
        val increment = 360.0f / size
        arr.add(SimulatedYaw(initialYaw, 0.0))
        for(i in 1 ..< size / 2)
           arr.add(SimulatedYaw(arr.last().yaw + increment, i.toDouble()))
        for(i in size / 2 ..< size)
            arr.add(SimulatedYaw(arr.last().yaw + increment, (size - i).toDouble()))
        return arr
    }
}