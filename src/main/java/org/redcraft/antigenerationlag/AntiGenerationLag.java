package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;

public class AntiGenerationLag extends JavaPlugin {

	static private JavaPlugin instance;

	private ChunkGenerationListener listener = new ChunkGenerationListener();
	private Scheduler scheduler = new Scheduler();

	@Override
	public void onEnable() {
		instance = this;
		Config.readConfig(this);
		this.getServer().getPluginManager().registerEvents(listener, this);
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, scheduler, 0, Config.freezeCheckTickInterval);
	}

	static public JavaPlugin getInstance() {
		return instance;
	}
}