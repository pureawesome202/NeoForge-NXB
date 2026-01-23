package net.narutoxboruto.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.entities.jutsus.SharkBombEntity;
import net.narutoxboruto.main.Main;
import org.joml.Matrix4f;

/**
 * Placeholder renderer for SharkBomb entity.
 * Uses a simple blue cube until custom model is provided.
 */
public class SharkBombRenderer extends EntityRenderer<SharkBombEntity> {
    
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        Main.MOD_ID, "textures/entity/shark_bomb_placeholder.png"
    );
    
    public SharkBombRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(SharkBombEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, 
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        
        // Orient the projectile in the direction it's moving
        float yaw = entity.getYRot();
        float pitch = entity.getXRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
        
        // Scale to make it visible (shark-like proportions)
        poseStack.scale(1.5F, 0.8F, 0.8F);
        
        // Render a simple elongated cube (placeholder for shark model)
        RenderType renderType = RenderType.entityCutoutNoCull(TEXTURE);
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        
        // Draw a simple box shape
        renderBox(poseStack, vertexConsumer, packedLight);
        
        poseStack.popPose();
        
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    
    /**
     * Render a simple box as placeholder.
     */
    private void renderBox(PoseStack poseStack, VertexConsumer consumer, int light) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        
        // Simple elongated box vertices (shark body shape)
        float length = 1.0F;
        float height = 0.4F;
        float width = 0.4F;
        
        // Front face
        vertex(consumer, matrix, -width, -height, length, 0, 0, light);
        vertex(consumer, matrix, width, -height, length, 1, 0, light);
        vertex(consumer, matrix, width, height, length, 1, 1, light);
        vertex(consumer, matrix, -width, height, length, 0, 1, light);
        
        // Back face
        vertex(consumer, matrix, -width, -height, -length, 0, 0, light);
        vertex(consumer, matrix, -width, height, -length, 0, 1, light);
        vertex(consumer, matrix, width, height, -length, 1, 1, light);
        vertex(consumer, matrix, width, -height, -length, 1, 0, light);
        
        // Top face
        vertex(consumer, matrix, -width, height, -length, 0, 0, light);
        vertex(consumer, matrix, -width, height, length, 0, 1, light);
        vertex(consumer, matrix, width, height, length, 1, 1, light);
        vertex(consumer, matrix, width, height, -length, 1, 0, light);
        
        // Bottom face
        vertex(consumer, matrix, -width, -height, -length, 0, 0, light);
        vertex(consumer, matrix, width, -height, -length, 1, 0, light);
        vertex(consumer, matrix, width, -height, length, 1, 1, light);
        vertex(consumer, matrix, -width, -height, length, 0, 1, light);
        
        // Left face
        vertex(consumer, matrix, -width, -height, -length, 0, 0, light);
        vertex(consumer, matrix, -width, -height, length, 1, 0, light);
        vertex(consumer, matrix, -width, height, length, 1, 1, light);
        vertex(consumer, matrix, -width, height, -length, 0, 1, light);
        
        // Right face
        vertex(consumer, matrix, width, -height, -length, 0, 0, light);
        vertex(consumer, matrix, width, height, -length, 0, 1, light);
        vertex(consumer, matrix, width, height, length, 1, 1, light);
        vertex(consumer, matrix, width, -height, length, 1, 0, light);
    }
    
    private void vertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, float u, float v, int light) {
        consumer.addVertex(matrix, x, y, z)
                .setColor(0xFF, 0xFF, 0xFF, 0xFF)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);
    }
    
    @Override
    public ResourceLocation getTextureLocation(SharkBombEntity entity) {
        return TEXTURE;
    }
}
