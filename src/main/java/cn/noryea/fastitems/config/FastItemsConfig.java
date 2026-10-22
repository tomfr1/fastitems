package cn.noryea.fastitems.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class FastItemsConfig {
    public static boolean enable = true;
    public static boolean castShadows = true;
    public static boolean renderSidesOfItems = false;
    public static boolean affect3DModels = true;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("fastitems.json");

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                JsonObject o = GSON.fromJson(Files.readString(PATH), JsonObject.class);
                if (o != null) {
                    enable = get(o, "enable", enable);
                    castShadows = get(o, "castShadows", castShadows);
                    renderSidesOfItems = get(o, "renderSidesOfItems", renderSidesOfItems);
                    affect3DModels = get(o, "affect3DModels", affect3DModels);
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        save();
    }

    public static void save() {
        try {
            JsonObject o = new JsonObject();
            o.addProperty("enable", enable);
            o.addProperty("castShadows", castShadows);
            o.addProperty("renderSidesOfItems", renderSidesOfItems);
            o.addProperty("affect3DModels", affect3DModels);
            Files.writeString(PATH, GSON.toJson(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean get(JsonObject o, String key, boolean def) {
        return o.has(key) ? o.get(key).getAsBoolean() : def;
    }
}
