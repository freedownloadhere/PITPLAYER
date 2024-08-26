package com.github.freedownloadhere.pitplayer.pathing.moveset
import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.moveset.AbsoluteDirection.*
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import net.minecraft.util.Vec3i
import java.awt.Color

/*
*
* Misnomer.
*
* */

enum class NeighbourCones(val arr : ArrayList<Movement>) {
    PosX(
        MovementArrayBuilder()
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
    PosXPosZ(
        MovementArrayBuilder()
        .new().add(DiagPXPZ).name("Walk Diagonally").push()
        .new().add(DiagPXPZ).add(PZ).name("1 Block L-shape Left").push()
        .new().add(DiagPXPZ).add(PX).name("1 Block L-shape Right").push()
        .new().add(DiagPXPZ, 2).name("2 Block Diagonal Jump").push()
        .new().add(DiagPXPZ, 2).add(PZ).name("2 Block L-shape Left").push()
        .new().add(DiagPXPZ, 2).add(PX).name("2 Block L-shape Right").push()
        .new().add(DiagPXPZ, 3).name("3 Block Diagonal Jump").push()
        .new().add(DiagPXPZ).add(NY).name("Walk Diagonally Down").push()
        .new().add(DiagPXPZ).add(PY).name("Walk Diagonally Up").push()
        .finish()
    ),
    PosZ(
        MovementArrayBuilder()
        .add(PosX.arr)
        .twist90onXZ()
        .finish()
    ),
    NegXPosZ(
        MovementArrayBuilder()
        .add(PosXPosZ.arr)
        .twist90onXZ()
        .finish()
    ),
    NegX(
        MovementArrayBuilder()
        .add(PosZ.arr)
        .twist90onXZ()
        .finish()
    ),
    NegXNegZ(
        MovementArrayBuilder()
        .add(NegXPosZ.arr)
        .twist90onXZ()
        .finish()
    ),
    NegZ(
        MovementArrayBuilder()
        .add(NegX.arr)
        .twist90onXZ()
        .finish()
    ),
    PosXNegZ(
        MovementArrayBuilder()
        .add(NegXNegZ.arr)
        .twist90onXZ()
        .finish()
    );

    fun drawCone(relativeTo : Vec3i) {
        val posList = mutableListOf<Vec3i>()
        for(move in arr)
            posList.add(relativeTo + move.dir)
        Renderer.blocksVec3i(posList, Color.RED)
    }
}