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

public record AddModifierPayload(int entityId, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, int tick) implements CustomPayload {
    public static final Id<AddModifierPayload> ID = new Id<>(Identifier.of(AttributeModifierHandlerMod.MOD_ID, "add_modifier"));
    public static final PacketCodec<RegistryByteBuf, AddModifierPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, AddModifierPayload::entityId,
            EntityAttribute.PACKET_CODEC, AddModifierPayload::attribute,
            EntityAttributeModifier.PACKET_CODEC, AddModifierPayload::modifier,
            PacketCodecs.INTEGER, AddModifierPayload::tick,
            AddModifierPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
