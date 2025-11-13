package net.narutoxboruto.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.modes.NarutoRun;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer {

    @Inject(method = "setupRotations", at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD)

    private void onSetupRotations(AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, float unusedFloat, CallbackInfo ci) { // Added the missing float parameter
        NarutoRun narutoRun = player.getData(MainAttachment.NARUTO_RUN.get());

        if (narutoRun != null && narutoRun.isActive()) {
            poseStack.translate(0, 0.2, 1);
            poseStack.mulPose(Axis.XP.rotation(-0.7854F));
        }
    }
}

