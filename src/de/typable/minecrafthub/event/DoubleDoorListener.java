package de.typable.minecrafthub.event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DoubleDoorListener implements Listener
{
	private static final Material[] DOOR_TYPE = new Material[] {
			Material.OAK_DOOR,
			Material.SPRUCE_DOOR,
			Material.BIRCH_DOOR,
			Material.JUNGLE_DOOR,
			Material.ACACIA_DOOR,
			Material.DARK_OAK_DOOR,
			Material.ACACIA_DOOR,
			Material.CRIMSON_DOOR,
			Material.WARPED_DOOR
	};
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(event.getClickedBlock() != null)
			{
				Block block = event.getClickedBlock();
				
				if(isDoor(block.getType()))
				{
					List<Block> blockList = getNearbyDoors(block.getLocation());
					
					if(blockList.isEmpty())
					{
						return;
					}
					
					for(Block blockItem : blockList)
					{
						if(block.getType() != blockItem.getType())
						{
							continue;
						}
						
						BlockState state = block.getState();
						BlockState stateItem = blockItem.getState();
						
						Door door = (Door) state.getBlockData();
						Door doorItem = (Door) stateItem.getBlockData();
						
						if(isCompatible(door, doorItem))
						{
							if(door.isOpen() && doorItem.isOpen())
							{
								doorItem.setOpen(false);
								
								stateItem.setBlockData(doorItem);
								stateItem.update();
							}
							
							if(!door.isOpen() && !doorItem.isOpen())
							{
								doorItem.setOpen(true);
								
								stateItem.setBlockData(doorItem);
								stateItem.update();
							}
						}
					}
				}
			}
		}
	}
	
	private boolean isDoor(Material material)
	{
		for(Material door : DOOR_TYPE)
		{
			if(material == door)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isCompatible(Door door, Door doorItem)
	{
		if(door.getFacing() != doorItem.getFacing())
		{
			return false;
		}
		
		if(door.getHalf() != doorItem.getHalf())
		{
			return false;
		}
		
		if(door.getHinge() == doorItem.getHinge())
		{
			return false;
		}
		
		return true;
	}
	
	private List<Block> getNearbyDoors(Location location)
	{
		List<Block> blockList = new ArrayList<>();
		
		int blockX = location.getBlockX();
		int blockY = location.getBlockY();
		int blockZ = location.getBlockZ();
		
		for(int x = blockX - 1; x <= blockX + 1; x++)
		{
			for(int z = blockZ - 1; z <= blockZ + 1; z++)
			{
				if(blockX != x || blockZ != z)
				{
					Block block = location.getWorld().getBlockAt(x, blockY, z);
					
					if(block != null && isDoor(block.getType()))
					{
						blockList.add(block);
					}
				}
			}
		}
		
		return blockList;
	}
}
