package org.redcraft.antigenerationlag;

import org.bukkit.plugin.java.JavaPlugin;

public class AntiGenerationLag extends JavaPlugin {

	private ChunkGenerationListener listener = new ChunkGenerationListener();
	private Scheduler scheduler = new Scheduler();

	@Override
	public void onEnable() {
		Config.readConfig(this);
		this.getServer().getPluginManager().registerEvents(listener, this);
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, scheduler, 0, Config.freezeCheckTickInterval);
	}
}