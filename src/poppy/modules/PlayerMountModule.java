package poppy.modules;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PlayerMountModule implements Listener
{
	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event)
	{
		final Player player = event.getPlayer();
        final Entity entity = event.getRightClicked();

        if(entity instanceof Player)
        {
            final Player mountablePlayer = (Player) entity;
            final Material helmet = mountablePlayer.getInventory().getHelmet().getType();

            if(helmet.equals(Material.SADDLE) && mountablePlayer.getPassengers().size() == 0)
            {
                mountablePlayer.addPassenger(player);
            }
        }
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		final HumanEntity entity = event.getWhoClicked();

		if(entity instanceof Player)
		{
			final Player player = (Player) entity;
			final SlotType slotType = event.getSlotType();

			if(slotType != SlotType.ARMOR)
			{
				return;
			}

			final ClickType clickType = event.getClick();

			if(!(clickType == ClickType.LEFT || clickType == ClickType.RIGHT))
			{
				return;
			}

			final ItemStack head = event.getCurrentItem();

			if(head == null || head.getType() != Material.SADDLE)
			{
				return;
			}

			player.eject();
		}
	}
}
