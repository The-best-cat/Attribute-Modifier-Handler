package net.noah.attribute_modifier_handler_mod.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.noah.attribute_modifier_handler_mod.AttributeModifierData;
import net.noah.attribute_modifier_handler_mod.access.IAttributeModifierHandlerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements IAttributeModifierHandlerAccess {
	private LivingEntity ENTITY = (LivingEntity) (Object)this;

	@Unique
	private final List<AttributeModifierData> modifierData = new ArrayList<>();

	@Override
	public void AddModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, int tick) {
		EntityAttributeInstance instance = ENTITY.getAttributeInstance(attribute);
		if (instance != null) {
			AttributeModifierData data = GetModifierData(modifier);
			if (data == null) {
				data = new AttributeModifierData(attribute, modifier, tick);
				modifierData.add(data);
				instance.addTemporaryModifier(modifier);
			} else {
				data.SetTick(tick);
				instance.updateModifier(modifier);
			}
		}
	}

	@Override
	public void AddModifier(RegistryEntry<EntityAttribute> attribute, Identifier identifier, double value, EntityAttributeModifier.Operation operation, int tick) {
		AddModifier(attribute, new EntityAttributeModifier(identifier, value, operation), tick);
	}

	@Override
	public void RemoveModifier(RegistryEntry<EntityAttribute> attribute, Identifier identifier) {
		EntityAttributeInstance instance = ENTITY.getAttributeInstance(attribute);
		if (instance != null) {
			AttributeModifierData data = GetModifierData(instance.getModifier(identifier));
			if (data != null) {
				instance.removeModifier(identifier);
				modifierData.remove(data);
			}
		}
	}

	@Override
	public AttributeModifierData GetModifierData(EntityAttributeModifier modifier) {
		for (var i : modifierData) {
			if (i.HasThisModifier(modifier)) {
				return i;
			}
		}
		return null;
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void TickAttributeModifier(CallbackInfo info) {
		Iterator<AttributeModifierData> itr = modifierData.iterator();
		while (itr.hasNext()) {
			AttributeModifierData data = itr.next();
			if (data.Tick()) {
				EntityAttributeInstance instance = ENTITY.getAttributeInstance(data.GetAttribute());
				if (instance != null) {
					instance.removeModifier(data.GetModifier());
				}
				itr.remove();
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
	private void WriteModifierData(CallbackInfo info, @Local NbtCompound nbt) {
		NbtList list = new NbtList();
		for (var i : modifierData) {
			list.add(i.ToNbt());
		}
		nbt.put("modifier_data", list);
	}

	@Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
	private void ReadModifierData(CallbackInfo info, @Local NbtCompound nbt) {
		if (nbt.contains("modifier_data",NbtElement.LIST_TYPE) && ENTITY.getWorld() != null && !ENTITY.getWorld().isClient()) {
			NbtList list = nbt.getList("modifier_data", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < list.size(); i++) {
				NbtCompound compound = list.getCompound(i);
				Optional<RegistryEntry.Reference<EntityAttribute>> attribute = Registries.ATTRIBUTE.getEntry(Identifier.tryParse(compound.getString("attribute_id")));
				if (attribute.isPresent() && ENTITY.getAttributeInstance(attribute.get()) != null) {
					EntityAttributeModifier modifier = new EntityAttributeModifier(
							Identifier.tryParse(compound.getString("modifier_id")),
							compound.getDouble("modifier_value"),
							EntityAttributeModifier.Operation.valueOf(compound.getString("modifier_operation"))
					);
					AddModifier(attribute.get(), modifier, compound.getInt("modifier_tick"));
				}
			}
		}
	}
}