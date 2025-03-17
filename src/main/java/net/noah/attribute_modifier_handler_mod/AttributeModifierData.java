package net.noah.attribute_modifier_handler_mod;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;

public class AttributeModifierData {
    private final RegistryEntry<EntityAttribute> ATTRIBUTE;
    private final EntityAttributeModifier MODIFIER;

    private int tick;

    public AttributeModifierData(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, int tick) {
        ATTRIBUTE = attribute;
        MODIFIER = modifier;
        this.tick = tick;
    }

    public boolean Tick() {
        if (tick > 0) {
            tick--;
        }
        return tick <= 0;
    }

    public void SetTick(int tick) {
        this.tick = tick;
    }

    public boolean HasThisModifier(EntityAttributeModifier modifier) {
        return MODIFIER.idMatches(modifier.id());
    }

    public NbtCompound ToNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("attribute_id", ATTRIBUTE.getIdAsString());
        nbt.putString("modifier_id", MODIFIER.id().toString());
        nbt.putDouble("modifier_value", MODIFIER.value());
        nbt.putString("modifier_operation", MODIFIER.operation().toString());
        nbt.putInt("modifier_tick", tick);
        return nbt;
    }

    public RegistryEntry<EntityAttribute> GetAttribute() {
        return ATTRIBUTE;
    }

    public EntityAttributeModifier GetModifier() {
        return MODIFIER;
    }
}
