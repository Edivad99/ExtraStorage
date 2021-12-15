package edivad.extrastorage.client.screen;

import edivad.extrastorage.container.AdvancedCrafterContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import vazkii.quark.api.IQuarkButtonIgnored;

public class AdvancedCrafterScreenQuark extends AdvancedCrafterScreen implements IQuarkButtonIgnored
{
    public AdvancedCrafterScreenQuark(AdvancedCrafterContainer container, PlayerInventory inventory, ITextComponent title)
    {
        super(container, inventory, title);
    }
}
