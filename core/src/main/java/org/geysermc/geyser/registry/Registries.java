/*
 * Copyright (c) 2019-2022 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.geysermc.geyser.registry;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.inventory.crafting.PotionMixData;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.geysermc.geyser.api.pack.ResourcePack;
import org.geysermc.geyser.entity.EntityDefinition;
import org.geysermc.geyser.inventory.recipe.GeyserRecipe;
import org.geysermc.geyser.item.type.Item;
import org.geysermc.geyser.registry.provider.ProviderSupplier;
import org.geysermc.geyser.registry.type.ItemMappings;
import org.geysermc.geyser.registry.type.ParticleMapping;
import org.geysermc.geyser.registry.type.SoundMapping;
import org.geysermc.geyser.translator.level.block.entity.BlockEntityTranslator;
import org.geysermc.geyser.translator.level.event.LevelEventTranslator;
import org.geysermc.geyser.translator.sound.SoundInteractionTranslator;
import org.geysermc.geyser.translator.sound.SoundTranslator;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityType;
import org.geysermc.mcprotocollib.protocol.data.game.level.event.LevelEvent;
import org.geysermc.mcprotocollib.protocol.data.game.level.particle.ParticleType;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.RecipeType;

/**
 * Holds all the common registries in Geyser.
 * Block specific registries can be found in {@link BlockRegistries}
 */
public final class Registries {
    private static CommonRegistries backingRegistries;

    public static CommonRegistries instance() {
        return CommonRegistriesDefault.instance();
    }

    /**
     * A registry holding all the providers.
     * This has to be initialized first to allow extensions to access providers during other registry events.
     */
    public static SimpleMappedRegistry<Class<?>, ProviderSupplier> providers() {
        return backingRegistries.providers();
    }

    /**
     * A registry holding a CompoundTag of the known entity identifiers.
     */
    public static SimpleRegistry<NbtMap> bedrockEntityIdentifiers() {
        return backingRegistries.bedrockEntityIdentifiers();
    }

    /**
     * A registry containing all the Bedrock packet translators.
     */
    public static PacketTranslatorRegistry<BedrockPacket> bedrockPacketTranslators() {
        return backingRegistries.bedrockPacketTranslators();
    }

    /**
     * A registry holding a CompoundTag of all the known biomes.
     */
    public static SimpleRegistry<NbtMap> biomesNbt() {
        return backingRegistries.biomesNbt();
    }

    /**
     * A mapped registry which stores Java biome identifiers and their Bedrock biome identifier.
     */
    public static SimpleRegistry<Object2IntMap<String>> biomeIdentifiers() {
        return backingRegistries.biomeIdentifiers();
    }

    /**
     * A mapped registry which stores a block entity identifier to its {@link BlockEntityTranslator}.
     */
    public static SimpleMappedRegistry<BlockEntityType, BlockEntityTranslator> blockEntities() {
        return backingRegistries.blockEntities();
    }

    /**
     * A map containing all entity types and their respective Geyser definitions
     */
    public static SimpleMappedRegistry<EntityType, EntityDefinition<?>> entityDefinitions() {
        return backingRegistries.entityDefinitions();
    }

    /**
     * A registry holding a list of all the known entity properties to be sent to the client after start game.
     */
    public static SimpleRegistry<Set<NbtMap>> bedrockEntityProperties() {
        return backingRegistries.bedrockEntityProperties();
    }

    /**
     * A map containing all Java entity identifiers and their respective Geyser definitions
     */
    public static SimpleMappedRegistry<String, EntityDefinition<?>> javaEntityIdentifiers() {
        return backingRegistries.javaEntityIdentifiers();
    }

    /**
     * A registry containing all the Java packet translators.
     */
    public static PacketTranslatorRegistry<Packet> javaPacketTranslators() {
        return backingRegistries.javaPacketTranslators();
    }

    /**
     * A registry containing all Java items ordered by their network ID.
     */
    public static ListRegistry<Item> javaItems() {
        return backingRegistries.javaItems();
    }

    public static SimpleMappedRegistry<String, Item> javaItemIdentifiers() {
        return backingRegistries.javaItemIdentifiers();
    }

    /**
     * A versioned registry which holds {@link ItemMappings} for each version. These item mappings contain
     * primarily Bedrock version-specific data.
     */
    public static VersionedRegistry<ItemMappings> items() {
        return backingRegistries.items();
    }

    /**
     * A mapped registry holding the {@link ParticleType} to a corresponding {@link ParticleMapping}, containing various pieces of
     * data primarily for how Bedrock should handle the particle.
     */
    public static SimpleMappedRegistry<ParticleType, ParticleMapping> particles() {
        return backingRegistries.particles();
    }

    /**
     * A registry holding all the potion mixes.
     */
    public static VersionedRegistry<Set<PotionMixData>> potionMixes() {
        return backingRegistries.potionMixes();
    }

    /**
     * A versioned registry holding all the recipes, with the net ID being the key, and {@link GeyserRecipe} as the value.
     */
    public static SimpleMappedRegistry<RecipeType, List<GeyserRecipe>> recipes() {
        return backingRegistries.recipes();
    }

    /**
     * A mapped registry holding {@link ResourcePack}'s with the pack uuid as keys.
     */
    public static DeferredRegistry<Map<String, ResourcePack>> resourcePacks() {
        return backingRegistries.resourcePacks();
    }

    /**
     * A mapped registry holding sound identifiers to their corresponding {@link SoundMapping}.
     */
    public static SimpleMappedRegistry<String, SoundMapping> sounds() {
        return backingRegistries.sounds();
    }

    /**
     * A mapped registry holding {@link LevelEvent}s to their corresponding {@link LevelEventTranslator}.
     */
    public static SimpleMappedRegistry<LevelEvent, LevelEventTranslator> soundLevelEvents() {
        return backingRegistries.soundLevelEvents();
    }

    /**
     * A mapped registry holding {@link SoundTranslator}s to their corresponding {@link SoundInteractionTranslator}.
     */
    public static SimpleMappedRegistry<SoundTranslator, SoundInteractionTranslator<?>> soundTranslators() {
        return backingRegistries.soundTranslators();
    }

    public static void init() {
        backingRegistries = instance();
        backingRegistries.postInit();
    }
}
