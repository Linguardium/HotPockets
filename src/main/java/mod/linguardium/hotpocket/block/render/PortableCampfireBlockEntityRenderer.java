package mod.linguardium.hotpocket.block.render;

import mod.linguardium.hotpocket.block.entity.PortableCampfireBlockEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

import static net.minecraft.block.HorizontalFacingBlock.FACING;

public class PortableCampfireBlockEntityRenderer extends BlockEntityRenderer<PortableCampfireBlockEntity> {
    public PortableCampfireBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
    }

    public void render(PortableCampfireBlockEntity campfireBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Direction direction = campfireBlockEntity.getCachedState().get(FACING);
        ItemStack stack = campfireBlockEntity.getItemBeingCooked();
            if (!stack.isEmpty()) {
                matrixStack.push();
                matrixStack.translate(0.5D, 0.3D, 0.5D);
                //matrixStack.translate(0.5D, 0.3D, 0.1D);
                Direction direction2 = Direction.fromHorizontal((direction.getHorizontal()) % 4);
                float g = -direction2.asRotation();
                matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(g));
                matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
                //matrixStack.translate(-0.3125D, -0.3125D, 0.0D);
                matrixStack.translate(0D, 0.1D, 0D);
                matrixStack.scale(0.375F, 0.375F, 0.375F);
                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, i, j, matrixStack, vertexConsumerProvider);
                matrixStack.pop();
            }

    }
}
