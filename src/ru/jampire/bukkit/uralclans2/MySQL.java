package ru.jampire.bukkit.uralclans2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import ru.jampire.bukkit.uralclans2.Clan;
import ru.jampire.bukkit.uralclans2.Lang;
import ru.jampire.bukkit.uralclans2.Logger;
import ru.jampire.bukkit.uralclans2.Main;
import ru.jampire.bukkit.uralclans2.Member;

public class MySQL {

   public static Connection connection = null;
   public static ResultSet resultSet = null;


   public static void connect() {
      try {
         if(!Main.plugin.getDataFolder().mkdirs()) {
            Main.plugin.getDataFolder().mkdirs();
         }

         if(Main.config.getString("database").equalsIgnoreCase("sqlite")) {
            Class.forName("org.sqlite.JDBC").newInstance();
            connection = DriverManager.getConnection("jdbc:sqlite://" + Main.plugin.getDataFolder().getAbsolutePath() + "/uralclans2.db");
            executeSync("CREATE TABLE IF NOT EXISTS `clan_list` (`id` INTEGER PRIMARY KEY,`name` varchar(255) NOT NULL UNIQUE,`icon` varchar(255),`leader` varchar(255) NOT NULL UNIQUE,`world` varchar(255) NOT NULL,`x` varchar(255) NOT NULL,`y` varchar(255) NOT NULL,`z` varchar(255) NOT NULL,`yaw` varchar(255) NOT NULL,`pitch` varchar(255) NOT NULL,`maxplayers` varchar(255) NOT NULL,`pvp` tinyint(1) NOT NULL)");
            executeSync("CREATE TABLE IF NOT EXISTS `clan_members` (`id` INTEGER PRIMARY KEY,`clan` varchar(255) NOT NULL,`name` varchar(255) NOT NULL,`isModer` tinyint(1) NOT NULL)");
         } else {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://" + Main.config.getString("mysql.host") + ":" + Main.config.getString("mysql.port") + "/" + Main.config.getString("mysql.database") + "?useUnicode=true&characterEncoding=UTF-8&" + "user=" + Main.config.getString("mysql.username") + "&password=" + Main.config.getString("mysql.password"));
            executeSync("CREATE TABLE IF NOT EXISTS `clan_list` (`id` int(11) NOT NULL AUTO_INCREMENT,`name` varchar(255) NOT NULL UNIQUE,`icon` varchar(255),`leader` varchar(255) NOT NULL UNIQUE,`world` varchar(255) NOT NULL,`x` varchar(255) NOT NULL,`y` varchar(255) NOT NULL,`z` varchar(255) NOT NULL,`yaw` varchar(255) NOT NULL,`pitch` varchar(255) NOT NULL,`maxplayers` varchar(255) NOT NULL,`pvp` tinyint(1) NOT NULL,PRIMARY KEY (`id`)) DEFAULT CHARSET=utf8 AUTO_INCREMENT=0");
            executeSync("CREATE TABLE IF NOT EXISTS `clan_members` (`id` int(11) NOT NULL AUTO_INCREMENT,`clan` varchar(255) NOT NULL,`name` varchar(255) NOT NULL,`isModer` tinyint(1) NOT NULL,PRIMARY KEY (`id`)) DEFAULT CHARSET=utf8 AUTO_INCREMENT=0");
         }

         try {
            connection.createStatement().execute("ALTER TABLE `clan_list` ADD `balance` varchar(255)");
         } catch (Exception var1) {
            ;
         }

         Logger.info(Lang.getMessage("mysql_connected"));
      } catch (Exception var2) {
         Logger.error(Lang.getMessage("mysql_error"));
         var2.printStackTrace();
      }

   }

   public static boolean hasConnected() {
      try {
         return !connection.isClosed();
      } catch (Exception var1) {
         return false;
      }
   }

   public static String strip(String str) {
      str = str.replaceAll("<[^>]*>", "");
      str = str.replace("\\", "\\\\");
      str = str.trim();
      return str;
   }

