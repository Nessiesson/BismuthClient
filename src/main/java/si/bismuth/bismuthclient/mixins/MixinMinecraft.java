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
		if (i == LiteModBismuthClient.sortInventory.getKeyCode()) {
			buf.writeBoolean(true);
			this.sendPluginChannelMessage("Bis|sort", buf);
		} else if (i == LiteModBismuthClient.sortContainer.getKeyCode()) {
			buf.writeBoolean(false);
			this.sendPluginChannelMessage("Bis|sort", buf);
		} else if (i == LiteModBismuthClient.getinv.getKeyCode()) {
			buf.writeBlockPos(this.objectMouseOver.getBlockPos());
			this.sendPluginChannelMessage("Bis|getinventory", buf);
		} else if (i == LiteModBismuthClient.finditem.getKeyCode()) {
			final GuiScreen screen = Minecraft.getMinecraft().currentScreen;
			if (screen instanceof GuiContainer) {
				final GuiContainer container = (GuiContainer) screen;
				final Slot mouse = ((IMixinGuiContainer) container).getHoveredSlot();
				if (mouse != null) {
					final ItemStack stack = mouse.getStack();
					if (!stack.isEmpty()) {
						buf.writeItemStack(stack);
						this.sendPluginChannelMessage("Bis|searchforitem", buf);
					}
				}
			}
		}
	}

	private boolean sendPluginChannelMessage(String channel, PacketBuffer data) {
		return ClientPluginChannelsClient.sendMessage(channel, data, PluginChannels.ChannelPolicy.DISPATCH_IF_REGISTERED);
	}
}
