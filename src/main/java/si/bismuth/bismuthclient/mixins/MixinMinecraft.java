package si.bismuth.bismuthclient.mixins;

import com.mumfrey.liteloader.client.ClientPluginChannelsClient;
import com.mumfrey.liteloader.core.PluginChannels;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import si.bismuth.bismuthclient.LiteModBismuthClient;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	@Shadow
	public RayTraceResult objectMouseOver;

	@Inject(method = "dispatchKeypresses", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
	private void sortInventoryPlease(CallbackInfo ci, int i) {
		if (!Keyboard.getEventKeyState()) {
			return;
		}

		final PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		if (i == LiteModBismuthClient.sort.getKeyCode()) {
			buf.writeBoolean(GuiScreen.isCtrlKeyDown());
			ClientPluginChannelsClient.sendMessage("Bis|sort", buf, PluginChannels.ChannelPolicy.DISPATCH_IF_REGISTERED);
		} else if (i == LiteModBismuthClient.getinv.getKeyCode()) {
			buf.writeBlockPos(this.objectMouseOver.getBlockPos());
			ClientPluginChannelsClient.sendMessage("Bis|getinventory", buf, PluginChannels.ChannelPolicy.DISPATCH_IF_REGISTERED);
		} else if (i == LiteModBismuthClient.finditem.getKeyCode()) {
			final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
			if (screen instanceof GuiContainer) {
				final GuiContainer container = (GuiContainer) screen;
				final Slot mouse = ((IMixinGuiContainer) container).getHoveredSlot();
				if (mouse != null) {
					final ItemStack stack = mouse.getStack();
					if (!stack.isEmpty()) {
						buf.writeItemStack(stack);
						ClientPluginChannelsClient.sendMessage("Bis|searchforitem", buf, PluginChannels.ChannelPolicy.DISPATCH_IF_REGISTERED);
					}
				}
			}
		}
	}
}
