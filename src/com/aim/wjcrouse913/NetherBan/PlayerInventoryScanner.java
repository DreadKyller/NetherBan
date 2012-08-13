package com.aim.wjcrouse913.NetherBan;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryScanner{
	private String invdir;
	private String dir;
	
	private Player player;
	private PlayerInventory inv;
	
	private NetherBan plu;
	
	public PlayerInventoryScanner(NetherBan plug){
		plu=plug;
	}
	
	public void use(Player p){
		this.inv=p.getInventory();
		this.player=p;
		this.invdir = NetherBan.mainDirectory+"/PlayerINV/"+NetherBan.normalname+"/"+p.getName()+".yml";
		this.dir=NetherBan.mainDirectory+"/PlayerINV/"+NetherBan.normalname;
	}
	
	public void save(){
		ItemStack[] i = this.inv.getContents();
		
		File main = new File(invdir);
		main.mkdirs();
		
		if(!main.exists()){
			try{
				main.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		FileConfiguration conf = YamlConfiguration.loadConfiguration(main);
		
		for(int a=0;a<i.length;a++){
			
			if(i[a]!=null){
				conf.set(a+".material", i[a].getType().name());
				conf.set(a+".amount", String.valueOf(i[a].getAmount()));
			}else{
				conf.set(a+".material", "null");
				conf.set(a+".amount", "0");
			}
		}
		try {
			conf.save(main);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ItemStack[] load(){
		ItemStack[] ret = new ItemStack[36];
		
		File main = new File(invdir);
		
		if(!main.exists()){
			return player.getInventory().getContents();
		}
		
		FileConfiguration conf = YamlConfiguration.loadConfiguration(main);
		
		int i=0;
		
		for(String s : conf.getKeys(false)){
			
			if(i<36){
				if(!conf.getString(s+".material").equalsIgnoreCase("null")){
					Material m = Material.valueOf(conf.getString(s+".material"));
					int am = Integer.parseInt(conf.getString(s+".amount"));
					
					ItemStack item = new ItemStack(m, am);
					
					ret[i] = item;
					
					i++;
				}else{
					ret[i]=null;
				}
			}else{
				break;
			}
		}
		return ret;
	}
}
