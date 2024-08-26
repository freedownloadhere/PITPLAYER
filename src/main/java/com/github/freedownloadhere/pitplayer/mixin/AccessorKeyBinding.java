package com.github.freedownloadhere.pitplayer.mixin;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface AccessorKeyBinding {
    @Accessor("pressed")
    boolean getPressed_pitplayer();

    @Accessor("pressed")
    void setPressed_pitplayer(boolean pressed);

    @Accessor("pressTime")
    int getPressTime_pitplayer();

    @Accessor("pressTime")
    void setPressTime_pitplayer(int pressTime);
}
