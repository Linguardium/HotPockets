package mod.linguardium.hotpocket;

import mod.linguardium.hotpocket.block.ModBlocks;
import mod.linguardium.hotpocket.components.ModComponents;
import mod.linguardium.hotpocket.items.ModItems;
import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "hotpocket";
    public static final String MOD_NAME = "HotPocket";



    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        ModItems.init();;
        ModComponents.init();
        ModBlocks.init();
        //TODO: Initializer
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}