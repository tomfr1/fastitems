package cn.noryea.fastitems;

import cn.noryea.fastitems.config.FastItemsConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class FastItemsSettingsScreen extends Screen {
    private final Screen parent;

    public FastItemsSettingsScreen(Screen parent) {
        super(Text.literal("Fast Items Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = width / 2 - 150;
        int y = height / 4;
        toggle(x, y, "Fast Items aktiv", () -> FastItemsConfig.enable, v -> FastItemsConfig.enable = v);
        toggle(x, y + 24, "Schatten für Items", () -> FastItemsConfig.castShadows, v -> FastItemsConfig.castShadows = v);
        toggle(x, y + 48, "Seiten von flachen Items zeigen", () -> FastItemsConfig.renderSidesOfItems, v -> FastItemsConfig.renderSidesOfItems = v);
        toggle(x, y + 72, "3D-Modelle (Blöcke) auch betreffen", () -> FastItemsConfig.affect3DModels, v -> FastItemsConfig.affect3DModels = v);
        addDrawableChild(ButtonWidget.builder(Text.literal("Fertig"), b -> close())
                .dimensions(x, y + 108, 300, 20).build());
    }

    private void toggle(int x, int y, String label, BooleanSupplier get, Consumer<Boolean> set) {
        addDrawableChild(ButtonWidget.builder(text(label, get.getAsBoolean()), b -> {
            set.accept(!get.getAsBoolean());
            b.setMessage(text(label, get.getAsBoolean()));
        }).dimensions(x, y, 300, 20).build());
    }

    private static Text text(String label, boolean on) {
        return Text.literal(label + ": " + (on ? "AN" : "AUS"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 4 - 20, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        FastItemsConfig.save();
        client.setScreen(parent);
    }
}
