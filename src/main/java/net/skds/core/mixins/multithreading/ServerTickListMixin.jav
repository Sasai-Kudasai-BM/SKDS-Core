package net.skds.core.mixins.multithreading;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.TickPriority;
import net.minecraft.world.server.ServerTickList;
import net.minecraft.world.server.ServerWorld;

@Mixin(value = { ServerTickList.class })
public class ServerTickListMixin<T> {

	@Shadow
	@Final
	protected Predicate<T> filter;
	@Shadow
	@Final
	private ServerWorld world;


	@Inject(method = "scheduleTick", at = @At(value = "HEAD", ordinal = 0), cancellable = true)
	public synchronized void scheduleTick(BlockPos pos, T itemIn, int scheduledTime, TickPriority priority, CallbackInfo ci) {
		if (!this.filter.test(itemIn)) {
			this.addEntry(new NextTickListEntry<>(pos, itemIn, (long)scheduledTime + this.world.getGameTime(), priority));
		}
		ci.cancel();
	}

	@Shadow private void addEntry(NextTickListEntry<?> p_219504_1_) {}
}