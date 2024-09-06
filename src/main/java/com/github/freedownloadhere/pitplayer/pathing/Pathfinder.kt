package com.github.freedownloadhere.pitplayer.pathing

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.simulation.*
import com.github.freedownloadhere.pitplayer.simulation.SimulatedMovement.*
import com.github.freedownloadhere.pitplayer.utils.Vector3d

object Pathfinder {
    data class Node(val state : PlayerImage, val moveset : Moveset, var g : Double, val h : Double, val next : Node? = null) {
        val f : Double
            get() = g + h
        val pos : Vector3d
            get() = state.pos.truncate()
    }

    private fun findBestNodeAndRemoveIt(container : HashMap<Vector3d, Node>) : Node {
        var bestPrice = Double.MAX_VALUE
        var bestNode : Node? = null
        var bestNodeKey : Vector3d? = null
        for(pair in container) {
            if(pair.value.f < bestPrice) {
                bestPrice = pair.value.f
                bestNode = pair.value
                bestNodeKey = pair.key
            }
        }
        container.remove(bestNodeKey)
        // this will never cause issues nope
        return bestNode!!
    }

    private fun constructPath(node : Node) : MutableList<Node> {
        var n : Node? = node
        val path = mutableListOf<Node>()
        while(n != null) {
            path.add(n)
            n = n.next
        }
        return path
    }

    fun pathfind(endPositionVector : Vector3d) : MutableList<Node>? {
        val bot = SimulatedPlayer()
        world.spawnEntityInWorld(bot)

        val initialState = PlayerImageHelper.getState(player)
        bot.loadState(initialState)

        val start = player.positionVector3d.truncate()
        val end = endPositionVector.truncate()

        val opened = hashMapOf<Vector3d, Node>()
        val closed = mutableSetOf<Vector3d>()
        opened[start] = Node(initialState, Moveset.None, 0.0, 0.0)

        while(opened.isNotEmpty()) {
            val curr = findBestNodeAndRemoveIt(opened)
            val currPos = curr.pos

            if(closed.contains(currPos))
                continue

            closed.add(currPos)

            if(currPos.matches(end)) {
                world.removeEntity(bot)
                return constructPath(curr)
            }

            // val yawArray = SimulatedYawArrayFactory.make(bot.rotationYaw)
            // for(simYaw in yawArray) {
                for(move in SimulatedMovement.Moveset.entries) {
                    bot.loadState(curr.state)
                    // bot.rotationYaw = simYaw.yaw
                    bot.simulate(move.sm)

                    val nextPos = bot.positionVector3d.truncate()
                    if(closed.contains(nextPos)) continue

                    val nextG = curr.g + move.cost // + simYaw.cost
                    if(opened.contains(nextPos) && nextG >= opened[nextPos]!!.g)
                        continue

                    val nextH = currPos.distanceTo(end)
                    opened[nextPos] = Node(
                        PlayerImageHelper.getState(bot),
                        move,
                        nextG,
                        nextH,
                        curr
                    )
                }
            // }
        }

        world.removeEntity(bot)
        return null
    }
}