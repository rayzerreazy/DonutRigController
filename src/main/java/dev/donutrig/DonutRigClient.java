package dev.donutrig;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class DonutRigClient implements ClientModInitializer {
    private static KeyBinding openControllerKey;

    @Override
    public void onInitializeClient() {
        RigState.load();

        KeyBinding.Category category = KeyBinding.Category.create(
                Identifier.of("donutrig", "general")
        );
        openControllerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.donutrig.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openControllerKey.wasPressed()) {
                if (client.currentScreen instanceof RigControllerScreen) {
                    client.setScreen(null);
                } else if (client.player != null) {
                    client.setScreen(new RigControllerScreen());
                }
            }
        });
    }
}
