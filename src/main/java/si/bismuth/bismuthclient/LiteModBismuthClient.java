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
	public KeyBinding sort = new KeyBinding("Sort", Keyboard.KEY_N, this.getName());

	@Override
	public void init(File configPath) {
		LiteLoader.getInput().registerKeyBinding(this.sort);
	}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		if (!inGame) {
			return;
		}
	}

	@Override
	public void onCustomPayload(String channel, PacketBuffer data) {
		System.out.println(channel);
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

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
		// noop
	}
}
