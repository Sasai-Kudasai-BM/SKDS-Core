package net.skds.core.mixins.multithreading;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.profiler.IProfiler;
import net.minecraft.server.MinecraftServer;
import net.skds.core.multithreading.MTHooks;

@Mixin(value = { MinecraftServer.class })
public class MinecraftServerMixin {

    @Shadow
    private IProfiler profiler;

    @Inject(method = "updateTimeLightAndEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/IProfiler;endStartSection(Ljava/lang/String;)V", ordinal = 1, shift = Shift.AFTER))
    private void tickHook(CallbackInfo ci) {
        MTHooks.afterWorldsTick(profiler, (MinecraftServer) (Object) this);
    }
}