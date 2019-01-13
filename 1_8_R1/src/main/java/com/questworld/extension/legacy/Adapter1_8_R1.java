package com.questworld.extension.legacy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.SkullMeta;

import com.questworld.util.Reflect;
import com.questworld.util.Text;
import com.questworld.util.Version;
import com.questworld.util.VersionAdapter;
import com.questworld.util.json.JsonBlob;

public class Adapter1_8_R1 extends VersionAdapter {

	public Adapter1_8_R1() {
		super(Version.ofString("v1_8_R1"));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void makeSpawnEgg(ItemStack result, EntityType mob) {
		result.setType(Material.MONSTER_EGG);
		result.setDurability(mob.getTypeId());
	}

	@Override
	public void makePlayerHead(ItemStack result, OfflinePlayer player) {
		result.setType(Material.SKULL_ITEM);
		result.setDurability((short) 3);
		SkullMeta sm = (SkullMeta) result.getItemMeta();
		sm.setOwner(player.getName());
		result.setItemMeta(sm);
	}

	@Override
	public ShapelessRecipe shapelessRecipe(String recipeName, ItemStack output) {
		return new ShapelessRecipe(output);
	}

	@Override
	public void sendActionbar(Player player, String message) {
		try {
			Class<?> componentClass = Reflect.NMS("IChatBaseComponent");
			Class<?> packetClass = Reflect.NMS("PacketPlayOutChat");
			Class<?> basePacketClass = Reflect.NMS("Packet");
			
			Object component = Reflect.NMS("ChatSerializer").getMethod("a", String.class).invoke(null, new JsonBlob(Text.colorize(message)).toString());
			
			Object packet = packetClass.getConstructor(componentClass, byte.class).newInstance(component, (byte) 2);
			
			Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
			
			Object connection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
			
			connection.getClass().getMethod("sendPacket", basePacketClass).invoke(connection, packet);
		} catch (Exception e) {
			throw new UnsupportedOperationException("Exception with actionbar", e);
		}
	}
	
	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		try {
			Class<?> componentClass = Reflect.NMS("IChatBaseComponent");
			Class<?> packetClass = Reflect.NMS("PacketPlayOutTitle");
			Class<?> basePacketClass = Reflect.NMS("Packet");
			Class<?> packetEnumClass = Reflect.NMS("EnumTitleAction");
			
			Object titleEnum = null;
			Object subtitleEnum = null;
			
			for(Object constant : packetEnumClass.getEnumConstants()) {
				if(constant.toString().equals("TITLE")) {
					titleEnum = constant;
				}
				else if(constant.toString().equals("SUBTITLE")) {
					subtitleEnum = constant;
				}
			}
			
			if(titleEnum == null || subtitleEnum == null) {
				throw new NullPointerException("Missing enum constant for title or subtitle");
			}
			
			Method toComponent = Reflect.NMS("ChatSerializer").getMethod("a", String.class);
			
			Object componentTitle;
			if(title != null) {
				componentTitle = toComponent.invoke(null, new JsonBlob(Text.colorize(title)).toString());
			}
			else {
				componentTitle = null;
			}
			
			Object componentSubtitle;
			if(title != null) {
				componentSubtitle = toComponent.invoke(null, new JsonBlob(Text.colorize(subtitle)).toString());
			}
			else {
				componentSubtitle = null;
			}
			
			Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
			
			Object connection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
			
			Constructor<?> packetConstructor = packetClass.getConstructor(packetEnumClass, componentClass, int.class, int.class, int.class);
			
			if(componentTitle != null) {
				connection.getClass().getMethod("sendPacket", basePacketClass).invoke(connection,
						packetConstructor.newInstance(titleEnum, componentTitle, fadeIn, stay, fadeOut));
			}
			
			if(componentSubtitle != null) {
				connection.getClass().getMethod("sendPacket", basePacketClass).invoke(connection,
						packetConstructor.newInstance(subtitleEnum, componentSubtitle, fadeIn, stay, fadeOut));
			}
		} catch (Exception e) {
			throw new UnsupportedOperationException("Exception with title", e);
		}
		
		return;
	}
	
	@Override
	public void setItemDamage(ItemStack result, int damage) {
		result.setDurability((short) damage);
	}
}