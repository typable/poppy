package poppy.modules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import poppy.Utils;


public class DoubleDoorModule implements Listener
{
	private static final Material[] DOOR_TYPE = new Material[]
	{ 
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
		Player player = event.getPlayer();

		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(event.getClickedBlock() != null)
			{
				Block block = event.getClickedBlock();

				if(isDoor(block.getType()))
				{
					if(player.isSneaking())
					{
						if(!Utils.isEmpty(player.getInventory().getItemInMainHand()))
						{
							return;
						}

						if(!Utils.isEmpty(player.getInventory().getItemInOffHand()))
						{
							return;
						}
					}

					BlockState state = block.getState();
					Door door = (Door) state.getBlockData();

					Location location = getOppositeDoor(door, block.getLocation());

					if(location == null)
					{
						return;
					}

					Block opposite = block.getWorld().getBlockAt(location);

					if(block.getType() != opposite.getType())
					{
						return;
					}

					BlockState stateOpposite = opposite.getState();
					Door doorOpposite = (Door) stateOpposite.getBlockData();

					if(isCompatible(door, doorOpposite))
					{
						if(door.isOpen() && doorOpposite.isOpen())
						{
							doorOpposite.setOpen(false);

							stateOpposite.setBlockData(doorOpposite);
							stateOpposite.update();

							player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1F, 1F);
						}

						if(!door.isOpen() && !doorOpposite.isOpen())
						{
							doorOpposite.setOpen(true);

							stateOpposite.setBlockData(doorOpposite);
							stateOpposite.update();

							player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 1F, 1F);
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

	private Location getOppositeDoor(Door door, Location location)
	{
		int invert = door.getHinge() == Door.Hinge.LEFT ? 1 : -1;

		switch(door.getFacing())
		{
			case NORTH:
				location.add(1 * invert, 0, 0);
				break;
			case EAST:
				location.add(0, 0, 1 * invert);
				break;
			case SOUTH:
				location.add(-1 * invert, 0, 0);
				break;
			case WEST:
				location.add(0, 0, -1 * invert);
				break;
			default:
				return null;
		}

		return location;
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
}