   public static void execute(final String query) {
      Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable() {
         public void run() {
            if(!MySQL.hasConnected()) {
               MySQL.connect();
            }

            try {
               Logger.debug(MySQL.strip(query));
               MySQL.connection.createStatement().execute(MySQL.strip(query));
            } catch (Exception var2) {
               Logger.error(Lang.getMessage("mysql_error2"));
               Logger.error(query);
               Logger.error(var2.getMessage());
            }

         }
      });
   }

   public static void executeSync(String query) {
      if(!hasConnected()) {
         connect();
      }

      try {
         Logger.debug(strip(query));
         connection.createStatement().execute(strip(query));
      } catch (Exception var2) {
         Logger.error(Lang.getMessage("mysql_error2"));
         Logger.error(query);
         Logger.error(var2.getMessage());
      }

   }

   public static ResultSet executeQuery(String query) {
      if(!hasConnected()) {
         connect();
      }

      Logger.debug(strip(query));
      ResultSet rs = null;

      try {
         rs = connection.createStatement().executeQuery(strip(query));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return rs;
   }

   public static void getClans() {
      try {
         resultSet = executeQuery("SELECT * FROM clan_list");

         Clan e;
         while(resultSet.next()) {
            e = new Clan(resultSet.getString("name"), resultSet.getString("leader"), new ArrayList(), resultSet.getString("world"), resultSet.getDouble("x"), resultSet.getDouble("y"), resultSet.getDouble("z"), resultSet.getFloat("yaw"), resultSet.getFloat("pitch"), resultSet.getInt("maxplayers"), resultSet.getString("pvp").equals("1"), resultSet.getInt("balance"));
            Clan.clans.put(resultSet.getString("name"), e);
         }

         resultSet = executeQuery("SELECT list.name AS clan_name, member.name AS member_name, member.isModer AS moder FROM clan_list AS list JOIN clan_members AS member ON member.clan=list.name");

         while(resultSet.next()) {
            e = Clan.getClan(resultSet.getString("clan_name"));
            if(e == null) {
               execute("DELETE FROM clan_members WHERE name=\'" + resultSet.getString("member_name") + "\' AND clan=\'" + resultSet.getString("clan_name") + "\'");
            } else {
               ArrayList members = e.getMembers();
               members.add(new Member(resultSet.getString("member_name"), resultSet.getString("moder").equals("1")));
               e.setMembers(members);
               Clan.clans.put(resultSet.getString("clan_name"), e);
            }
         }

         Logger.info(Lang.getMessage("clan_loaded"));
      } catch (Exception var2) {
         Logger.error(Lang.getMessage("clan_load_error"));
      }

   }

   public static void getClansAsync() {
      Bukkit.getScheduler().runTaskTimerAsynchronously(Main.plugin, new Runnable() {
         public void run() {
            Clan.clans.clear();

            try {
               MySQL.resultSet = MySQL.executeQuery("SELECT * FROM clan_list");

               Clan e;
               while(MySQL.resultSet.next()) {
                  e = new Clan(MySQL.resultSet.getString("name"), MySQL.resultSet.getString("leader"), new ArrayList(), MySQL.resultSet.getString("world"), MySQL.resultSet.getDouble("x"), MySQL.resultSet.getDouble("y"), MySQL.resultSet.getDouble("z"), MySQL.resultSet.getFloat("yaw"), MySQL.resultSet.getFloat("pitch"), MySQL.resultSet.getInt("maxplayers"), MySQL.resultSet.getString("pvp").equals("1"), MySQL.resultSet.getInt("balance"));
                  Clan.clans.put(MySQL.resultSet.getString("name"), e);
               }

               MySQL.resultSet = MySQL.executeQuery("SELECT list.name AS clan_name, member.name AS member_name, member.isModer AS moder FROM clan_list AS list JOIN clan_members AS member ON member.clan=list.name");

               while(MySQL.resultSet.next()) {
                  e = Clan.getClan(MySQL.resultSet.getString("clan_name"));
                  if(e == null) {
                     MySQL.execute("DELETE FROM clan_members WHERE name=\'" + MySQL.resultSet.getString("member_name") + "\' AND clan=\'" + MySQL.resultSet.getString("clan_name") + "\'");
                  } else {
                     ArrayList members = e.getMembers();
                     members.add(new Member(MySQL.resultSet.getString("member_name"), MySQL.resultSet.getString("moder").equals("1")));
                     e.setMembers(members);
                     Clan.clans.put(MySQL.resultSet.getString("clan_name"), e);
                  }
               }
            } catch (Exception var3) {
               Logger.error(Lang.getMessage("clan_load_error"));
            }

         }
      }, 1200L, 1200L);
   }

   public static void disconnect() {
      try {
         if(connection != null) {
            connection.close();
         }
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }
}
