package com.github.freedownloadhere.pitplayer.mixin;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityLivingBase.class)
public interface AccessorEntityLivingBase {
    @Accessor("jumpTicks")
    int getJumpTicks_pitplayer();

    @Accessor("jumpTicks")
    void setJumpTicks_pitplayer(int jumpTicks_pitplayer);
}
