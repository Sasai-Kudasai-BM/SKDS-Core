package net.skds.core.network;

import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.skds.core.util.data.ChunkSectionAdditionalData;

public class CSDPacket {

	private ChunkSectionAdditionalData data;
	private ByteBuf buffer;
	private int x;
	private int y;
	private int z;

	public CSDPacket(ChunkSectionAdditionalData data, int chunkX, int chunkZ) {
		this.x = chunkX;
		this.z = chunkZ;
		this.z = data.section.getYLocation() >> 4;
		this.data = data;
	}

	public CSDPacket(PacketBuffer buffer) {
		x = buffer.readInt();
		y = buffer.readByte();
		z = buffer.readInt();
		this.buffer = buffer.copy();
	}

	public void encoder(PacketBuffer buffer) {
		buffer.writeInt(x);
		buffer.writeByte(y);
		buffer.writeInt(z);
		data.write(buffer);
	}

	public static CSDPacket decoder(PacketBuffer buffer) {
		return new CSDPacket(buffer);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		Minecraft minecraft = Minecraft.getInstance();
		World w = minecraft.player.world;
		Chunk chunk = w.getChunk(x, z);
		if (!chunk.isEmpty()) {

			ChunkSection section = chunk.getSections()[y];
			if (section == null) {
				section = new ChunkSection(y << 4);
				chunk.getSections()[y] = section;
			}
			ChunkSectionAdditionalData.getFromSection(section).read(new PacketBuffer(buffer));

		}
		context.get().setPacketHandled(true);
	}
}