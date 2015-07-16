package ru.jampire.bukkit.uralclans2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import ru.jampire.bukkit.uralclans2.Lang;
import ru.jampire.bukkit.uralclans2.Main;
import ru.jampire.bukkit.uralclans2.Member;
import ru.jampire.bukkit.uralclans2.MySQL;

public class Clan {

   private String name;
   private String leader;
   @SuppressWarnings("rawtypes")
private ArrayList members;
   private String world;
   private double x;
   private double y;
   private double z;
   private float yaw;
   private float pitch;
   private int maxplayers;
   private boolean pvp;
   private int balance;
   @SuppressWarnings("rawtypes")
public static HashMap clans = new HashMap();


   @SuppressWarnings("rawtypes")
public Clan(String name, String leader, ArrayList members, String world, double x, double y, double z, float yaw, float pitch, int maxplayers, boolean pvp, int balance) {
      this.name = name;
      this.leader = leader;
      this.members = members;
      this.world = world;
      this.x = x;
      this.y = y;
      this.z = z;
      this.yaw = yaw;
      this.pitch = pitch;
      this.maxplayers = maxplayers;
      this.pvp = pvp;
      this.balance = balance;
   }

   public String getRealName() {
      return this.name;
   }

   public String getName() {
      return this.name.replace("&", "§") + ChatColor.RESET;
   }

   public String getLeader() {
      return this.leader;
   }

   @SuppressWarnings("rawtypes")
public ArrayList getMembers() {
      return this.members;
   }

   public Location getHome() {
      return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
   }

   public int getMaxPlayers() {
      return this.maxplayers;
   }

   public int getBalance() {
      return this.balance;
   }

   public boolean isPvP() {
      return this.pvp;
   }

   public void setPvP(boolean pvp) {
      this.pvp = pvp;
      MySQL.execute("UPDATE clan_list SET pvp=\'" + (pvp?"1":"0") + "\' WHERE name=\'" + this.name + "\'");
   }

   public void setBalance(int balance) {
      this.balance = balance;
      MySQL.execute("UPDATE clan_list SET balance=\'" + balance + "\' WHERE name=\'" + this.name + "\'");
   }

   @SuppressWarnings("rawtypes")
public void setMembers(ArrayList members) {
      this.members = members;
   }

   public static Clan getClan(String clan) {
      return (Clan)clans.get(clan);
   }

