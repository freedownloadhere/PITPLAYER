package com.github.freedownloadhere.pitplayer

import net.minecraft.block.Block
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.util.PriorityQueue
import kotlin.math.abs

object GPS {
    data class DirVec3i(val d : Vec3i, val c : Int)
    data class PathData(var g : Int = 9999999, var h : Int) {
        var connection : Vec3i? = null
        val f : Int
            get() = g + h
    }

    private val directions = arrayListOf(
        DirVec3i(Vec3i(1, 0, 0), 10),
        DirVec3i(Vec3i(0, 0, 1), 10),
        DirVec3i(Vec3i(-1, 0, 0), 10),
        DirVec3i(Vec3i(0, 0, -1), 10),
        DirVec3i(Vec3i(1, 1, 0), 14),
        DirVec3i(Vec3i(0, 1, 1), 14),
        DirVec3i(Vec3i(-1, 1, 0), 14),
        DirVec3i(Vec3i(0, 1, -1), 14),
        DirVec3i(Vec3i(1, -1, 0), 14),
        DirVec3i(Vec3i(0, -1, 1), 14),
        DirVec3i(Vec3i(-1, -1, 0), 14),
        DirVec3i(Vec3i(0, -1, -1), 14),
    )

    private fun manhattan(v1 : Vec3i, v2 : Vec3i) : Int {
        return (abs(v1.x - v2.x) + abs(v1.y - v2.y) + abs(v1.z - v2.z))
    }

    fun inArea(area : AreaRect) : Boolean {
        return (
        area.x1 <= player.posX && player.posX <= area.x2 &&
        area.y1 <= player.posY && player.posY <= area.y2 &&
        area.z1 <= player.posZ && player.posZ <= area.z2)
    }

    fun getPlayerPos() : Vec3 {
        return Vec3(player.posX, player.posY, player.posZ)
    }

    fun getPlayerBlockPos() : Vec3i {
        return Vec3i(
            if(player.posX >= 0) player.posX.toInt() else player.posX.toInt() - 1,
            if(player.posY >= 0) player.posY.toInt() else player.posY.toInt() - 1,
            if(player.posZ >= 0) player.posZ.toInt() else player.posZ.toInt() - 1
        )
    }

    fun blockIsSolid(pos : Vec3i) : Boolean {
        return world.getBlockState(BlockPos(pos.x, pos.y, pos.z)).block.material.isSolid
    }

    fun validPathingBlock(curr : Vec3i, next : Vec3i) : Boolean {
        if(!blockIsSolid(next)) return false
        if(blockIsSolid(next.add(Vec3i(0, 1, 0))) || blockIsSolid(next.add(Vec3i(0, 2, 0)))) return false
        if(next.y == curr.y + 1 && blockIsSolid(curr.add(Vec3i(0, 3, 0)))) return false
        if(next.y == curr.y - 1 && blockIsSolid(next.add(Vec3i(0, 3, 0)))) return false
        return true
    }

    val lastPath = mutableListOf<Vec3i>()

    fun pathfindTo(dest : Vec3i, start : Vec3i = getPlayerBlockPos()) {
        val p = hashMapOf<Vec3i, PathData>()
        val q = PriorityQueue(Comparator { v1 : Vec3i, v2 : Vec3i -> Int
            val p1 = p[v1]!!
            val p2 = p[v2]!!
            if(p1.f == p2.f)  p1.h - p2.h
            p1.f - p2.f
        })

        q.add(start)
        p[start] = PathData(0, manhattan(start, dest) * 10)

        while(!q.isEmpty()) {
            val curr = q.remove()

            for(dir in directions) {
                val next = curr.add(dir.d)
                val cost = p[curr]!!.g + dir.c
                if(!validPathingBlock(curr, next)) continue
                if(!p.contains(next)) {
                    p[next] = PathData(cost, manhattan(next, dest) * 10)
                    p[next]!!.connection = curr
                    q.add(next)
                }
                else if(cost < p[next]!!.g) {
                    p[next]!!.g = cost
                    p[next]!!.connection = curr
                }
                if(next == dest) {
                    var v : Vec3i? = dest
                    lastPath.clear()
                    while(v != null && v != start) {
                        lastPath.add(v)
                        v = p[v]?.connection
                    }
                    return
                }
            }
        }
    }
}