package dev.donutrig;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public final class RigState {
    public enum Mode {
        RIGHT_AT_LEAST_LEFT,
        EQUAL
    }

    public static int right = 7;
    public static int left = 3;
    public static Mode mode = Mode.RIGHT_AT_LEAST_LEFT;
    public static boolean enabled = true;

    private static final Path CONFIG = FabricLoader.getInstance()
            .getConfigDir().resolve("donut-rig-controller.properties");

    private RigState() {}

    public static void setRight(int value) {
        right = clamp(value);
        enforceMode();
        save();
    }

    public static void setLeft(int value) {
        left = clamp(value);
        enforceMode();
        save();
    }

    public static void setMode(Mode newMode) {
        mode = newMode;
        enforceMode();
        save();
    }

    public static void toggleEnabled() {
        enabled = !enabled;
        save();
    }

    public static void randomizeValidOutcome() {
        if (mode == Mode.EQUAL) {
            int n = ThreadLocalRandom.current().nextInt(1, 10);
            right = n;
            left = n;
        } else {
            left = ThreadLocalRandom.current().nextInt(1, 10);
            right = ThreadLocalRandom.current().nextInt(left, 10);
        }
        save();
    }

    private static void enforceMode() {
        if (mode == Mode.EQUAL) {
            left = right;
        } else if (right < left) {
            right = left;
        }
    }

    private static int clamp(int value) {
        return Math.max(1, Math.min(9, value));
    }

    public static String resultLabel() {
        if (right > left) return "RIGHT WINS";
        if (right == left) return "DRAW";
        return "LEFT WINS";
    }

    public static void load() {
        if (!Files.exists(CONFIG)) return;
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(CONFIG)) {
            p.load(in);
            right = clamp(Integer.parseInt(p.getProperty("right", "7")));
            left = clamp(Integer.parseInt(p.getProperty("left", "3")));
            mode = Mode.valueOf(p.getProperty("mode", Mode.RIGHT_AT_LEAST_LEFT.name()));
            enabled = Boolean.parseBoolean(p.getProperty("enabled", "true"));
            enforceMode();
        } catch (IOException | RuntimeException ignored) {
            right = 7;
            left = 3;
            mode = Mode.RIGHT_AT_LEAST_LEFT;
            enabled = true;
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("right", Integer.toString(right));
        p.setProperty("left", Integer.toString(left));
        p.setProperty("mode", mode.name());
        p.setProperty("enabled", Boolean.toString(enabled));
        try {
            Files.createDirectories(CONFIG.getParent());
            try (OutputStream out = Files.newOutputStream(CONFIG)) {
                p.store(out, "Donut Rig Controller settings");
            }
        } catch (IOException ignored) {
            // The controller remains usable for this session if config saving fails.
        }
    }
}
