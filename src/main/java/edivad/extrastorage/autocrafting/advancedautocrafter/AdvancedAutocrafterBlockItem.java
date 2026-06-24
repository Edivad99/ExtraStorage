package edivad.extrastorage.autocrafting.advancedautocrafter;

import java.util.function.Consumer;
import edivad.extrastorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;


public class AdvancedAutocrafterBlockItem extends BlockItem {

  public AdvancedAutocrafterBlockItem(AdvancedAutocrafterBlock block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display,
      Consumer<Component> builder, TooltipFlag tooltipFlag) {
    if (Minecraft.getInstance().hasShiftDown()) {
      var autocrafterBlock = (AdvancedAutocrafterBlock)this.getBlock();
      var tier = autocrafterBlock.getTier();
      builder.accept(Component.translatable(Translations.SLOT_CRAFTING, tier.getSlots())
          .withStyle(ChatFormatting.GREEN));
      builder.accept(Component.translatable(Translations.BASE_SPEED, tier.getCraftingSpeed())
          .withStyle(ChatFormatting.GREEN));
    } else {
      builder.accept(Component.translatable(Translations.HOLD_SHIFT).withStyle(ChatFormatting.GRAY));
    }
  }
}
