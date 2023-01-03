package de.typable.minecrafthub.constant;

import org.bukkit.ChatColor;


public class Constants
{
	public static final long TICK = 20;
	public static final int BLOCKS_PER_CHUNK = 16;
	public static final int CHUNKS_PER_EMERALD = 25;
	public static final int MIN_FEE = 1;
	public static final int MAX_FEE = 64;

	public static final class Messages
	{
		public static final String NOT_ENOUGH_PERMISSION = ChatColor.RED + "You don't have enough Permission to perform this command!";
		public static final String FAILED_TO_SAVE_CONFIG_FILE = ChatColor.RED + "Failed to save changes!";
	}
}
