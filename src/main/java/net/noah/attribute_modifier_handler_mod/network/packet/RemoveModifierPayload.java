package net.noah.attribute_modifier_handler_mod.network.packet;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.noah.attribute_modifier_handler_mod.AttributeModifierHandlerMod;

public record RemoveModifierPayload(int entityId, RegistryEntry<EntityAttribute> attribute, Identifier identifier) implements CustomPayload {
    public static final Id<RemoveModifierPayload> ID = new Id<>(Identifier.of(AttributeModifierHandlerMod.MOD_ID, "remove_modifier"));
    public static final PacketCodec<RegistryByteBuf, RemoveModifierPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, RemoveModifierPayload::entityId,
            EntityAttribute.PACKET_CODEC, RemoveModifierPayload::attribute,
            Identifier.PACKET_CODEC, RemoveModifierPayload::identifier,
            RemoveModifierPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
