package com.github.freedownloadhere.pitplayer.mixin;

import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerMovementHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin {
    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    public void getMotionVector_pitplayer(CallbackInfo ci) {
        Entity entity = ((EntityLivingBase)(Object)this);
        if(!(entity instanceof EntityPlayerSP)) return;
        double motionX = (long)(entity.motionX * 1000) / 1000.0;
        double motionY = (long)(entity.motionY * 1000) / 1000.0;
        double motionZ = (long)(entity.motionZ * 1000) / 1000.0;
        Vec3 motionVector = new Vec3(motionX, motionY, motionZ);
        PlayerMovementHelper.INSTANCE.setMotionVec(motionVector);
    }
}