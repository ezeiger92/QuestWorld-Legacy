package com.questworld.extension.legacy;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import com.questworld.util.PartialAdapter;
import com.questworld.util.Reflect;
import com.questworld.util.Text;
import com.questworld.util.Version;

public class Adapter1_8_R2 extends PartialAdapter {
	
	public Adapter1_8_R2() {
		super(Version.ofString("1_8_R2"));
	}

	@Override
	public void sendActionbar(Player player, String text) {
		try {
			Class<?> componentClass = Reflect.NMS("IChatBaseComponent");
			Class<?> packetClass = Reflect.NMS("PacketPlayOutChat");
			Class<?> basePacketClass = Reflect.NMS("Packet");
			
			Object component = Reflect.NMS("ChatComponentText").getConstructor(String.class).newInstance(Text.colorize(text));

			Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
			
			Object connection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
			
			Object packet = packetClass.getConstructor(componentClass, byte.class).newInstance(component, (byte) 2);
			
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
			Class<?> packetEnumClass = Reflect.NMS("PacketPlayOutTitle$EnumTitleAction");
			
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
			
			Constructor<?> newComponent = Reflect.NMS("ChatComponentText").getConstructor(String.class);
			
			Object componentTitle;
			if(title != null) {
				componentTitle = newComponent.newInstance(Text.colorize(title));
			}
			else {
				componentTitle = null;
			}
			
			Object componentSubtitle;
			if(title != null) {
				componentSubtitle = newComponent.newInstance(Text.colorize(subtitle));
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
}
