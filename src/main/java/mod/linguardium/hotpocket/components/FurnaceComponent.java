package mod.linguardium.hotpocket.components;

import io.github.cottonmc.component.item.impl.ItemInventoryComponent;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FurnaceComponent extends ItemInventoryComponent implements NamedScreenHandlerFactory {
    public static int INPUT_SLOT=0;
    public static int FUEL_SLOT=1;
    public static int OUTPUT_SLOT=2;


    private double cookTime;
    private double cookTimeMax;
    private int fuelTime;
    private int fuelTimeMax;
    private SmeltingRecipe recipe;
    private boolean updateRecipe=false;
    protected final PropertyDelegate propertyDelegate;

    public FurnaceComponent() {
        super(3);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch(index) {
                    case 0:
                        return FurnaceComponent.this.fuelTime;
                    case 1:
                        return FurnaceComponent.this.fuelTimeMax;
                    case 2:
                        return (int)FurnaceComponent.this.cookTime;
                    case 3:
                        return (int)FurnaceComponent.this.cookTimeMax;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        FurnaceComponent.this.fuelTime = value;
                        break;
                    case 1:
                        FurnaceComponent.this.fuelTimeMax = value;
                        break;
                    case 2:
                        FurnaceComponent.this.cookTime = value;
                        break;
                    case 3:
                        FurnaceComponent.this.cookTimeMax = value;
                }

            }

            public int size() {
                return 4;
            }
        };
    }

    public boolean isBurning() {
        return fuelTime > 0;
    }
    public int getCookTime() {
        return (int)cookTime;
    }
    public int getCookTimeMax() {
        return (int)cookTimeMax;
    }
    public int getFuelTime() {
        return fuelTime;
    }
    public void tick(World world) {
        tick(1,world);
    }
    public void tick(double time, World world) {
        if (isBurning()) {
            --fuelTime;
        }
        if (world.isClient())
            return;
        if (updateRecipe) {
            cookTime = 0;
            updateRecipe=false;
            recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING,this.asInventory(),world).orElse(null);
            if (recipe != null) {
                cookTimeMax = recipe.getCookTime();
            }else{
                cookTimeMax=0;
            }
        }
        ItemStack itemStack = this.stacks.get(FUEL_SLOT);

        if (!isBurning() && (stacks.get(INPUT_SLOT).isEmpty() || stacks.get(FUEL_SLOT).isEmpty())) {
            if (cookTime > 0) {
                cookTime = MathHelper.clamp(this.cookTime - 2, 0, cookTimeMax);
            }
        } else {
            if (!isBurning() && this.canAcceptRecipeOutput(recipe)) {
                fuelTimeMax = fuelTime = FuelRegistry.INSTANCE.get(itemStack.getItem());
                if (isBurning()) {
                    if (!itemStack.isEmpty()) {
                        Item item = itemStack.getItem();
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            Item remainder = item.getRecipeRemainder();
                            if (remainder!=null)
                            stacks.set(FUEL_SLOT,new ItemStack(remainder));
                        }
                    }
                }
            }

            if (this.isBurning() && this.canAcceptRecipeOutput(recipe)) {
                cookTime+=time;
                if (cookTime >= cookTimeMax) {
                    cookTime = 0;
                    craftRecipe(recipe);
                }
            } else {
                this.cookTime = 0;
            }
        }
    }

    @Override
    public void onChanged() {
        super.onChanged();
        if (recipe != null) {
            if (!recipe.matches(this.asInventory(), null)) {
                cookTime = 0;
                cookTimeMax = 0;
                recipe = null;
                updateRecipe = true;
            }
        } else {
            updateRecipe = true;
        }
    }

    private void craftRecipe(Recipe<?> recipe) {
        if (recipe != null && this.canAcceptRecipeOutput(recipe)) {
            ItemStack input =stacks.get(INPUT_SLOT);
            ItemStack itemStack2 = recipe.getOutput();
            ItemStack itemStack3 = stacks.get(OUTPUT_SLOT);
            if (itemStack3.isEmpty()) {
                stacks.set(OUTPUT_SLOT, itemStack2.copy());
            } else if (itemStack3.getItem() == itemStack2.getItem()) {
                itemStack3.increment(1);
            }
            input.decrement(1);
            onChanged();
        }
    }
    protected boolean canAcceptRecipeOutput(Recipe<?> recipe) {
        if (recipe != null && !stacks.get(INPUT_SLOT).isEmpty()) {
            ItemStack recipeOutput = recipe.getOutput();
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                ItemStack output = stacks.get(OUTPUT_SLOT);
                if (output.isEmpty()) {
                    return true;
                } else if (!output.isItemEqualIgnoreDamage(recipeOutput)) {
                    return false;
                } else return output.getCount() < this.getMaxStackSize(OUTPUT_SLOT) && output.getCount() < output.getMaxCount();
            }
        } else {
            return false;
        }
    }
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        cookTime=tag.getDouble("cookTime");
        cookTimeMax=tag.getDouble("cookTimeMax");
        fuelTime=tag.getInt("fuelTime");
        fuelTimeMax=tag.getInt("fuelTimeMax");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("cookTime",cookTime);
        tag.putDouble("cookTimeMax",cookTimeMax);
        tag.putInt("fuelTime",fuelTime);
        tag.putInt("fuelTimeMax",fuelTimeMax);
        return tag;
    }
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FurnaceScreenHandler(syncId, inv, this.asInventory(), this.propertyDelegate);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("container.furnace");
    }
}
