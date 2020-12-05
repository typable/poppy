package de.typable.minecrafthub.event;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import de.typable.minecrafthub.constant.DefaultConstants;


public class StandbyListener implements Listener, Runnable
{
	private Plugin plugin;
	private BukkitTask task;
	private boolean enabled;
	private long delay;

	public StandbyListener(Plugin plugin)
	{
		this.plugin = plugin;
		this.delay = 10 * 60;
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event)
	{
		if(enabled && task != null)
		{
			task.cancel();
			task = null;
		}
	}

	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent event)
	{
		if(enabled)
		{
			Collection<? extends Player> online = Bukkit.getOnlinePlayers();

			if(online.size() == 1 && online.contains(event.getPlayer()))
			{
				task = Bukkit.getScheduler().runTaskLater(plugin, this, delay * DefaultConstants.TICK);
			}
		}
	}

	@Override
	public void run()
	{
		Bukkit.getServer().shutdown();
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public long getDelay()
	{
		return delay;
	}

	public void setDelay(long delay)
	{
		this.delay = delay;
	}
}
