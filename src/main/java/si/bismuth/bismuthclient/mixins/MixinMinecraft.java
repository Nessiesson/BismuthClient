package si.bismuth.bismuthclient.mixins;

import com.mumfrey.liteloader.client.ClientPluginChannelsClient;
import com.mumfrey.liteloader.core.PluginChannels;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import si.bismuth.bismuthclient.LiteModBismuthClient;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	@Inject(method = "dispatchKeypresses", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
	private void sortInventoryPlease(CallbackInfo ci, int i) {
		if (i == LiteModBismuthClient.sort.getKeyCode()) {
			final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
			buf.writeBoolean(GuiScreen.isCtrlKeyDown());
			ClientPluginChannelsClient.sendMessage("Bis|sort", buf, PluginChannels.ChannelPolicy.DISPATCH_IF_REGISTERED);
		}
	}
}
