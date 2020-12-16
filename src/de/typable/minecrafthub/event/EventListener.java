package de.typable.minecrafthub.event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class EventListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.PHYSICAL)
		{
			if(event.getClickedBlock() != null)
			{
				if(event.getClickedBlock().getType() == Material.FARMLAND)
				{
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event)
	{
		if(event.getEntityType() == EntityType.CREEPER)
		{
			event.blockList().clear();
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		// FIXME Conversion Exception on '%

		String format = ChatColor.WHITE + event.getPlayer().getName() + ": " + ChatColor.GRAY
		      + event.getMessage();
		event.setFormat(format);
	}
}
