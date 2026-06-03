package com.teamtea.rotborn.block;

import com.teamtea.rotborn.core.RotData;
import com.teamtea.rotborn.registry.ModBlockEntities;
import com.teamtea.rotborn.registry.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class IceBoxBlockEntity extends BlockEntity implements MenuProvider, Container, Nameable {
    public static final int SLOT_COUNT = 9;

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    public IceBoxBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ICE_BOX_ENTITY.get(), pos, blockState);
    }

    @Override
    public Component getName() {
        return Component.translatable("block.rotborn.ice_box");
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ChestMenu(MenuType.GENERIC_9x1, containerId, playerInventory, this, 1);
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        stack.limitSize(getMaxStackSize(stack));
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, items);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, IceBoxBlockEntity entity) {
        NonNullList<ItemStack> itemStacks = entity.items;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack.isEmpty()) continue;
            // if (!itemStack.has(ModDataComponents.ROT_DATA)) continue;
            RotData rotData = itemStack.get(ModDataComponents.ROT_DATA);
            if (rotData != null) {
                itemStack.set(ModDataComponents.ROT_DATA,
                        new RotData(rotData.startGameTime(), rotData.rotAfterTicks() + 1));
                entity.setChanged();
            }
        }
    }
}
