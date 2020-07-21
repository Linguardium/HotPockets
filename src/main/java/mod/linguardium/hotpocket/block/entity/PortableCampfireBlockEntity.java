package mod.linguardium.hotpocket.block.entity;

import mod.linguardium.hotpocket.block.ModBlocks;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import java.util.Optional;
import java.util.Random;

public class PortableCampfireBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {
    private ItemStack itemBeingCooked;
    private int cookingTime;
    private int cookingTotalTime;
    private int damage = 0;
    public PortableCampfireBlockEntity() {
        super(ModBlocks.MINI_CAMPFIRE_TYPE);
        this.itemBeingCooked = ItemStack.EMPTY;
        this.cookingTime = 0;
        this.cookingTotalTime = 0;
    }
    public void setDamage(int damage) {
        this.damage=damage;
    }
    public int getDamage() {
        return damage;
    }
    public void tick() {
        if (world==null)
            return;
        if (world.isClient()) {
                this.spawnSmokeParticles();
        } else {
                this.updateItemsBeingCooked();
        }
    }

    private void updateItemsBeingCooked() {
            if (!itemBeingCooked.isEmpty()) {
                this.cookingTime++;
                if (cookingTime >= this.cookingTotalTime) {
                    Inventory inventory = new SimpleInventory(itemBeingCooked);
                    ItemStack itemStack2 = world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, inventory, world).map(campfireCookingRecipe->campfireCookingRecipe.craft(inventory)).orElse(itemBeingCooked);
                    ItemScatterer.spawn(this.world, pos.getX(), pos.getY(), pos.getZ(), itemStack2);
                    itemBeingCooked=ItemStack.EMPTY;
                    this.updateListeners();
                }
            }
    }

    private void spawnSmokeParticles() {
        if (world != null) {
            Random random = world.random;
            int j;
            if (random.nextFloat() < 0.11F) {
                for(j = 0; j < random.nextInt(2); ++j) {
                    world.addImportantParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, (double)pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + random.nextDouble() + random.nextDouble(), (double)pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
                }
            }

            Direction direction = (getCachedState().get(HorizontalFacingBlock.FACING));
                if (!itemBeingCooked.isEmpty() && random.nextFloat() < 0.2F) {
                    //Direction direction = Direction.fromHorizontal(Math.floorMod(j, 4));
                    float f = 0.3125F;
                    double x = pos.getX()+0.5D + direction.getOffsetX()*f- direction.rotateYClockwise().getOffsetZ()*f;
                    double y = pos.getY()+0.3D;
                    double z = pos.getZ()+0.5D + (direction.getOffsetZ())*f + direction.rotateYClockwise().getOffsetX()*f;// + direction.getOffsetZ();
                    double randX = (double)pos.getX() + 0.7D + (double)((float)direction.rotateYClockwise().getOffsetX() * 0);
                    double randY = (double)pos.getY() + 0.6D;
                    double randZ = (double)pos.getZ() + 0.3D - (double)((float)direction.getOffsetZ() * 0) + (double)((float)direction.rotateYClockwise().getOffsetZ() * 0.3125F);
                    world.addParticle(ParticleTypes.SMOKE, x,y,z, 0.0D, 5.0E-4D, 0.0D);
                }
        }
    }

    public ItemStack getItemBeingCooked() {
        return itemBeingCooked;
    }

    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        //this.itemsBeingCooked.clear();
        if (tag.contains("Item", NbtType.COMPOUND)) {
            itemBeingCooked = ItemStack.fromTag(tag.getCompound("Item"));
        }
        if (tag.contains("CookingTime", NbtType.INT)) {
            cookingTime=tag.getInt("CookingTime");
        }
        if (tag.contains("CookingTotalTime", NbtType.INT)) {
            cookingTotalTime=tag.getInt("CookingTotalTime");
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Item",itemBeingCooked.toTag(new CompoundTag()));
        tag.putInt("CookingTimes", this.cookingTime);
        tag.putInt("CookingTotalTimes", this.cookingTotalTime);
        return tag;
    }

    public Optional<CampfireCookingRecipe> getRecipeFor(ItemStack item) {
        if (world != null)
            return this.world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(item), this.world);
        return Optional.empty();
    }

    public boolean addItem(ItemStack item, int integer) {
        if (itemBeingCooked.isEmpty()) {
                this.cookingTotalTime = integer;
                this.cookingTime = 0;
                this.itemBeingCooked= item.split(1);
                this.updateListeners();
                return true;
        }

        return false;
    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        if (tag.contains("Item", NbtType.COMPOUND)) {
            itemBeingCooked = ItemStack.fromTag(tag.getCompound("Item"));
        }else{
            itemBeingCooked=ItemStack.EMPTY;
        }
        if (tag.contains("CookingTime", NbtType.INT)) {
            cookingTime=tag.getInt("CookingTime");
        }
        if (tag.contains("CookingTotalTime", NbtType.INT)) {
            cookingTotalTime=tag.getInt("CookingTotalTime");
        }
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        tag.put("Item",itemBeingCooked.toTag(new CompoundTag()));
        tag.putInt("CookingTimes", this.cookingTime);
        tag.putInt("CookingTotalTimes", this.cookingTotalTime);
        return tag;
    }
}
