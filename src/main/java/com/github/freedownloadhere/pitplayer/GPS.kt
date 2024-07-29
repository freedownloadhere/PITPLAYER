package com.github.freedownloadhere.pitplayer

import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.util.PriorityQueue
import kotlin.math.abs

object GPS {
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

    private fun manhattan(v1 : Vec3i, v2 : Vec3i) : Int {
        return (abs(v1.x - v2.x) + abs(v1.y - v2.y) + abs(v1.z - v2.z))
    }

    private fun euclidean(v1 : Vec3i, v2 : Vec3i) : Int {
        return (v1.x - v2.x) * (v1.x - v2.x) + (v1.y - v2.y) * (v1.y - v2.y) + (v1.z - v2.z) * (v1.z - v2.z)
    }

    private fun withinError(v1 : Vec3, v2 : Vec3, error : Double) : Boolean {
        return (abs(v1.xCoord - v2.xCoord) <= error && abs(v1.yCoord - v2.yCoord) <= error && abs(v1.zCoord - v2.zCoord) <= error)
    }

    fun inArea(area : AreaRect) : Boolean {
        return (
        area.x1 <= player.posX && player.posX <= area.x2 &&
        area.y1 <= player.posY && player.posY <= area.y2 &&
        area.z1 <= player.posZ && player.posZ <= area.z2)
    }

    fun getPlayerBlockPos() : Vec3i {
        return Vec3i(
            if(player.posX >= 0) player.posX.toInt() else player.posX.toInt() - 1,
            if(player.posY >= 0) player.posY.toInt() else player.posY.toInt() - 1,
            if(player.posZ >= 0) player.posZ.toInt() else player.posZ.toInt() - 1
        )
    }

    private fun blockIsSolid(pos : Vec3i) : Boolean {
        val block = world.getBlockState(BlockPos(pos.x, pos.y, pos.z)).block.material
        return block.isSolid && !block.isLiquid
    }

    private fun blockIsNotSolid(pos : Vec3i) : Boolean {
        return !blockIsSolid(pos)
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
        //if(next.x - curr.x != 0 && next.z - curr.z != 0)
            //if(isObstructed(Vec3i(curr.x, next.y, next.z)) || isObstructed(Vec3i(next.x, next.y, curr.z)))
                //return false
        if(next.y == curr.y + 1 && blockIsSolid(curr.upThree)) return false
        if(next.y == curr.y - 1 && blockIsSolid(next.upThree)) return false
        return true
    }

    val path = mutableListOf<Vec3>()

    fun pathfindTo(dest : Vec3i, start : Vec3i = getPlayerBlockPos()) {
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
                    path.clear()
                    while(v != null && v != start) {
                        path.add(Vec3(v.x.toDouble() + 0.5, v.y.toDouble() + 1.0, v.z.toDouble() + 0.5))
                        v = p[v]?.connection
                    }
                    return
                }
            }
        }
    }

    fun followPath() {
        if(path.isEmpty()) return
        if(withinError(path.last(), player.positionVector, 1.5))
            path.removeLast()
        if(path.isEmpty()) return
        PlayerRemote.lookAt(path.last().addVector(0.0, 1.625, 0.0))
    }
}