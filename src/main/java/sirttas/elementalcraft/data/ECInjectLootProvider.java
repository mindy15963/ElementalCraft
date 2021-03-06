package sirttas.elementalcraft.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraft.world.storage.loot.functions.SetCount;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.loot.LootHandler;
import sirttas.elementalcraft.loot.function.RandomSpell;

/**
 * greatly inspired by Botania
 *
 * 
 */
public class ECInjectLootProvider extends AbstractECLootProvider {

	public ECInjectLootProvider(DataGenerator generator) {
		super(generator);
		LootFunctionManager.registerFunction(new RandomSpell.Serializer());
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		for (String target : LootHandler.INJECTED_TABLES) {
			save(cache, genDefault(), new ResourceLocation(ElementalCraft.MODID, target));
		}
	}

	private static Path getPath(Path root, ResourceLocation id) {
		return root.resolve("data/" + id.getNamespace() + "/loot_tables/inject/" + id.getPath() + ".json");
	}

	private static LootPool.Builder genDefault() {
		List<LootEntry.Builder<?>> entries = Lists.newArrayList(
				ItemLootEntry.builder(ECItems.inertCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 5))).weight(20),
				ItemLootEntry.builder(ECItems.fireCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.earthCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.waterCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.airCrystal).acceptFunction(SetCount.builder(RandomValueRange.of(1, 3))).weight(10),
				ItemLootEntry.builder(ECItems.scroll).acceptFunction(RandomSpell.builder()).weight(15),
				ItemLootEntry.builder(ECItems.emptyReceptacle).weight(2)
				);
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1));

		entries.forEach(pool::addEntry);
		return pool;
	}

	private void save(DirectoryCache cache, LootPool.Builder pool, ResourceLocation location) throws IOException {
		save(cache, LootTable.builder().addLootPool(pool).setParameterSet(LootParameterSets.BLOCK), getPath(generator.getOutputFolder(), location));
	}

	@Override
	public String getName() {
		return "ElementalCraft inject loot tables";
	}
}
