package com.github.freedownloadhere.pitplayer.pathing

import net.minecraft.util.Vec3i

enum class AbsoluteDirection(val vec : Vec3i, val cost : Double) {
    PX(Vec3i(1, 0, 0), 1.0),
    PZ(Vec3i(0, 0, 1), 1.0),
    NX(Vec3i(-1, 0, 0), 1.0),
    NZ(Vec3i(0, 0, -1), 1.0),

    DownPX(Vec3i(1, -1, 0), 1.4),
    DownPZ(Vec3i(0, -1, 1), 1.4),
    DownNX(Vec3i(-1, -1, 0), 1.4),
    DownNZ(Vec3i(0, -1, -1), 1.4),

    UpPX(Vec3i(1, 1, 0), 2.0),
    UpPZ(Vec3i(0, 1, 1), 2.0),
    UpNX(Vec3i(-1, 1, 0), 2.0),
    UpNZ(Vec3i(0, 1, -1), 2.0),

    DiagPXPZ(Vec3i(1, 0, 1), 1.4),
    DiagPXNZ(Vec3i(1, 0, -1), 1.4),
    DiagNXPZ(Vec3i(-1, 0, 1), 1.4),
    DiagNXNZ(Vec3i(-1, 0, -1), 1.4),

    DiagDownPXPZ(Vec3i(1, -1, 1), 1.8),
    DiagDownPXNZ(Vec3i(1, -1, -1), 1.8),
    DiagDownNXPZ(Vec3i(-1, -1, 1), 1.8),
    DiagDownNXNZ(Vec3i(-1, -1, -1), 1.8),

    DiagUpPXPZ(Vec3i(1, 1, 1), 3.4),
    DiagUpPXNZ(Vec3i(1, 1, -1), 3.4),
    DiagUpNXPZ(Vec3i(-1, 1, 1), 3.4),
    DiagUpNXNZ(Vec3i(-1, 1, -1), 3.4)
}