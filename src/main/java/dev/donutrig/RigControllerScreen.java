package dev.donutrig;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class RigControllerScreen extends Screen {
    private static final int PANEL_WIDTH = 330;
    private static final int PANEL_HEIGHT = 246;
    private int panelX;
    private int panelY;

    public RigControllerScreen() {
        super(Text.literal("Donut Rig Controller"));
    }

    @Override
    protected void init() {
        panelX = (width - PANEL_WIDTH) / 2;
        panelY = (height - PANEL_HEIGHT) / 2;

        int numberWidth = 28;
        int gap = 4;
        int rowWidth = 9 * numberWidth + 8 * gap;
        int startX = panelX + (PANEL_WIDTH - rowWidth) / 2;

        for (int n = 1; n <= 9; n++) {
            final int value = n;
            int x = startX + (n - 1) * (numberWidth + gap);
            addDrawableChild(ButtonWidget.builder(Text.literal(Integer.toString(n)),
                    button -> RigState.setRight(value))
                    .dimensions(x, panelY + 91, numberWidth, 22).build());
            addDrawableChild(ButtonWidget.builder(Text.literal(Integer.toString(n)),
                    button -> RigState.setLeft(value))
                    .dimensions(x, panelY + 137, numberWidth, 22).build());
        }

        addDrawableChild(ButtonWidget.builder(Text.literal(modeButtonText()),
                button -> {
                    RigState.setMode(RigState.mode == RigState.Mode.RIGHT_AT_LEAST_LEFT
                            ? RigState.Mode.EQUAL : RigState.Mode.RIGHT_AT_LEAST_LEFT);
                    clearAndInit();
                }).dimensions(panelX + 12, panelY + 177, 150, 22).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("RANDOM VALID"),
                button -> {
                    RigState.randomizeValidOutcome();
                    clearAndInit();
                }).dimensions(panelX + 168, panelY + 177, 150, 22).build());

        addDrawableChild(ButtonWidget.builder(Text.literal(enabledButtonText()),
                button -> {
                    RigState.toggleEnabled();
                    clearAndInit();
                }).dimensions(panelX + 12, panelY + 207, 150, 22).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("DONE"),
                button -> close())
                .dimensions(panelX + 168, panelY + 207, 150, 22).build());
    }

    private String modeButtonText() {
        return RigState.mode == RigState.Mode.RIGHT_AT_LEAST_LEFT
                ? "MODE: RIGHT >= LEFT" : "MODE: EQUAL";
    }

    private String enabledButtonText() {
        return RigState.enabled ? "CONTROLLER: ON" : "CONTROLLER: OFF";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xEE101820);
        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + 2, 0xFF55C7A5);
        context.drawCenteredTextWithShadow(textRenderer, "DONUT RIG CONTROLLER",
                width / 2, panelY + 12, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "RIGHT = YOU     |     LEFT = VIEWER",
                width / 2, panelY + 34, 0xFFB9C8D2);

        context.drawTextWithShadow(textRenderer, "RIGHT", panelX + 14, panelY + 69, 0xFF55D6A8);
        context.drawTextWithShadow(textRenderer, "LEFT", panelX + 14, panelY + 115, 0xFFFFC66D);

        context.drawCenteredTextWithShadow(textRenderer,
                "YOUR DISPLAY: " + RigState.right + "     VIEWER DISPLAY: " + RigState.left,
                width / 2, panelY + 159, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                "RESULT: " + RigState.resultLabel(),
                width / 2, panelY + 164, 0xFF55D6A8);

        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer,
                "Visual planner only — does not change server dropper output",
                width / 2, panelY + PANEL_HEIGHT + 8, 0xFFB9C8D2);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
