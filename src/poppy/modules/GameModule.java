package poppy.modules;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class GameModule implements Listener
{
    private Map<UUID, PlayerState> states = new HashMap<>();

    public void onDisable()
    {
        this.leaveAll();
    }

    @EventHandler
    private void onPlayerQuitEvent(final PlayerQuitEvent event)
    {
        /* NOTE: No need for checking if user is ingame because the leave()
           method does already check it. */
        this.leave(event.getPlayer());
    }

    @EventHandler
    private void onBlockBreakEvent(final BlockBreakEvent event)
    {
        if (this.isIngame(event.getPlayer()))
        {
            this.onBlockBreak(event);
        }
    }

    @EventHandler
    private void onBlockPlaceEvent(final BlockPlaceEvent event)
    {
        if (this.isIngame(event.getPlayer()))
        {
            this.onBlockPlace(event);
        }
    }

    @EventHandler
    private void onPlayerInteractEvent(final PlayerInteractEvent event)
    {
        if (this.isIngame(event.getPlayer()))
        {
            this.onPlayerInteract(event);
        }
    }

    // NOTE: Default implementations

    public void onJoin(final Player player) { }

    public boolean canJoin(final Player player)
    {
        return true;
    }

    public void onLeave(final Player player) { }

    public void onBlockBreak(final BlockBreakEvent event)
    {
        event.setCancelled(true);
    }

    public void onBlockPlace(final BlockPlaceEvent event)
    {
        event.setCancelled(true);
    }

    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        event.setCancelled(true);
    }

    public void join(final Player player)
    {
        if (!this.canJoin(player))
        {
            return;
        }
    
        final UUID uuid = player.getUniqueId();
        this.states.put(uuid, PlayerState.consume(player));
    
        this.onJoin(player);
    }

    public void leave(final Player player)
    {
        if (!this.isIngame(player))
        {
            return;
        }
        
        this.onLeave(player);
        
        final UUID uuid = player.getUniqueId();
        final PlayerState state = this.states.get(uuid);
        state.restore(player);

        this.states.remove(uuid);
    }

    public void leaveAll()
    {
        for (final UUID uuid : this.states.keySet())
        {
            this.leave(Bukkit.getPlayer(uuid));
        }
    }

    public List<Player> getPlayers()
    {
        final List<Player> players = new ArrayList<>();

        for (final UUID uuid : this.states.keySet())
        {
            players.add(Bukkit.getPlayer(uuid));
        }

        return players;
    }

    public int getPlayerCount()
    {
        return this.states.size();
    }

    private boolean isIngame(final Player player)
    {
        final UUID uuid = player.getUniqueId();

        return this.states.containsKey(uuid);
    }
}

class PlayerState
{
    private GameMode gameMode;
    private Double health;
    private Integer foodLevel;

    private PlayerState() { }

    public static PlayerState consume(final Player player)
    {
        final PlayerState state = PlayerState.clone(player);

        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20.0);
        player.setFoodLevel(20);

        return state;
    }

    public static PlayerState clone(final Player player)
    {
        final PlayerState state = new PlayerState();
        state.gameMode = player.getGameMode();
        state.health = player.getHealth();
        state.foodLevel = player.getFoodLevel();

        return state;
    }

    public void restore(final Player player)
    {
        player.getInventory().clear();
    
        player.setGameMode(this.gameMode);
        player.setHealth(this.health);
        player.setFoodLevel(this.foodLevel);
    }
}
