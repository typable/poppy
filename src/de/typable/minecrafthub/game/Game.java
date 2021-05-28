package de.typable.minecrafthub.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.typable.minecrafthub.Config;
import de.typable.minecrafthub.constant.DefaultConstants;

public abstract class Game implements CommandExecutor, Listener
{
	private Map<Player, PlayerState> players;
	private Config config;

	public Game()
	{
		players = new HashMap<>();
	}

	public void join(Player player)
	{
		if(players.containsKey(player))
		{
			player.sendMessage(DefaultConstants.Messages.ALREADY_INGAME);

			return;
		}

		players.put(player, PlayerState.of(player));
	}

	public void leave(Player player)
	{
		PlayerState playerState = players.get(player);

		if(playerState == null)
		{
			player.sendMessage(DefaultConstants.Messages.NOT_INGAME);

			return;
		}

		playerState.apply(player);
		players.remove(player);
	}

	public void leaveAll()
	{
		Iterator<Entry<Player, PlayerState>> iterator = players.entrySet().iterator();

		while(iterator.hasNext())
		{
			Entry<Player, PlayerState> entry = iterator.next();
			Player player = entry.getKey();
			PlayerState playerState = entry.getValue();

			playerState.apply(player);
			iterator.remove();
		}
	}

	public boolean isIngame(Player player)
	{
		return player != null && players.containsKey(player);
	}

	public Map<Player, PlayerState> getPlayers()
	{
		return players;
	}

	public void setConfig(Config config)
	{
		this.config = config;
	}

	public Config getConfig()
	{
		return config;
	}
}
