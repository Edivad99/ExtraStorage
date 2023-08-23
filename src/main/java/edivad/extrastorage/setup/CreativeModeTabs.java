package edivad.extrastorage.setup;

import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.items.storage.fluid.FluidStorageType;
import edivad.extrastorage.items.storage.item.ItemStorageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabs {

  private static final DeferredRegister<CreativeModeTab> TABS =
      DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExtraStorage.ID);

  private static final RegistryObject<CreativeModeTab> MAIN_TAB =
      TABS.register("main_tab", () -> CreativeModeTab.builder()
          .withTabsBefore(new ResourceLocation("refinedstorage", "general"))
          .title(Component.literal(ExtraStorage.MODNAME))
          .icon(() -> new ItemStack(ESBlocks.CRAFTER.get(CrafterTier.GOLD).get()))
          .displayItems((params, output) -> {
            for (var tier : CrafterTier.values()) {
              output.accept(ESItems.CRAFTER.get(tier).get());
            }
            for (var type : ItemStorageType.values()) {
              output.accept(ESItems.ITEM_STORAGE.get(type).get());
            }
            for (var type : FluidStorageType.values()) {
              output.accept(ESItems.FLUID_STORAGE.get(type).get());
            }
            output.accept(ESItems.ADVANCED_IMPORTER.get());
            output.accept(ESItems.ADVANCED_EXPORTER.get());
            output.accept(ESItems.RAW_NEURAL_PROCESSOR.get());
            output.accept(ESItems.NEURAL_PROCESSOR.get());
          })
          .build());

  public static void register(IEventBus modEventBus) {
    TABS.register(modEventBus);
  }
}
