package sirttas.elementalcraft.block.instrument.firefurnace;


import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.tile.renderer.RendererEC;
import sirttas.elementalcraft.item.ItemEC;

@OnlyIn(Dist.CLIENT)
public class RendererFireFurnace extends RendererEC<AbstractTileFireFurnace<?>> {

	public RendererFireFurnace(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(AbstractTileFireFurnace<?> te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay) {
		ItemStack stack = te.getStackInSlot(0);
		ItemStack stack2 = te.getStackInSlot(1);
		
		if (!ItemEC.isEmpty(stack) || !ItemEC.isEmpty(stack2)) {
			matrixStack.translate(0.5F, 0.3F, 0.5F);
			matrixStack.rotate(Vector3f.YP.rotationDegrees(getAngle(partialTicks)));
			if (!ItemEC.isEmpty(stack)) {
				renderItem(stack, matrixStack, buffer, light, overlay);
			}
			if (!ItemEC.isEmpty(stack2)) {
				matrixStack.translate(0, 0.5F, 0);
				renderItem(stack2, matrixStack, buffer, light, overlay);
			}
		}
	}
}
