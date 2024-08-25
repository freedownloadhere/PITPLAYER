package com.github.freedownloadhere.pitplayer.utils

import net.minecraft.util.Vec3i
import kotlin.math.abs

object Bresenham {
    fun xz(curr : Vec3i, next : Vec3i) : ArrayList<Vec3i> {
        val l = arrayListOf<Vec3i>()
        val (x1, x2) = curr.x to next.x
        val (z1, z2) = curr.z to next.z
        var x = x1
        var z = z1
        var dX = abs(x2 - x1)
        var dZ = abs(z2 - z1)
        var swapped = false
        val sgX = if(x2 - x1 >= 0) 1 else -1
        val sgZ = if(z2 - z1 >= 0) 1 else -1
        if(dZ > dX) { val temp = dX; dX = dZ; dZ = temp; swapped = true }
        var e = 2 * dZ - dX
        val a = 2 * dZ
        val b = 2 * dZ - 2 * dX
        for(i in 1..dX) {
            if(e < 0) {
                if(swapped) z += sgZ
                else x += sgX
                e += a
            } else {
                x += sgX
                z += sgZ
                e += b
            }
            l.add(Vec3i(x, curr.y, z))
        }
        return l
    }
}