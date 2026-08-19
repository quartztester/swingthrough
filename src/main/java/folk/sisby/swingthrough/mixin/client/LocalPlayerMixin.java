package folk.sisby.swingthrough.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
	@ModifyVariable(method = "pick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/Entity;pick(DFZ)Lnet/minecraft/world/phys/HitResult;"))
	private static HitResult swingthrough$seeThroughTransparentBlocks(HitResult hitResult) {
		if (hitResult instanceof BlockHitResult) {
			BlockHitResult blockHit = (BlockHitResult) hitResult;
			Level level = Minecraft.getInstance().level;
			if (level != null) {
				BlockPos pos = blockHit.getBlockPos();
				if (level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) {
					return BlockHitResult.miss(blockHit.getLocation(), blockHit.getDirection(), pos);
				}
			}
		}
		return hitResult;
	}
}
