package edivad.extrastorage.data.models;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

import java.util.stream.Stream;
import com.mojang.math.Quadrant;
import com.refinedmods.refinedstorage.common.support.direction.DefaultDirectionType;
import com.refinedmods.refinedstorage.common.util.IdentifierUtil;
import com.refinedmods.refinedstorage.neoforge.networking.CablePartUnbakedBlockStateModel;
import edivad.extrastorage.ExtraStorage;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterBlock;
import edivad.extrastorage.autocrafting.advancedautocrafter.CrafterTier;
import edivad.extrastorage.setup.ESBlocks;
import edivad.extrastorage.setup.ESItems;
import edivad.extrastorage.storage.AdvancedFluidStorageVariant;
import edivad.extrastorage.storage.AdvancedItemStorageVariant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;

public class ExtraStorageBlockModelProvider extends ModelProvider {

  private static final TextureSlot NORTH_CUTOUT = TextureSlot.create("cutout_north");
  private static final TextureSlot EAST_CUTOUT = TextureSlot.create("cutout_east");
  private static final TextureSlot SOUTH_CUTOUT = TextureSlot.create("cutout_south");
  private static final TextureSlot WEST_CUTOUT = TextureSlot.create("cutout_west");
  private static final TextureSlot UP_CUTOUT = TextureSlot.create("cutout_up");
  private static final TextureSlot CABLE = TextureSlot.create("cable");
  private static final TextureSlot BORDER = TextureSlot.create("border");

  private static final ModelTemplate SIDES_CUTOUT_MODEL = ModelTemplates.create(
      "refinedstorage:sides_cutout",
      TextureSlot.PARTICLE,
      TextureSlot.NORTH,
      TextureSlot.EAST,
      TextureSlot.SOUTH,
      TextureSlot.WEST,
      TextureSlot.UP,
      TextureSlot.DOWN,
      NORTH_CUTOUT,
      EAST_CUTOUT,
      SOUTH_CUTOUT,
      WEST_CUTOUT,
      UP_CUTOUT
  );
  private static final ModelTemplate EMISSIVE_SIDES_CUTOUT_MODEL = ModelTemplates.create(
      "refinedstorage:emissive_sides_cutout",
      TextureSlot.PARTICLE,
      TextureSlot.NORTH,
      TextureSlot.EAST,
      TextureSlot.SOUTH,
      TextureSlot.WEST,
      TextureSlot.UP,
      TextureSlot.DOWN,
      NORTH_CUTOUT,
      EAST_CUTOUT,
      SOUTH_CUTOUT,
      WEST_CUTOUT,
      UP_CUTOUT
  );

  private static final ModelTemplate EXPORTER_ITEM_MODEL = ModelTemplates.createItem(
      ExtraStorage.rl("advanced_exporter/base").toString(),
      CABLE,
      BORDER
  );
  private static final ModelTemplate IMPORTER_ITEM_MODEL = ModelTemplates.createItem(
      ExtraStorage.rl("advanced_importer/base").toString(),
      CABLE,
      BORDER
  );

  public ExtraStorageBlockModelProvider(PackOutput packOutput) {
    super(packOutput, ExtraStorage.ID);
  }

  @Override
  protected Stream<? extends Holder<Block>> getKnownBlocks() {
    return Stream.empty();
  }

  @Override
  protected Stream<? extends Holder<Item>> getKnownItems() {
    return Stream.empty();
  }

