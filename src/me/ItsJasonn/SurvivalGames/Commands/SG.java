package me.ItsJasonn.SurvivalGames.Commands;

import me.ItsJasonn.SurvivalGames.Main.Plugin;
import me.ItsJasonn.SurvivalGames.Main.Tools;
import me.ItsJasonn.SurvivalGames.Map.Map;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SG implements CommandExecutor {
	@SuppressWarnings("static-access")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String command = cmd.getName();
		if (command.equalsIgnoreCase("SG")) {
			if ((sender instanceof Player)) {
				Player player = (Player) sender;
				if (player.isOp()) {
					if (args.length == 0) {
						player.sendMessage(ChatColor.DARK_AQUA + "/SG" + ChatColor.AQUA + " - Show this help page.");
						player.sendMessage(ChatColor.DARK_AQUA + "/SG Create" + ChatColor.AQUA + " - Create a new map..");
						player.sendMessage(ChatColor.DARK_AQUA + "/SG Delete" + ChatColor.AQUA + " - Delete an existing survival games map.");
						player.sendMessage(ChatColor.DARK_AQUA + "/SG Setlobby" + ChatColor.AQUA + " - Set the lobby spawn location.");
						player.sendMessage(ChatColor.DARK_AQUA + "/SG Setspawn [Index]" + ChatColor.AQUA + " - Set a spawn location for an index.");
						player.sendMessage(ChatColor.DARK_AQUA + "/SG Setdeathmatch" + ChatColor.AQUA + " - Set the spawn location for the deathmatch.");
						player.sendMessage(ChatColor.DARK_AQUA + "/SG Forcestart" + ChatColor.AQUA + " - Start a match of the player's current game.");
						player.sendMessage(ChatColor.DARK_AQUA + "/SG ChestMode" + ChatColor.AQUA + " - Enables/Disables the ability to add Survival Games items to the world.");
					} else if (args.length == 1) {
						if (args[0].equalsIgnoreCase("Create")) {
							if (!Map.getMapManager().DoesExist()) {
								Map.getMapManager().CreateMap();
								player.sendMessage(ChatColor.GREEN + "Survival games map has been created!");
							} else {
								player.sendMessage(ChatColor.RED + "Survival games map does not exist!");
							}
						} else if (args[0].equalsIgnoreCase("Delete")) {
							Map.getMapManager().RemoveMap();
							player.sendMessage(ChatColor.GREEN + "Survival games map has been removed!");
						} else if (args[0].equalsIgnoreCase("Setlobby")) {
							if (Map.getMapManager().DoesExist()) {
								Map.getMapManager().SetLobby(player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
								player.sendMessage(ChatColor.GREEN + "Lobby location for Survival games map has been set!");
							} else {
								player.sendMessage(ChatColor.RED + "This Survival games map does not exist!");
							}
						} else if (args[0].equalsIgnoreCase("Setdeathmatch")) {
							if (Map.getMapManager().DoesExist()) {
								Map.getMapManager().SetSpawn(0, player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
								player.sendMessage(ChatColor.GREEN + "Deathmatch location for Survival games map has been set!");
							} else {
								player.sendMessage(ChatColor.RED + "This Survival games map does not exist!");
							}
						} else if (args[0].equalsIgnoreCase("SetSpawn")) {
							player.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.DARK_AQUA + "/SG " + WordUtils.capitalizeFully(args[0]) + " [Index]");
						} else if (args[0].equalsIgnoreCase("Forcestart")) {
							Map.getMapManager().StartGame();
						} else if (args[0].equalsIgnoreCase("ChestMode")) {
							if(Plugin.getCore().chestMode.contains(player)) {
								player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "DISABLED " + ChatColor.GREEN + "chest-mode!");
								Plugin.getCore().chestMode.remove(player);
							} else {
								player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "ENABLED " + ChatColor.GREEN + "chest-mode!");
								player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Right-Click with an item: " + ChatColor.GREEN + "Add/Remove the item from the list.");
								Plugin.getCore().chestMode.add(player);
							}
						}
					} else if ((args.length == 2) && (args[0].equalsIgnoreCase("Setspawn"))) {
						String index = args[1];
						if (Tools.isInt(index)) {
							if (Map.getMapManager().DoesExist()) {
								Map.getMapManager().SetSpawn(Integer.parseInt(index), player.getLocation().getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
								player.sendMessage(ChatColor.GREEN + "Spawn location in Survival Games for index '" + index + "' has been set!");
							} else {
								player.sendMessage(ChatColor.RED + "Survival games map does not exist!");
							}
						} else {
							player.sendMessage(ChatColor.RED + "Third parameter '" + args[1] + "' is not a number!");
						}
					}
				} else {
					player.sendMessage(ChatColor.RED + "You are not permitted to use this command!");
				}
			} else if ((sender instanceof ConsoleCommandSender)) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "This is a player-only command!");
			}
		}
		return false;
	}
}
