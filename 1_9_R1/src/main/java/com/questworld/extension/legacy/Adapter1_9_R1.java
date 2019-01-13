package com.questworld.extension.legacy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.questworld.util.PartialAdapter;
import com.questworld.util.Version;

public class Adapter1_9_R1 extends PartialAdapter {
	
	public Adapter1_9_R1() {
		super(Version.ofString("1_9_R1"));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void makeSpawnEgg(ItemStack result, EntityType mob) {
		result.setType(Material.MONSTER_EGG);
		
		Bukkit.getUnsafe().modifyItemStack(result, "{EntityTag:{id:" + mob.getName() + "}}");
	}
}
