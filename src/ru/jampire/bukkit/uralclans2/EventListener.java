package ru.jampire.bukkit.uralclans2;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import ru.jampire.bukkit.uralclans2.Clan;
import ru.jampire.bukkit.uralclans2.Lang;
import ru.jampire.bukkit.uralclans2.Main;
import ru.jampire.bukkit.uralclans2.Member;
import ru.jampire.bukkit.uralclans2.Request;
import ru.jampire.bukkit.uralclans2.Warm;

public class EventListener implements Listener {

   @EventHandler
   public void PlayerKickEvent(PlayerKickEvent event) {
      if(Request.get(event.getPlayer()) != null) {
         Request.deny(Request.get(event.getPlayer()));
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void PlayerMoveEvent(PlayerMoveEvent event) {
      if(event.getFrom().distance(event.getTo()) > 0.0D) {
         Warm.cancelWarming(event.getPlayer());
      }

   }

   @EventHandler
   public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
      if(event.getEntity() instanceof Player) {
         Object d = event.getDamager();
		 
         if(d instanceof Arrow) {
            d = ((Arrow)d).getShooter();
         } else if(d instanceof ThrownPotion) {
            d = ((ThrownPotion)d).getShooter();
         }

         if(d instanceof Player) {
            Player damager = (Player)d;
            Player attacker = (Player)event.getEntity();
            Clan userClan = Clan.getClanByName(damager.getName());

            ApplicableRegionSet set = Main.getWG().getRegionManager(attacker.getWorld()).getApplicableRegions(attacker.getLocation());
            if(set.getFlag(DefaultFlag.PVP) == State.ALLOW && Main.config.getInt("settings.pvp") == 1) {
               return;
            }

            if(Clan.hasMember(damager.getName()) && Clan.hasMember(attacker.getName()) && userClan.hasClanMember(attacker.getName()) && attacker.getName() != damager.getName()) {
               if(!userClan.isPvP()) {
                  return;
               }
               damager.sendMessage(Lang.getMessage("damage_in_clan"));
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   
   public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
	   
      if(Clan.hasMember(event.getPlayer().getName()) && event.getFormat().contains("!clantag!")) {
          event.setFormat(event.getFormat().replace("!clantag!", Lang.getMessage("clantag_format", new Object[]{Clan.getClanByName(event.getPlayer().getName()).getName()})));
      } else {
	      event.setFormat(event.getFormat().replace("!clantag!", ""));
	  }
      if (Main.config.getBoolean("settings.clan_chat")) {
	      if(event.getMessage().startsWith("%") && event.getMessage().length() > 1) {
	         Clan userClan = Clan.getClanByName(event.getPlayer().getName());
			 
	         if(userClan == null) {
	            event.getPlayer().sendMessage(Lang.getMessage("command_error7"));
	            event.setCancelled(true);
	            return;
	         }
	
	         event.getRecipients().clear();
	         Iterator var4 = userClan.getMembers().iterator();
	
	         while(var4.hasNext()) {
	            Member c = (Member)var4.next();
	            OfflinePlayer pl = Bukkit.getOfflinePlayer(c.getName());
				
	            if(pl.isOnline()) {
	               event.getRecipients().add(pl.getPlayer());
	            }
	         }
	
	         ChatColor c1 = ChatColor.YELLOW;
	   
	         if(userClan.isModer(event.getPlayer().getName())) {
	            c1 = ChatColor.GREEN;
	         }
	
	         if(userClan.hasLeader(event.getPlayer().getName())) {
	            c1 = ChatColor.DARK_RED;
	         }
	
	         event.setFormat(Lang.getMessage("clanchat_format", new Object[]{Lang.getMessage("clan"), c1 + event.getPlayer().getName(),"%2$s"}));
	         event.setMessage(event.getMessage().substring(1, event.getMessage().length()).replace("§", "&"));
	      }
      }
      if (Main.config.getBoolean("settings.leader_chat")) {
	      if(event.getMessage().startsWith("*") && event.getMessage().length() > 1) {
	         Clan userClan = Clan.getClanByName(event.getPlayer().getName());
			 
	         if(userClan == null) {
	            event.getPlayer().sendMessage(Lang.getMessage("command_error7"));
	            event.setCancelled(true);
	            return;
	         } else if(!userClan.isModer(event.getPlayer().getName()) && !userClan.hasLeader(event.getPlayer().getName())) {
	            event.getPlayer().sendMessage(Lang.getMessage("command_error44"));
	            event.setCancelled(true);
	            return;
	         }
	
	         event.getRecipients().clear();
	         Iterator var4 = userClan.getMembers().iterator();
	
	         while(var4.hasNext()) {
	            Member c = (Member)var4.next();
	            OfflinePlayer pl = Bukkit.getOfflinePlayer(c.getName());
				
	            if(pl.isOnline()) {
	               if (userClan.isModer(pl.getPlayer().getName()) || userClan.hasLeader(pl.getPlayer().getName())) {
	                  event.getRecipients().add(pl.getPlayer());
	               }
	            }
	         }
	
	         ChatColor c1 = ChatColor.GREEN;
	
	         if(userClan.hasLeader(event.getPlayer().getName())) {
	            c1 = ChatColor.DARK_RED;
	         }
	
	         event.setFormat(Lang.getMessage("clanchatleader_format", new Object[]{Lang.getMessage("clan"), c1 + event.getPlayer().getName(), "%2$s"}));
	         event.setMessage(event.getMessage().substring(1, event.getMessage().length()).replace("§", "&"));
	      }
      }
   }


   public void PlayerChatEvent(PlayerChatEvent event) {
	   
       if(Clan.hasMember(event.getPlayer().getName()) && event.getFormat().contains("!clantag!")) {
           event.setFormat(event.getFormat().replace("!clantag!", Lang.getMessage("clantag_format", new Object[]{Clan.getClanByName(event.getPlayer().getName()).getName()})));
       } else {
	       event.setFormat(event.getFormat().replace("!clantag!", ""));
	   }

       if (Main.config.getBoolean("settings.clan_chat")) {
 	      if(event.getMessage().startsWith("%") && event.getMessage().length() > 1) {
 	         Clan userClan = Clan.getClanByName(event.getPlayer().getName());
 			 
 	         if(userClan == null) {
 	            event.getPlayer().sendMessage(Lang.getMessage("command_error7"));
 	            event.setCancelled(true);
 	            return;
 	         }
 	
 	         event.getRecipients().clear();
 	         Iterator var4 = userClan.getMembers().iterator();
 	
 	         while(var4.hasNext()) {
 	            Member c = (Member)var4.next();
 	            OfflinePlayer pl = Bukkit.getOfflinePlayer(c.getName());
 				
 	            if(pl.isOnline()) {
 	               event.getRecipients().add(pl.getPlayer());
 	            }
 	         }
 	
 	         ChatColor c1 = ChatColor.YELLOW;
 	   
 	         if(userClan.isModer(event.getPlayer().getName())) {
 	            c1 = ChatColor.GREEN;
 	         }
 	
 	         if(userClan.hasLeader(event.getPlayer().getName())) {
 	            c1 = ChatColor.DARK_RED;
 	         }
 	
 	         event.setFormat(Lang.getMessage("clanchat_format", new Object[]{Lang.getMessage("clan"), c1 + event.getPlayer().getName(),"%2$s"}));
 	         event.setMessage(event.getMessage().substring(1, event.getMessage().length()).replace("§", "&"));
 	      }
       }
      if (Main.config.getBoolean("settings.leader_chat")) {
	      if(event.getMessage().startsWith("*") && event.getMessage().length() > 1) {
	         Clan userClan = Clan.getClanByName(event.getPlayer().getName());
			 
	         if(userClan == null) {
	            event.getPlayer().sendMessage(Lang.getMessage("command_error7"));
	            event.setCancelled(true);
	            return;
	         } else if(!userClan.isModer(event.getPlayer().getName()) && !userClan.hasLeader(event.getPlayer().getName())) {
	            event.getPlayer().sendMessage(Lang.getMessage("command_error44"));
	            event.setCancelled(true);
	            return;
	         }
	
	         event.getRecipients().clear();
	         Iterator var4 = userClan.getMembers().iterator();
	
	         while(var4.hasNext()) {
	            Member c = (Member)var4.next();
	            OfflinePlayer pl = Bukkit.getOfflinePlayer(c.getName());
				
	            if(pl.isOnline()) {
	               if (userClan.isModer(pl.getPlayer().getName()) || userClan.hasLeader(pl.getPlayer().getName())) {
	                  event.getRecipients().add(pl.getPlayer());
	               }
	            }
	         }
	
	         ChatColor c1 = ChatColor.GREEN;
	
	         if(userClan.hasLeader(event.getPlayer().getName())) {
	            c1 = ChatColor.DARK_RED;
	         }
	
	         event.setFormat(Lang.getMessage("clanchatleader_format", new Object[]{Lang.getMessage("clan"), c1 + event.getPlayer().getName(), "%2$s"}));
	         event.setMessage(event.getMessage().substring(1, event.getMessage().length()).replace("§", "&"));
	      }
   		}
   }
   
   @EventHandler(
      priority = EventPriority.LOW
   )
   
   public void AsyncPlayerChatTagEvent(AsyncPlayerChatEvent event) {
	   
      if(Clan.hasMember(event.getPlayer().getName()) && event.getFormat().contains("!clantag!")) {
         event.setFormat(event.getFormat().replace("!clantag!", Lang.getMessage("clantag_format", new Object[]{Clan.getClanByName(event.getPlayer().getName()).getName()})));
      } else {
          event.setFormat(event.getFormat().replace("!clantag!", ""));
      }
	   
   }

   public void PlayerChatTagEvent(PlayerChatEvent event) {
	   
      if(Clan.hasMember(event.getPlayer().getName()) && event.getFormat().contains("!clantag!")) {
         event.setFormat(event.getFormat().replace("!clantag!", Lang.getMessage("clantag_format", new Object[]{Clan.getClanByName(event.getPlayer().getName()).getName()})));
      } else {
         event.setFormat(event.getFormat().replace("!clantag!", ""));
      }
   	
   }
}
