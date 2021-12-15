package edivad.extrastorage.client.screen;

import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
//import vazkii.quark.api.IQuarkButtonIgnored;

public class AdvancedCrafterScreenQuark extends AdvancedCrafterScreen //implements IQuarkButtonIgnored
{
    public AdvancedCrafterScreenQuark(AdvancedCrafterContainerMenu container, Inventory inventory, Component title)
    {
        super(container, inventory, title);
    }
}
