package net.skds.skds_core;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.skds.skds_core.util.blockupdate.BlockUpdataer;
import net.skds.skds_core.util.blockupdate.WWSGlobal;

public class Events {

    private static long inTickTime = System.nanoTime();
    private static int lastTickTime = 0;

    // @SubscribeEvent
    // public void aa(OnConfi e) {
    // }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload e) {

        World w = (World) e.getWorld();
        WWSGlobal.unloadWorld(w);
        if (!w.isRemote) {
            BlockUpdataer.onWorldUnload((ServerWorld) e.getWorld());
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e) {

        World w = (World) e.getWorld();
        WWSGlobal.loadWorld(w);
        if (!w.isRemote) {
            BlockUpdataer.onWorldLoad((ServerWorld) e.getWorld());
        }
    }

    @SubscribeEvent
    public void tick(ServerTickEvent event) {
        boolean in = event.phase == Phase.START;
        if (in) {
            inTickTime = System.nanoTime();
            WWSGlobal.tickInG();
        }
        BlockUpdataer.tick(in);
        if (!in) {
            WWSGlobal.tickOutG();

            lastTickTime = (int) (System.nanoTime() - inTickTime);
        }
    }

    public static int getLastTickTime() {
        return lastTickTime;
    }

    public static int getRemainingTickTimeNanos() {
        return 50_000_000 - (int) (System.nanoTime() - inTickTime);
    }

    public static int getRemainingTickTimeMicros() {
        return getRemainingTickTimeNanos() / 1000;
    }

    public static int getRemainingTickTimeMilis() {
        return getRemainingTickTimeNanos() / 1000_000;
    }
}