package com.questworld.extension.legacy;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.questworld.util.PartialAdapter;
import com.questworld.util.Reflect;
import com.questworld.util.Text;
import com.questworld.util.Version;

public class Adapter1_12_R1 extends PartialAdapter {

	public Adapter1_12_R1() {
		super(Version.ofString("v1_12_r1"));
	}

	@Override
	public void makePlayerHead(ItemStack result, OfflinePlayer player) {
		result.setType(Material.SKULL_ITEM);
		result.setDurability((short) 3);
		SkullMeta sm = (SkullMeta) result.getItemMeta();
		sm.setOwningPlayer(player);
		result.setItemMeta(sm);
	}
	
	@Override
	public void sendActionbar(Player player, String text) {
		try {
			Class<?> componentClass = Reflect.NMS("IChatBaseComponent");
			Class<?> packetClass = Reflect.NMS("PacketPlayOutChat");
			Class<?> basePacketClass = Reflect.NMS("Packet");
			Class<?> messageClass = Reflect.NMS("ChatMessageType");
			
			Object component = Reflect.NMS("ChatComponentText").getConstructor(String.class).newInstance(Text.colorize(text));
	
			for(Object entry : messageClass.getEnumConstants()) {
				if(entry.toString().equals("GAME_INFO")) {
					Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
					
					Object connection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
					
					Object packet = packetClass.getConstructor(componentClass, messageClass).newInstance(component, entry);
					
					connection.getClass().getMethod("sendPacket", basePacketClass).invoke(connection, packet);
					
					return;
				}
			}
			
			throw new IllegalStateException("Missing enum constant");
			
		} catch (Exception e) {
			throw new UnsupportedOperationException("Exception with actionbar", e);
		}
	}
}
