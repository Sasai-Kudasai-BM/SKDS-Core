package net.skds.core.client.debug;

import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.skds.core.client.renderers.DebugEntityRenderer;
import net.skds.core.debug.DebugEntity;
import net.skds.core.debug.RegisterDebug;


@OnlyIn(Dist.CLIENT)
public class RegisterDebugClient {	

	@SuppressWarnings("unchecked")
	public static void register() {
        RenderingRegistry.registerEntityRenderingHandler((EntityType<DebugEntity>) RegisterDebug.DEBUG_ENTITY.get(), DebugEntityRenderer::new);
	}
}
