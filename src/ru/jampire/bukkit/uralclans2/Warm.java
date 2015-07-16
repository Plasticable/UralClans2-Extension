package ru.jampire.bukkit.uralclans2;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ru.jampire.bukkit.uralclans2.Clan;
import ru.jampire.bukkit.uralclans2.Lang;
import ru.jampire.bukkit.uralclans2.Main;

public class Warm {

   @SuppressWarnings("rawtypes")
private static HashMap players = new HashMap();
   @SuppressWarnings("rawtypes")
private static HashMap playerloc = new HashMap();


   @SuppressWarnings("unchecked")
public static void addPlayer(Player player, Clan clan) {
      if(player.hasPermission("UralClans2.warm.ignore")) {
         clan(player, clan);
      } else if(isWarming(player)) {
         player.sendMessage(Lang.getMessage("warm_alredy"));
      } else {
         player.sendMessage(Lang.getMessage("warm_use", new Object[]{Integer.valueOf(Main.config.getInt("settings.warm"))}));
         int taskIndex = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Warm.WarmTask(player, clan), (long)(Main.config.getInt("settings.warm") * 20));
         players.put(player.getName(), Integer.valueOf(taskIndex));
         playerloc.put(player.getName(), player.getLocation());
      }
   }

   public static boolean hasMoved(Player player) {
      Location curloc = player.getLocation();
      Location cmdloc = (Location)playerloc.get(player.getName());
      return cmdloc.distanceSquared(curloc) > 0.0D;
   }

   public static boolean isWarming(Player player) {
      return players.containsKey(player.getName());
   }

   public static void cancelWarming(Player player) {
      if(isWarming(player)) {
         Bukkit.getScheduler().cancelTask(((Integer)players.get(player.getName())).intValue());
         players.remove(player.getName());
         playerloc.remove(player.getName());
         player.sendMessage(Lang.getMessage("warm_canceled"));
      }

   }

   public static void clan(Player pl, Clan clan) {
      pl.teleport(clan.getHome());
      pl.sendMessage(Lang.getMessage("clan_teleport"));
   }

   private static class WarmTask implements Runnable {

      private Player player;
      private Clan clan;


      public WarmTask(Player player, Clan clan) {
         this.player = player;
         this.clan = clan;
      }

      public void run() {
         Warm.players.remove(this.player.getName());
         Warm.playerloc.remove(this.player.getName());
         Warm.clan(this.player, this.clan);
      }
   }
}
