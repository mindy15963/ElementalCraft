package sirttas.elementalcraft.spell.earth;

import com.google.common.collect.Multimap;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.spell.IBlockCastedSpell;
import sirttas.elementalcraft.spell.IEntityCastedSpell;
import sirttas.elementalcraft.spell.Spell;

public class SpellGavelFall extends Spell implements IEntityCastedSpell, IBlockCastedSpell {

	public static final String NAME = "gravelfall";

	private void spawn(World world, BlockPos pos) {
		FallingBlockEntity entity = new FallingBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, Blocks.GRAVEL.getDefaultState());

		entity.fallTime = 1;
		entity.setHurtEntities(true);
		world.addEntity(entity);
	}

	@Override
	public boolean consume(Entity sender) {
		return consume(sender, Items.GRAVEL, 3);
	}

	private void checkAndSpawn(World world, BlockPos pos) {
		if (world.isAirBlock(pos)) {
			spawn(world, pos);
		}
	}

	private ActionResultType spawnGravel(Entity sender, BlockPos pos) {
		World world = sender.getEntityWorld();

		checkAndSpawn(world, pos.up(4));
		checkAndSpawn(world, pos.up(5));
		checkAndSpawn(world, pos.up(6));
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType castOnBlock(Entity sender, BlockPos target) {
		return spawnGravel(sender, target);
	}

	@Override
	public ActionResultType castOnEntity(Entity sender, Entity target) {
		return spawnGravel(sender, target.getPosition());
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers() {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers();

		multimap.put(PlayerEntity.REACH_DISTANCE.getName(), new AttributeModifier(REACH_DISTANCE_MODIFIER, "Reach distance modifier", 5.0D, AttributeModifier.Operation.ADDITION));
		return multimap;
	}

}
