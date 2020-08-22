package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;

public class AntiGenerationLag extends JavaPlugin {

	static public JavaPlugin instance;

	private ChunkGenerationListener listener = new ChunkGenerationListener();

	@Override
	public void onEnable() {
		instance = this;
		Config.readConfig(this);
		this.getServer().getPluginManager().registerEvents(listener, this);
	}
}