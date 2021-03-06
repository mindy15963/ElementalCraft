package sirttas.elementalcraft.block.instrument.firefurnace;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.block.BlockECContainer;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.particle.ParticleHelper;

public abstract class AbstractBlockFireFurnace extends BlockECContainer {

	public AbstractBlockFireFurnace(Properties properties) {
		super(properties);
	}

	public AbstractBlockFireFurnace() {
		super();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final AbstractTileFireFurnace<?> furnace = (AbstractTileFireFurnace<?>) world.getTileEntity(pos);
	
		if (furnace != null) {
			if (!ItemEC.isEmpty(furnace.getStackInSlot(1))) {
				furnace.dropExperience(player);
				return this.onSlotActivated(furnace, player, ItemStack.EMPTY, 1);
			}
			return this.onSlotActivated(furnace, player, player.getHeldItem(hand), 0);
		}
		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		double x = pos.getX() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;
		double y = pos.getY() + 6 * BlockEC.BIT_SIZE;
		double z = pos.getZ() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;
	
		AbstractTileFireFurnace<?> furnace = (AbstractTileFireFurnace<?>) world.getTileEntity(pos);
	
		if (furnace != null && furnace.isRunning()) {
			world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.SMOKE, x, y + 0.5D, z, 0.0D, 0.0D, 0.0D);
			ParticleHelper.createElementFlowParticle(furnace.getTankElementType(), world, new Vec3d(pos), Direction.UP, rand);
		}
	}

}