package si.bismuth.bismuthclient.mixins;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetHandlerPlayServer.class)
public abstract class MixinNetHandlerPlayServer {
	@Redirect(method = "processEntityAction", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayerMP;motionY:D", opcode = Opcodes.GETFIELD))
	private double mc111444(EntityPlayerMP player) {
		return -1D;
	}
}
