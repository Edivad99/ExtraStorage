package edivad.extrastorage.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.util.RenderUtils;
import edivad.extrastorage.Main;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.client.screen.custombutton.AdvancedCrafterModeSideButton;
import edivad.extrastorage.container.AdvancedCrafterContainerMenu;
import edivad.extrastorage.network.PacketHandler;
import edivad.extrastorage.network.packet.UpdateCrafterMode;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedCrafterScreen extends BaseScreen<AdvancedCrafterContainerMenu>
{
    private final CrafterTier tier;
    private final AdvancedCrafterContainerMenu container;
    private final AdvancedCrafterBlockEntity blockEntity;

    public AdvancedCrafterScreen(AdvancedCrafterContainerMenu container, Inventory inventory, Component title)
    {
        super(container, 211, 173 + (container.getBlockEntity().getTier().ordinal() * 36), inventory, title);
        this.tier = container.getBlockEntity().getTier();
        this.container = container;
        this.blockEntity = container.getBlockEntity();
    }

    @Override
    public void init() {
        super.init();
        addSideButton(new AdvancedCrafterModeSideButton(this, blockEntity) {
            @Override
            public void onPress() {
                var crafterMode = blockEntity.getCrafterMode();
                blockEntity.setCrafterMode(AdvancedCrafterNetworkNode.CrafterMode.getById(crafterMode.ordinal() + 1));
                PacketHandler.INSTANCE.sendToServer(new UpdateCrafterMode(blockEntity.getBlockPos(), crafterMode.ordinal() + 1));
            }
        });
    }

    @Override
    public void onPostInit(int i, int i1) {
    }

    @Override
    public void tick(int i, int i1) {
    }

    @Override
    public void renderBackground(PoseStack poseStack, int x, int y, int mouseX, int mouseY)
    {
        bindTexture(Main.MODID, "gui/" + tier.getID() + ".png");
        if(imageHeight <= 256)
            blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
        else
            blit(poseStack, x, y, 0, 0, imageWidth, imageHeight, 512, 512);
    }

    @Override
    public void renderForeground(PoseStack poseStack, int mouseX, int mouseY)
    {
        renderString(poseStack, 7, 7, RenderUtils.shorten(title.getString(), 26));
        renderString(poseStack, 7, 78 + (36 * tier.ordinal()), new TranslatableComponent("container.inventory").getString());
    }
}
