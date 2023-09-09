package poppy.modules;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SpleefGameModule extends GameModule
{
    private final static int MAX_PLAYERS = 2;
    private final static ItemStack ITEM_SHOVEL = new ItemStack(Material.STONE_SHOVEL);
    private final static Material BLOCK_TYPE = Material.SUSPICIOUS_SAND;

    private boolean playing = false;

    @Override
    public void onJoin(final Player player)
    {
        if (this.getPlayerCount() == MAX_PLAYERS)
        {
            doStart();
        }
    }

    @Override
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        if (this.playing && event.getHand() == EquipmentSlot.HAND)
        {
            final Block block = event.getClickedBlock();

            if (block != null && block.getType() == BLOCK_TYPE)
            {
                // do stuff here
                return;
            }
        }

        event.setCancelled(true);
    }

    @Override
    public boolean canJoin(final Player player)
    {
    	return !this.playing && this.getPlayerCount() < MAX_PLAYERS;
    }

    private void doStart()
    {
        this.playing = true;
    
        for (final Player player : this.getPlayers())
        {
            player.getInventory().setItem(0, ITEM_SHOVEL);
        }
    }
}
