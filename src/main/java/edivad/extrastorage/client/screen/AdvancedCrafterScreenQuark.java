package edivad.extrastorage.client.screen;

import edivad.extrastorage.container.AdvancedCrafterContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import vazkii.quark.api.IQuarkButtonIgnored;

public class AdvancedCrafterScreenQuark extends AdvancedCrafterScreen implements IQuarkButtonIgnored
{
    public AdvancedCrafterScreenQuark(AdvancedCrafterContainer container, Inventory inventory, Component title)
    {
        super(container, inventory, title);
    }
}
