package com.teamtea.fresh_food.block;

import com.teamtea.fresh_food.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class IceBoxBlock extends BaseEntityBlock {
    public static final MapCodec<IceBoxBlock> CODEC = simpleCodec(IceBoxBlock::new);
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<Direction> FACING =
            BlockStateProperties.HORIZONTAL_FACING;

    public IceBoxBlock(Properties properties) {
        super(properties);
        registerDefaultState(
                stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(OPEN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(OPEN, FACING));
    }

    private static final VoxelShape NORTH_SHAPE =
            Block.box(1, 0, 2, 15, 10, 14);

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[] { shape, Shapes.empty() };

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;

        for (int i = 0; i < times; i++) {
            buffer[1] = Shapes.empty();

            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = Shapes.or(
                            buffer[1],
                            Shapes.box(
                                    1 - maxZ,
                                    minY,
                                    minX,
                                    1 - minZ,
                                    maxY,
                                    maxX
                            )
                    )
            );

            buffer[0] = buffer[1];
        }

        return buffer[0];
    }

    private static final Map<Direction, VoxelShape> SHAPES =
            Arrays.stream(Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new))
                    .collect(Collectors.toMap(
                            d -> d,
                            d -> rotateShape(Direction.NORTH, d, NORTH_SHAPE)
                    ));

    // private static final VoxelShape NORTH_SHAPE =
    //         Block.box(1, 0, 2, 15, 10, 14);
    //
    // private static final VoxelShape SOUTH_SHAPE =
    //         Block.box(1, 0, 2, 15, 10, 14);
    //
    // private static final VoxelShape EAST_SHAPE =
    //         Block.box(2, 0, 1, 14, 10, 15);
    //
    // private static final VoxelShape WEST_SHAPE =
    //         Block.box(2, 0, 1, 14, 10, 15);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }



    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IceBoxBlockEntity(pos, state);
    }


    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        level.setBlock(pos, state.setValue(OPEN, !state.getValue(OPEN)), Block.UPDATE_CLIENTS);
        if (level.getBlockEntity(pos) instanceof IceBoxBlockEntity iceBox) {
            player.openMenu(iceBox);
        }
        return InteractionResult.CONSUME;
    }

    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(type, ModBlockEntities.ICE_BOX_ENTITY.get(), IceBoxBlockEntity::tick);
        } else {
            return null;
        }
    }

    protected static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expected, BlockEntityTicker<? super E> ticker) {
        return expected == actual ? (BlockEntityTicker<A>) ticker : null;
    }
}
