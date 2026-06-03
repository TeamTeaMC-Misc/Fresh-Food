package com.teamtea.fresh_food.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RottingBlock extends Block {
    public static final int MAX_AGE = 2;
    public static final IntegerProperty ROT_AGE = IntegerProperty.create("rot_age", 0, MAX_AGE);

    public RottingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ROT_AGE, 0));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return simpleCodec(RottingBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROT_AGE);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(ROT_AGE) < MAX_AGE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(ROT_AGE);
        if (age < MAX_AGE) {
            level.setBlock(pos, state.setValue(ROT_AGE, age + 1), 2);
        }
    }
}
