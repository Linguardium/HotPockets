package mod.linguardium.hotpocket.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
/*
public class CraftingFurnaceBlockEntity extends BlockEntity implements Tickable, NamedScreenHandlerFactory {

    public CraftingFurnaceBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {

    }

    @Override
    public Text getDisplayName() {
        return null;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

}
*/