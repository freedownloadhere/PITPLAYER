package com.github.freedownloadhere.pitplayer.pathing
import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.AbsoluteDirection.*
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import net.minecraft.util.Vec3
import java.awt.Color

enum class NeighbourCones(val arr : ArrayList<Movement>) {
    PosX(MovementArrayBuilder()
        .new().add(PX).name("Walk Straight").push()
        .new().add(PX, 2).name("1 Block Jump").push()
        .new().add(PX, 3).name("2 Block Jump").push()
        .new().add(PX, 3).add(PZ, 1).name("2 Block L-shape Left").push()
        .new().add(PX, 3).add(NZ, 1).name("2 Block L-shape Right").push()
        .new().add(PX, 4).name("3 Block Jump").push()
        .new().add(PX, 4).add(PZ, 1).name("3 Block L-shape Left").push()
        .new().add(PX, 4).add(NZ, 1).name("3 Block L-shape Right").push()
        .new().add(PX, 5).name("4 Block Jump").push()
        .new().add(PX, 5).add(PZ, 1).name("4 Block L-shape Left").push()
        .new().add(PX, 5).add(NZ, 1).name("4 Block L-shape Right").push()
        .new().add(PX).add(NY).name("Walk Up").push()
        .new().add(PX).add(PY).name("Walk Down").push()
        .finish()
    ),
    PosXPosZ(MovementArrayBuilder()
        .new().add(PX).add(PZ).name("Walk Diagonally").push()
        .new().add(PX).add(PZ).add(PZ).name("1 Block L-shape Left").push()
        .new().add(PX).add(PZ).add(PX).name("1 Block L-shape Right").push()
        .new().add(PX, 2).add(PZ, 2).name("2 Block Diagonal Jump").push()
        .new().add(PX, 2).add(PZ, 2).add(PZ).name("2 Block L-shape Left").push()
        .new().add(PX, 2).add(PZ, 2).add(PX).name("2 Block L-shape Right").push()
        .new().add(PX, 3).add(PZ, 3).name("3 Block Diagonal Jump").push()
        .new().add(PX).add(PZ).add(NY).name("Walk Diagonally Down").push()
        .new().add(PX).add(PZ).add(PY).name("Walk Diagonally Up").push()
        .finish()
    ),
    PosZ(MovementArrayBuilder()
        .add(PosX.arr)
        .twist90onXZ()
        .finish()
    ),
    NegXPosZ(MovementArrayBuilder()
        .add(PosXPosZ.arr)
        .twist90onXZ()
        .finish()
    ),
    NegX(MovementArrayBuilder()
        .add(PosZ.arr)
        .twist90onXZ()
        .finish()
    ),
    NegXNegZ(MovementArrayBuilder()
        .add(NegXPosZ.arr)
        .twist90onXZ()
        .finish()
    ),
    NegZ(MovementArrayBuilder()
        .add(NegX.arr)
        .twist90onXZ()
        .finish()
    ),
    PosXNegZ(MovementArrayBuilder()
        .add(NegXNegZ.arr)
        .twist90onXZ()
        .finish()
    );

    fun debugDrawCone() {
        val posList = mutableListOf<Vec3>()
        val playerPos = player.blockBelow?.toVec3() ?: return
        for(move in arr)
            posList.add(playerPos + move.dir)
        Renderer.blocks(posList, Color.RED)
    }
}