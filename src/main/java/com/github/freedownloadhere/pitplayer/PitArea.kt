package com.github.freedownloadhere.pitplayer

enum class PitArea(val rect : AreaRect, val str : String) {
    UNKNOWN (AreaRect(0.0, 0.0, 0.0, 0.0, 0.0, 0.0), "Unknown"),
    OG_SPAWN(AreaRect(20.0, 120.0, -20.0, -20.0, 100.0, 20.0), "Spawn"),
    OG_MID  (AreaRect(-20.0, 100.0, 20.0, 20.0, 0.0, -20.0), "Mid"),
    OG_WATER(AreaRect(0.0, 0.0, 0.0, -200.0, 255.0, -200.0), "Water"),
    OG_ROCKS(AreaRect(0.0, 0.0, 0.0, 200.0, 255.0, -200.0), "Rocks"),
    OG_LAVA (AreaRect(0.0, 0.0, 0.0, 200.0, 255.0, 200.0), "Lava"),
    OG_SKY  (AreaRect(0.0, 0.0, 0.0, -200.0, 255.0, 200.0), "Sky"),
}