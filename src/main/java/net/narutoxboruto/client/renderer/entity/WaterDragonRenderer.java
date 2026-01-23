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
import net.narutoxboruto.entities.jutsus.WaterDragonEntity;
import net.narutoxboruto.main.Main;
import org.joml.Matrix4f;

/**
 * PLACEHOLDER renderer for Water Dragon entity.
 * 
 * TODO: This is a TEMPORARY implementation!
 * Awaiting from owner:
 * - Custom dragon model
 * - Custom textures
 * - Animation system
 * 
 * Current: Simple elongated blue shape representing the dragon body.
 * Replace entire renderer when custom model is provided.
 */
public class WaterDragonRenderer extends EntityRenderer<WaterDragonEntity> {
    
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        Main.MOD_ID, "textures/entity/water_dragon_placeholder.png"
    );
    
    public WaterDragonRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    
    @Override
    public void render(WaterDragonEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, 
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        
        // Orient the dragon in the direction it's moving
        float yaw = entity.getYRot();
        float pitch = entity.getXRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
        
        // Scale to make it larger (dragon-like proportions)
        // TODO: Adjust scale when custom model is provided
        poseStack.scale(3.0F, 1.5F, 1.5F);
        
        // Render a simple elongated shape (placeholder for dragon model)
        RenderType renderType = RenderType.entityTranslucent(TEXTURE);
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        
        // Draw a simple elongated box shape
        renderDragonBody(poseStack, vertexConsumer, packedLight);
        
        poseStack.popPose();
        
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    
    /**
     * Render a simple elongated box as placeholder for dragon body.
     * TODO: Replace with actual dragon model rendering.
     */
    private void renderDragonBody(PoseStack poseStack, VertexConsumer consumer, int light) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        
        // Simple elongated box vertices (dragon body shape)
        float length = 1.5F;
        float height = 0.5F;
        float width = 0.5F;
        
        // Front face (dragon head)
        vertex(consumer, matrix, -width, -height, length, 0, 0, light);
        vertex(consumer, matrix, width, -height, length, 1, 0, light);
        vertex(consumer, matrix, width, height, length, 1, 1, light);
        vertex(consumer, matrix, -width, height, length, 0, 1, light);
        
        // Back face (dragon tail)
        vertex(consumer, matrix, -width * 0.5f, -height * 0.5f, -length, 0, 0, light);
        vertex(consumer, matrix, -width * 0.5f, height * 0.5f, -length, 0, 1, light);
        vertex(consumer, matrix, width * 0.5f, height * 0.5f, -length, 1, 1, light);
        vertex(consumer, matrix, width * 0.5f, -height * 0.5f, -length, 1, 0, light);
        
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
                .setColor(0x80, 0xC0, 0xFF, 0xE0) // Light blue with transparency
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);
    }
    
    @Override
    public ResourceLocation getTextureLocation(WaterDragonEntity entity) {
        return TEXTURE;
    }
}
