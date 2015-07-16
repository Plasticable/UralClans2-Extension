package ru.jampire.bukkit.uralclans2;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.jampire.bukkit.uralclans2.Clan;
import ru.jampire.bukkit.uralclans2.ConfigHandler;
import ru.jampire.bukkit.uralclans2.Lang;
import ru.jampire.bukkit.uralclans2.Main;
import ru.jampire.bukkit.uralclans2.Member;
import ru.jampire.bukkit.uralclans2.Request;
import ru.jampire.bukkit.uralclans2.Warm;

public class ClanCommand implements CommandExecutor {

   @SuppressWarnings({ "rawtypes", "unused", "deprecation", "unchecked" })
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if(!(sender instanceof Player)) {
         sender.sendMessage(Lang.getMessage("command_error0"));
         return true;
      } else if(!sender.hasPermission("UralClans2.use")) {
         sender.sendMessage(Lang.getMessage("command_error38"));
         return true;
      } else {
         Clan userClan = Clan.getClanByName(sender.getName());
         Player user = (Player)sender;
         if(args.length == 0) {
	        if(!sender.hasPermission("UralClans2.leader")) {
	            if(userClan == null) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_create"));
	            }

	            if(userClan != null && userClan.hasLeader(user.getName())) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_leader"));
	            }
	            
	            if(userClan != null && userClan.hasLeader(user.getName())) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_addmoder"));
	            }

