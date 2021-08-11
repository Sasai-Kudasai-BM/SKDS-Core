package net.skds.core.mixins.multithreading;

import java.util.concurrent.locks.ReentrantLock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.util.palette.PalettedContainer;

@Mixin(value = { PalettedContainer.class })
public class PalettedContainerMixin<T> {

	@Shadow
	private final ReentrantLock lock = new ReentrantLock();

	@Inject(method = "lock", at = @At(value = "HEAD"), cancellable = true)
	public void lock(CallbackInfo ci) {
		lock.lock();
		ci.cancel();
	}

	@Inject(method = "get", at = @At(value = "HEAD"), cancellable = true)
	public synchronized void lockedSwap(int x, int y, int z, CallbackInfoReturnable<T> ci) {

		ci.setReturnValue(this.get(getIndex(x, y, z)));
	}

	//============================
	@Inject(method = "lockedSwap", at = @At(value = "HEAD"), cancellable = true)
	public synchronized void lockedSwap(int x, int y, int z, T state, CallbackInfoReturnable<T> ci) {
	
		lock.lock();
		T t = this.doSwap(getIndex(x, y, z), state);
		lock.unlock();
		ci.setReturnValue(t);
	}
	//=============================
	@Shadow
	protected T get(int index) {
		return null;
	}

	@Shadow
	protected T doSwap(int index, T state) {
		return null;
	}

	@Shadow
	private static int getIndex(int x, int y, int z) {
		return 0;
	}
}