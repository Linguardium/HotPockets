package mod.linguardium.hotpocket.block;

import mod.linguardium.hotpocket.block.render.PortableCampfireBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class ModBlocksClient {
    public static void init() {
        BlockEntityRendererRegistry.INSTANCE.register(ModBlocks.MINI_CAMPFIRE_TYPE,PortableCampfireBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MINI_CAMPFIRE, RenderLayer.getCutout());
    }
}
