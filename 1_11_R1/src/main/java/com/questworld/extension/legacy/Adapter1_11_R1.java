package com.questworld.extension.legacy;

import org.bukkit.entity.Player;

import com.questworld.util.PartialAdapter;
import com.questworld.util.Text;

public class Adapter1_11_R1 extends PartialAdapter {

	@Override
	protected String forVersion() {
		return "1_11_R1";
	}
	
	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		player.sendTitle(Text.colorize(title), Text.colorize(subtitle), fadeIn, stay, fadeOut);
	}
}
