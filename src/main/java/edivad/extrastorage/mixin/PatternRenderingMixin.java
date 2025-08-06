package edivad.extrastorage.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.refinedmods.refinedstorage.common.autocrafting.PatternRendering;
import edivad.extrastorage.autocrafting.advancedautocrafter.AdvancedAutocrafterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

@Mixin(PatternRendering.class)
public class PatternRenderingMixin {

  @Inject(method = "canDisplayOutputInScreen", at = @At("HEAD"), cancellable = true)
  private static void canDisplayOutputInAdvancedAutocrafterScreen(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
    var screen = Minecraft.getInstance().screen;
    if (screen instanceof AdvancedAutocrafterScreen advancedAutocrafterScreen) {
      boolean result = advancedAutocrafterScreen.getMenu().containsPattern(stack);
      cir.setReturnValue(result);
    }
  }
}
