package com.questworld.extension.legacy;

import com.questworld.api.QuestExtension;
import com.questworld.util.Reflect;

public class LegacySupport extends QuestExtension {
	public LegacySupport() {
		Reflect.addAdapter(new Adapter1_8_R1());
		Reflect.addAdapter(new Adapter1_8_R2());
		Reflect.addAdapter(new Adapter1_11_R1());
		Reflect.addAdapter(new Adapter1_12_R1());
	}
}
