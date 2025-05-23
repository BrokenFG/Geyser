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

package org.geysermc.geyser.translator.inventory.chest;

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.geysermc.geyser.inventory.Generic9X3Container;
import org.geysermc.geyser.inventory.holder.BlockInventoryHolder;
import org.geysermc.geyser.inventory.holder.InventoryHolder;
import org.geysermc.geyser.level.block.Blocks;
import org.geysermc.geyser.level.block.property.ChestType;
import org.geysermc.geyser.level.block.property.Properties;
import org.geysermc.geyser.level.block.type.BlockState;
import org.geysermc.geyser.session.GeyserSession;

public class SingleChestInventoryTranslator extends ChestInventoryTranslator<Generic9X3Container> {
    private final InventoryHolder holder;

    public SingleChestInventoryTranslator(int size) {
        super(size, 27);
        this.holder = new BlockInventoryHolder(Blocks.CHEST.defaultBlockState().withValue(Properties.CHEST_TYPE, ChestType.SINGLE), ContainerType.CONTAINER,
                Blocks.ENDER_CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL) {
            @Override
            protected boolean isValidBlock(BlockState blockState) {
                if (blockState.is(Blocks.ENDER_CHEST) || blockState.is(Blocks.BARREL)) {
                    // Can't have double ender chests or barrels
                    return true;
                }

                // Add provision to ensure this isn't a double chest
                return super.isValidBlock(blockState) && blockState.getValue(Properties.CHEST_TYPE) == ChestType.SINGLE;
            }
        };
    }

    @Override
    public boolean prepareInventory(GeyserSession session, Generic9X3Container container) {
        return holder.prepareInventory(session, container);
    }

    @Override
    public void openInventory(GeyserSession session, Generic9X3Container container) {
        holder.openInventory(session, container);
    }

    @Override
    public void closeInventory(GeyserSession session, Generic9X3Container container, boolean force) {
        holder.closeInventory(session, container, ContainerType.CONTAINER);
    }

    @Override
    public Generic9X3Container createInventory(GeyserSession session, String name, int windowId, org.geysermc.mcprotocollib.protocol.data.game.inventory.ContainerType containerType) {
        return new Generic9X3Container(session, name, windowId, this.size, containerType);
    }

    @Override
    protected ContainerSlotType slotType(Generic9X3Container generic9X3Container) {
        if (generic9X3Container.isBarrel()) {
            return ContainerSlotType.BARREL;
        }
        return super.slotType(generic9X3Container);
    }
}
