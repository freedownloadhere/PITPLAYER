package com.github.freedownloadhere.pitplayer.pathing

import com.github.freedownloadhere.pitplayer.Debug
import com.github.freedownloadhere.pitplayer.extensions.*
import kotlinx.coroutines.delay
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i

object Pathfinder {
    private data class Node(
        val pos : Vec3i,
        val g : Double = 0.0,
        val h : Double = 0.0,
        val next : Node? = null
    ) {
        val f : Double
            get() = g + h
    }

    private fun makeSimplePath(n : Node) : MutableList<Vec3> {
        val l = mutableListOf<Vec3>()
        var c : Node? = n
        var delta1 = Vec3i(0, 0, 0)
        while(c?.next != null) {
            val delta2 = c.pos - c.next!!.pos
            if(delta1.matches(delta2)) { c = c.next; continue }
            delta1 = delta2
            l.add(c.pos.toVec3().toBlockTop())
            c = c.next
        }
        return l
    }

    private fun makeFullPath(n : Node) : MutableList<Vec3> {
        val l = mutableListOf<Vec3>()
        var c : Node? = n
        while(c != null) {
            l.add(c.pos.toVec3().toBlockTop())
            c = c.next
        }
        return l
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

    suspend fun pathfind(dest : Vec3i?, start : Vec3i?) : MutableList<Vec3>? {
        if(dest == null || start == null)
            return null

        val closed = mutableSetOf<Vec3i>()
        val opened = mutableMapOf<Vec3i, Node>()
        opened[start] = Node(start, 0.0, 0.0, null)

        while(opened.isNotEmpty()) {
            val curr = opened.bestNode()

            if(closed.contains(curr.pos)) continue
            closed.add(curr.pos)

            if(curr.pos.matches(dest))
                return makeSimplePath(curr)

            for(cone in NeighbourCones.entries) {
                var move : Movement? = null
                var nextPos : Vec3i? = null
                for (m in cone.arr) {
                    nextPos = curr.pos + m.dir
                    if(closed.contains(nextPos)) continue
                    if(!PathBlockHelper.isValid(curr.pos, nextPos, m)) continue
                    move = m
                    break
                }

                if(move == null || nextPos == null) continue

                val nextG = curr.g + move.cost
                if(opened.contains(nextPos) && nextG >= opened[nextPos]!!.g) continue

                val nextH = nextPos.distance(dest)
                opened[nextPos] = Node(nextPos, nextG, nextH, curr)
            }

            if(Debug.pathStep) {
                Debug.pathStepCount--
                if(Debug.pathStepCount == 0) {
                    Debug.pathCurrent = makeFullPath(curr)
                    while(Debug.pathStepCount == 0) { delay(200L) }
                }
            }
        }

        return null
    }
}