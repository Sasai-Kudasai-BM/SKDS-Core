package net.skds.skds_core.mixins.multithreading;

import java.util.concurrent.locks.ReentrantLock;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.util.palette.PalettedContainer;

@Mixin(value = { PalettedContainer.class })
public class PalettedContainerMixin {

	@Final
	@Shadow(aliases = "lock")
	final private ReentrantLock lock = new ReentrantLock();

	
	@Inject(method = "lock", at = @At(value = "HEAD"),cancellable = true)
	public void lock(CallbackInfo ci) {
		lock.lock();
		ci.cancel();
	}

	//@Overwrite
	//public void unlock() {
	//	this.lock.unlock();
	//}
}