package com.github.freedownloadhere.pitplayer.pathing.moveset

import net.minecraft.util.Vec3i

enum class AbsoluteDirection(val dir : Vec3i, val cost : Double) {
    PX(Vec3i(1, 0, 0), 1.0),
    PY(Vec3i(0, 1, 0), 1.5),
    PZ(Vec3i(0, 0, 1), 1.0),
    NX(Vec3i(-1, 0, 0), 1.0),
    NY(Vec3i(0, -1, 0), 1.25),
    NZ(Vec3i(0, 0, -1), 1.0),
    DiagPXPZ(Vec3i(1, 0, 1), 1.4)
}