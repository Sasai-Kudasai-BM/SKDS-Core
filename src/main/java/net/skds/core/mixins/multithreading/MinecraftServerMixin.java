package net.skds.core.mixins.multithreading;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.skds.core.multithreading.MTHooks;

@Mixin(value = { MinecraftServer.class })
public class MinecraftServerMixin {

    @Inject(method = "updateTimeLightAndEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/IProfiler;endStartSection(Ljava/lang/String;)V", ordinal = 1))
    private void tickHook(CallbackInfo ci) {
        MTHooks.afterWorldsTick();
    }
}