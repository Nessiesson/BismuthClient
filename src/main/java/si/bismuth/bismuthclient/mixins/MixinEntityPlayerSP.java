package si.bismuth.bismuthclient.mixins;

import net.minecraft.client.entity.EntityPlayerSP;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {
	@Redirect(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;motionY:D", opcode = Opcodes.GETFIELD, ordinal = 0))
	private double mc111444(EntityPlayerSP player) {
		return -1D;
	}
}
