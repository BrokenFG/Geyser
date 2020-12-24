/*
 * Copyright (c) 2019-2020 GeyserMC. http://geysermc.org
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

package org.geysermc.connector.network.translators.inventory.translators;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientClickWindowButtonPacket;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.data.inventory.StackRequestSlotInfoData;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.CraftResultsDeprecatedStackRequestActionData;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionData;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionType;
import com.nukkitx.protocol.bedrock.packet.ItemStackRequestPacket;
import com.nukkitx.protocol.bedrock.packet.ItemStackResponsePacket;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.geysermc.connector.inventory.GeyserItemStack;
import org.geysermc.connector.inventory.Inventory;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.translators.inventory.BedrockContainerSlot;
import org.geysermc.connector.network.translators.inventory.updater.UIInventoryUpdater;
import org.geysermc.connector.network.translators.item.ItemTranslator;

import java.util.stream.Collectors;

public class StonecutterInventoryTranslator extends AbstractBlockInventoryTranslator {
    public StonecutterInventoryTranslator() {
        super(2, "minecraft:stonecutter[facing=north]", ContainerType.STONECUTTER, UIInventoryUpdater.INSTANCE);
    }

    @Override
    public boolean shouldHandleRequestFirst(StackRequestActionData action, Inventory inventory) {
        return action.getType() == StackRequestActionType.CRAFT_NON_IMPLEMENTED_DEPRECATED;
    }

    @Override
    public ItemStackResponsePacket.Response translateSpecialRequest(GeyserSession session, Inventory inventory, ItemStackRequestPacket.Request request) {
        // TODO: Also surely to change in the future
        StackRequestActionData data = request.getActions()[1];
        if (!(data instanceof CraftResultsDeprecatedStackRequestActionData)) {
            return rejectRequest(request);
        }
        CraftResultsDeprecatedStackRequestActionData craftData = (CraftResultsDeprecatedStackRequestActionData) data;
        // Get the ID of the item we are cutting
        int id = inventory.getItem(0).getId();
        // Look up all possible options of cutting from this ID
        IntSet results = session.getStonecutterRecipes().get(id);
        if (results == null) {
            return rejectRequest(request);
        }
        ItemStack javaOutput = ItemTranslator.translateToJava(craftData.getResultItems()[0]);
        // TODO bruh
        // Getting the index of the item in the Java stonecutter list
        // We do need to sort them by their Java ID to preserve index, though
        int index = results.stream().sorted().collect(Collectors.toList()).indexOf(javaOutput.getId());
        ClientClickWindowButtonPacket packet = new ClientClickWindowButtonPacket(inventory.getId(), index + 1);
        System.out.println(packet.toString());
        session.sendDownstreamPacket(packet);
        // We don't know there is an output here, so we tell ourselves that there is
        inventory.setItem(1, GeyserItemStack.from(javaOutput, session.getItemNetId().incrementAndGet()));
        return translateRequest(session, inventory, request);
    }

    @Override
    public int bedrockSlotToJava(StackRequestSlotInfoData slotInfoData) {
        if (slotInfoData.getContainer() == ContainerSlotType.STONECUTTER_INPUT) {
            return 0;
        }
        if (slotInfoData.getContainer() == ContainerSlotType.STONECUTTER_RESULT || slotInfoData.getContainer() == ContainerSlotType.CREATIVE_OUTPUT) {
            return 1;
        }
        return super.bedrockSlotToJava(slotInfoData);
    }

    @Override
    public BedrockContainerSlot javaSlotToBedrockContainer(int slot) {
        if (slot == 0) {
            return new BedrockContainerSlot(ContainerSlotType.STONECUTTER_INPUT, 3);
        }
        if (slot == 1) {
            return new BedrockContainerSlot(ContainerSlotType.STONECUTTER_RESULT, 50);
        }
        return super.javaSlotToBedrockContainer(slot);
    }

    @Override
    public int javaSlotToBedrock(int slot) {
        if (slot == 0) {
            return 3;
        }
        if (slot == 1) {
            return 50;
        }
        return super.javaSlotToBedrock(slot);
    }
}