	            if(userClan != null && userClan.hasLeader(user.getName())) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_delmoder"));
	            }

	            if(userClan != null && userClan.hasLeader(user.getName())) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_take"));
	            }

	            if(userClan != null && userClan.hasLeader(user.getName())) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_sethome"));
	            }

	            if(userClan != null && userClan.hasLeader(user.getName())) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_removehome"));
	            }

        	} else {
                sender.sendMessage(Lang.getMessage("command_error43"));
                return true;
        	}
	        	
        	if(!sender.hasPermission("UralClans2.moder")) {
	            if(userClan != null && (userClan.hasLeader(user.getName()) || userClan.isModer(user.getName()))) {
		        	sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_msg"));
		        }

	            if(userClan != null && (userClan.hasLeader(user.getName()) || userClan.isModer(user.getName()))) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_invite"));
	            }
	
	            if(userClan != null && (userClan.hasLeader(user.getName()) || userClan.isModer(user.getName()))) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_kick"));
	            }

	            if(userClan != null && (userClan.hasLeader(user.getName()) || userClan.isModer(user.getName()))) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_upgrade"));
	            }

	            if(userClan != null && (userClan.hasLeader(user.getName()) || userClan.isModer(user.getName()))) {
	               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_pvp"));
	            }
        	} else {
	            sender.sendMessage(Lang.getMessage("command_error43"));
	            return true;
        	}
            if(userClan != null) {
               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_info"));
            }
    		
            if(userClan != null && userClan.hasLeader(user.getName())) {
               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_disband"));
            }

            if(userClan != null) {
               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_online"));
            }

            sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_list"));
            sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_top"));
            if(userClan != null && !userClan.hasLeader(user.getName())) {
               if(userClan.isModer(user.getName())) {
                  sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_leave1"));
               } else {
                  sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_leave2"));
               }
            }

            if(userClan != null) {
               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_home"));
            }

            if(userClan != null) {
               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_balance"));
            }

            if(userClan != null) {
               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_deposit"));
            }
            
            if(sender.hasPermission("UralClans2.admin")) {
               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_admin"));
               sender.sendMessage(ChatColor.YELLOW + "/" + label + " " + Lang.getMessage("command_reload"));
            }

            return true;
         } else if(args.length <= 0) {
            return true;
         } else if(args[0].equalsIgnoreCase("create")) {
            if(!sender.hasPermission("UralClans2.leader")) {
               sender.sendMessage(Lang.getMessage("command_error43"));
               return true;
            } else if(args.length == 1) {
               sender.sendMessage(Lang.getMessage("command_error1"));
               return true;
            } else if(userClan != null) {
               sender.sendMessage(Lang.getMessage("command_error2"));
               return true;
            } else if(Clan.getClan(args[1]) != null) {
               sender.sendMessage(Lang.getMessage("command_error3"));
               return true;
            } else if(args[1].length() > Main.config.getInt("settings.max_symbols")) {
               sender.sendMessage(Lang.getMessage("command_error4", new Object[]{Integer.valueOf(Main.config.getInt("settings.max_symbols"))}));
               return true;
            } else if(args[1].length() < Main.config.getInt("settings.min_symbols")) {
               sender.sendMessage(Lang.getMessage("command_error5", new Object[]{Integer.valueOf(Main.config.getInt("settings.min_symbols"))}));
               return true;
            } else if(!ChatColor.stripColor(args[1].replaceAll("&", "§")).matches(Main.config.getString("settings.clan_regex"))) {
               sender.sendMessage(Lang.getMessage("command_error6"));
               return true;
            } else {
               userClan = Clan.create(args[1], sender.getName());
               userClan.broadcast(Lang.getMessage("clan_created", new Object[]{Clan.getClanByName(sender.getName()).getName()}));
               return true;
            }
         } else if(args[0].equalsIgnoreCase("disband")) {
            if(!sender.hasPermission("UralClans2.use")) {
               sender.sendMessage(Lang.getMessage("command_error38"));
               return true;
            } else if(userClan == null) {
               sender.sendMessage(Lang.getMessage("command_error7"));
               return true;
            } else if(!userClan.hasLeader(sender.getName())) {
               sender.sendMessage(Lang.getMessage("command_error8"));
               return true;
            } else {
               userClan.broadcast(Lang.getMessage("clan_disband", new Object[]{sender.getName()}));
               userClan.disband();
               return true;
            }
         } else {
            int var25;
            StringBuilder var30;
            if(args[0].equalsIgnoreCase("msg")) {
               if(!sender.hasPermission("UralClans2.moder")) {
                  sender.sendMessage(Lang.getMessage("command_error43"));
                  return true;
               } else if(userClan == null) {
                  sender.sendMessage(Lang.getMessage("command_error7"));
                  return true;
               } else if(!userClan.hasLeader(sender.getName()) && !userClan.isModer(sender.getName())) {
                  sender.sendMessage(Lang.getMessage("command_error9"));
                  return true;
               } else {
                  var30 = new StringBuilder();

                  for(var25 = 1; var25 < args.length; ++var25) {
                     var30.append(args[var25] + " ");
                  }

                  if(var30.toString().length() <= 3) {
                     sender.sendMessage(Lang.getMessage("command_error10"));
                     return true;
                  } else {
                     ChatColor var33 = ChatColor.AQUA;
                     if(userClan.isModer(sender.getName())) {
                        var33 = ChatColor.GREEN;
                     }

                     if(userClan.hasLeader(sender.getName())) {
                        var33 = ChatColor.GOLD;
                     }

                     Iterator var36 = userClan.getMembers().iterator();

                     while(var36.hasNext()) {
                        Member var35 = (Member)var36.next();
                        if(Bukkit.getOfflinePlayer(var35.getName()).isOnline()) {
                           Bukkit.getPlayer(var35.getName()).sendMessage(Lang.getMessage("command_msg_format", new Object[]{Lang.getMessage("clan"), Lang.getMessage("command_msg_1"), var33 + sender.getName(), var30.toString()}));
                        }
                     }

                     return true;
                  }
               }
            } else if(args[0].equalsIgnoreCase("online")) {
               if(!sender.hasPermission("UralClans2.use")) {
                  sender.sendMessage(Lang.getMessage("command_error38"));
                  return true;
               } else if(userClan == null) {
                  sender.sendMessage(Lang.getMessage("command_error7"));
                  return true;
               } else {
                  sender.sendMessage(Lang.getMessage("command_online_1"));
                  Iterator var29 = userClan.getMembers().iterator();

                  while(var29.hasNext()) {
                     Member var32 = (Member)var29.next();
                     if(Bukkit.getOfflinePlayer(var32.getName()).isOnline()) {
                        if(sender.getName().equalsIgnoreCase(var32.getName())) {
                           sender.sendMessage(ChatColor.YELLOW + " > " + ChatColor.GREEN + var32.getName());
                        } else {
                           sender.sendMessage(ChatColor.YELLOW + " - " + var32.getName());
                        }
                     }
                  }

                  return true;
               }
            } else {
               Iterator i;
               if(args[0].equalsIgnoreCase("info")) {
                  if(!sender.hasPermission("UralClans2.use")) {
                     sender.sendMessage(Lang.getMessage("command_error38"));
                     return true;
                  } else if(userClan == null) {
                     sender.sendMessage(Lang.getMessage("command_error7"));
                     return true;
                  } else {
                     sender.sendMessage(Lang.getMessage("command_info_1", new Object[]{userClan.getName(), Integer.valueOf(userClan.getMembers().size()), Integer.valueOf(userClan.getMaxPlayers())}));
                     sender.sendMessage(Lang.getMessage("command_info_2", new Object[]{Bukkit.getOfflinePlayer(userClan.getLeader()).getName()}));
                     var30 = new StringBuilder();

                     Member var27;
                     ChatColor var34;
                     for(i = userClan.getMembers().iterator(); i.hasNext(); var30.append(var34 + Bukkit.getOfflinePlayer(var27.getName()).getName() + ChatColor.GREEN + ", ")) {
                        var27 = (Member)i.next();
                        var34 = ChatColor.GREEN;
                        if(var27.isModer()) {
                           var34 = ChatColor.DARK_GREEN;
                        }
                     }

                     sender.sendMessage(Lang.getMessage("command_info_3", new Object[]{var30.toString().substring(0, var30.toString().length() - 2)}));
                     return true;
                  }
               } else if(args[0].equalsIgnoreCase("addmoder")) {
                  if(!sender.hasPermission("UralClans2.leader")) {
                     sender.sendMessage(Lang.getMessage("command_error43"));
                     return true;
                  } else if(userClan == null) {
                     sender.sendMessage(Lang.getMessage("command_error7"));
                     return true;
                  } else if(args.length == 1) {
                     sender.sendMessage(Lang.getMessage("command_error11"));
                     return true;
                  } else if(!userClan.hasLeader(sender.getName())) {
                     sender.sendMessage(Lang.getMessage("command_error12"));
                     return true;
                  } else if(!userClan.hasClanMember(args[1])) {
                     sender.sendMessage(Lang.getMessage("command_error13"));
                     return true;
                  } else if(userClan.hasLeader(args[1])) {
                     sender.sendMessage(Lang.getMessage("command_error14"));
                     return true;
                  } else if(userClan.isModer(args[1])) {
                     sender.sendMessage(Lang.getMessage("command_error15"));
                     return true;
                  } else {
                     userClan.setModer(args[1], true);
                     userClan.broadcast(Lang.getMessage("clan_addmoder", new Object[]{Bukkit.getOfflinePlayer(args[1]).getName()}));
                     return true;
                  }
               } else if(args[0].equalsIgnoreCase("delmoder")) {
                  if(!sender.hasPermission("UralClans2.leader")) {
                     sender.sendMessage(Lang.getMessage("command_error43"));
                     return true;
                  } else if(userClan == null) {
                     sender.sendMessage(Lang.getMessage("command_error7"));
                     return true;
                  } else if(args.length == 1) {
                     sender.sendMessage(Lang.getMessage("command_error11"));
                     return true;
                  } else if(!userClan.hasLeader(sender.getName())) {
                     sender.sendMessage(Lang.getMessage("command_error16"));
                     return true;
                  } else if(!userClan.hasClanMember(args[1])) {
                     sender.sendMessage(Lang.getMessage("command_error13"));
                     return true;
                  } else if(!userClan.isModer(args[1])) {
                     sender.sendMessage(Lang.getMessage("command_error17"));
                     return true;
                  } else {
                     userClan.setModer(args[1], false);
                     userClan.broadcast(Lang.getMessage("clan_delmoder", new Object[]{Bukkit.getOfflinePlayer(args[1]).getName()}));
                     return true;
                  }
               } else if(args[0].equalsIgnoreCase("invite")) {
                  if(!sender.hasPermission("UralClans2.moder")) {
                     sender.sendMessage(Lang.getMessage("command_error43"));
                     return true;
                  } else if(args.length == 1) {
                     sender.sendMessage(Lang.getMessage("command_error11"));
                     return true;
                  } else if(userClan == null) {
                     sender.sendMessage(Lang.getMessage("command_error7"));
                     return true;
                  } else if(!userClan.hasLeader(sender.getName()) && !userClan.isModer(sender.getName())) {
                     sender.sendMessage(Lang.getMessage("command_error18"));
                     return true;
                  } else if(!Bukkit.getOfflinePlayer(args[1]).isOnline()) {
                     sender.sendMessage(Lang.getMessage("command_error19"));
                     return true;
                  } else if(Clan.hasMember(args[1])) {
                     sender.sendMessage(Lang.getMessage("command_error2"));
                     return true;
                  } else if(userClan.getMaxPlayers() <= userClan.getMembers().size()) {
                     sender.sendMessage(Lang.getMessage("command_error20", new Object[]{Integer.valueOf(userClan.getMaxPlayers())}));
                     return true;
                  } else if(Request.get(Bukkit.getPlayer(args[1])) != null) {
                     sender.sendMessage(Lang.getMessage("command_error21"));
                     return true;
                  } else {
                     Request.send(userClan, Bukkit.getPlayer(args[1]), sender.getName());
                     sender.sendMessage(Lang.getMessage("clan_invite", new Object[]{sender.getName(), args[1]}));
                     Bukkit.getPlayer(args[1]).sendMessage(Lang.getMessage("clan_invite", new Object[]{userClan.getName(), sender.getName()}));
                     Bukkit.getPlayer(args[1]).sendMessage(Lang.getMessage("clan_invite_accept"));
                     Bukkit.getPlayer(args[1]).sendMessage(Lang.getMessage("clan_invite_deny"));
                     return true;
                  }
               } else if(args[0].equalsIgnoreCase("kick")) {
                  if(!sender.hasPermission("UralClans2.moder")) {
                     sender.sendMessage(Lang.getMessage("command_error43"));
                     return true;
                  } else if(args.length == 1) {
                     sender.sendMessage(Lang.getMessage("command_error11"));
                     return true;
                  } else if(userClan == null) {
                     sender.sendMessage(Lang.getMessage("command_error7"));
                     return true;
                  } else if(!userClan.hasLeader(sender.getName()) && !userClan.isModer(sender.getName())) {
                     sender.sendMessage(Lang.getMessage("command_error22"));
                     return true;
                  } else if(!userClan.hasClanMember(args[1])) {
                     sender.sendMessage(Lang.getMessage("command_error13"));
                     return true;
                  } else if(args[1].equalsIgnoreCase(userClan.getLeader())) {
                     sender.sendMessage(Lang.getMessage("command_error23"));
                     return true;
                  } else {
                     userClan.kick(args[1]);
                     userClan.broadcast(Lang.getMessage("clan_kick_1", new Object[]{Bukkit.getPlayer(args[1]).getName()}));
                     Bukkit.getPlayer(args[1]).sendMessage(Lang.getMessage("clan_kick_2"));
                     return true;
                  }
               } else {
                  int var23;
                  int var28;
                  if(args[0].equalsIgnoreCase("list")) {
                     if(!sender.hasPermission("UralClans2.use")) {
                        sender.sendMessage(Lang.getMessage("command_error38"));
                        return true;
                     } else if(Clan.clans.size() == 0) {
                        sender.sendMessage(Lang.getMessage("command_error42"));
                        return true;
                     } else {
                        var23 = 0;
                        if(args.length > 1) {
                           try {
                              if(Integer.parseInt(args[1]) < 1) {
                                 throw new Exception();
                              }

                              var23 = Integer.parseInt(args[1]);
                           } catch (Exception var19) {
                              sender.sendMessage(Lang.getMessage("command_error24"));
                              return true;
                           }

                           var23 = (var23 - 1) * 10;
                           if(Clan.clans.size() - var23 < 0) {
                              sender.sendMessage(Lang.getMessage("command_error25"));
                              return true;
                           }
                        }

                        var25 = 0;
                        sender.sendMessage(Lang.getMessage("clan_list", new Object[]{Integer.valueOf((int)Math.ceil((double)var23 / 10.0D) + 1), Integer.valueOf((int)Math.ceil((double)Clan.clans.size() / 10.0D))}));

                        for(var28 = var23; var28 < Clan.clans.size() && var25 != 10; ++var28) {
                           ++var25;
                           Clan var31 = (Clan)Clan.clans.values().toArray()[var28];
                           sender.sendMessage(ChatColor.YELLOW + " - " + var31.getName() + ChatColor.YELLOW + " [" + var31.getMembers().size() + "] (" + Bukkit.getOfflinePlayer(var31.getLeader()).getName() + ")");
                        }

                        return true;
                     }
                  } else if(args[0].equalsIgnoreCase("leave")) {
                     if(!sender.hasPermission("UralClans2.use")) {
                        sender.sendMessage(Lang.getMessage("command_error38"));
                        return true;
                     } else if(userClan == null) {
                        sender.sendMessage(Lang.getMessage("command_error7"));
                        return true;
                     } else if(userClan.hasLeader(sender.getName())) {
                        sender.sendMessage(Lang.getMessage("command_error26"));
                        return true;
                     } else {
                        if(userClan.isModer(sender.getName())) {
                           userClan.setModer(sender.getName(), false);
                           userClan.broadcast(Lang.getMessage("clan_leave_1", new Object[]{sender.getName()}));
                        } else {
                           userClan.broadcast(Lang.getMessage("clan_leave_2", new Object[]{sender.getName()}));
                           userClan.kick(sender.getName());
                        }

                        return true;
                     }
                  } else if(args[0].equalsIgnoreCase("home")) {
                     if(!sender.hasPermission("UralClans2.use")) {
                        sender.sendMessage(Lang.getMessage("command_error38"));
                        return true;
                     } else if(userClan == null) {
                        sender.sendMessage(Lang.getMessage("command_error7"));
                        return true;
                     } else if(!userClan.hasHome()) {
                        sender.sendMessage(Lang.getMessage("command_error27"));
                        return true;
                     } else {
                        Warm.addPlayer(user, userClan);
                        return true;
                     }
                  } else if(args[0].equalsIgnoreCase("removehome")) {
                     if(!sender.hasPermission("UralClans2.leader")) {
                        sender.sendMessage(Lang.getMessage("command_error43"));
                        return true;
                     } else if(userClan == null) {
                        sender.sendMessage(Lang.getMessage("command_error7"));
                        return true;
                     } else if(!userClan.hasLeader(user.getName())) {
                        sender.sendMessage(Lang.getMessage("command_error28"));
                        return true;
                     } else if(!userClan.hasHome()) {
                        sender.sendMessage(Lang.getMessage("command_error27"));
                        return true;
                     } else {
                        userClan.setHome((String)null, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                        userClan.broadcast(Lang.getMessage("clan_removehome", new Object[]{sender.getName()}));
                        return true;
                     }
                  } else if(args[0].equalsIgnoreCase("sethome")) {
                     if(!sender.hasPermission("UralClans2.control")) {
                        sender.sendMessage(Lang.getMessage("command_error43"));
                        return true;
                     } else {
                        Player var26 = (Player)sender;
                        if(userClan == null) {
                           sender.sendMessage(Lang.getMessage("command_error7"));
                           return true;
                        } else if(!userClan.hasLeader(user.getName())) {
                           sender.sendMessage(Lang.getMessage("command_error29"));
                           return true;
                        } else if(!Main.getWG().canBuild(var26, var26.getLocation())) {
                           sender.sendMessage(Lang.getMessage("command_error30"));
                           return true;
                        } else {
                           userClan.setHome(var26.getWorld().getName(), var26.getLocation().getX(), var26.getLocation().getY(), var26.getLocation().getZ(), var26.getLocation().getYaw(), var26.getLocation().getPitch());
                           userClan.broadcast(Lang.getMessage("clan_sethome", new Object[]{sender.getName()}));
                           return true;
                        }
                     }
                  } else if(args[0].equalsIgnoreCase("leader")) {
                     if(!sender.hasPermission("UralClans2.leader")) {
                        sender.sendMessage(Lang.getMessage("command_error43"));
                        return true;
                     } else if(userClan == null) {
                        sender.sendMessage(Lang.getMessage("command_error7"));
                        return true;
                     } else if(!userClan.hasLeader(user.getName())) {
                        sender.sendMessage(Lang.getMessage("command_error31"));
                        return true;
                     } else if(args.length == 1) {
                        sender.sendMessage(Lang.getMessage("command_error11"));
                        return true;
                     } else if(!Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore()) {
                        sender.sendMessage(Lang.getMessage("command_error32"));
                        return true;
                     } else if(Clan.getClanByName(args[1]) == null) {
                        sender.sendMessage(Lang.getMessage("command_error13"));
                        return true;
                     } else if(!Clan.getClanByName(args[1]).getName().equalsIgnoreCase(userClan.getName())) {
                        sender.sendMessage(Lang.getMessage("command_error33"));
                        return true;
                     } else if(args[1].equalsIgnoreCase(sender.getName())) {
                        sender.sendMessage(Lang.getMessage("command_error34"));
                        return true;
                     } else {
                        if(userClan.isModer(args[1])) {
                           userClan.setModer(args[1], false);
                        }

                        userClan.setLeader(args[1]);
                        userClan.broadcast(Lang.getMessage("clan_leader", new Object[]{sender.getName(), args[1]}));
                        return true;
                     }
                  } else {
                     boolean var22;
                     if(args[0].equalsIgnoreCase("take")) {
                        try {
                           Main.getEconomy();
                        } catch (Exception var14) {
                           return true;
                        }

                        if(!sender.hasPermission("UralClans2.leader")) {
                           sender.sendMessage(Lang.getMessage("command_error43"));
                           return true;
                        } else if(userClan == null) {
                           sender.sendMessage(Lang.getMessage("command_error7"));
                           return true;
                        } else if(!userClan.hasLeader(user.getName())) {
                           sender.sendMessage(Lang.getMessage("command_error31"));
                           return true;
                        } else if(args.length == 1) {
                           sender.sendMessage(Lang.getMessage("clan_take_1"));
                           return true;
                        } else {
                           var22 = false;

                           try {
                              var23 = Integer.parseInt(args[1]);
                              if(var23 < 0) {
                                 throw new Exception();
                              }
                           } catch (Exception var20) {
                              sender.sendMessage(Lang.getMessage("clan_take_2"));
                              return true;
                           }

                           if(userClan.getBalance() < var23) {
                              sender.sendMessage(Lang.getMessage("clan_take_3"));
                              return true;
                           } else {
                              try {
                                 Main.getEconomy().depositPlayer(sender.getName(), (double)var23);
                              } catch (Exception var13) {
                                 ;
                              }

                              userClan.setBalance(userClan.getBalance() - var23);
                              sender.sendMessage(Lang.getMessage("clan_take_4", new Object[]{Integer.valueOf(var23)}));
                              return true;
                           }
                        }
                     } else if(args[0].equalsIgnoreCase("balance")) {
                        try {
                           Main.getEconomy();
                        } catch (Exception var15) {
                           return true;
                        }

                        if(!sender.hasPermission("UralClans2.use")) {
                           sender.sendMessage(Lang.getMessage("command_error38"));
                           return true;
                        } else if(userClan == null) {
                           sender.sendMessage(Lang.getMessage("command_error7"));
                           return true;
                        } else {
                           sender.sendMessage(Lang.getMessage("clan_balance", new Object[]{Integer.valueOf(userClan.getBalance())}));
                           return true;
                        }
                     } else if(args[0].equalsIgnoreCase("deposit")) {
                        try {
                           Main.getEconomy();
                        } catch (Exception var18) {
                           return true;
                        }

                        if(!sender.hasPermission("UralClans2.use")) {
                           sender.sendMessage(Lang.getMessage("command_error38"));
                           return true;
                        } else if(userClan == null) {
                           sender.sendMessage(Lang.getMessage("command_error7"));
                           return true;
                        } else if(args.length == 1) {
                           sender.sendMessage(Lang.getMessage("clan_deposit_1"));
                           return true;
                        } else {
                           var22 = false;

                           try {
                              var23 = Integer.parseInt(args[1]);
                              if(var23 < 0) {
                                 throw new Exception();
                              }
                           } catch (Exception var21) {
                              sender.sendMessage(Lang.getMessage("clan_deposit_2"));
                              return true;
                           }

                           try {
                              if(!Main.getEconomy().has(sender.getName(), (double)var23)) {
                                 sender.sendMessage(Lang.getMessage("clan_deposit_3"));
                                 return true;
                              }
                           } catch (Exception var17) {
                              ;
                           }

                           try {
                              Main.getEconomy().withdrawPlayer(sender.getName(), (double)var23);
                              userClan.setBalance(userClan.getBalance() + var23);
                           } catch (Exception var16) {
                              ;
                           }

                           sender.sendMessage(Lang.getMessage("clan_deposit_4", new Object[]{Integer.valueOf(var23)}));
                           return true;
                        }
                     } else if(!args[0].equalsIgnoreCase("top")) {
                        if(args[0].equalsIgnoreCase("upgrade")) {
                           if(!sender.hasPermission("UralClans2.moder")) {
                              sender.sendMessage(Lang.getMessage("command_error38"));
                              return true;
                           } else if(userClan == null) {
                              sender.sendMessage(Lang.getMessage("command_error7"));
                              return true;
                           } else if(!userClan.hasLeader(sender.getName()) && !userClan.isModer(sender.getName())) {
                              sender.sendMessage(Lang.getMessage("command_error35"));
                              return true;
                           } else if(userClan.getMaxPlayers() >= Main.config.getInt("settings.max_upgrade")) {
                              sender.sendMessage(Lang.getMessage("command_error36"));
                              return true;
                           } else {
                              userClan.upgrade(1);
                              userClan.broadcast(Lang.getMessage("clan_upgrade", new Object[]{sender.getName()}));
                              return true;
                           }
                        } else if(args[0].equalsIgnoreCase("reload")) {
                           if(!sender.hasPermission("SpeedCraftClans.admin")) {
                              sender.sendMessage(Lang.getMessage("command_error38"));
                              return true;
                           } else {
                              ConfigHandler.configInit();
                              sender.sendMessage(Lang.getMessage("command_reload_1"));
                              return true;
                           }
                        } else if(args[0].equalsIgnoreCase("pvp")) {
                           if(!sender.hasPermission("UralClans2.moder")) {
                              sender.sendMessage(Lang.getMessage("command_error43"));
                              return true;
                           } else if(userClan == null) {
                              sender.sendMessage(Lang.getMessage("command_error7"));
                              return true;
                           } else if(!userClan.hasLeader(sender.getName()) && !userClan.isModer(sender.getName())) {
                              sender.sendMessage(Lang.getMessage("command_error37"));
                              return true;
                           } else {
                              userClan.setPvP(!userClan.isPvP());
                              userClan.broadcast(userClan.isPvP()?Lang.getMessage("clan_pvp_1", new Object[]{sender.getName()}):Lang.getMessage("clan_pvp_2", new Object[]{sender.getName()}));
                              return true;
                           }
                        } else {
                           if(args[0].equalsIgnoreCase("admin")) {
                              if(!sender.hasPermission("UralClans2.admin")) {
                                 sender.sendMessage(Lang.getMessage("command_error38"));
                                 return true;
                              }

                              sender.sendMessage(Lang.getMessage("command_error39"));
                              if(args.length == 1) {
                                 return true;
                              }

                              if(args.length > 1) {
                                 return true;
                              }
                           } else {
                              if(args[0].equalsIgnoreCase("accept")) {
                                 if(Request.get((Player)sender) == null) {
                                    sender.sendMessage(Lang.getMessage("command_error40"));
                                    return true;
                                 }

                                 Request.accept(Request.get((Player)sender));
                                 sender.sendMessage(Lang.getMessage("clan_accept"));
                                 return true;
                              }

                              if(args[0].equalsIgnoreCase("deny")) {
                                 if(Request.get((Player)sender) == null) {
                                    sender.sendMessage(Lang.getMessage("command_error40"));
                                    return true;
                                 }

                                 Request.deny(Request.get((Player)sender));
                                 sender.sendMessage(Lang.getMessage("clan_deny"));
                                 return true;
                              }
                           }

                           sender.sendMessage(Lang.getMessage("command_error41"));
                           return true;
                        }
                     } else if(!sender.hasPermission("UralClans2.use")) {
                        sender.sendMessage(Lang.getMessage("command_error38"));
                        return true;
                     } else if(Clan.clans.size() == 0) {
                        sender.sendMessage(Lang.getMessage("command_error42"));
                        return true;
                     } else {
                        HashMap sorted = new HashMap();
                        i = Clan.clans.values().iterator();

                        while(i.hasNext()) {
                           Clan entries = (Clan)i.next();
                           sorted.put(entries, Integer.valueOf(entries.getMembers().size()));
                        }

                        LinkedList var24 = new LinkedList(sorted.entrySet());
                        Collections.sort(var24, new Comparator() {
                           public int compare(Entry o1, Entry o2) {
                              return ((Integer)o2.getValue()).compareTo((Integer)o1.getValue());
                           }

						@Override
							public int compare(Object arg0, Object arg1) {
								// TODO Автоматически созданная заглушка метода
								return 0;
							}
                        });
                        var28 = 1;

                        for(Iterator var11 = var24.iterator(); var11.hasNext(); ++var28) {
                           Entry entry = (Entry)var11.next();
                           Clan c = (Clan)entry.getKey();
                           sender.sendMessage(Lang.getMessage("command_top_1", new Object[]{Integer.valueOf(var28), c.getName(), c.getLeader(), entry.getValue()}));
                           if(var28 == 10) {
                              break;
                           }
                        }

                        return true;
                     }
                  }
               }
            }
         }
      }
   }
}
