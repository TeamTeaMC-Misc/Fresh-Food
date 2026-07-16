package com.teamtea.fresh_food;

import com.teamtea.fresh_food.core.ProcessResult;
import com.teamtea.fresh_food.core.RotData;
import com.teamtea.fresh_food.core.RotRule;
import com.teamtea.fresh_food.core.RotTags;
import com.teamtea.fresh_food.mixin.common.chunk.ChunkMapAccessor;
import com.teamtea.fresh_food.registry.ModDataComponents;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class RottingUtil {
    static boolean processContainer(ServerLevel level, Container container, long gameTime) {
        boolean changed = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack oldStack = container.getItem(i);

            changed |= processItemStackStorage(level, oldStack, gameTime);

            ProcessResult result = processStack(level, oldStack, gameTime);
            if (result.changed()) {
                container.setItem(i, result.stack());
                changed = true;
            }
        }

        if (changed) {
            container.setChanged();
        }

        return changed;
    }

    static void processLoadedContainers(ServerLevel level, long gameTime) {
        var chunks = ((ChunkMapAccessor) level.getChunkSource().chunkMap)
                .rotborn$visibleChunkMap()
                .values();

        // We need to know if its needs
        if (FMLEnvironment.isProduction()) return;

        for (ChunkHolder chunkHolder : chunks) {
            LevelChunk chunk = chunkHolder.getTickingChunk();
            if (chunk == null) {
                continue;
            }

            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                processBlockEntity(level, blockEntity, gameTime);
            }
        }
    }

    private static boolean processBlockEntity(ServerLevel level, BlockEntity blockEntity, long gameTime) {
        if (blockEntity.isRemoved() || !blockEntity.hasLevel()) {
            return false;
        }

        if (shouldSkipBlockEntity(blockEntity)) {
            return false;
        }

        // System.out.println(blockEntity+blockEntity.getBlockPos().toShortString());

        boolean changed = false;

        if (blockEntity instanceof Container container
                && (container instanceof RandomizableContainerBlockEntity
                || blockEntity.is(RotTags.TICKING_CONTAINER)
        )) {
            changed |= processContainer(level, container, gameTime);
        } else {
            changed |= processBlockItemStorage(level, blockEntity, gameTime);
        }

        if (changed) {
            blockEntity.setChanged();
        }

        return changed;
    }

    private static boolean shouldSkipBlockEntity(BlockEntity blockEntity) {
        if (blockEntity instanceof RandomizableContainerBlockEntity randomizable
                && randomizable.getLootTable() != null) {
            return true;
        }

        if (!(blockEntity instanceof RandomizableContainerBlockEntity r)) {
            if (!blockEntity.is(RotTags.TICKING_CONTAINER)) {
                return true;
            }
        }

        BlockState state = blockEntity.getBlockState();

        return state.hasProperty(BlockStateProperties.CHEST_TYPE)
                && state.getValue(BlockStateProperties.CHEST_TYPE) == ChestType.RIGHT;
    }

    private static ProcessResult processStack(ServerLevel level, ItemStack stack, long gameTime) {
        if (stack.isEmpty()) {
            return new ProcessResult(stack, false);
        }

        RotRule.RotOutcome rule = RotRule.getRotRule(level, stack).orElse(null);

        if (rule == null) {
            if (stack.has(ModDataComponents.ROT_DATA.get())) {
                stack.remove(ModDataComponents.ROT_DATA.get());
                return new ProcessResult(stack, true);
            }

            return new ProcessResult(stack, false);
        }

        RotData rotData = stack.get(ModDataComponents.ROT_DATA.get());

        if (rotData == null) {
            stack.set(
                    ModDataComponents.ROT_DATA.get(),
                    new RotData(gameTime, rule.rotAfterTicks())
            );
            return new ProcessResult(stack, true);
        }

        if (!rotData.expired(gameTime)) {
            return new ProcessResult(stack, false);
        }

        return new ProcessResult(rule.createResult(stack), true);
    }


    private static boolean processBlockItemStorage(ServerLevel level, BlockEntity be, long gameTime) {
        if (!be.is(RotTags.TICKING_ENTITY)) return false;

        var handler = level.getCapability(
                Capabilities.Item.BLOCK,
                be.getBlockPos(),
                null
        );

        if (handler == null || handler.size() <= 0) {
            return false;
        }

        return processResourceHandler(level, handler, gameTime);
    }

    static boolean processResourceHandler(
            ServerLevel level,
            ResourceHandler<ItemResource> handler,
            long gameTime
    ) {
        boolean changed = false;

        for (int slot = 0; slot < handler.size(); slot++) {
            ItemResource resource = handler.getResource(slot);
            if (resource.isEmpty()) {
                continue;
            }

            int count = handler.getAmountAsInt(slot);
            ItemStack oldStack = resource.toStack(count);

            ProcessResult result = processStack(level, oldStack, gameTime);
            if (!result.changed()) {
                continue;
            }

            ItemStack newStack = result.stack();

            try (Transaction tx = Transaction.openRoot()) {
                int extracted = handler.extract(slot, resource, count, tx);
                if (extracted != count) {
                    continue;
                }

                if (!newStack.isEmpty()) {
                    int inserted = handler.insert(slot, ItemResource.of(newStack), newStack.getCount(), tx);
                    if (inserted != newStack.getCount()) {
                        continue;
                    }
                }

                tx.commit();
                changed = true;
            }
        }

        return changed;
    }

    private static boolean processItemStackStorage(ServerLevel level, ItemStack containerStack, long gameTime) {
        if (containerStack.isEmpty()) {
            return false;
        }

        ResourceHandler<ItemResource> handler;
        try {
            handler = containerStack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(containerStack));
        } catch (Exception e) {
            return false;
        }

        if (handler == null || handler.size() <= 0) {
            return false;
        }

        return processResourceHandler(level, handler, gameTime);
    }

    static void processItemEntity(ServerLevel level, ItemEntity itemEntity, long gameTime) {
        if (!itemEntity.isAlive()) {
            return;
        }

        ItemStack oldStack = itemEntity.getItem();
        ProcessResult result = processStack(level, oldStack, gameTime);

        if (!result.changed()) {
            return;
        }

        ItemStack newStack = result.stack();

        if (newStack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(newStack == oldStack ? newStack.copy() : newStack);
            itemEntity.needsSync = true;
        }
    }


}
