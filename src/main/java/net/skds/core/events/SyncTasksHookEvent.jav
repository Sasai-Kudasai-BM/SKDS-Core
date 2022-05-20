package net.skds.core.events;

import net.minecraft.profiler.IProfiler;
import net.minecraftforge.eventbus.api.Event;

public class SyncTasksHookEvent extends Event {

	public final IProfiler profiler;

	public SyncTasksHookEvent(IProfiler profiler) {
		this.profiler = profiler;
	}
	
}