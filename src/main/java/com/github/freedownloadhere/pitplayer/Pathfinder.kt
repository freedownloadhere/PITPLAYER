package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

object Pathfinder {
    private data class Node(val pos : Vec3i, val g : Double = 0.0, val h : Double = 0.0, val next : Node? = null) {
        val f : Double
            get() = g + h
    }

    private enum class Directions(val vec : Vec3i, val cost : Double) {
        PX(Vec3i(1, 0, 0), 1.0),
        PZ(Vec3i(0, 0, 1), 1.0),
        NX(Vec3i(-1, 0, 0), 1.0),
        NZ(Vec3i(0, 0, -1), 1.0),

        DiagPXPZ(Vec3i(1, 0, 1), 1.4),
        DiagPXNZ(Vec3i(1, 0, -1), 1.4),
        DiagNXPZ(Vec3i(-1, 0, 1), 1.4),
        DiagNXNZ(Vec3i(-1, 0, -1), 1.4),

        DownPX(Vec3i(1, -1, 0), 1.4),
        DownPZ(Vec3i(0, -1, 1), 1.4),
        DownNX(Vec3i(-1, -1, 0), 1.4),
        DownNZ(Vec3i(0, -1, -1), 1.4),

        DiagDownPXPZ(Vec3i(1, -1, 1), 1.8),
        DiagDownPXNZ(Vec3i(1, -1, -1), 1.8),
        DiagDownNXPZ(Vec3i(-1, -1, 1), 1.8),
        DiagDownNXNZ(Vec3i(-1, -1, -1), 1.8),

        UpPX(Vec3i(1, 1, 0), 2.0),
        UpPZ(Vec3i(0, 1, 1), 2.0),
        UpNX(Vec3i(-1, 1, 0), 2.0),
        UpNZ(Vec3i(0, 1, -1), 2.0),
    }

    private fun isValid(c : Vec3i, n : Vec3i) : Boolean {
        if(!world.isWalkable(n)) return false
        if(n.x - c.x != 0 && n.z - c.z != 0)
            if(world.isObstructed(Vec3i(c.x, c.y, n.z)) || world.isObstructed(Vec3i(n.x, c.y, c.z)))
                return false
        if(n.y == c.y + 1 && world.isSolid(c.upThree)) return false
        if(n.y == c.y - 1 && world.isSolid(n.upThree)) return false
        return true
    }

    var blockLine : MutableList<Vec3i>? = null
    fun makeBlockLine(dest : Vec3i?, start : Vec3i?) {
        if(dest == null || start == null) {
            blockLine = null
            return
        }
        val l = mutableListOf<Vec3i>()
        val m = (dest.z - start.z).toDouble() / (dest.x - start.x)
        val n = start.z - m * start.x
        val f = { x : Int -> floor(m * x + n).toInt() }
        val z1 = min(start.z, dest.z)
        val z2 = max(start.z, dest.z)
        val xrange = if(start.x <= dest.x) start.x..dest.x else start.x downTo dest.x
        for(x in xrange) {
            val zrange =
                if(start.z <= dest.z) {
                    if(start.x <= dest.x) max(z1, f(x))..min(z2, f(x + 1))
                    else max(z1, f(x + 1))..min(z2, f(x))
                } else {
                    if(start.x <= dest.x) min(z2, f(x)) downTo max(z1, f(x + 1))
                    else max(z1, f(x + 1)) downTo min(z2, f(x))
                }
            for (z in zrange) {
                val block = Vec3i(x, start.y, z).upOne.firstBlockBelow
                if(block == null) { blockLine = null; return }
                l.add(block)
            }
        }
        blockLine = l
    }

    private fun canTraverseLine(dest : Vec3i, start : Vec3i) : Boolean {
        makeBlockLine(dest, start)
        if(blockLine == null) return false
        for(i in 0..(blockLine!!.size - 2))
            if(!isValid(blockLine!![i], blockLine!![i + 1]))
                return false
        return true
    }

    private fun makeSimple(n : Node) : MutableList<Vec3> {
        val l = mutableListOf<Vec3i>()
        val l2 = mutableListOf<Vec3>()
        var c : Node? = n
        var delta1 = Vec3i(0, 0, 0)
        while(c?.next != null) {
            val delta2 = c.pos.subtract(c.next!!.pos)
            if(delta1.matches(delta2)) { c = c.next; continue }
            delta1 = delta2
            l.add(c.pos)
            c = c.next
        }
        var i = 0
        var j = 1
        l2.add(l.first().toVec3().toBlockTop())
        while(j < l.size) {
            if(!canTraverseLine(l[i], l[j])) {
                j--
                l2.add(l[j].toVec3().toBlockTop())
                i = j
            }
            j++
        }
        l2.add(l.last().toVec3().toBlockTop())
        return l2
    }

    private fun MutableMap<Vec3i, Node>.bestNode() : Node {
        var best : Node? = null
        var bestF = Double.MAX_VALUE
        var bestH = Double.MAX_VALUE
        for(node in values) {
            if(node.f < bestF || (node.f == bestF && node.h < bestH)) {
                best = node
                bestF = node.f
                bestH = node.h
            }
        }
        remove(best!!.pos)
        return best
    }

    fun pathfind(dest : Vec3i?, start : Vec3i?) : MutableList<Vec3>? {
        if(dest == null || start == null) return null
        val nah = mutableSetOf<Vec3i>()
        val yea = mutableMapOf<Vec3i, Node>()
        yea[start] = Node(start)
        while(yea.isNotEmpty()) {
            val c = yea.bestNode()
            if(nah.contains(c.pos)) continue
            nah.add(c.pos)
            if(c.pos.matches(dest)) { return makeSimple(c) }
            for(d in Directions.entries) {
                val npos = c.pos.add(d.vec)
                if(nah.contains(npos)) continue
                if(!isValid(c.pos, npos)) continue
                val ng = c.g + d.cost
                if(yea.contains(npos) && ng >= yea[npos]!!.g) continue
                yea[npos] = Node(npos, ng, npos.distance(dest), c)
            }
        }
        return null
    }
}