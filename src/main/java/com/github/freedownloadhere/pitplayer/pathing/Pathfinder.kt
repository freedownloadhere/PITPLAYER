package com.github.freedownloadhere.pitplayer.pathing

import com.github.freedownloadhere.pitplayer.extensions.*
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

    private fun isValid(c : Vec3i, n : Vec3i) : Boolean {
        if(!world.isWalkable(n)) return false
        if(n.x - c.x != 0 && n.z - c.z != 0)
            if(world.isObstructed(Vec3i(c.x, c.y, n.z)) || world.isObstructed(Vec3i(n.x, c.y, c.z)))
                return false
        if(n.y == c.y + 1 && world.isSolid(c.upThree)) return false
        if(n.y == c.y - 1 && world.isSolid(n.upThree)) return false
        return true
    }

    private fun isNotValid(c : Vec3i, n : Vec3i) : Boolean {
        return !isValid(c, n)
    }

    private fun makeSimplePath(n : Node) : MutableList<Vec3> {
        val l = mutableListOf<Vec3>()
        var c : Node? = n
        var delta1 = Vec3i(0, 0, 0)
        while(c?.next != null) {
            val delta2 = c.pos.subtract(c.next!!.pos)
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

    fun pathfind(dest : Vec3i?, start : Vec3i?) : MutableList<Vec3>? {
        if(dest == null || start == null)
            return null

        val closed = mutableSetOf<Vec3i>()
        val opened = mutableMapOf<Vec3i, Node>()
        opened[start] = Node(start, 0.0, 0.0, null)

        while(opened.isNotEmpty()) {
            // gets the node with the lowest F value
            // and also removes it from the list
            val curr = opened.bestNode()

            if(closed.contains(curr.pos)) continue
            closed.add(curr.pos)

            if(curr.pos.matches(dest))
                return makeSimplePath(curr)

            for(d in blockTypeOf(curr.pos).moveset) {
                val nextpos = curr.pos.add(d.vec)

                if(closed.contains(nextpos)) continue
                if(isNotValid(curr.pos, nextpos)) continue

                // if we already have a shorter path
                // to the next node, leave it like that
                val nextg = curr.g + d.cost
                if(opened.contains(nextpos) && nextg >= opened[nextpos]!!.g) continue

                val nexth = nextpos.distance(dest)
                opened[nextpos] = Node(nextpos, nextg, nexth, curr)
            }
        }

        // if we get to this point, we
        // did not find any valid path
        return null
    }
}