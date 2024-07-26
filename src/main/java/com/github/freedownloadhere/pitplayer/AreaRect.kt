package com.github.freedownloadhere.pitplayer

class AreaRect(
    var x1 : Double,
    var y1 : Double,
    var z1 : Double,
    var x2 : Double,
    var y2 : Double,
    var z2 : Double,
) {
    init {
        if(x1 > x2)  {
            val xswap = x1
            x1 = x2
            x2 = xswap
        }
        if(y1 > y2)  {
            val yswap = y1
            y1 = y2
            y2 = yswap
        }
        if(z1 > z2)  {
            val zswap = z1
            z1 = z2
            z2 = zswap
        }
    }
}