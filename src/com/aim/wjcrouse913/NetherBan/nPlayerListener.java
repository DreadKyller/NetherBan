package com.aim.wjcrouse913.NetherBan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;



public class nPlayerListener implements Listener {
	public static NetherBan plugin;

	public nPlayerListener(NetherBan instance) {
		plugin = instance;
		
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		if(NetherBan.commands == true){
			Player player = event.getPlayer();
			if(plugin.playerBanish.containsKey(player)){
				player.sendMessage(NetherBan.prefix + ChatColor.GRAY + " You cannot use commands while banned!");
				event.setCancelled(true);
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event){
		try {
		    BufferedReader in = new BufferedReader(new FileReader("plugins/NetherBan/banished.txt"));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	final Player player = event.getPlayer();
		    	String name = player.getName();
		    	if(str.equals(name)){
		    		plugin.playerBanish.put(player, false);
		    		player.teleport(plugin.getServer().getWorld(NetherBan.nethername).getSpawnLocation());
			    	player.sendMessage(NetherBan.prefix + ChatColor.GRAY + " You are still banished to the Nether!");
		    	}
		    }
		    in.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		try {
		    BufferedReader in = new BufferedReader(new FileReader("plugins/NetherBan/whitelist.txt"));
		    String str;
		    while ((str = in.readLine()) != null) {
		    	final Player player = event.getPlayer();
		    	String name = player.getName();
		    	if(str.equals(name)){
		    		plugin.playerSafe.put(player, false);
		    	}
		    }
		    in.close();
		}catch (IOException e){
			e.printStackTrace();

		}
	}
	@EventHandler(priority = EventPriority.HIGH)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
    	if(NetherBan.emptybucket == true){
    		Player player = event.getPlayer();
    		if(plugin.playerBanish.containsKey(player)){
    			player.sendMessage(NetherBan.prefix + ChatColor.GRAY + " You cannot empty your bucket while banished!");
    			event.setCancelled(true);
    		}
    	}
    }
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		if(NetherBan.mute == true){
			Player player = event.getPlayer();
			if(plugin.playerBanish.containsKey(player)){
				player.sendMessage(NetherBan.prefix + ChatColor.GRAY + " Your cry falls on deaf ears.");
				event.setCancelled(true);

			}
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPortal(PlayerPortalEvent event){
		Player player = event.getPlayer();
		if(plugin.playerBanish.containsKey(player)){
			player.sendMessage(NetherBan.prefix + ChatColor.GRAY + " There is no escape from the Nether! Mwahahahaha!");
			event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		if(plugin.playerBanish.containsKey(player)){
			Location loc = plugin.getServer().getWorld(NetherBan.nethername).getSpawnLocation();
			event.setRespawnLocation(loc);
		}
		if(NetherBan.death == true){
			plugin.playerBanish.put(player, false);
			Location loc = plugin.getServer().getWorld(NetherBan.nethername).getSpawnLocation();
			event.setRespawnLocation(loc);
			player.sendMessage(NetherBan.prefix + ChatColor.GRAY + " You have been banished to the Nether for dying!");
		}
		if(NetherBan.kickDeath == true){
			Location loc = plugin.getServer().getWorld(NetherBan.nethername).getSpawnLocation();
			event.setRespawnLocation(loc);
			player.sendMessage(NetherBan.prefix + ChatColor.GRAY + " You have been kicked to the Nether for dying!");
		
		}
	}
}