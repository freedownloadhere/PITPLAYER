package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.util.*
import kotlin.math.abs

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

    var gCostLimit = 300

    fun pathfind(dest : Vec3, start : Vec3) : MutableList<Vec3>? {
        return pathfind(dest.toVec3i(), start.toVec3i())
    }

    fun pathfind(dest : Vec3i, start : Vec3i) : MutableList<Vec3>? {
        val p = hashMapOf<Vec3i, PathData>()
        val q = PriorityQueue { v1: Vec3i, v2: Vec3i ->
            Int
            val p1 = p[v1]!!
            val p2 = p[v2]!!
            if (p1.f == p2.f) p1.h - p2.h
            p1.f - p2.f
        }

        q.add(start)
        p[start] = PathData(0, manhattan(start, dest))

        while(!q.isEmpty()) {
            val curr = q.remove()

            for(dir in Directions.entries) {
                val next = curr.add(dir.v.vec)
                val cost = (p[curr]!!.g + dir.v.cost)
                if(cost > gCostLimit) continue
                if(!validPathingBlock(curr, next)) continue
                if(!p.contains(next)) {
                    p[next] = PathData(cost, manhattan(next, dest))
                    p[next]!!.connection = curr
                    q.add(next)
                }
                else if(cost < p[next]!!.g) {
                    p[next]!!.g = cost
                    p[next]!!.connection = curr
                }
                if(next == dest) {
                    var v : Vec3i? = dest
                    val path = mutableListOf<Vec3>()
                    path.clear()
                    while(v != null && v != start) {
                        path.add(Vec3(v.x.toDouble() + 0.5, v.y.toDouble() + 1.0, v.z.toDouble() + 0.5))
                        v = p[v]?.connection
                    }
                    return path
                }
            }
        }

        return null
    }
}