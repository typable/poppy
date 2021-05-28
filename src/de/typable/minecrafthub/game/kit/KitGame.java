package de.typable.minecrafthub.game.kit;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import de.typable.minecrafthub.game.Game;

public class KitGame extends Game
{
	public KitGame()
	{
		super();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;

			if(args.length > 0)
			{
				if(args[0].equals("join"))
				{
					join(player);
				}

				if(args[0].equals("leave"))
				{
					leave(player);
				}
			}
		}

		return false;
	}

	@Override
	public void join(Player player)
	{
		super.join(player);
		player.setGameMode(GameMode.SURVIVAL);
		player.getInventory().clear();
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(isIngame(event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}
}
