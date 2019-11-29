package si.bismuth.bismuthclient;

import com.google.common.collect.ImmutableList;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.PluginChannelListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.PacketBuffer;
import org.lwjgl.input.Keyboard;
import si.bismuth.bismuthclient.config.Config;
import si.bismuth.bismuthclient.config.GuiConfig;

import java.io.File;
import java.util.List;

public class LiteModBismuthClient implements Tickable, Configurable, PluginChannelListener {
	public static Config config = new Config();
	public static KeyBinding sort = new KeyBinding("Sort", Keyboard.KEY_N, "@NAME@");

	@Override
	public void init(File configPath) {
		LiteLoader.getInput().registerKeyBinding(sort);
	}

	@Override
	public List<String> getChannels() {
		return ImmutableList.of("Bis|sort");
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
	@Override public void onCustomPayload(String channel, PacketBuffer data) {}
	@Override public void upgradeSettings(String version, File configPath, File oldConfigPath) {}
	@Override public void onTick(Minecraft mc, float partialTicks, boolean inGame, boolean clock) {}
	// @formatter:on
}
