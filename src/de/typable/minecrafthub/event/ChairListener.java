package de.typable.minecrafthub.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import de.typable.minecrafthub.util.Util;


public class ChairListener implements Listener
{
	private static final Material[] CHAIR_TYPE = new Material[]
	{
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

	private Map<Block, ArmorStand> blockMap = new HashMap<>();

	public void onDisable()
	{
		for(ArmorStand armorStand : blockMap.values())
		{
			armorStand.eject();
			armorStand.remove();
		}
	}

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
					if(!isInRange(block, player))
					{
						return;
					}
					
					if(blockMap.containsKey(block))
					{
						return;
					}

					if(isCompatible(block))
					{
						ArmorStand armorStand = createMountableChair(block, player);

						if(armorStand == null)
						{
							return;
						}

						event.setCancelled(true);

						blockMap.put(block, armorStand);
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

			if(entity != null && entity instanceof ArmorStand)
			{
				if(blockMap.containsValue(entity))
				{
					for(Entry<Block, ArmorStand> entry : blockMap.entrySet())
					{
						if(entry.getValue() == entity)
						{
							blockMap.remove(entry.getKey());

							break;
						}
					}
				}

				Location location = entity.getLocation();
				location.add(0, 1, 0);

				for(Entity passenger : entity.getPassengers()) {
					location.setPitch(passenger.getLocation().getPitch());
					location.setYaw(passenger.getLocation().getYaw());
					passenger.teleport(location);
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
			ArmorStand armorStand = blockMap.get(block);
			armorStand.eject();

			blockMap.remove(block);
		}
	}

	private boolean isInRange(Block block, Player player)
	{
		return block.getLocation().add(0.5, 0.5, 0.5).distance(player.getLocation()) <= 2;
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

	private boolean isCompatible(Block block)
	{
		Block upperBlock = block.getWorld().getBlockAt(block.getLocation().add(0, 1, 0));

		if(!Util.isAir(upperBlock.getType()))
		{
			return false;
		}

		Stairs stairs = (Stairs) block.getState().getBlockData();

		if(stairs.getHalf() != Half.BOTTOM)
		{
			return false;
		}

		if(stairs.isWaterlogged())
		{
			return false;
		}

		if(stairs.getShape() == Stairs.Shape.INNER_RIGHT || stairs.getShape() == Stairs.Shape.INNER_LEFT)
		{
			return false;
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	private ArmorStand createMountableChair(Block block, Player player)
	{
		Stairs stairs = (Stairs) block.getState().getBlockData();

		Float yaw = Util.convertFacingToYaw(stairs.getFacing());

		if(yaw == null)
		{
			return null;
		}

		Location location = block.getLocation().add(0.5, -0.4, 0.5);
		location.setYaw(yaw);

		ArmorStand armorStand = (ArmorStand) block.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		armorStand.setPassenger((Entity) player);
		armorStand.setSmall(true);
		armorStand.setGravity(false);
		armorStand.setInvulnerable(true);
		armorStand.setVisible(false);

		return armorStand;
	}
}
