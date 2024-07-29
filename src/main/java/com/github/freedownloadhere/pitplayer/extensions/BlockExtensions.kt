package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.block.Block

val Block.isSolid : Boolean
    get() = material.isSolid