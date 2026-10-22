package cn.noryea.fastitems.mixin;

import cn.noryea.fastitems.Flattenable;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ItemRenderState.LayerRenderState.class)
public abstract class LayerRenderStateMixin implements Flattenable {

    @Final
    @Shadow
    private List<BakedQuad> quads;

    @Override
    public void fastitems$flatten() {
        quads.removeIf(quad -> quad.face() != Direction.SOUTH);
    }
}
