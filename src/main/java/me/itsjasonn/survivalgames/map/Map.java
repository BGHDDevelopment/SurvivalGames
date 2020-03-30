package me.itsjasonn.survivalgames.map;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import me.itsjasonn.survivalgames.listener.EntityStats;
import me.itsjasonn.survivalgames.main.Plugin;
import me.itsjasonn.survivalgames.utils.DateCalculator;
import me.itsjasonn.survivalgames.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Map {
	private File file = new File(Plugin.getCore().getDataFolder(), "/data/maps.yml");
	private YamlConfiguration config = YamlConfiguration.loadConfiguration(this.file);
	public static ArrayList<Player> playerList = new ArrayList<Player>();
	public static HashMap<Player, Integer> playerID = new HashMap<Player, Integer>();
	public static ArrayList<Player> spectators = new ArrayList<Player>();
	public static boolean ending = false;
	public static boolean inLobby = true;
	public static int matchCounter = 30;
	public static int matchID = 0;
	private static Map instance = new Map();

	public static Map getMapManager() {
		return instance;
	}

	public void CreateMap() {
		getConfig().createSection("lobby");
		getConfig().createSection("spawns");
		saveFile();
	}

	public void RemoveMap() {
		this.file.delete();
	}

	public void SetLobby(World world, double x, double y, double z, float yaw, float pitch) {
		getConfig().set("lobby.world", world.getName());
		getConfig().set("lobby.x", Double.valueOf(x));
		getConfig().set("lobby.y", Double.valueOf(y));
		getConfig().set("lobby.z", Double.valueOf(z));
		getConfig().set("lobby.yaw", Float.valueOf(yaw));
		getConfig().set("lobby.pitch", Float.valueOf(pitch));
		saveFile();
	}

	public void SetSpawn(int index, World world, double x, double y, double z, float yaw, float pitch) {
		getConfig().set("spawns." + index + ".world", world.getName());
		getConfig().set("spawns." + index + ".x", Double.valueOf(x));
		getConfig().set("spawns." + index + ".y", Double.valueOf(y));
		getConfig().set("spawns." + index + ".z", Double.valueOf(z));
		getConfig().set("spawns." + index + ".yaw", Float.valueOf(yaw));
		getConfig().set("spawns." + index + ".pitch", Float.valueOf(pitch));
		saveFile();
	}

	public void TeleportToHub(Player player) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF("lobby");
		} catch (Exception e) {
			e.printStackTrace();
		}
		player.sendPluginMessage(Plugin.getCore(), "BungeeCord", b.toByteArray());
	}

	public void resetScoreboard(Player player) {
		Scoreboard sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Objective obj = sb.registerNewObjective("timer", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + " ---=[Survival Games]=--- ");
		if (inLobby) {
			obj.getScore(ChatColor.GREEN + "         Time left: " + ChatColor.YELLOW + DateCalculator.getMS(matchCounter, false)).setScore(2);
			obj.getScore(ChatColor.GREEN + "                               ").setScore(1);
			obj.getScore(ChatColor.GREEN + "         Players Left: " + ChatColor.YELLOW + playerList.size() + "/" + Bukkit.getServer().getMaxPlayers()).setScore(0);
		} else {
			obj.getScore(ChatColor.GREEN + "         Time left: " + ChatColor.YELLOW + DateCalculator.getMS(matchCounter, false)).setScore(2);
			obj.getScore(ChatColor.GREEN + "                               ").setScore(1);
			obj.getScore(ChatColor.GREEN + "         Players Left: " + ChatColor.YELLOW + (playerList.size() - spectators.size()) + "/" + Bukkit.getServer().getMaxPlayers()).setScore(0);
		}
		for (Player players : getPlayers()) {
			if (!ending) {
				players.setScoreboard(sb);
			} else {
				player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
			}
		}
	}

	public void TeleportToLobby(Player player) {
		Location location = new Location(Bukkit.getServer().getWorld(this.config.getString("lobby.world")), this.config.getDouble("lobby.x"), this.config.getDouble("lobby.y"), this.config.getDouble("lobby.z"), (float) this.config.getDouble("lobby.yaw"), (float) this.config.getDouble("lobby.pitch"));
		player.teleport(location);
	}

	public void TeleportToSpawn(Player player) {
		int index = playerID.get(player);
		Location location = new Location(Bukkit.getServer().getWorld(this.config.getString("spawns." + index + ".world")), this.config.getDouble("spawns." + index + ".x"), this.config.getDouble("spawns." + index + ".y"), this.config.getDouble("spawns." + index + ".z"), (float) this.config.getDouble("spawns." + index + ".yaw"), (float) this.config.getDouble("spawns." + index + ".pitch"));
		player.teleport(location);
	}

	@SuppressWarnings("deprecation")
	public void StartGame() {
		for (Player players : getPlayers()) {
			players.showPlayer(players);
		}
		StartCountdown(1);

		inLobby = false;
		for (Player players : playerList) {
			getMapManager().resetScoreboard(players);

			players.setGameMode(GameMode.ADVENTURE);
			players.setHealth(players.getMaxHealth());
			players.setFoodLevel(20);
			players.setLevel(0);
			players.setExp(0.0F);
			players.getInventory().clear();

			TeleportToSpawn(players);
			Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(50), Integer.valueOf(10), ChatColor.RED + "" + ChatColor.BOLD + "Survival Games", ChatColor.GOLD + "Free-for-all");

			players.getInventory().clear();
			players.sendMessage(ChatColor.GOLD + "---------========   Survival Games   ========---------");
			players.sendMessage(ChatColor.GOLD + "- Try to kill all of the living survivors.");
			players.sendMessage(ChatColor.GOLD + "- Search for chests to improve your gear and weapons.");
			players.sendMessage(ChatColor.GOLD + "- The last survivor alive wins.");
			players.sendMessage(ChatColor.GOLD + "- If the timer runs out the deathmatch will start");
			players.sendMessage(ChatColor.GOLD + "- The deathmatch is the round where the game ends when there is one player left.");
			players.sendMessage(ChatColor.GOLD + "-----------===========================-----------");
			players.playSound(players.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
			players.playSound(players.getEyeLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 10.0F, 0.0F);
			
			players.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "* PVP will start in 30 seconds! Prepare yourself! *");
			EntityStats.antiDamage.add(players);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Plugin.getCore(), new Runnable() {
				public void run() {
					EntityStats.antiDamage.remove(players);
					players.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "* PVP has started! *");
					players.playSound(players.getEyeLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 10.0F, 0.0F);
				}
			}, 30 * 20);
		}
	}

	public void StopMatch() {
		ArrayList<Player> list = new ArrayList<Player>();
		for (Player players : getPlayers()) {
			list.add(players);
		}
		for (Player players : list) {
			try {
				TeleportToHub(players);
				removePlayer(players, false);
			} catch (IllegalArgumentException e) {
				TeleportToLobby(players);

				removePlayer(players, false);
				addPlayer(players, true);
			}
		}
		playerList.clear();
		playerID.clear();
		matchCounter = 30;
		matchID = 0;
		inLobby = true;
		ending = false;
		
		refillChests();
	}

	@SuppressWarnings("deprecation")
	public void addPlayer(Player player, boolean sendMessage) {
		playerList.add(player);

		setPlayerID(player);

		player.setGameMode(GameMode.ADVENTURE);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setExp(0.0F);
		player.getInventory().clear();

		resetScoreboard(player);
		for (Player joinedPlayers : getPlayers()) {
			if (sendMessage) {
				joinedPlayers.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.AQUA + " has joined the game! " + ChatColor.DARK_AQUA + "(" + ChatColor.AQUA + getPlayers().size() + ChatColor.DARK_AQUA + "/" + ChatColor.AQUA + Bukkit.getServer().getMaxPlayers() + ChatColor.DARK_AQUA + ")");
			}
		}
		if (getPlayers().size() == 12) {
			StartCountdown(0);
		}
	}

	@SuppressWarnings("deprecation")
	public void removePlayer(Player player, boolean sendMessage) {
		player.setGameMode(GameMode.ADVENTURE);
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setLevel(0);
		player.setExp(0.0F);
		player.getInventory().clear();
		for (PotionEffect effects : player.getActivePotionEffects()) {
			player.removePotionEffect(effects.getType());
		}
		resetScoreboard(player);
		
		playerList.remove(player);
		if (getPlayers().size() > 0) {
			for (Player joinedPlayers : getPlayers()) {
				if (sendMessage) {
					joinedPlayers.sendMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.AQUA + " has left the game! " + ChatColor.DARK_AQUA + "(" + ChatColor.AQUA + getPlayers().size() + ChatColor.DARK_AQUA + "/" + ChatColor.AQUA + Bukkit.getServer().getMaxPlayers() + ChatColor.DARK_AQUA + ")");
				}
			}
		}
		if(inLobby && getPlayers().size() == 6) {
			for(Player joinedPlayers : getPlayers()) {
				matchCounter = 30;

				joinedPlayers.sendMessage(ChatColor.GRAY + "Timer stopped! Not enough players!");
				if (matchID != 0) {
					Bukkit.getServer().getScheduler().cancelTask(matchID);
				}
			}
		}
		
		if(isSpectating(player)) {
			spectators.remove(player);
		}
		
		if(getPlayers().size() == 0) {
			StopMatch();
		}
	}

	@SuppressWarnings("deprecation")
	public void StartCountdown(final int type) {
		if (type == 0) {
			matchCounter = 30;
			for (Player players : getPlayers()) {
				Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), "", ChatColor.AQUA + "The game will start in " + matchCounter + " seconds...");
			}
		} else if (type == 1) {
			matchCounter = 420;
			for (Player players : getPlayers()) {
				Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), "", ChatColor.AQUA + "The game will start in " + matchCounter / 60 + " minutes...");
			}
		} else if (type == 2) {
			matchCounter = 180;
			for (Player players : getPlayers()) {
				Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), "", ChatColor.AQUA + "The game will end in " + matchCounter / 60 + " minutes...");
			}
		}
		matchID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.getCore(), new Runnable() {
			public void run() {
				if(matchCounter > 1) {
					if(!ending) {
						matchCounter -= 1;
					}
					
					for(Player players : getPlayers()) {
						Map.getMapManager().resetScoreboard(players);
					}
					if(!ending) {
						if(matchCounter == 300 || matchCounter == 180 || matchCounter == 60 || matchCounter == 30 || matchCounter == 10 || matchCounter == 5 || matchCounter == 4 || matchCounter == 3 || matchCounter == 2 || matchCounter == 1) {
							for (Player players : getPlayers()) {
								String pref = "seconds";
								if(matchCounter == 1) {
									pref = "second";
								} else if(matchCounter == 300 || matchCounter == 180) {
									pref = "minutes";
								} else if(matchCounter == 60) {
									pref = "minute";
								}
								if(type == 0) {
									if(pref.equalsIgnoreCase("second") || pref.equalsIgnoreCase("seconds")) {
										Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), "", ChatColor.AQUA + "The game will start in " + matchCounter + " " + pref + "...");
									} else if(pref.equalsIgnoreCase("minute") || pref.equalsIgnoreCase("minutes")) {
										Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), "", ChatColor.AQUA + "The game will start in " + (matchCounter / 60) + " " + pref + "...");
									}
								} else if(type == 1) {
									if(pref.equalsIgnoreCase("second") || pref.equalsIgnoreCase("seconds")) {
										Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), "", ChatColor.AQUA + "The deathmatch will start in " + matchCounter + " " + pref + "...");
									} else if(pref.equalsIgnoreCase("minute") || pref.equalsIgnoreCase("minutes")) {
										Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), "", ChatColor.AQUA + "The deathmatch will start in " + (matchCounter / 60) + " " + pref + "...");
									}
								} else if(type == 2) {
									if(pref.equalsIgnoreCase("second") || pref.equalsIgnoreCase("seconds")) {
										Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), "", ChatColor.AQUA + "The game will end in " + matchCounter + " " + pref + "...");
									} else if(pref.equalsIgnoreCase("minute") || pref.equalsIgnoreCase("minutes")) {
										Title.sendFullTitle(players, Integer.valueOf(10), Integer.valueOf(40), Integer.valueOf(10), "", ChatColor.AQUA + "The game will end in " + (matchCounter / 60) + " " + pref + "...");
									}
								}
							}
						}
					}
				} else {
					Bukkit.getServer().getScheduler().cancelTask(Map.matchID);
					if(getPlayers().size() == 0) {
						StopMatch();
						return;
					}
					if (getPlayers().size() == 1 && type == 1) {
						End();
						return;
					}
					if(type == 0) {
						StartGame();
					} else if(type == 1) {
						StartDeathmatch();
					} else if(type == 2) {
						End();
					}
				}
			}
		}, 20L, 20L);
	}

	public void StartDeathmatch() {
		for (final Player players : getPlayers()) {
			TeleportToSpawn(players);
			
			if (getPlayers().size() > 1) {
				if (players == getPlayers().get(0)) {
					StartCountdown(2);
				}
				if (!isSpectating(players)) {
					players.sendMessage(ChatColor.GOLD + "---------========   Deathmatch   ========---------");
					players.sendMessage(ChatColor.GOLD + "- The last minutes will choose life or death.");
					players.sendMessage(ChatColor.GOLD + "- Kill the remaining survivors and stay alive till you are the last player.");
					players.sendMessage(ChatColor.GOLD + "-----------========================-----------");
					players.playSound(players.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);

					players.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "* PVP will start in 10 seconds! Prepare yourself! *");
					EntityStats.antiDamage.add(players);
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Plugin.getCore(), new Runnable() {
						public void run() {
							EntityStats.antiDamage.remove(players);
							players.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "* PVP has started! *");
						}
					}, 200);
				}
			} else {
				End();
			}
		}
	}

	public void setSpectator(Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		player.setFoodLevel(20);
		player.setExp(0.0F);
		player.setLevel(0);

		spectators.add(player);
	}

	@SuppressWarnings("deprecation")
	public void End() {
		ending = true;
		for (final Player players : getPlayers()) {
			resetScoreboard(players);

			players.setHealth(players.getMaxHealth());
			players.setFoodLevel(20);
			players.setExp(0.0F);
			players.setLevel(0);
			if (!isSpectating(players)) {
				players.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "---------===========---------");
				players.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "You survived the Survival Games and won!");
				players.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "---------===========---------");

				Firework fw = (Firework) players.getWorld().spawn(players.getLocation(), Firework.class);
				FireworkMeta fwMeta = fw.getFireworkMeta();
				fwMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withFlicker().withTrail().withColor(Color.ORANGE).build());
				fw.setFireworkMeta(fwMeta);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getCore(), new Runnable() {
					public void run() {
						Firework fw = (Firework) players.getWorld().spawn(players.getLocation(), Firework.class);
						FireworkMeta fwMeta = fw.getFireworkMeta();
						fwMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withFlicker().withTrail().withColor(Color.ORANGE).build());
						fw.setFireworkMeta(fwMeta);
						Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getCore(), new Runnable() {
							public void run() {
								Firework fw = (Firework) players.getWorld().spawn(players.getLocation(), Firework.class);
								FireworkMeta fwMeta = fw.getFireworkMeta();
								fwMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BURST).withFlicker().withTrail().withColor(Color.ORANGE).build());
								fw.setFireworkMeta(fwMeta);
							}
						}, 40);
					}
				}, 40);
			} else {
				players.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "---------=================---------");
				players.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You died in Survival Games and lost!");
				players.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "---------=================---------");
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getCore(), new Runnable() {
			public void run() {
				StopMatch();
			}
		}, 140);
	}
	
	public void refillChests() {
		File file = new File(Plugin.getCore().getDataFolder(), "/dat0/chests.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
		for(String matObj : config.getStringList("content")) {
			try {
				Material material = Material.getMaterial(matObj);
				ItemStack item = new ItemStack(material, 1);
				if(item.getMaxStackSize() > 1 && material != Material.DIAMOND && material != Material.IRON_INGOT && material != Material.GOLD_INGOT) {
					item.setAmount(2);
				}
				itemList.add(item);
			} catch(NullPointerException e) {
				//Do nothing
			}
		}
		
		if(config.isList("content")) {
			for(World worlds : Bukkit.getServer().getWorlds()) {
				for(Chunk chunk : worlds.getLoadedChunks()) {
					for(BlockState state : chunk.getTileEntities()) {
						if(state instanceof Chest) {
							Chest chest = (Chest) state;
							chest.getBlockInventory().clear();
							chest.update();
							
							Random random = new Random();
							
							for(int i=0;i<chest.getBlockInventory().getSize();i++) {
								if(random.nextInt(13) == 0) {
									chest.getBlockInventory().setItem(i, itemList.get(random.nextInt(itemList.size() - 1)));
								}
							}
							chest.update();
							
							int filledSpots = 0;
							for(int i=0;i<chest.getBlockInventory().getSize();i++) {
								if(chest.getBlockInventory().getItem(i) != null && chest.getBlockInventory().getItem(i).getType() != Material.AIR) {
									filledSpots++;
								}
							}
							if(filledSpots < 2) {
								chest.getBlockInventory().setItem(random.nextInt(chest.getBlockInventory().getSize()), itemList.get(random.nextInt(itemList.size() - 1)));
								chest.getBlockInventory().setItem(random.nextInt(chest.getBlockInventory().getSize()), itemList.get(random.nextInt(itemList.size() - 1)));
							}
							
							chest.update();
						}
					}
				}
			}
		}
	}
	
	public ArrayList<Player> getPlayers() {
		getMapManager();
		return playerList;
	}

	public boolean DoesExist() {
		return this.file.exists();
	}

	public boolean isInGame(Player player) {
		return playerList.contains(player);
	}

	public boolean isSpectating(Player player) {
		return spectators.contains(player);
	}

	public void saveFile() {
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public YamlConfiguration getConfig() {
		return this.config;
	}

	public void setPlayerID(Player player) {
		if (!playerID.containsKey(player)) {
			Random random = new Random();
			int rInt = random.nextInt(Bukkit.getServer().getMaxPlayers()) + 1;
			if (playerID.values().contains(Integer.valueOf(rInt))) {
				setPlayerID(player);
			} else {
				playerID.put(player, Integer.valueOf(rInt));
			}
		}
	}
}
