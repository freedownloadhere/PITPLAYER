package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.util.*

object Pathfinder {
    private enum class Directions(val vec : Vec3i, val cost : Int) {
        PX(Vec3i(1, 0, 0), 10),
        PZ(Vec3i(0, 0, 1), 10),
        NX(Vec3i(-1, 0, 0), 10),
        NZ(Vec3i(0, 0, -1), 10),

        DiagPXPZ(Vec3i(1, 0, 1), 14),
        DiagPXNZ(Vec3i(1, 0, -1), 14),
        DiagNXPZ(Vec3i(-1, 0, 1), 14),
        DiagNXNZ(Vec3i(-1, 0, -1), 14),

        UpPX(Vec3i(1, 1, 0), 20),
        UpPZ(Vec3i(0, 1, 1), 20),
        UpNX(Vec3i(-1, 1, 0), 20),
        UpNZ(Vec3i(0, 1, -1), 20),

        DownPX(Vec3i(1, -1, 0), 14),
        DownPZ(Vec3i(0, -1, 1), 14),
        DownNX(Vec3i(-1, -1, 0), 14),
        DownNZ(Vec3i(0, -1, -1), 14),

        DiagDownPXPZ(Vec3i(1, -1, 1), 18),
        DiagDownPXNZ(Vec3i(1, -1, -1), 18),
        DiagDownNXPZ(Vec3i(-1, -1, 1), 18),
        DiagDownNXNZ(Vec3i(-1, -1, -1), 18),
    }

    private fun blockIsSolid(pos : Vec3i) : Boolean {
        val block = world.getBlockState(BlockPos(pos.x, pos.y, pos.z)).block.material
        return block.isSolid && !block.isLiquid
    }

    private fun isObstructed(pos : Vec3i) : Boolean {
        return blockIsSolid(pos.upOne) || blockIsSolid(pos.upTwo)
    }

    private fun isNotObstructed(pos : Vec3i) : Boolean {
        return !isObstructed(pos)
    }

    private fun isWalkable(pos : Vec3i) : Boolean {
        return blockIsSolid(pos) && isNotObstructed(pos)
    }

    private fun isNotWalkable(pos : Vec3i) : Boolean {
        return !isWalkable(pos)
    }

    private fun validPathingBlock(curr : Vec3i, next : Vec3i) : Boolean {
        if(isNotWalkable(next)) return false
        if(next.x - curr.x != 0 && next.z - curr.z != 0)
            if(isObstructed(Vec3i(curr.x, curr.y, next.z)) || isObstructed(Vec3i(next.x, curr.y, curr.z)))
                return false
        if(next.y == curr.y + 1 && blockIsSolid(curr.upThree)) return false
        if(next.y == curr.y - 1 && blockIsSolid(next.upThree)) return false
        return true
    }

    private data class Node(val pos : Vec3i, val g : Int = 0, val h : Int = 0, val next : Node? = null) {
        val f : Int
            get() = g + h
    }

    private fun makePath(n : Node) : MutableList<Vec3> {
        val l = mutableListOf<Vec3>()
        var c : Node? = n
        while(c != null) {
            l.add(c.pos.toVec3().add(Vec3(0.5, 1.0, 0.5)))
            c = c.next
        }
        return l
    }

    fun pathfind(dest : Vec3i, start : Vec3i) : MutableList<Vec3>? {
        val nah = mutableSetOf<Vec3i>()
        val yea = PriorityQueue {
            n1 : Node, n2 : Node -> Int
            if(n1.f == n2.f) n1.h - n2.h else n1.f - n2.f
        }
        yea.add(Node(start))
        while(yea.isNotEmpty()) {
            val c = yea.remove()
            if(nah.contains(c.pos)) continue
            nah.add(c.pos)
            if(c.pos.matches(dest)) { return makePath(c) }
            for(d in Directions.entries) {
                val npos = c.pos.add(d.vec)
                if(nah.contains(npos)) continue
                if(!validPathingBlock(c.pos, npos)) continue
                val n = Node(npos, c.g + d.cost, manhattan(npos, dest), c)
                yea.add(n)
            }
        }
        return null
    }

}