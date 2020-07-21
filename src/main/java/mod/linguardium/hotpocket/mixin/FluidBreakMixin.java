package mod.linguardium.hotpocket.mixin;

import mod.linguardium.hotpocket.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowableFluid.class)
public class FluidBreakMixin {
    @Inject(method="canFill",at=@At(value = "HEAD"), cancellable = true)
    private void BreakPortableCampfire(BlockView world, BlockPos pos, BlockState state, Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock().equals(ModBlocks.MINI_CAMPFIRE)) {
            cir.setReturnValue(true);
        }
    }
}
