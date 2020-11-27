package de.typable.minecrafthub.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;


public class ChairListener implements Listener
{
	private static final Material[] CHAIR_TYPE = new Material[] {
			Material.OAK_STAIRS,
			Material.SPRUCE_STAIRS,
			Material.BIRCH_STAIRS,
			Material.JUNGLE_STAIRS,
			Material.ACACIA_STAIRS,
			Material.DARK_OAK_STAIRS,
			Material.ACACIA_STAIRS,
			Material.CRIMSON_STAIRS,
			Material.WARPED_STAIRS
	};
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(event.getClickedBlock() != null)
			{
				Block block = event.getClickedBlock();
				
				if(isChair(block.getType()))
				{
					Block upperBlock = block.getWorld().getBlockAt(block.getLocation().add(0, 1, 0));
					
					if(upperBlock != null && upperBlock.getType() == Material.AIR)
					{
						Stairs stairs = (Stairs) block.getState().getBlockData();
						
						if(isCompatible(stairs))
						{
							Location location = block.getLocation().add(0.5, 0, 0.5);
							Arrow arrow = (Arrow) block.getWorld().spawnArrow(location, new Vector(), 0F, 0F);
							arrow.setGravity(false);
							arrow.setInvulnerable(true);
							arrow.setPickupStatus(PickupStatus.DISALLOWED);
							arrow.setPassenger((Entity) event.getPlayer());
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDismount(EntityDismountEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			Entity entity = event.getDismounted();
			
			if(entity != null && entity instanceof Arrow)
			{
				entity.remove();
			}
		}
	}
	
	private boolean isChair(Material material)
	{
		for(Material chair : CHAIR_TYPE)
		{
			if(material == chair)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isCompatible(Stairs stairs)
	{
		if(stairs.isWaterlogged())
		{
			return false;
		}
		
		if(stairs.getShape() != Stairs.Shape.STRAIGHT)
		{
			return false;
		}
		
		return true;
	}
}
