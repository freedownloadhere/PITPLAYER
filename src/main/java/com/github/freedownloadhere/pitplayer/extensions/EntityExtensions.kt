package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.entity.Entity
import net.minecraft.util.Vec3i

val Entity.blockBelow : Vec3i?
    get() {
        var pos = positionVector.toBlockPos()
        while(pos.y >= 0 && !world.isSolid(pos))
            pos = pos.downOne
        return if(pos.y < 0) null else pos
    }