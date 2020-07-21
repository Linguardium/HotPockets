package mod.linguardium.hotpocket.block;

import mod.linguardium.hotpocket.block.entity.PortableCampfireBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.object.builder.FabricBlockInternals;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static mod.linguardium.hotpocket.Main.MOD_ID;

public class ModBlocks {
    public static final PortableCampfire MINI_CAMPFIRE = new PortableCampfire(FabricBlockSettings.of(Material.WOOD, MaterialColor.SPRUCE).strength(2.0F).sounds(BlockSoundGroup.WOOD).nonOpaque().lightLevel(12).breakInstantly().breakByHand(true));
    public static final BlockEntityType<PortableCampfireBlockEntity> MINI_CAMPFIRE_TYPE = BlockEntityType.Builder.create(PortableCampfireBlockEntity::new,MINI_CAMPFIRE).build(null);
    public static void init() {
        Registry.register(Registry.BLOCK,new Identifier(MOD_ID,"portable_campfire"),MINI_CAMPFIRE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE,new Identifier(MOD_ID,"portable_campfire_blockentity"),MINI_CAMPFIRE_TYPE);
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"portable_campfire"),new BlockItem(MINI_CAMPFIRE, new Item.Settings().maxDamage(3).group(ItemGroup.DECORATIONS)));
    }
}
