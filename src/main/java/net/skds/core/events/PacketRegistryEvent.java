package net.skds.core.events;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketRegistryEvent extends Event implements IModBusEvent {

	private final SimpleChannel channel;
	private int id;

	public PacketRegistryEvent(SimpleChannel channel, int id) {
		this.id = id;
		this.channel = channel;
	}
	
	public <MSG> void registerPacket(Class<MSG> type, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer) {
		channel.registerMessage(id++, type, encoder, decoder, consumer);
	}
}