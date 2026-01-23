package net.narutoxboruto.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.narutoxboruto.main.Main;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.modes.ChakraControl;
import net.narutoxboruto.attachments.modes.WallRunning;
import net.narutoxboruto.util.RotationUtil;

/**
 * Handles wall and ceiling running physics when chakra control is active.
 * When sprinting into walls, automatically transitions player to walk on the wall surface
 * as if it's the new ground plane (like a half-pipe or auto-jump).
 */
@EventBusSubscriber(modid = Main.MOD_ID)
public class WallRunningHandler {
    
    private static final int MIN_WALL_HEIGHT = 2; // Minimum blocks tall for wall running
    
    // Normal eye height - this is how far camera is from ground when standing
    // When on a wall, camera should be this far from the wall surface
    private static final double EYE_HEIGHT = 1.62;
    
    // Cache to track if player is wall running (workaround for attachment sync issues)
    // Made public static so MixinEntity can access it directly
    public static RotationUtil.Surface lastSetSurface = RotationUtil.Surface.GROUND;
    
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        
        // Get player's chakra control and wall running state
        ChakraControl chakraControl = player.getData(MainAttachment.CHAKRA_CONTROL);
        WallRunning wallRunning = player.getData(MainAttachment.WALL_RUNNING);
        int chakra = player.getData(MainAttachment.CHAKRA).getValue();
        
        // Debug: Check if the surface persisted
        if (player.tickCount % 20 == 0 && lastSetSurface != RotationUtil.Surface.GROUND) {
            System.out.println("[WallRunHandler] Last set: " + lastSetSurface + 
                             ", getData: " + wallRunning.getSurface() +
                             ", isWallRunning: " + wallRunning.isWallRunning());
        }
        
        // If chakra control is off or no chakra, reset to ground
        if (!chakraControl.isActive() || chakra <= 0) {
            if (wallRunning.isWallRunning() || lastSetSurface != RotationUtil.Surface.GROUND) {
                System.out.println("[WallRun] Resetting due to chakra control off");
                wallRunning.reset(player);
                lastSetSurface = RotationUtil.Surface.GROUND;
            }
            return;
        }
        
        // If on ground and not moving toward any wall, reset to allow fresh attachment
        if (lastSetSurface == RotationUtil.Surface.GROUND && !wallRunning.isWallRunning()) {
            // This ensures clean state for next attachment
            lastSetSurface = RotationUtil.Surface.GROUND;
        }
        
        // No additional chakra drain - uses same drain as water walking (base chakra control drain)
        
        // Check for surface transitions (detect walls, handle detachment)
        detectAndUpdateSurface(player, wallRunning);
        
        // Enforce wall offset to prevent model clipping into blocks
        // This keeps the player at the correct distance from the wall surface
        enforceWallOffset(player, lastSetSurface);
        
