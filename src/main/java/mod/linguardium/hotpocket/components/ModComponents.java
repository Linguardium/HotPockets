package mod.linguardium.hotpocket.components;

import mod.linguardium.hotpocket.components.FurnaceComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import net.minecraft.util.Identifier;
import static mod.linguardium.hotpocket.Main.MOD_ID;
import static mod.linguardium.hotpocket.items.ModItems.FURNACE_ITEM;

public class ModComponents {
    public static final ComponentType<FurnaceComponent> FURNACE_COMPONENT =
            ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MOD_ID,"furnace"), FurnaceComponent.class).attach(ItemComponentCallback.event(FURNACE_ITEM), a->new FurnaceComponent());
    static {

    }
    public static void init() {

    }

}
