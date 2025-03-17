package net.noah.attribute_modifier_handler_mod.access;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.noah.attribute_modifier_handler_mod.AttributeModifierData;

public interface IAttributeModifierHandlerAccess {
    void AddModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, int tick);
    void AddModifier(RegistryEntry<EntityAttribute> attribute, Identifier identifier, double value, EntityAttributeModifier.Operation operation, int tick);
    void RemoveModifier(RegistryEntry<EntityAttribute> attribute, Identifier identifier);
    AttributeModifierData GetModifierData(EntityAttributeModifier modifier);
}
