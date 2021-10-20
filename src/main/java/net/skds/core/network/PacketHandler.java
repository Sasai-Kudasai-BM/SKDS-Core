package net.skds.core.network;

import java.util.Collection;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.skds.core.SKDSCore;
import net.skds.core.events.PacketRegistryEvent;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(SKDSCore.MOD_ID, "network"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	public static void send(Collection<ServerPlayerEntity> players, Object message) {
		for (ServerPlayerEntity player : players) {
			send(player, message);
		}
	}

	public static void send(PlayerEntity player, Object message) {
		if (player instanceof ServerPlayerEntity) {
			CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
		}
	}

	public static void sendToServer(Object message) {
		CHANNEL.sendToServer(message);
	}

	public static SimpleChannel get() {
		return CHANNEL;
	}

	public static void init() {
		int id = 0;
		PacketRegistryEvent event = new PacketRegistryEvent(CHANNEL, id);
		event.registerPacket(DebugPacket.class, DebugPacket::encoder, DebugPacket::decoder, DebugPacket::handle);
		ModLoader.get().postEvent(new PacketRegistryEvent(CHANNEL, id));
	}
}