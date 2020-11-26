package de.typable.minecrafthub.event;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.PHYSICAL)
		{
			if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.FARMLAND)
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event)
	{
		event.blockList().clear();
	}
}