  @Override
  protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    for (var type : AdvancedItemStorageVariant.values()) {
      itemModels.generateFlatItem(ESItems.ITEM_STORAGE_PART.get(type).get(), ModelTemplates.FLAT_HANDHELD_ITEM);
      itemModels.generateFlatItem(ESItems.ITEM_DISK.get(type).get(),
          ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    for (var type : AdvancedFluidStorageVariant.values()) {
      itemModels.generateFlatItem(ESItems.FLUID_STORAGE_PART.get(type).get(),
          ModelTemplates.FLAT_HANDHELD_ITEM);
      itemModels.generateFlatItem(ESItems.FLUID_DISK.get(type).get(),
          ModelTemplates.FLAT_HANDHELD_ITEM);
    }
    itemModels.generateFlatItem(ESItems.RAW_NEURAL_PROCESSOR.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModels.generateFlatItem(ESItems.NEURAL_PROCESSOR.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);

    registerCrafters(blockModels, itemModels);
    registerStorageBlocks(blockModels, itemModels);
    registerExporter(blockModels, itemModels);
    registerImporter(blockModels, itemModels);
  }

  private void registerCrafters(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    final Identifier side = modLocation("block/crafter/base/side");
    final Identifier top = modLocation("block/crafter/base/top");
    final Identifier bottom = modLocation("block/crafter/base/bottom");
    final Identifier cutoutSide = modLocation("block/crafter/base/cutouts/side_disconnected");
    final Identifier cutoutTop = modLocation("block/crafter/base/cutouts/top_disconnected");

    final Identifier inactiveModel = SIDES_CUTOUT_MODEL.create(
        modLocation("block/autocrafter/inactive"),
        new TextureMapping()
            .put(TextureSlot.PARTICLE, texture(side))
            .put(TextureSlot.NORTH, texture(side))
            .put(TextureSlot.EAST, texture(side))
            .put(TextureSlot.SOUTH, texture(side))
            .put(TextureSlot.WEST, texture(side))
            .put(TextureSlot.UP, texture(top))
            .put(TextureSlot.DOWN, texture(bottom))
            .put(NORTH_CUTOUT, texture(cutoutSide))
            .put(EAST_CUTOUT, texture(cutoutSide))
            .put(SOUTH_CUTOUT, texture(cutoutSide))
            .put(WEST_CUTOUT, texture(cutoutSide))
            .put(UP_CUTOUT, texture(cutoutTop)),
        blockModels.modelOutput
    );

    for (var tier : CrafterTier.values())
    {
      var name = switch (tier)
      {
        case IRON -> "iron";
        case GOLD -> "gold";
        case DIAMOND -> "diamond";
        case NETHERITE -> "netherite";
      };
      var basePath = "block/crafter/" + name + "/cutouts/";

      var cutoutSideActive = modLocation(basePath + "side_connected");
      var cutoutTopActive = modLocation(basePath + "top_connected");
      var activeModel = EMISSIVE_SIDES_CUTOUT_MODEL.create(
          modLocation("block/crafter/" + name),
          new TextureMapping()
              .put(TextureSlot.PARTICLE, texture(side))
              .put(TextureSlot.NORTH, texture(side))
              .put(TextureSlot.EAST, texture(side))
              .put(TextureSlot.SOUTH, texture(side))
              .put(TextureSlot.WEST, texture(side))
              .put(TextureSlot.UP, texture(top))
              .put(TextureSlot.DOWN, texture(bottom))
              .put(NORTH_CUTOUT, texture(cutoutSideActive))
              .put(EAST_CUTOUT, texture(cutoutSideActive))
              .put(SOUTH_CUTOUT, texture(cutoutSideActive))
              .put(WEST_CUTOUT, texture(cutoutSideActive))
              .put(UP_CUTOUT, texture(cutoutTopActive)),
          blockModels.modelOutput
      );
      itemModels.itemModelOutput.accept(ESItems.CRAFTER.get(tier).get().asItem(),
          ItemModelUtils.plainModel(activeModel));

      blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(ESBlocks.CRAFTER.get(tier).get())
          .with(PropertyDispatch.initial(AdvancedAutocrafterBlock.ACTIVE)
              .select(false, plainVariant(inactiveModel))
              .select(true, plainVariant(activeModel)))
          .with(PropertyDispatch.modify(DefaultDirectionType.FACE_CLICKED.getProperty())
              .generate(direction -> variant -> variant
                  .withXRot(getAutocrafterXRot(direction))
                  .withYRot(getAutocrafterYRot(direction)))));
    }
  }

  private void registerStorageBlocks(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    for (var variant : AdvancedItemStorageVariant.values()) {
      var blockModel = ModelTemplates.CUBE_ALL.create(
          modLocation("block/storage_block/" + variant.getName() + "_storage_block"),
          TextureMapping.cube(texture(
              modLocation("block/storage_block/" + variant.getName() + "_storage_block"))),
          blockModels.modelOutput
      );
      var block = ESBlocks.ITEM_STORAGE.get(variant).get();
      blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(blockModel)));
      itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(blockModel));
    }
    for (var variant : AdvancedFluidStorageVariant.values()) {
      var blockModel = ModelTemplates.CUBE_ALL.create(
          modLocation("block/fluid_storage_block/" + variant.getName() + "_fluid_storage_block"),
          TextureMapping.cube(texture(modLocation("block/fluid_storage_block/" + variant.getName() + "_fluid_storage_block"))),
          blockModels.modelOutput
      );
      var block = ESBlocks.FLUID_STORAGE.get(variant).get();
      blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, plainVariant(blockModel)));
      itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(blockModel));
    }
  }

  private void registerImporter(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    var block = ESBlocks.ADVANCED_IMPORTER.get();

    var itemModel = IMPORTER_ITEM_MODEL.create(
        modLocation("item/advanced_importer"),
        new TextureMapping()
            .put(CABLE, texture(Identifier.fromNamespaceAndPath(IdentifierUtil.MOD_ID, "block/cable/gray")))
            .put(BORDER, texture(modLocation("block/cable_part_border"))),
        itemModels.modelOutput
    );
    itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(itemModel));

    blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
        MultiVariant.of(new CustomBlockStateModelBuilder.Simple(
            new CablePartUnbakedBlockStateModel(DyeColor.GRAY,
                modLocation("block/advanced_importer/base")))))
    );
  }

  private void registerExporter(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    var block = ESBlocks.ADVANCED_EXPORTER.get();

    var itemModel = EXPORTER_ITEM_MODEL.create(
        modLocation("item/advanced_exporter"),
        new TextureMapping()
            .put(CABLE, texture(Identifier.fromNamespaceAndPath(IdentifierUtil.MOD_ID, "block/cable/gray")))
            .put(BORDER, texture(modLocation("block/cable_part_border"))),
        itemModels.modelOutput
    );
    itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(itemModel));

    blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block,
        MultiVariant.of(new CustomBlockStateModelBuilder.Simple(
            new CablePartUnbakedBlockStateModel(DyeColor.GRAY,
                modLocation("block/advanced_exporter/base")))))
    );
  }

  private Quadrant getAutocrafterXRot(final Direction direction) {
    return switch (direction) {
      case DOWN -> Quadrant.R180;
      case UP -> Quadrant.R0;
      case NORTH, SOUTH, WEST, EAST -> Quadrant.R90;
    };
  }

  private Quadrant getAutocrafterYRot(final Direction direction) {
    return switch (direction) {
      case DOWN, UP, NORTH -> Quadrant.R0;
      case SOUTH -> Quadrant.R180;
      case EAST -> Quadrant.R90;
      case WEST -> Quadrant.R270;
    };
  }

  private static Material texture(final Identifier location) {
    return new Material(location);
  }
}
