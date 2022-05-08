package com.diamantino.electromatic.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AABBUtils {

    public static AABB expand(AABB aabb, double amt) {
        return aabb.inflate(amt, amt, amt);
    }

    public static AABB translate(AABB aabb, double x, double y, double z) {
        return aabb.move(x,y,z);
    }

    public static AABB rotate (AABB aabb, Direction facing){
        switch (facing){
            case UP:
                return aabb;
            case DOWN:
                return new AABB(aabb.minX, 1 - aabb.minY, aabb.minZ, aabb.maxX, 1 - aabb.maxY, aabb.maxZ);
            case EAST:
                return new AABB(aabb.minY, 1 - aabb.minZ, 1 - aabb.minX, aabb.maxY, 1 - aabb.maxZ, 1 - aabb.maxX);
            case WEST:
                return new AABB(1 - aabb.minY, 1 - aabb.minZ, aabb.minX, 1 - aabb.maxY, 1 - aabb.maxZ, aabb.maxX);
            case NORTH:
                return new AABB(1 - aabb.minX, 1 - aabb.minZ, 1 - aabb.minY, 1 - aabb.maxX, 1 - aabb.maxZ, 1 - aabb.maxY);
            case SOUTH:
                return new AABB(aabb.minX, 1 - aabb.minZ, aabb.minY, aabb.maxX, 1 - aabb.maxZ, aabb.maxY);
        }
        return aabb;
    }

    /**
     * Returns true if the shapes intersect.
     */
    public static Boolean testOcclusion (VoxelShape shape1, VoxelShape shape2){
        return shape1.toAabbs().stream().anyMatch(s ->
                shape2.toAabbs().stream().anyMatch(s::intersects));
    }

    public static VoxelShape rotate (VoxelShape shape, Direction facing){
        VoxelShape out = Shapes.empty();
        for(AABB aabb : shape.toAabbs()){
            aabb = rotate(aabb, facing);
            out = Shapes.or(out, Block.box(aabb.minX * 16, aabb.minY * 16, aabb.minZ * 16, aabb.maxX * 16, aabb.maxY * 16, aabb.maxZ * 16));
        }
        return out;
    }

}