        // Physics is handled by MixinLivingEntityTravel - it intercepts travel()
        // and converts forward movement to climbing when wall running
    }
    
    /**
     * Keep player at the correct distance from the wall surface.
     * Player hitbox MUST be outside the wall to prevent suffocation.
     * Model will be rotated to appear feet-on-wall by rendering system.
     */
    private static void enforceWallOffset(ServerPlayer player, RotationUtil.Surface surface) {
        if (surface == RotationUtil.Surface.GROUND) return;
        
        Vec3 pos = player.position();
        BlockPos playerBlock = player.blockPosition();
        Direction wallDir = getWallDirection(surface);
        
        if (wallDir == null) return;
        
        // Find the actual wall block
        BlockPos wallBlock = playerBlock.relative(wallDir);
        Level level = player.level();
        if (!level.getBlockState(wallBlock).isSolid()) {
            wallBlock = playerBlock.above().relative(wallDir);
            if (!level.getBlockState(wallBlock).isSolid()) {
                return; // No wall found, don't adjust position
            }
        }
        
        // Player hitbox is 0.6 wide (0.3 from center), 1.8 tall
        // Position must be OUTSIDE the wall block by at least 0.3 to prevent suffocation
        // Add a larger margin to avoid clipping
        double safeDistance = 0.55;
        
        // Calculate target position: wall surface + safe distance
        // The model will be ROTATED by rendering to appear on the wall
        Vec3 targetPos = pos;
        switch (wallDir) {
            case NORTH -> {
                double wallSurface = wallBlock.getZ(); // North face at min Z
                targetPos = new Vec3(pos.x, pos.y, wallSurface + safeDistance); // push south (+Z)
            }
            case SOUTH -> {
                double wallSurface = wallBlock.getZ() + 1.0; // South face at max Z
                targetPos = new Vec3(pos.x, pos.y, wallSurface - safeDistance); // push north (-Z)
            }
            case EAST -> {
                double wallSurface = wallBlock.getX() + 1.0; // East face at max X
                targetPos = new Vec3(wallSurface - safeDistance, pos.y, pos.z); // push west (-X)
            }
            case WEST -> {
                double wallSurface = wallBlock.getX(); // West face at min X
                targetPos = new Vec3(wallSurface + safeDistance, pos.y, pos.z); // push east (+X)
            }
            case UP -> {
                BlockPos ceilingBlock = playerBlock.above(2);
                double ceilingY = ceilingBlock.getY();
                targetPos = new Vec3(pos.x, ceilingY - 2.0, pos.z);
            }
            default -> {
                return;
            }
        }
        
        // Smoothly interpolate to target position (prevents snapping)
        Vec3 smoothPos = pos.lerp(targetPos, 0.3);
        player.setPos(smoothPos.x, smoothPos.y, smoothPos.z);
    }
    
    private static Direction getWallDirection(RotationUtil.Surface surface) {
        return switch (surface) {
            case NORTH_WALL -> Direction.NORTH;
            case SOUTH_WALL -> Direction.SOUTH;
            case EAST_WALL -> Direction.EAST;
            case WEST_WALL -> Direction.WEST;
            case CEILING -> Direction.UP;
            default -> null;
        };
    }
    
    /**
     * Detect which surface the player should be on and update accordingly.
     * Handles transitions: ground→wall, wall→ceiling, ceiling→wall, wall→ground
     * This allows walking in a full circle in a closed room.
     */
    private static void detectAndUpdateSurface(ServerPlayer player, WallRunning wallRunning) {
        Level level = player.level();
        BlockPos playerPos = player.blockPosition();
        
        // Use our cached surface, not the potentially stale getData() value
        RotationUtil.Surface currentSurface = lastSetSurface;
        
        Vec3 lookDir = player.getLookAngle();
        
        // Check for surface transitions based on current surface
        if (currentSurface == RotationUtil.Surface.GROUND) {
            // From ground: check for walls in ALL 4 directions, not just where looking
            // This ensures we attach even if walking sideways into a wall
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                if (tryAttachToWall(player, wallRunning, level, playerPos, dir)) {
                    return;
                }
            }
        } else if (currentSurface == RotationUtil.Surface.CEILING) {
            // From ceiling: check for walls when looking horizontally, or ground when looking down
            Direction lookingDir = Direction.getNearest((float)lookDir.x, 0, (float)lookDir.z);
            
            // Check if we should transition to a wall
            if (tryAttachToWallFromCeiling(player, wallRunning, level, playerPos, lookingDir)) {
                return;
            }
            
            // Check if we should drop to ground (looking down and no ceiling block)
            BlockPos ceilingCheck = playerPos.above(2);
            if (!level.getBlockState(ceilingCheck).isSolid()) {
                wallRunning.reset(player);
                return;
            }
        } else {
            // Currently on a wall - handle wrap-around and surface transitions
            // Use our cached surface, NOT the stale data attachment
            Direction wallDir = getSurfaceDirection(currentSurface);
            BlockPos feetPos = player.blockPosition();
            Vec3 velocity = player.getDeltaMovement();
            
            // Determine if player is moving UP or DOWN on the wall (in world Y)
            boolean movingUp = velocity.y > 0.01;
            boolean movingDown = velocity.y < -0.01;
            
            // Check for 1-block step transitions (treat as slopes/stairs)
            // If there's a 1-block height difference, smoothly transition to climbing it
            BlockPos stepCheck = feetPos.relative(wallDir).above();
            boolean hasStepAbove = level.getBlockState(stepCheck).isSolid();
            
            if (hasStepAbove && movingUp) {
                // Treat this as a natural climb - the movement system will handle it
                // Just ensure we stay attached to the wall
                return;
            }
            
            // Check if there's still wall at current position
            // Must be solid block directly in front of player at this height
            BlockPos wallCheck = feetPos.relative(wallDir);
            BlockPos wallCheckAbove = wallCheck.above();
            boolean hasWallHere = level.getBlockState(wallCheck).isSolid() ||
                                level.getBlockState(wallCheckAbove).isSolid();
            
            // If no wall at player position, detach immediately
            if (!hasWallHere) {
                // Try to wrap to adjacent surfaces
                
                // TOP - Check for ceiling
                BlockPos ceilingCheck = feetPos.above(2);
                if (level.getBlockState(ceilingCheck).isSolid() && movingUp) {
                    wallRunning.setSurface(RotationUtil.Surface.CEILING, player);
                    return;
                }
                
                // BOTTOM - Check for ground (use actual ground block check, not onGround() which we override)
                BlockPos groundCheck = feetPos.below();
                boolean actuallyOnGround = level.getBlockState(groundCheck).isSolid() && 
                                          (player.getY() - Math.floor(player.getY()) < 0.1);
                if (actuallyOnGround) {
                    System.out.println("[WallRun] Detaching from wall - reached ground");
                    wallRunning.reset(player);
                    lastSetSurface = RotationUtil.Surface.GROUND;
                    return;
                }
                
                // CORNERS - Check for perpendicular walls
                for (Direction adjacentDir : Direction.Plane.HORIZONTAL) {
                    if (adjacentDir != wallDir && adjacentDir != wallDir.getOpposite()) {
                        BlockPos adjacentWall = feetPos.relative(adjacentDir);
                        if (level.getBlockState(adjacentWall).isSolid() ||
                            level.getBlockState(adjacentWall.above()).isSolid()) {
                            RotationUtil.Surface newSurface = WallRunning.surfaceFromDirection(adjacentDir);
                            wallRunning.setSurface(newSurface, player);
                            return;
                        }
                    }
                }
                
                // No surface to wrap to - fall off
                System.out.println("[WallRun] Detaching from wall - no surface to wrap to");
                wallRunning.reset(player);
                lastSetSurface = RotationUtil.Surface.GROUND;
                return;
            }
            
            // Wall still present - stay attached
        }
    }
    
    /**
     * Try to attach to a wall in the given direction.
     * Returns true if attached, false otherwise.
     */
    private static boolean tryAttachToWall(ServerPlayer player, WallRunning wallRunning, 
            Level level, BlockPos playerPos, Direction dir) {
        BlockPos wallCheck = playerPos.relative(dir);
        BlockPos wallCheckAbove = wallCheck.above();
        
        boolean solidAtFeet = level.getBlockState(wallCheck).isSolid();
        boolean solidAtHead = level.getBlockState(wallCheckAbove).isSolid();
        
        double distToWall = getDistanceToBlockEdge(player, dir);
        boolean closeToWall = distToWall < 0.7; // Increased from 0.4 for easier attachment
        
        // Check if actually on ground (not using onGround() which we override)
        BlockPos groundCheck = playerPos.below();
        boolean actuallyOnGround = level.getBlockState(groundCheck).isSolid() && 
                                  (player.getY() - Math.floor(player.getY()) < 0.1);
        
        // Check if moving toward the wall
        Vec3 velocity = player.getDeltaMovement();
        boolean movingTowardWall = switch (dir) {
            case NORTH -> velocity.z < -0.01;  // Moving in -Z direction
            case SOUTH -> velocity.z > 0.01;   // Moving in +Z direction
            case WEST -> velocity.x < -0.01;   // Moving in -X direction
            case EAST -> velocity.x > 0.01;    // Moving in +X direction
            default -> false;
        };
        
        // Can attach if:
        // 1. Wall exists (solid at feet or head)
        // 2. Close enough to wall
        // 3. Either on ground OR already wall running OR moving toward wall
        boolean canAttach = (solidAtFeet || solidAtHead) && closeToWall && 
                           (actuallyOnGround || wallRunning.isWallRunning() || movingTowardWall);
        
        // Debug logging every 20 ticks
        if (player.tickCount % 20 == 0 && (solidAtFeet || solidAtHead)) {
            System.out.println("=== WALL ATTACH ATTEMPT ===");
            System.out.println("Direction: " + dir);
            System.out.println("Solid at feet: " + solidAtFeet);
            System.out.println("Solid at head: " + solidAtHead);
            System.out.println("Distance to wall: " + distToWall);
            System.out.println("Close enough: " + closeToWall);
            System.out.println("Actually on ground: " + actuallyOnGround);
            System.out.println("Moving toward wall: " + movingTowardWall);
            System.out.println("Already wall running: " + wallRunning.isWallRunning());
            System.out.println("lastSetSurface: " + lastSetSurface);
            System.out.println("Can attach: " + canAttach);
            System.out.println("Current surface: " + wallRunning.getSurface());
            System.out.println("========================");
        }
        
        if (canAttach) {
            RotationUtil.Surface newSurface = WallRunning.surfaceFromDirection(dir);
            if (lastSetSurface != newSurface) {
                System.out.println(">>> ATTACHING TO WALL: " + newSurface + " <<<");
                lastSetSurface = newSurface;
            }
            wallRunning.setSurface(newSurface, player);
            
            return true;
        }
        return false;
    }
    
    /**
     * Try to attach to a wall when currently on ceiling.
     */
    private static boolean tryAttachToWallFromCeiling(ServerPlayer player, WallRunning wallRunning,
            Level level, BlockPos playerPos, Direction dir) {
        // When on ceiling, walls are "below" us in the new orientation
        // Check for solid blocks to the side at ceiling level
        BlockPos wallCheck = playerPos.relative(dir);
        BlockPos wallCheckAbove = wallCheck.above();
        
        boolean solidAtLevel = level.getBlockState(wallCheck).isSolid();
        boolean solidAbove = level.getBlockState(wallCheckAbove).isSolid();
        
        double distToWall = getDistanceToBlockEdge(player, dir);
        boolean closeToWall = distToWall < 0.4;
        
        if ((solidAtLevel || solidAbove) && closeToWall) {
            RotationUtil.Surface newSurface = WallRunning.surfaceFromDirection(dir);
            wallRunning.setSurface(newSurface, player);
            return true;
        }
        return false;
    }
    
    /**
     * Calculate how close the player is to the edge of a block in the given direction.
     * Returns 0.0 when touching the block, up to 1.0 when at the far edge of their current block.
     */
    private static double getDistanceToBlockEdge(ServerPlayer player, Direction dir) {
        double x = player.getX();
        double z = player.getZ();
        
        // Player hitbox is 0.6 wide (0.3 from center)
        double halfWidth = 0.3;
        
        return switch (dir) {
            case NORTH -> (z % 1.0 + 1.0) % 1.0 - halfWidth; // Distance to north edge (negative Z)
            case SOUTH -> 1.0 - ((z % 1.0 + 1.0) % 1.0) - halfWidth; // Distance to south edge (positive Z)
            case WEST -> (x % 1.0 + 1.0) % 1.0 - halfWidth; // Distance to west edge (negative X)
            case EAST -> 1.0 - ((x % 1.0 + 1.0) % 1.0) - halfWidth; // Distance to east edge (positive X)
            default -> 1.0;
        };
    }
    
    /**
     * Get distance from player to the nearest wall block in the given direction.
     * Returns absolute distance (always positive).
     */
    private static double getDistanceToWall(ServerPlayer player, Direction dir) {
        BlockPos playerPos = player.blockPosition();
        Level level = player.level();
        
        // Find the wall block - check up to 3 blocks away
        for (int i = 1; i <= 3; i++) {
            BlockPos checkPos = playerPos.relative(dir, i);
            if (level.getBlockState(checkPos).isSolid() || 
                level.getBlockState(checkPos.above()).isSolid() ||
                level.getBlockState(checkPos.below()).isSolid()) {
                // Found wall, calculate exact distance
                double playerCoord = (dir.getAxis() == Direction.Axis.X) ? player.getX() : player.getZ();
                double wallCoord;
                
                if (dir == Direction.NORTH) {
                    wallCoord = checkPos.getZ() + 1.0;
                    return Math.abs(playerCoord - wallCoord);
                } else if (dir == Direction.SOUTH) {
                    wallCoord = checkPos.getZ();
                    return Math.abs(wallCoord - playerCoord);
                } else if (dir == Direction.EAST) {
                    wallCoord = checkPos.getX();
                    return Math.abs(wallCoord - playerCoord);
                } else if (dir == Direction.WEST) {
                    wallCoord = checkPos.getX() + 1.0;
                    return Math.abs(playerCoord - wallCoord);
                }
            }
        }
        return 10.0; // No wall found
    }
    
    /**
     * Snap the player's position directly to the wall surface when attaching.
     * This ensures the model appears right at the wall, not floating.
     */
    private static void snapToWall(ServerPlayer player, Direction wallDir, BlockPos wallBlock) {
        Vec3 pos = player.position();
        
        // Position player so their center is right at the wall surface
        // Use tiny offset to prevent z-fighting
        float offset = 0.01f;
        
        switch (wallDir) {
            case NORTH -> {
                double wallSurface = wallBlock.getZ() + 1.0;
                player.setPos(pos.x, pos.y, wallSurface + offset);
            }
            case SOUTH -> {
                double wallSurface = wallBlock.getZ();
                player.setPos(pos.x, pos.y, wallSurface - offset);
            }
            case EAST -> {
                double wallSurface = wallBlock.getX();
                player.setPos(wallSurface - offset, pos.y, pos.z);
            }
            case WEST -> {
                double wallSurface = wallBlock.getX() + 1.0;
                player.setPos(wallSurface + offset, pos.y, pos.z);
            }
            default -> {}
        }
    }
    
    /**
     * Adjust player position to avoid suffocation when bounding box rotates.
     * Based on Gravity API's adjustEntityPosition (lines 129-158).
     * 
     * When the bounding box is suddenly rotated (e.g., from GROUND to NORTH_WALL),
     * the player might be partially inside blocks. This method finds nearby colliding
     * blocks and pushes the player out by the minimum distance needed.
     */
    private static void adjustEntityPosition(ServerPlayer player, RotationUtil.Surface surface) {
        if (surface == RotationUtil.Surface.GROUND) return;
        
        // Get player's rotated bounding box
        net.minecraft.world.phys.AABB boundingBox = player.getBoundingBox();
        Level world = player.level();
        
        // Find all nearby block collisions
        // Check blocks within the expanded bounding box
        net.minecraft.world.phys.AABB expandedBox = boundingBox.inflate(1.0);
        
        java.util.List<net.minecraft.world.phys.AABB> collidingBoxes = new java.util.ArrayList<>();
        
        for (BlockPos pos : BlockPos.betweenClosed(
                (int) Math.floor(expandedBox.minX),
                (int) Math.floor(expandedBox.minY),
                (int) Math.floor(expandedBox.minZ),
                (int) Math.floor(expandedBox.maxX),
                (int) Math.floor(expandedBox.maxY),
                (int) Math.floor(expandedBox.maxZ))) {
            
            BlockState blockState = world.getBlockState(pos);
            if (!blockState.isAir()) {
                net.minecraft.world.phys.shapes.VoxelShape shape = blockState.getCollisionShape(world, pos);
                if (!shape.isEmpty()) {
                    net.minecraft.world.phys.AABB blockBox = shape.bounds().move(pos);
                    if (blockBox.intersects(boundingBox)) {
                        collidingBoxes.add(blockBox);
                    }
                }
            }
        }
        
        // If no collisions, position is fine
        if (collidingBoxes.isEmpty()) {
            return;
        }
        
        // Calculate union of all colliding boxes
        net.minecraft.world.phys.AABB collisionUnion = collidingBoxes.get(0);
        for (int i = 1; i < collidingBoxes.size(); i++) {
            collisionUnion = collisionUnion.minmax(collidingBoxes.get(i));
        }
        
        // Determine which direction to push the player based on surface orientation
        // Push AWAY from the wall surface (opposite of wall normal)
        Direction pushDirection = switch (surface) {
            case NORTH_WALL -> Direction.SOUTH;  // Wall faces north, push player south
            case SOUTH_WALL -> Direction.NORTH;  // Wall faces south, push player north
            case EAST_WALL -> Direction.WEST;    // Wall faces east, push player west
            case WEST_WALL -> Direction.EAST;    // Wall faces west, push player east
            case CEILING -> Direction.DOWN;      // Ceiling faces up, push player down
            case GROUND -> Direction.UP;         // Should not happen, but just in case
        };
        
        // Calculate offset needed to push player out of collision
        Vec3 offset = getPositionAdjustmentOffset(boundingBox, collisionUnion, pushDirection);
        
        // Apply the adjustment
        if (offset.lengthSqr() > 0) {
            Vec3 newPos = player.position().add(offset);
            player.setPos(newPos.x, newPos.y, newPos.z);
        }
    }
    
    /**
     * Calculate the minimum offset needed to move the player out of collision.
     * Based on Gravity API's getPositionAdjustmentOffset (lines 160-182).
     */
    private static Vec3 getPositionAdjustmentOffset(
            net.minecraft.world.phys.AABB entityBox,
            net.minecraft.world.phys.AABB collisionBox,
            Direction movingDirection) {
        
        Direction.Axis axis = movingDirection.getAxis();
        double offset = 0;
        
        if (movingDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            // Moving in positive direction (e.g., EAST, UP, SOUTH)
            // Check if collision is pushing from the positive side
            double pushing = collisionBox.max(axis);
            double pushed = entityBox.min(axis);
            if (pushing > pushed) {
                offset = pushing - pushed + 0.01; // Small safety margin
            }
        } else {
            // Moving in negative direction (e.g., WEST, DOWN, NORTH)
            // Check if collision is pushing from the negative side
            double pushing = collisionBox.min(axis);
            double pushed = entityBox.max(axis);
            if (pushing < pushed) {
                offset = -(pushed - pushing + 0.01); // Small safety margin
            }
        }
        
        // Convert offset to vector in the moving direction
        Vec3 unitVec = Vec3.atLowerCornerOf(movingDirection.getNormal());
        return unitVec.scale(Math.abs(offset));
    }
    
    /**
     * Get the direction a surface is facing (toward the wall/ceiling/floor).
     * This is a static version that doesn't rely on the data attachment.
     */
    private static Direction getSurfaceDirection(RotationUtil.Surface surface) {
        return switch (surface) {
            case GROUND -> Direction.DOWN;
            case NORTH_WALL -> Direction.NORTH;
            case SOUTH_WALL -> Direction.SOUTH;
            case EAST_WALL -> Direction.EAST;
            case WEST_WALL -> Direction.WEST;
            case CEILING -> Direction.UP;
        };
    }
}
