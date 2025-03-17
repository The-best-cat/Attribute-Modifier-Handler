package net.noah.attribute_modifier_handler_mod.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.noah.attribute_modifier_handler_mod.access.IAttributeModifierHandlerAccess;
import net.noah.attribute_modifier_handler_mod.network.packet.AddModifierPayload;
import net.noah.attribute_modifier_handler_mod.network.packet.RemoveModifierPayload;

public class PacketRegister {
    public static void Register() {
        PayloadTypeRegistry.playC2S().register(AddModifierPayload.ID, AddModifierPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(RemoveModifierPayload.ID, RemoveModifierPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(AddModifierPayload.ID, (payload, context) -> {
            Entity entity = context.player().getServerWorld().getEntityById(payload.entityId());
            if (entity instanceof LivingEntity e && !entity.getWorld().isClient()) {
                ((IAttributeModifierHandlerAccess)e).AddModifier(payload.attribute(), payload.modifier(), payload.tick());
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(RemoveModifierPayload.ID, (payload, context) -> {
            Entity entity = context.player().getServerWorld().getEntityById(payload.entityId());
            if (entity instanceof LivingEntity e && !entity.getWorld().isClient()) {
                ((IAttributeModifierHandlerAccess)e).RemoveModifier(payload.attribute(), payload.identifier());
            }
        });
    }
}
