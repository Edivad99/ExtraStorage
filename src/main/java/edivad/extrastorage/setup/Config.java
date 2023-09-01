package edivad.extrastorage.setup;

import edivad.extrastorage.ExtraStorage;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {

  public static void init() {
    var SERVER_BUILDER = new ForgeConfigSpec.Builder();
    SERVER_BUILDER.comment(ExtraStorage.MODNAME + "'s config");
    AdvancedCrafter.registerServerConfig(SERVER_BUILDER);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_BUILDER.build());
  }

  public static class AdvancedCrafter {

    public static ForgeConfigSpec.IntValue BASE_ENERGY;
    public static ForgeConfigSpec.BooleanValue INCLUDE_PATTERN_ENERGY;
    public static ForgeConfigSpec.BooleanValue UNIFORMLY_DISTRIBUTE_PROCESSING;

    public static void registerServerConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
      SERVER_BUILDER.push("crafters");

      BASE_ENERGY = SERVER_BUILDER
          .comment("Indicates the minimum level of energy that the crafter consumes")
          .defineInRange("base_energy", 15, 1, 1000);

      INCLUDE_PATTERN_ENERGY = SERVER_BUILDER
          .comment("Include the amount of patterns in the crafter in your power consumption")
          .define("include_pattern_energy", true);

      UNIFORMLY_DISTRIBUTE_PROCESSING = SERVER_BUILDER
          .comment("The crafter's speed is limited to the number of available slots in the inventory it is connected to.",
              "https://github.com/Edivad99/ExtraStorage/issues/55")
          .define("uniformly_distribute_processing", false);

      SERVER_BUILDER.pop();
    }
  }
}
