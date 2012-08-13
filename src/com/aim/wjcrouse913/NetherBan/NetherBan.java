package com.aim.wjcrouse913.NetherBan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.plugin.Plugin;

/* This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details. */

public class NetherBan extends JavaPlugin {
	public static String prefix = "[" + ChatColor.DARK_RED + "NetherBan"
			+ ChatColor.WHITE + "]";
	public static String version = "v0.5";
	public static String whitelist;
	public static String nethername;
	public static String normalname;
	public static boolean kickDeath;
	public static boolean death;
	public static boolean target;
	public static boolean commands;
	public static boolean pvp;
	public static boolean emptybucket;
	public static boolean build;
	public static boolean destroy;
	public static boolean mute;
	public static boolean bedrock;
	public static boolean lava;
	public static boolean tnt;
	public static boolean fire;
	public static boolean lightning;
	public static boolean eraseInventory;
	public static boolean saveInventory;

	public PlayerInventoryScanner invs;

	public static String mainDirectory;;
	// public static String mainPath;
	static File ConfigCreate;
	static File Whitelist;
	static File BanList;
	static Properties prop = new Properties();

	public Map<Player, Boolean> playerSafe = new HashMap<Player, Boolean>();
	public Map<Player, Boolean> playerBanish = new HashMap<Player, Boolean>();
	public Map<Player, PlayerInventory> inventories = new HashMap<Player, PlayerInventory>();

	public static final Logger log = Logger.getLogger("Minecraft");

	public String getMainDir() {
		String mainPath = "";
		if (mainDirectory == null) {

			if (!this.getDataFolder().exists()) {
				this.getDataFolder().mkdirs();
			}

			mainPath = this.getDataFolder().getAbsolutePath();

			if (!(mainPath.endsWith("/") || mainPath.endsWith("\\") || mainPath
					.endsWith(File.separator))) {
				mainPath += File.separator;
			}
		}
		return mainPath;
	}

