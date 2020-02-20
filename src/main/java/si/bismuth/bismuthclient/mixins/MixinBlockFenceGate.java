package si.bismuth.bismuthclient.mixins;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockFenceGate.class)
public abstract class MixinBlockFenceGate {
	@Redirect(method = "canPlaceBlockAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/material/Material;isSolid()Z"))
	private boolean onPlaceFenceGate(Material material) {
		return true;
	}
}
