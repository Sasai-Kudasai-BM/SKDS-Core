package net.skds.core;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.skds.core.api.IWorldExtended;
import net.skds.core.util.blockupdate.BlockUpdataer;
import net.skds.core.util.blockupdate.WWSGlobal;

public class Events {

    private static long inTickTime = System.nanoTime();
    private static int lastTickTime = 0;

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload e) {

        World w = (World) e.getWorld();
        WWSGlobal wwsg = ((IWorldExtended) w).getWWS();
        wwsg.unloadWorld(w);
        if (!w.isRemote) {
            BlockUpdataer.onWorldUnload((ServerWorld) e.getWorld());
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e) {

        World w = (World) e.getWorld();
        ((IWorldExtended) w).addWWS();
       // WWSGlobal.loadWorld(w);
        if (!w.isRemote) {
            BlockUpdataer.onWorldLoad((ServerWorld) w);
        }
    }

    @SubscribeEvent
    public void tick(WorldTickEvent event) {

        boolean in = event.phase == Phase.START;
        World w = event.world;
        if (in) {
            //System.out.println("W I ========================");
            WWSGlobal wwsg = ((IWorldExtended) w).getWWS();
            wwsg.tickIn();
            
        }
        //BlockUpdataer.tick(in);
        if (!in) {
            //System.out.println("W O ========================");
            WWSGlobal wwsg = ((IWorldExtended) w).getWWS();
            wwsg.tickOut();

            lastTickTime = (int) (System.nanoTime() - inTickTime);
        }
    }

    @SubscribeEvent
    public void tick(ServerTickEvent event) {
        boolean in = event.phase == Phase.START;
        if (in) {
            inTickTime = System.nanoTime();
            //System.out.println("IN========================");
        }
        BlockUpdataer.tick(in);
        if (!in) {
            lastTickTime = (int) (System.nanoTime() - inTickTime);
            ///System.out.println("OUT========================");
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