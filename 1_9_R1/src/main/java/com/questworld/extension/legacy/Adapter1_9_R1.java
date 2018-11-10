package com.questworld.extension.legacy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.questworld.util.PartialAdapter;

public class Adapter1_9_R1 extends PartialAdapter {

	@Override
	protected String forVersion() {
		return "1_9_R1";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void makeSpawnEgg(ItemStack result, EntityType mob) {
		if(result.getType() == Material.MONSTER_EGG) {
			Bukkit.getUnsafe().modifyItemStack(result, "{EntityTag:{id:" + mob.getName() + "}}");
		}
	}
}
