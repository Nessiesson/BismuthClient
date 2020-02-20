package si.bismuth.bismuthclient;

import com.google.common.collect.ImmutableList;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.PluginChannelListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.api.WorldObserver;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import si.bismuth.bismuthclient.config.Config;
import si.bismuth.bismuthclient.config.GuiConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class LiteModBismuthClient implements Tickable, Configurable, PluginChannelListener, WorldObserver {
	public static final Config config = new Config();
	public static final KeyBinding sortInventory = new KeyBinding("SortInventory", Keyboard.KEY_NONE, "@NAME@");
	public static final KeyBinding sortContainer = new KeyBinding("SortContainer", Keyboard.KEY_NONE, "@NAME@");
	public static final KeyBinding getinv = new KeyBinding("GetInventory", Keyboard.KEY_NONE, "@NAME@");
	public static final KeyBinding finditem = new KeyBinding("FindItem", Keyboard.KEY_NONE, "@NAME@");
	public static final Logger logger = LogManager.getLogger();
	private final static Random rng = new Random();
	private boolean shouldBeActive = false;
	private int bismuthProtocolId = 0;

	@Override
	public void init(File configPath) {
		LiteLoader.getInput().registerKeyBinding(sortInventory);
		LiteLoader.getInput().registerKeyBinding(sortContainer);
		LiteLoader.getInput().registerKeyBinding(getinv);
		LiteLoader.getInput().registerKeyBinding(finditem);
	}

	@Override
	public List<String> getChannels() {
		return ImmutableList.of("Bis|register", "Bis|sort", "Bis|getinventory", "Bis|searchforitem");
	}

	@Override
	public void onCustomPayload(String channel, PacketBuffer data) {
		switch (channel) {
			default: {
				logger.warn("Received data on registered channel '{}' that does not have a handler!", channel);
				break;
			}

			case "Bis|register": {
				this.shouldBeActive = true;
				this.bismuthProtocolId = data.readVarInt();
				break;
			}

			case "Bis|getinventory": {
				final int size = data.readVarInt();
				final NonNullList<ItemStack> inventory = NonNullList.withSize(size, ItemStack.EMPTY);
				for (int i = 0; i < size; i++) {
					try {
						inventory.set(i, data.readItemStack());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				break;
			}

			case "Bis|searchforitem": {
				final int size = data.readVarInt();
				final NonNullList<BlockPos> positions = NonNullList.withSize(size, BlockPos.ORIGIN);
				for (int i = 0; i < size; i++) {
					positions.set(i, data.readBlockPos());
				}

				final Minecraft mc = Minecraft.getMinecraft();
				mc.player.closeScreen();
				for (BlockPos pos : positions) {
					for (int i = 0; i < 20; i++) {
						mc.effectRenderer.addEffect(new ParticleShowItems(mc.player.world, pos.getX() + rng.nextDouble(), pos.getY() + rng.nextDouble(), pos.getZ() + rng.nextDouble(), 0D, 0D, 0D, 2F));
					}
				}

				break;
			}
		}
	}

	@Override
	public void onWorldChanged(World world) {
		if (world == null) {
			this.shouldBeActive = false;
		}
	}

	@Override
	public String getName() {
		return "@NAME@";
	}

	@Override
	public String getVersion() {
		return "@VERSION@";
	}

	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass() {
		return GuiConfig.class;
	}

	// @formatter:off
	@Override public void upgradeSettings(String version, File configPath, File oldConfigPath) {}
	@Override public void onTick(Minecraft mc, float partialTicks, boolean inGame, boolean clock) {}
	// @formatter:on
}
