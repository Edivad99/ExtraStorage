package edivad.extrastorage.fromrs;

import com.refinedmods.refinedstorage.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.common.support.widget.AbstractSideButtonWidget;
import com.refinedmods.refinedstorage.common.support.widget.AbstractYesNoSideButtonWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;
import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;
import net.minecraft.resources.ResourceLocation;

public class VisibleToTheAutocrafterManagerSideButtonWidget extends AbstractYesNoSideButtonWidget {
  private static final MutableComponent TITLE =
      createTranslation("gui", "autocrafter.visible_to_the_autocrafter_manager");
  private static final MutableComponent HELP =
      createTranslation("gui", "autocrafter.visible_to_the_autocrafter_manager.help");
  private static final ResourceLocation YES =
      createIdentifier("widget/side_button/autocrafter/visible_to_the_autocrafter_manager/yes");
  private static final ResourceLocation NO =
      createIdentifier("widget/side_button/autocrafter/visible_to_the_autocrafter_manager/no");

  public VisibleToTheAutocrafterManagerSideButtonWidget(final ClientProperty<Boolean> property) {
    super(property, TITLE, YES, NO);
  }

  @Override
  protected Component getHelpText() {
    return HELP;
  }
}
