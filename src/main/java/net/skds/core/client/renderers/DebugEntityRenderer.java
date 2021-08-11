package net.skds.core.client.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.debug.DebugEntity;

@OnlyIn(Dist.CLIENT)
public class DebugEntityRenderer extends EntityRenderer<DebugEntity> {

	@SuppressWarnings("unused")
	private ItemRenderer itemRenderer;
	private Minecraft mc;

	public DebugEntityRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.5F;
		this.mc = Minecraft.getInstance();
		this.itemRenderer = mc.getItemRenderer();
	}

	
	@Override
	public void render(DebugEntity debugEntity, float entityYaw, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer bufferIn, int packedLightIn) {

		matrixStack.push();
		
		renderPhysicalBox(matrixStack, bufferIn.getBuffer(RenderType.getLines()), debugEntity, 1.0f, 1.0f, 1.0f);

		matrixStack.pop();
	}



	private void renderPhysicalBox(MatrixStack matrixStack, IVertexBuilder bufferIn, DebugEntity debugEntity, float red,
			float green, float blue) {
		AxisAlignedBB axisalignedbb = debugEntity.getBoundingBox().offset(-debugEntity.getPosX(), -debugEntity.getPosY(),
				-debugEntity.getPosZ());
		WorldRenderer.drawBoundingBox(matrixStack, bufferIn, axisalignedbb, red, green, blue, 1.0F);
	}

	@Override
	public boolean shouldRender(DebugEntity e, ClippingHelper camera, double camX, double camY, double camZ) {
		return super.shouldRender(e, camera, camX, camY, camZ);
	}

	@Override
	@SuppressWarnings("deprecation")
	public ResourceLocation getEntityTexture(DebugEntity entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
