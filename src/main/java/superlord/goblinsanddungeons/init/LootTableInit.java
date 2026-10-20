package superlord.goblinsanddungeons.init;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import superlord.goblinsanddungeons.GoblinsAndDungeons;

public class LootTableInit {

	private static final Set<ResourceKey<LootTable>> LOOT_TABLES = Sets.newHashSet();
	private static final Set<ResourceKey<LootTable>> READ_ONLY_LOOT_TABLES = Collections.unmodifiableSet(LOOT_TABLES);

	public static final ResourceKey<LootTable> CAMP_LOOT_TABLE = register("chests/camp_loot_table");
	public static final ResourceKey<LootTable> KEEP_LOOT_TABLE = register("chests/keep_loot_table");
	public static final ResourceKey<LootTable> SIMPLE_DUNGEON_INJECT = register("inject/simple_dungeon");
	public static final ResourceKey<LootTable> ABANDONED_MINESHAFT_INJECT = register("inject/abandoned_mineshaft");

	private static ResourceKey<LootTable> register(String path) {
		return register(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, path)));
	}

	private static ResourceKey<LootTable> register(ResourceKey<LootTable> id) {
		if (LOOT_TABLES.add(id)) {
			return id;
		} else {
			throw new IllegalArgumentException(id + " is already a registered built-in loot table");
		}
	}

	public static Set<ResourceKey<LootTable>> getAll() {
		return READ_ONLY_LOOT_TABLES;
	}
}
