package mod.linguardium.hotpocket.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static mod.linguardium.hotpocket.Main.MOD_ID;

public class ModItems {
    public static HotPocketItem FURNACE_ITEM = new HotPocketItem(1, new Item.Settings().maxCount(1).fireproof().group(ItemGroup.TOOLS)) ;

    public static void init() {
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"basic_furnace"),FURNACE_ITEM);
    }
}