	public void loadProcedure() {
		FileInputStream InFile = null;
		try {
			InFile = new FileInputStream(ConfigCreate);
			prop.load(InFile);
			InFile.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (prop.getProperty("Save-Inventory") == null
				|| prop.getProperty("Banished-Have-no-Inventory") == null) {
			FileOutputStream out;
			try {
				out = new FileOutputStream(ConfigCreate);
				prop.put("Banished-Have-no-Inventory", "false");
				prop.put("Save-Inventory", "true");
				prop.store(out, "NetherBan Config");
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		kickDeath = Boolean.parseBoolean(prop.getProperty("Kick-on-Death"));
		death = Boolean.parseBoolean(prop.getProperty("Banish-on-Death"));
		target = Boolean.parseBoolean(prop
				.getProperty("Entities-Target-Banished"));
		lightning = Boolean.parseBoolean(prop
				.getProperty("Display-Lightning-On-Banish"));
		commands = Boolean.parseBoolean(prop
				.getProperty("Banished-Cant-Use-Commands"));
		nethername = prop.getProperty("Nether-World-Name");
		normalname = prop.getProperty("Normal-World-Name");
		BanList = new File(mainDirectory + nethername + "/banished.yml");
		pvp = Boolean.parseBoolean(prop
				.getProperty("PvP-Disabled-For-Banished"));
		emptybucket = Boolean.parseBoolean(prop
				.getProperty("Banished-Cant-Empty-Bucket"));
		build = Boolean.parseBoolean(prop.getProperty("Banished-Cant-Build"));
		destroy = Boolean.parseBoolean(prop
				.getProperty("Banished-Cant-Destroy"));
		mute = Boolean.parseBoolean(prop.getProperty("Mute-on-Ban"));
		bedrock = Boolean.parseBoolean(prop.getProperty("Blacklist-Bedrock"));
		lava = Boolean.parseBoolean(prop.getProperty("Blacklist-Lava"));
		tnt = Boolean.parseBoolean(prop.getProperty("Blacklist-TnT"));
		fire = Boolean.parseBoolean(prop.getProperty("Blacklist-Fire"));
		eraseInventory = Boolean.parseBoolean(prop
				.getProperty("Banished-Have-no-Inventory"));
		saveInventory = Boolean
				.parseBoolean(prop.getProperty("Save-Inventory"));
	}

	public void onEnable() {
		invs = new PlayerInventoryScanner(this);

		mainDirectory = getMainDir();

		ConfigCreate = new File(mainDirectory + "NetherBan.prop");
		Whitelist = new File(mainDirectory + "whitelist.yml");
		BanList = new File(mainDirectory + "banished.yml");

		new File(mainDirectory).mkdir();
		if (!Whitelist.exists()) {
			try {
				Whitelist.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		new File(mainDirectory).mkdir();
		if (!BanList.exists()) {
			try {
				BanList.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();

			}
		}
		if (!ConfigCreate.exists()) {
			try {
				ConfigCreate.createNewFile();
				FileOutputStream out = new FileOutputStream(ConfigCreate);
				prop.put("Kick-on-Death", "false");
				prop.put("Banish-on-Death", "false");
				prop.put("Entities-Target-Banished", "false");
				prop.put("Display-Lightning-On-Banish", "true");
				prop.put("Banished-Cant-Use-Commands", "true");
				prop.put("Nether-World-Name", "world_nether");
				prop.put("Normal-World-Name", "world");
				prop.put("PvP-Disabled-For-Banished", "true");
				prop.put("Banished-Cant-Empty-Bucket", "true");
				prop.put("Banished-Cant-Build", "true");
				prop.put("Banished-Cant-Destroy", "true");
				prop.put("Mute-on-Ban", "false");
				prop.put("Blacklist-Lava", "false");
				prop.put("Blacklist-TnT", "false");
				prop.put("Blacklist-Fire", "false");
				prop.put("Banished-Have-no-Inventory", "false");
				prop.put("Save-Inventory", "true");
				prop.store(out, "NetherBan Config");
				out.flush();
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			loadProcedure();
		} else {
			loadProcedure();
		}
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new nEntityListener(this), this);
		pm.registerEvents(new nPlayerListener(this), this);
		pm.registerEvents(new nBlockListener(this), this);
		System.out.println("[NetherBan] NetherBan Enabled. Torturing souls...");
	}

	public void onDisable() {
		System.out.println("[NetherBan] NetherBan Disabled.");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (this.getServer().getWorld(nethername) != null
				&& this.getServer().getWorld(normalname) != null) {
			if (label.equalsIgnoreCase("nbhelp")) {
				if (sender instanceof Player) {
					sender.sendMessage(ChatColor.GRAY + "---------------"
							+ ChatColor.WHITE + "[ " + ChatColor.DARK_RED
							+ "NetherBan " + ChatColor.GREEN + "Help"
							+ ChatColor.WHITE + " ]" + ChatColor.GRAY
							+ "---------------");
					sender.sendMessage(ChatColor.GRAY + "-" + ChatColor.RED
							+ "/nbban <player>" + ChatColor.GRAY + " -- "
							+ ChatColor.GREEN + "Ban a player to the Nether"
							+ ChatColor.GRAY + "------");
					sender.sendMessage(ChatColor.GRAY + "-" + ChatColor.RED
							+ "/nbkick <player>" + ChatColor.GRAY + " - "
							+ ChatColor.GREEN + "Kick a player to the Nether"
							+ ChatColor.GRAY + "-------");
					sender.sendMessage(ChatColor.GRAY + "-" + ChatColor.RED
							+ "/nbunban <player>" + ChatColor.GRAY + "-"
							+ ChatColor.GREEN + "Unban player from the Nether"
							+ ChatColor.GRAY + "----");
					sender.sendMessage(ChatColor.GRAY + "-" + ChatColor.RED
							+ "/nbwl <player>" + ChatColor.GRAY + " -- "
							+ ChatColor.GREEN + "Makes a player unbanishable"
							+ ChatColor.GRAY + "------");
					sender.sendMessage(ChatColor.GRAY + "-" + ChatColor.RED
							+ "/nbversion" + ChatColor.GRAY + " -- "
							+ ChatColor.GREEN + "Shows the NetherBan Version!"
							+ ChatColor.GRAY + "---------");
					sender.sendMessage(ChatColor.GRAY + "-" + ChatColor.RED
							+ "/nbhelp" + ChatColor.GRAY + " -- "
							+ ChatColor.GREEN + "Displays this message!"
							+ ChatColor.GRAY + "------------------");
					sender.sendMessage(ChatColor.GRAY
							+ "------------------------------------------------");
					return true;
				} else {
					sender.sendMessage("---------------[ NetherBan Help ]---------------");
					sender.sendMessage("-/nbban <player> -- Ban a Player to the Nether--");
					sender.sendMessage("-/nbkick <player> - Kick a Player to the Nether-");
					sender.sendMessage("-/nbunban <player>-Unban Player from ze Nether--");
					sender.sendMessage("-/nbwl <player> -- Make a player unbanishable---");
					sender.sendMessage("-/nbversion -- Shows NetherBan Version!---------");
					sender.sendMessage("-/nbhelp -- Displays this message!--------------");
					sender.sendMessage("------------------------------------------------");
					return true;
				}
			}
			if (label.equalsIgnoreCase("nbversion")) {
				if (sender instanceof Player) {
					sender.sendMessage(prefix + ChatColor.GRAY
							+ " This server is running NetherBan "
							+ ChatColor.GREEN + version);
					return true;
				} else {
					sender.sendMessage("[NetherBan] "
							+ this.getServer().getIp() + ChatColor.DARK_RED
							+ "NetherBan" + ChatColor.GRAY
							+ " is running NetherBan " + version);
					return true;
				}
			}
			Plugin permissions = this.getServer().getPluginManager()
					.getPlugin("Permissions");
			if (label.equalsIgnoreCase("nbban")
					|| label.equalsIgnoreCase("netherban")) {
				if (permissions != null) {
					if (sender instanceof Player) {
						if (!(sender.hasPermission("netherban.nbban"))) {
							sender.sendMessage(ChatColor.RED
									+ "You don't have permission to do that!");
							return true;
						} else {
							return onPlayerBan(sender, args);
						}
					} else {
						return onPlayerBan(sender, args);
					}
				} else {
					if (!sender.isOp()) {
						sender.sendMessage(ChatColor.RED
								+ "You don't have permission to do that!");
						return true;
					} else {
						return onPlayerBan(sender, args);
					}
				}
			}
			if (label.equalsIgnoreCase("nbunban")) {
				if (permissions != null) {
					if (sender instanceof Player) {
						if (!(sender.hasPermission("netherban.nbunban"))) {
							sender.sendMessage(ChatColor.RED
									+ "You don't have permission to do that!");
							return true;
						} else {
							return onPlayerUnbanned(sender, args);
						}
					} else {
						return onPlayerUnbanned(sender, args);
					}
				} else {
					if (!sender.isOp()) {
						sender.sendMessage(ChatColor.RED
								+ "You don't have permission to do that!");
						return true;
					} else {
						return onPlayerUnbanned(sender, args);
					}
				}
			}
			if (label.equalsIgnoreCase("nbkick")) {
				if (permissions != null) {
					if (sender instanceof Player) {
						if (!(sender.hasPermission("netherban.nbkick"))) {
							sender.sendMessage(ChatColor.RED
									+ "You don't have permission to do that!");
							return true;
						} else {
							return onPlayerKick(sender, args);
						}
					} else {
						return onPlayerKick(sender, args);
					}
				} else {
					if (!sender.isOp()) {
						sender.sendMessage(ChatColor.RED
								+ "You don't have permission to do that!");
						return true;
					} else {
						return onPlayerKick(sender, args);
					}
				}
			}
			if (label.equalsIgnoreCase("nbwl")
					|| label.equalsIgnoreCase("nbwhitelist")) {
				if (permissions != null) {
					if (sender instanceof Player) {
						if (!(sender.hasPermission("netherban.whitelist"))) {
							sender.sendMessage(ChatColor.RED
									+ "You don't have permission to do that!");
							return true;
						} else {
							return onWhitelist(sender, args);
						}
					} else {
						return onWhitelist(sender, args);
					}
				} else {
					if (!sender.isOp()) {
						sender.sendMessage(ChatColor.RED
								+ "You don't have permission to do that!");
						return true;
					} else {
						return onWhitelist(sender, args);
					}
				}
			}
		} else {
			sender.sendMessage(prefix + ChatColor.RED
					+ " Error: Your NetherBan.prop is not configured properly!");
			return true;
		}
		return false;
	}

	public boolean onWhitelist(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(prefix + ChatColor.RED
					+ " Not enough arguments!");
			return false;
		}
		if (args.length > 1) {
			sender.sendMessage(prefix + ChatColor.WHITE + "] " + ChatColor.RED
					+ " Too many arguments!");
			return false;
		}
		if (args.length == 1) {
			String p = args[0];
			Player safe = this.getServer().getPlayer(p);
			if (safe instanceof Player) {
				try {
					String text = safe.getName();
					File file = new File(mainDirectory + "whitelist.yml");
					file.getParentFile().mkdirs();
					if (!file.exists()) {
						file.createNewFile();
					}
					FileConfiguration banwl = YamlConfiguration
							.loadConfiguration(file);
					banwl.set(text, safe.getUniqueId());
					banwl.save(file);
				} catch (IOException e) {
					e.printStackTrace();
					return true;
				}
				playerSafe.put(safe, false);
				safe.sendMessage(prefix + ChatColor.GRAY
						+ " You have been added to the Whitelist!");
				sender.sendMessage(prefix + " " + ChatColor.GREEN
						+ safe.getDisplayName() + ChatColor.GRAY
						+ " added to the Whitelist!");
				return true;
			}
		}
		return false;
	}

	public boolean onPlayerBan(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(prefix + ChatColor.RED
					+ " Not enough arguments!");
			return false;
		}
		if (args.length > 1) {
			sender.sendMessage(prefix + ChatColor.RED + " Too many arguments!");
			return false;

		}
		if (args.length == 1) {
			String p = args[0];
			Player banned = this.getServer().getPlayer(p);
			if (!playerSafe.containsKey(banned)) {
				if (banned instanceof Player) {
					try {
						String text = banned.getName();
						File file = BanList;
						file.getParentFile().mkdirs();
						if (!file.exists()) {
							file.createNewFile();
						}
						FileConfiguration ban = YamlConfiguration
								.loadConfiguration(file);
						ban.set(text, banned.getUniqueId());
						ban.save(file);
						System.out.println("[NetherBan] "
								+ banned.getDisplayName()
								+ " was banished to the Nether!");
						if (lightning == true) {
							banned.getWorld().strikeLightning(
									banned.getLocation());
						}
						if (saveInventory) {
							invs.use(banned);
							invs.save();
						}
						if (eraseInventory) {
							banned.getInventory().clear();
						}
						banned.teleport(this.getServer().getWorld(nethername)
								.getSpawnLocation());
						banned.sendMessage(prefix + ChatColor.GRAY
								+ " You have been banished to the Nether!");
						sender.sendMessage(prefix + " " + ChatColor.GREEN
								+ banned.getDisplayName() + ChatColor.GRAY
								+ " has been banished to the Nether!");
						playerBanish.put(banned, false);
						return true;
					} catch (IOException e) {
						e.printStackTrace();
						return true;
					}
				} else {
					try {
						String text = p;
						File file = BanList;
						file.getParentFile().mkdirs();
						if (!file.exists()) {
							file.createNewFile();
						}
						FileConfiguration ban = YamlConfiguration
								.loadConfiguration(file); // new
															// Configuration(file);
						ban.set(text, "-0.0");
						ban.save(file);
						System.out.println("[NetherBan] " + p
								+ " was banished to the Nether!");
						sender.sendMessage(prefix + " " + ChatColor.GREEN + p
								+ ChatColor.GRAY
								+ " has been banished to the Nether!");
						return true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				sender.sendMessage(prefix + " " + ChatColor.GREEN
						+ banned.getDisplayName() + ChatColor.GRAY
						+ " cannot be banished to the Nether!");
				return true;
			}
		}
		return false;
	}

	public boolean onPlayerUnbanned(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(prefix + ChatColor.RED + "Not enough arguments!");
			return true;
		}
		if (args.length > 1) {
			sender.sendMessage(prefix + ChatColor.RED + "Too many arguments!");
			return true;
		}
		if (args.length == 1) {
			String p = args[0];
			Player unbanned = this.getServer().getPlayer(p);
			if (unbanned instanceof Player) {
				try {
					File filei = BanList;
					filei.getParentFile().mkdirs();
					if (!filei.exists()) {
						filei.createNewFile();
					}
					FileConfiguration bans = YamlConfiguration
							.loadConfiguration(filei);
					String name = unbanned.getName();
					if (bans.get(name) != null) {
						String text = " ";
						bans.set(text, null);
						invs.use(unbanned);
						unbanned.getInventory().setContents(invs.load());
						playerBanish.remove(unbanned);
						unbanned.teleport(this.getServer().getWorld(normalname)
								.getSpawnLocation());
						sender.sendMessage(prefix + ChatColor.GREEN
								+ unbanned.getDisplayName() + ChatColor.GRAY
								+ " unbanished from the Nether!");
						unbanned.sendMessage(prefix + ChatColor.GRAY
								+ " You have been freed from the Nether!");
						return true;
					}
					bans.save(filei);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					File filei = BanList;
					filei.getParentFile().mkdirs();
					if (!filei.exists()) {
						filei.createNewFile();
					}
					FileConfiguration bans = YamlConfiguration
							.loadConfiguration(filei);
					if (bans.get(p) != null) {
						String text = " ";
						bans.set(text, null);
						bans.save(filei);
						sender.sendMessage(prefix
								+ ChatColor.GREEN
								+ p
								+ ChatColor.GRAY
								+ " is offline but was unbanished from the Nether! Reload server for it to take effect!");
						return true;
					} else {
						sender.sendMessage(prefix + ChatColor.GREEN + p
								+ ChatColor.GRAY + " is not banished!");
						return true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public boolean onPlayerKick(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(prefix + ChatColor.RED
					+ " Not enough arguments!");
			return false;
		}
		if (args.length > 1) {
			sender.sendMessage(prefix + ChatColor.RED + " Too many arguments!");
			return false;
		}
		if (args.length == 1) {
			String p = args[0];
			Player kicked = this.getServer().getPlayer(p);
			if (kicked instanceof Player) {
				sender.sendMessage(prefix + " " + ChatColor.GREEN
						+ kicked.getDisplayName() + ChatColor.GRAY
						+ " kicked to the Nether!");
				kicked.sendMessage(prefix + ChatColor.GRAY
						+ " You have been kicked to the Nether!");

				kicked.teleport(this.getServer().getWorld(nethername)
						.getSpawnLocation());
				return true;
			}

		}
		return false;
	}
}