   @SuppressWarnings("rawtypes")
public static Clan getClanByName(String player) {
      Iterator var2 = clans.values().iterator();

      while(var2.hasNext()) {
         Clan c = (Clan)var2.next();
         Iterator var4 = c.getMembers().iterator();

         while(var4.hasNext()) {
            Member mem = (Member)var4.next();
            if(mem.getName().equalsIgnoreCase(player)) {
               return c;
            }
         }
      }

      return null;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
public void invite(String member) {
      ArrayList members = this.members;
      members.add(new Member(member.toLowerCase(), false));
      this.members = members;
      MySQL.execute("INSERT INTO clan_members (clan, name, isModer) VALUES (\'" + this.name + "\', \'" + member.toLowerCase() + "\', \'0\')");
   }

   @SuppressWarnings("rawtypes")
public void kick(String member) {
      if(this.isModer(member)) {
         this.setModer(member, false);
      }

      ArrayList members = this.members;
      Member my = null;
      Iterator var5 = members.iterator();

      while(var5.hasNext()) {
         Member memb = (Member)var5.next();
         if(memb.getName().equalsIgnoreCase(member)) {
            my = memb;
         }
      }

      members.remove(my);
      this.members = members;
      MySQL.execute("DELETE FROM clan_members WHERE clan=\'" + this.name + "\' AND name=\'" + member.toLowerCase() + "\'");
   }

   @SuppressWarnings("rawtypes")
public void setModer(String name, boolean isModer) {
      Iterator var4 = this.members.iterator();

      while(var4.hasNext()) {
         Member m = (Member)var4.next();
         if(m.getName().equalsIgnoreCase(name)) {
            m.setModer(isModer);
         }
      }

      MySQL.execute("UPDATE clan_members SET isModer=\'" + (isModer?"1":"0") + "\' WHERE clan=\'" + this.name + "\' AND name=\'" + name.toLowerCase() + "\'");
   }

   @SuppressWarnings("rawtypes")
public boolean isModer(String name) {
      Iterator var3 = this.members.iterator();

      Member m;
      do {
         if(!var3.hasNext()) {
            return false;
         }

         m = (Member)var3.next();
      } while(!m.getName().equalsIgnoreCase(name) || !m.isModer());

      return true;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
public static Clan create(String clan, String leader) {
      ArrayList members = new ArrayList();
      members.add(new Member(leader.toLowerCase(), false));
      Clan c = new Clan(clan, leader.toLowerCase(), members, (String)null, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, Main.config.getInt("settings.default_max"), true, 0);
      clans.put(clan, c);
      MySQL.execute("INSERT INTO clan_list (name, leader, world, x, y, z, yaw, pitch, maxplayers, pvp, balance) VALUES (\'" + clan + "\', \'" + leader.toLowerCase() + "\', \'null\', \'0\', \'0\', \'0\', \'0\', \'0\', \'" + Main.config.getInt("settings.default_max") + "\', \'1\', \'0\')");
      MySQL.execute("INSERT INTO clan_members (clan, name, isModer) VALUES (\'" + clan + "\', \'" + leader.toLowerCase() + "\', \'0\')");
      return c;
   }

   public void disband() {
      clans.remove(this.name);
      MySQL.execute("DELETE FROM clan_list WHERE name=\'" + this.name + "\'");
      MySQL.execute("DELETE FROM clan_members WHERE clan=\'" + this.name + "\'");
   }

   public void setLeader(String leader) {
      this.leader = leader.toLowerCase();
      MySQL.execute("UPDATE clan_list SET leader=\'" + leader.toLowerCase() + "\' WHERE name=\'" + this.name + "\'");
   }

   public void setHome(String world, double x, double y, double z, float yaw, float pitch) {
      this.world = world;
      this.x = x;
      this.y = y;
      this.z = z;
      this.yaw = yaw;
      this.pitch = pitch;
      MySQL.execute("UPDATE clan_list SET world=\'" + world + "\', x=\'" + x + "\', y=\'" + y + "\', z=\'" + z + "\', yaw=\'" + yaw + "\', pitch=\'" + pitch + "\' WHERE name=\'" + this.name + "\'");
   }

   public boolean hasLeader(String player) {
      return this.getLeader().equalsIgnoreCase(player);
   }

   public boolean hasHome() {
      return this.x == 0.0D && this.y == 0.0D && this.z == 0.0D && this.yaw == 0.0F && this.pitch == 0.0F?false:Bukkit.getWorld(this.world) != null;
   }

   @SuppressWarnings("rawtypes")
public boolean hasClanMember(String player) {
      Iterator var3 = this.members.iterator();

      while(var3.hasNext()) {
         Member mem = (Member)var3.next();
         if(mem.getName().equalsIgnoreCase(player)) {
            return true;
         }
      }

      return false;
   }

   @SuppressWarnings("rawtypes")
public static boolean hasMember(String player) {
      Iterator var2 = clans.values().iterator();

      while(var2.hasNext()) {
         Clan c = (Clan)var2.next();
         Iterator var4 = c.getMembers().iterator();

         while(var4.hasNext()) {
            Member mem = (Member)var4.next();
            if(mem.getName().equalsIgnoreCase(player)) {
               return true;
            }
         }
      }

      return false;
   }

   public void upgrade(int i) {
      this.maxplayers += i;
      MySQL.execute("UPDATE clan_list SET maxplayers=\'" + this.maxplayers + "\' WHERE name=\'" + this.name + "\'");
   }

   @SuppressWarnings("rawtypes")
public void broadcast(String message) {
      Iterator var3 = this.members.iterator();

      while(var3.hasNext()) {
         Member m = (Member)var3.next();
         if(Bukkit.getOfflinePlayer(m.getName()).isOnline()) {
            Bukkit.getPlayer(m.getName()).sendMessage(Lang.getMessage("command_broadcast_format", new Object[]{Lang.getMessage("clan"), message}));
         }
      }

   }
}
