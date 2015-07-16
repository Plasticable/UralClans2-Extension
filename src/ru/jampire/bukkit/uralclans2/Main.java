package ru.jampire.bukkit.uralclans2;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import java.util.ArrayList;
import java.util.Iterator;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import ru.jampire.bukkit.uralclans2.ClanCommand;
import ru.jampire.bukkit.uralclans2.ConfigHandler;
import ru.jampire.bukkit.uralclans2.EventListener;
import ru.jampire.bukkit.uralclans2.Lang;
import ru.jampire.bukkit.uralclans2.Logger;
import ru.jampire.bukkit.uralclans2.MySQL;
import ru.jampire.bukkit.uralclans2.Request;

public class Main extends JavaPlugin {

   public static FileConfiguration config;
   public static Plugin plugin;


   public static WorldGuardPlugin getWG() {
      Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
      return plugin != null && plugin instanceof WorldGuardPlugin?(WorldGuardPlugin)plugin:null;
   }

   @SuppressWarnings("rawtypes")
public static Economy getEconomy() throws Exception {
      RegisteredServiceProvider economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
      return (Economy)economyProvider.getProvider();
   }

   public void onEnable() {
      long time = System.currentTimeMillis();
      plugin = this;
      ConfigHandler.configInit();
      MySQL.connect();
      MySQL.getClans();
      MySQL.getClansAsync();
      this.getCommand("clan").setExecutor(new ClanCommand());
      Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
         @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
		public void run() {
            ArrayList toDelete = new ArrayList();
            Iterator var3 = Request.requests.iterator();

            Request r;
            while(var3.hasNext()) {
               r = (Request)var3.next();
               if(System.currentTimeMillis() - r.getTime() >= 15000L) {
                  toDelete.add(r);
               }
            }

            for(var3 = toDelete.iterator(); var3.hasNext(); Request.deny(r)) {
               r = (Request)var3.next();
               r.getPlayer().sendMessage(Lang.getMessage("invite_canceled"));
               OfflinePlayer pl = Bukkit.getOfflinePlayer(r.getSender());
               if(pl.isOnline()) {
                  pl.getPlayer().sendMessage(Lang.getMessage("invite_canceled2", new Object[]{r.getPlayer().getName()}));
               }
            }

         }
      }, 0L, 20L);
      Bukkit.getPluginManager().registerEvents(new EventListener(), this);
      Logger.info(Lang.getMessage("plugin_enabled", new Object[]{Long.valueOf(System.currentTimeMillis() - time)}));
   }

   public void onDisable() {
      MySQL.disconnect();
      Logger.info(Lang.getMessage("plugin_disabled"));
   }
}
