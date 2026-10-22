package cn.noryea.fastitems;

import cn.noryea.fastitems.config.FastItemsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class FastItemsFabric implements ClientModInitializer {
    private static KeyBinding openKey;

    @Override
    public void onInitializeClient() {
        FastItemsConfig.load();

        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("fastitems", "main"));
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fastitems.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) {
                client.setScreen(new FastItemsSettingsScreen(client.currentScreen));
            }
        });
    }
}
