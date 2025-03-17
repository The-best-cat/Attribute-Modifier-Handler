package net.noah.attribute_modifier_handler_mod;

import net.fabricmc.api.ModInitializer;

import net.noah.attribute_modifier_handler_mod.network.PacketRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeModifierHandlerMod implements ModInitializer {
	public static final String MOD_ID = "attribute_modifier_handler_mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		PacketRegister.Register();
	}
}