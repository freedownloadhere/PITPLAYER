package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.util.*

object Pathfinder {
    private data class DirVec3i(val vec : Vec3i, val cost : Int)
    private data class PathData(var g : Int, var h : Int) {
        var connection : Vec3i? = null
        val f : Int
            get() = g + h
    }
    private enum class Directions(val v : DirVec3i) {
        PX(DirVec3i(Vec3i(1, 0, 0), 10)),
        PZ(DirVec3i(Vec3i(0, 0, 1), 10)),
        NX(DirVec3i(Vec3i(-1, 0, 0), 10)),
        NZ(DirVec3i(Vec3i(0, 0, -1), 10)),

        DiagPXPZ(DirVec3i(Vec3i(1, 0, 1), 14)),
        DiagPXNZ(DirVec3i(Vec3i(1, 0, -1), 14)),
        DiagNXPZ(DirVec3i(Vec3i(-1, 0, 1), 14)),
        DiagNXNZ(DirVec3i(Vec3i(-1, 0, -1), 14)),

        UpPX(DirVec3i(Vec3i(1, 1, 0), 20)),
        UpPZ(DirVec3i(Vec3i(0, 1, 1), 20)),
        UpNX(DirVec3i(Vec3i(-1, 1, 0), 20)),
        UpNZ(DirVec3i(Vec3i(0, 1, -1), 20)),

        DownPX(DirVec3i(Vec3i(1, -1, 0), 14)),
        DownPZ(DirVec3i(Vec3i(0, -1, 1), 14)),
        DownNX(DirVec3i(Vec3i(-1, -1, 0), 14)),
        DownNZ(DirVec3i(Vec3i(0, -1, -1), 14)),

        DiagDownPXPZ(DirVec3i(Vec3i(1, -1, 1), 18)),
        DiagDownPXNZ(DirVec3i(Vec3i(1, -1, -1), 18)),
        DiagDownNXPZ(DirVec3i(Vec3i(-1, -1, 1), 18)),
        DiagDownNXNZ(DirVec3i(Vec3i(-1, -1, -1), 18)),
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

    private data class Node(val pos : Vec3i) {
        var g = 0
        var h = 0
        val f : Int
            get() = g + h
        var connection : Node? = null
    }

    private fun makePath(n : Node) : MutableList<Vec3> {
        val l = mutableListOf<Vec3>()
        var c : Node? = n
        while(c != null) {
            l.add(c.pos.toVec3().add(Vec3(0.5, 1.0, 0.5)))
            c = c.connection
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
            val current = yea.remove()
            if(nah.contains(current.pos)) continue
            nah.add(current.pos)
            if(current.pos.matches(dest)) { return makePath(current) }
            for(dir in Directions.entries) {
                val neighbourPos = current.pos.add(dir.v.vec)
                if(nah.contains(neighbourPos)) continue
                if(!validPathingBlock(current.pos, neighbourPos)) continue
                val neighbour = Node(neighbourPos)
                neighbour.g = current.g + dir.v.cost
                neighbour.h = manhattan(neighbourPos, dest)
                neighbour.connection = current
                yea.add(neighbour)
            }
        }
        return null
    }
}