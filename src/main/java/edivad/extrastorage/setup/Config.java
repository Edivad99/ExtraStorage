package edivad.extrastorage.setup;

import edivad.extrastorage.Main;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {

    public static void init() {
        var SERVER_BUILDER = new ForgeConfigSpec.Builder();
        AdvancedCrafter.registerServerConfig(SERVER_BUILDER);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
    }

    public static class AdvancedCrafter {
        public static ForgeConfigSpec.IntValue BASE_ENERGY;
        public static ForgeConfigSpec.BooleanValue INCLUDE_PATTERN_ENERGY;

        public static void registerServerConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
            SERVER_BUILDER.comment(Main.MODNAME + "'s config").push("crafters");

            BASE_ENERGY = SERVER_BUILDER
                    .comment("Indicates the minimum level of energy that the crafter consumes")
                    .defineInRange("base_energy", 15, 1, 1000);

            INCLUDE_PATTERN_ENERGY = SERVER_BUILDER
                    .comment("Include the amount of patterns in the crafter in your power consumption")
                    .define("include_pattern_energy", true);

            SERVER_BUILDER.pop();
        }
    }
}
