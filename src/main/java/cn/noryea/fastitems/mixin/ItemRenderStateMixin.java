package cn.noryea.fastitems.mixin;

import cn.noryea.fastitems.Flattenable;
import net.minecraft.client.render.item.ItemRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRenderState.class)
public abstract class ItemRenderStateMixin implements Flattenable {

    @Final
    @Shadow
    private ItemRenderState.LayerRenderState[] layers;

    @Shadow
    private int layerCount;

    @Override
    public void fastitems$flatten() {
        for (int i = 0; i < layerCount; i++) {
            ((Flattenable) (Object) layers[i]).fastitems$flatten();
        }
    }
}
