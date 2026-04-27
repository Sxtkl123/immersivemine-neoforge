package com.sxtkl.immersiveminer.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MineUtils {

    private static final BlockPos[] NEIGHBORS = new BlockPos[] {
        new BlockPos(1, 1, 1), new BlockPos(1, 1, 0), new BlockPos(1, 1, -1),
        new BlockPos(1, 0, 1), new BlockPos(1, 0, 0), new BlockPos(1, 0, -1),
        new BlockPos(1, -1, 1), new BlockPos(1, -1, 0), new BlockPos(1, -1, -1),
        new BlockPos(0, 1, 1), new BlockPos(0, 1, 0), new BlockPos(0, 1, -1),
        new BlockPos(0, 0, 1), new BlockPos(0, 0, -1),
        new BlockPos(0, -1, 1), new BlockPos(0, -1, 0), new BlockPos(0, -1, -1),
        new BlockPos(-1, 1, 1), new BlockPos(-1, 1, 0), new BlockPos(-1, 1, -1),
        new BlockPos(-1, 0, 1), new BlockPos(-1, 0, 0), new BlockPos(-1, 0, -1),
        new BlockPos(-1, -1, 1), new BlockPos(-1, -1, 0), new BlockPos(-1, -1, -1)
    };

    /** 获取安全破坏点（DFS树叶子节点） */
    public static BlockPos getSafeBlock(Level level, BlockPos start) {
        return traverse(level, start, new VeinVisitor<>() {
            @Override public BlockPos onInvalid() { return start; }
            @Override public BlockPos onVisit(BlockPos pos, int size) { return null; }
            @Override public BlockPos onLeaf(BlockPos pos, int size) { return pos; } //  找到叶子立即返回
            @Override public BlockPos onComplete(int size) { return start; }
        });
    }

    /** 获取矿脉总大小（用于 Jade 显示） */
    public static int getUltimineSize(Level level, BlockPos start) {
        return traverse(level, start, new VeinVisitor<>() {
            @Override public Integer onInvalid() { return -1; }
            @Override public Integer onVisit(BlockPos pos, int size) { return null; }
            @Override public Integer onLeaf(BlockPos pos, int size) { return null; }
            @Override public Integer onComplete(int size) { return size > 256 ? -1 : size; }
        });
    }

    /**
     * 统一 DFS 遍历骨架
     * @param visitor 自定义遍历行为回调
     */
    private static <T> T traverse(Level level, BlockPos start, VeinVisitor<T> visitor) {
        BlockState state = level.getBlockState(start);
        if (state.isAir()) return visitor.onInvalid();

        Block target = state.getBlock();
        Set<BlockPos> visited = new HashSet<>();
        Deque<BlockPos> stack = new ArrayDeque<>();

        visited.add(start);
        stack.push(start);

        // 起点也可能直接触发回调
        T initial = visitor.onVisit(start, 1);
        if (initial != null) return initial;

        while (!stack.isEmpty()) {
            BlockPos current = stack.pop();
            boolean isLeaf = true;

            for (BlockPos offset : NEIGHBORS) {
                BlockPos neighbor = current.offset(offset);
                if (visited.contains(neighbor)) continue;

                if (level.getBlockState(neighbor).is(target)) {
                    visited.add(neighbor);
                    stack.push(neighbor);
                    isLeaf = false;

                    T res = visitor.onVisit(neighbor, visited.size());
                    if (res != null) return res;
                }
            }

            if (isLeaf) {
                T res = visitor.onLeaf(current, visited.size());
                if (res != null) return res;
            }

            if (visited.size() >= 256) break;
        }

        return visitor.onComplete(visited.size());
    }

    /** 遍历行为定义接口 */
    public interface VeinVisitor<T> {
        T onInvalid();                            // 起点无效（空气/非方块）
        T onVisit(BlockPos pos, int currentSize); // 每次发现新方块时调用
        T onLeaf(BlockPos pos, int currentSize);  // 当前方块为叶子节点时调用
        T onComplete(int totalSize);              // 遍历正常结束或触达上限时调用
    }
}