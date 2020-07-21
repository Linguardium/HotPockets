package mod.linguardium.hotpocket;

import mod.linguardium.hotpocket.block.ModBlocksClient;
import net.fabricmc.api.ClientModInitializer;

public class MainClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModBlocksClient.init();
    }
}
