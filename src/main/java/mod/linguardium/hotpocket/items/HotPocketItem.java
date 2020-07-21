package mod.linguardium.hotpocket.items;

import mod.linguardium.hotpocket.components.FurnaceComponent;
import mod.linguardium.hotpocket.components.ModComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class HotPocketItem extends Item {


    public final double speed;

    public HotPocketItem(double speedMultiplier, Item.Settings settings) {
        super(settings);
        speed=speedMultiplier;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        Optional<FurnaceComponent> furnace = ModComponents.FURNACE_COMPONENT.maybeGet(stack);
        if (furnace.isPresent()) {
            furnace.get().tick(speed, world);
            if (!world.isClient) {
                if (furnace.get().isBurning()) {
                    stack.getOrCreateTag().putInt("CustomModelData", 1);
                } else {
                    stack.getOrCreateTag().putInt("CustomModelData", 0);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        Optional<FurnaceComponent> furnace = ModComponents.FURNACE_COMPONENT.maybeGet(stack);
        if (furnace.isPresent()) {
            FurnaceComponent f = furnace.get();
            ItemStack input = f.getStack(FurnaceComponent.INPUT_SLOT);
            ItemStack fuel = f.getStack(FurnaceComponent.FUEL_SLOT);
            ItemStack output = f.getStack(FurnaceComponent.OUTPUT_SLOT);
            if (f.isBurning()) {
                if (!input.isEmpty()) {
                    tooltip.add(new TranslatableText("item.hotpocket.furnace.cooking", input.getName()).formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
                    tooltip.add(new TranslatableText("item.hotpocket.furnace.cookingtime", f.getCookTimeMax() - f.getCookTime()).formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
                }
                tooltip.add(new TranslatableText("item.hotpocket.furnace.fueltime",f.getFuelTime()).formatted(Formatting.ITALIC).formatted(Formatting.GRAY));

                if (!fuel.isEmpty() && FuelRegistry.INSTANCE.get(fuel.getItem()) != null) {
                    tooltip.add(new TranslatableText("item.hotpocket.furnace.nextFuel",fuel.getName()).formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
                }
            }
            if (!output.isEmpty()) {
                tooltip.add(new TranslatableText("item.hotpocket.furnace.output", output.getCount(), output.getName()).formatted(Formatting.ITALIC).formatted(Formatting.GRAY));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);


    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            return TypedActionResult.success(stack);
        }
        Optional<FurnaceComponent> f = ModComponents.FURNACE_COMPONENT.maybeGet(stack);
        if (f.isPresent()) {
          user.openHandledScreen(f.get());
        }
        return TypedActionResult.consume(stack);
    }

}
