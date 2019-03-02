/*
 * Copyright (C) 2018-2019  C4
 *
 * This file is part of Curios, a mod made for Minecraft.
 *
 * Curios is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Curios is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Curios.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.curios.api.capability;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CurioType;

import javax.annotation.Nonnull;

public interface ICurio {

    /**
     * Called every tick while the ItemStack is equipped
     * @param identifier        The {@link CurioType} identifier of the ItemStack's slot
     * @param entityLivingBase  The wearer of the ItemStack
     */
    default void onCurioTick(String identifier, EntityLivingBase entityLivingBase) {}

    /**
     * Determines if the ItemStack can be equipped into a slot
     * @param identifier        The {@link CurioType} identifier of the slot being equipped into
     * @param entityLivingBase  The wearer of the ItemStack
     * @return  True if the ItemStack can be equipped/put in, false if not
     */
    default boolean canEquip(String identifier, EntityLivingBase entityLivingBase) {
        return true;
    }

    /**
     * Determines if the ItemStack can be unequipped from a slot
     * @param identifier        The {@link CurioType} identifier of the slot being unequipped from
     * @param entityLivingBase  The wearer of the ItemStack
     * @return  True if the ItemStack can be unequipped/taken out, false if not
     */
    default boolean canUnequip(String identifier, EntityLivingBase entityLivingBase) {
        return true;
    }

    /**
     * A map of AttributeModifier associated with the ItemStack and the {@link CurioType} identifier
     * @param identifier    The CurioType identifier for the context
     * @return  A map of attribute modifiers to apply
     */
    default Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {
        return HashMultimap.create();
    }

    /**
     * Plays a sound server-side when a curio is equipped from right-clicking the ItemStack in hand
     * This can be overriden to play nothing, but it is advised to always play something as an auditory feedback for players
     * @param entityLivingBase  The wearer of the ItemStack
     */
    default void playEquipSound(EntityLivingBase entityLivingBase) {
        entityLivingBase.world.playSound(null, entityLivingBase.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
                SoundCategory.NEUTRAL, 1.0f, 1.0f);
    }

    /**
     * Determines if the ItemStack can be automatically equipped into the first available slot when right-clicked
     * @return  True to enable right-clicking auto-equip, false to disable
     */
    default boolean canRightClickEquip() { return false; }

    /**
     * Compares the current ItemStack and the previous ItemStack in the slot to detect any changes and returns true if
     * the change should be synced to all tracking clients.
     * Note that this check occurs every tick so implementations need to code their own timers for other intervals.
     * @param identifier        The identifier of the {@link CurioType} of the slot
     * @param entityLivingBase  The EntityLivingBase that is wearing the ItemStack
     * @return  True to curios the ItemStack change to all tracking clients, false to do nothing
     */
    default boolean shouldSyncToTracking(String identifier, EntityLivingBase entityLivingBase) {
        return false;
    }

    /**
     * Gets a tag that is used to sync extra curio data from the server to the client
     * Only used when {@link ICurio#shouldSyncToTracking(String, EntityLivingBase)} returns true
     * @return  Data to be sent to the client
     */
    @Nonnull
    default NBTTagCompound getSyncTag() { return new NBTTagCompound(); }

    /**
     * Used client-side to read data tags created by {@link ICurio#getSyncTag()} received from the server
     * @param compound Data received from the server
     */
    default void readSyncTag(NBTTagCompound compound) {}

    /**
     * Determines if the ItemStack has rendering
     * @param identifier        The identifier of the {@link CurioType} of the slot
     * @param entityLivingBase  The EntityLivingBase that is wearing the ItemStack
     * @return  True if the ItemStack has rendering, false if it does not
     */
    default boolean hasRender(String identifier, EntityLivingBase entityLivingBase) { return false; }

    /**
     * Performs rendering of the ItemStack if {@link ICurio#hasRender(String, EntityLivingBase)} returns true
     * @param identifier            The identifier of the {@link CurioType} of the slot
     * @param entitylivingbaseIn    The EntityLivingBase that is wearing the ItemStack
     */
    @OnlyIn(Dist.CLIENT)
    default void doRender(String identifier, EntityLivingBase entitylivingbaseIn, float limbSwing,
                          float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
                          float scale) {}
}
