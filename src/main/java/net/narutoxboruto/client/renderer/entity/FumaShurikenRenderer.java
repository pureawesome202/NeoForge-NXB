package net.narutoxboruto.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.narutoxboruto.entities.throwables.AbstractThrowableWeapon;

public class FumaShurikenRenderer <T extends AbstractThrowableWeapon> extends ThrowableWeaponRenderer<T> {

    public FumaShurikenRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    protected void setupRotations(T pEntity, float pPartialTicks, PoseStack pMatrixStack) {
        float spin = pEntity.getFumaSpin(pPartialTicks);
        super.setupRotations(pEntity, pPartialTicks, pMatrixStack);
        pMatrixStack.mulPose(com.mojang.math.Axis.ZP.rotation(spin));
        pMatrixStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90));
    }
}
