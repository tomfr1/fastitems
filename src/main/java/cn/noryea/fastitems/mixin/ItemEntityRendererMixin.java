package cn.noryea.fastitems.mixin;

import cn.noryea.fastitems.Flattenable;
import cn.noryea.fastitems.config.FastItemsConfig;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity, ItemEntityRenderState> {

    @Final
    @Shadow
    private Random random;

    protected ItemEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(
            method = "render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At("HEAD"),
            cancellable = true)
    private void fastitems$render(ItemEntityRenderState state, MatrixStack matrices,
                                  OrderedRenderCommandQueue queue, CameraRenderState camera, CallbackInfo ci) {
        if (!FastItemsConfig.enable) return;
        if (state.itemRenderState.isEmpty()) return;

        boolean threeD = state.itemRenderState.isSideLit();
        if (threeD && !FastItemsConfig.affect3DModels) return;

        // Schatten-Einstellung (wirkt ab dem nächsten Frame)
        this.shadowRadius = FastItemsConfig.castShadows ? 0.15F : 0.0F;

        // Seiten von flachen Items entfernen
        if (!threeD && !FastItemsConfig.renderSidesOfItems) {
            ((Flattenable) (Object) state.itemRenderState).fastitems$flatten();
        }

        matrices.push();

        // auf und ab
        Box box = state.itemRenderState.getModelBoundingBox();
        float yOffset = (float) -box.minY + 0.0625F;
        float bob = MathHelper.sin(state.age / 10.0F + state.uniqueOffset) * 0.1F + 0.1F;
        matrices.translate(0.0F, bob + yOffset, 0.0F);

        // zur Kamera drehen (statt um die Y-Achse zu rotieren)
        matrices.multiply(camera.orientation);

        // Stapel-Darstellung (Anzahl)
        ItemEntityRenderer.render(matrices, queue, state.light, state, this.random, box);

        matrices.pop();
        super.render(state, matrices, queue, camera);

        ci.cancel();
    }
}
