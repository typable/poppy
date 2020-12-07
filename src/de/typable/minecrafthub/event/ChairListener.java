package de.typable.minecrafthub.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;


public class ChairListener implements Listener
{
	private static final Material[] CHAIR_TYPE = new Material[] { Material.OAK_STAIRS, Material.SPRUCE_STAIRS, Material.BIRCH_STAIRS, Material.JUNGLE_STAIRS, Material.ACACIA_STAIRS, Material.DARK_OAK_STAIRS, Material.ACACIA_STAIRS, Material.CRIMSON_STAIRS, Material.WARPED_STAIRS };

	private Map<Block, Arrow> blockMap = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(event.getClickedBlock() != null && !event.getPlayer().isSneaking())
			{
				Block block = event.getClickedBlock();

				if(isChair(block.getType()))
				{
					if(block.getLocation().add(0.5, 0.5, 0.5).distance(player.getLocation()) > 2)
					{
						return;
					}
					
					if(blockMap.containsKey(block))
					{
						return;
					}

					Block upperBlock = block.getWorld().getBlockAt(block.getLocation().add(0, 1, 0));

					if(upperBlock != null && upperBlock.getType() == Material.AIR)
					{
						Stairs stairs = (Stairs) block.getState().getBlockData();

						if(isCompatible(stairs))
						{
							Location location = block.getLocation().add(0.5, 0, 0.5);
							Arrow arrow = (Arrow) block.getWorld()
							      .spawnArrow(location, new Vector(), 0F, 0F);
							arrow.setGravity(false);
							arrow.setInvulnerable(true);
							arrow.setPickupStatus(PickupStatus.DISALLOWED);
							arrow.setPassenger((Entity) event.getPlayer());

							event.setCancelled(true);

							blockMap.put(block, arrow);
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
				if(blockMap.containsValue(entity))
				{
					for(Entry<Block, Arrow> entry : blockMap.entrySet())
					{
						if(entry.getValue() == entity)
						{
							blockMap.remove(entry.getKey());

							break;
						}
					}
				}

				entity.remove();
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		Block block = event.getBlock();

		if(blockMap.containsKey(block))
		{
			Arrow arrow = blockMap.get(block);
			arrow.eject();

			blockMap.remove(block);
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
		if(stairs.getHalf() != Half.BOTTOM)
		{
			return false;
		}

		if(stairs.isWaterlogged())
		{
			return false;
		}

		if(stairs.getShape() == Stairs.Shape.INNER_RIGHT || stairs
		      .getShape() == Stairs.Shape.INNER_LEFT)
		{
			return false;
		}

		return true;
	}
}
