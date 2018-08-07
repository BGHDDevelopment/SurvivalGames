package me.ItsJasonn.SurvivalGames.Commands;

import me.ItsJasonn.SurvivalGames.Utils.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Hub
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    String command = cmd.getName();
    if (command.equalsIgnoreCase("Hub")) {
      if ((sender instanceof Player))
      {
        Player player = (Player)sender;
        
        Server server = new Server("lobby");
        server.sendPlayerToServer(player);
      }
      else if ((sender instanceof ConsoleCommandSender))
      {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "This is a player-only command!");
      }
    }
    return false;
  }
}
