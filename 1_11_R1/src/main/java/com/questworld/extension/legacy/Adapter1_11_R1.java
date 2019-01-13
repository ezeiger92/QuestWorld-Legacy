package com.questworld.extension.legacy;

import org.bukkit.entity.Player;

import com.questworld.util.PartialAdapter;
import com.questworld.util.Text;
import com.questworld.util.Version;

public class Adapter1_11_R1 extends PartialAdapter {

	public Adapter1_11_R1() {
		super(Version.ofString("v1_11_r1"));
	}
	
	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		player.sendTitle(Text.colorize(title), Text.colorize(subtitle), fadeIn, stay, fadeOut);
	}
}
