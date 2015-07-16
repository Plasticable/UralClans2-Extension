package ru.jampire.bukkit.uralclans2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import ru.jampire.bukkit.uralclans2.Lang;
import ru.jampire.bukkit.uralclans2.Main;

public class ConfigHandler {

   public static void winConvert(File f) throws Exception {
      Path p = Paths.get(f.toURI());
      ByteBuffer bb = ByteBuffer.wrap(Files.readAllBytes(p));
      CharBuffer cb = Charset.forName("UTF-8").decode(bb);
      bb = Charset.forName("windows-1251").encode(cb);
      Files.write(p, bb.array(), new OpenOption[0]);
   }

   @SuppressWarnings("unused")
public static void configInit() {
      File fconfig = new File(Main.plugin.getDataFolder(), "config.yml");
      File flang = new File(Main.plugin.getDataFolder(), "lang.yml");
      if(!Main.plugin.getDataFolder().mkdirs()) {
         Main.plugin.getDataFolder().mkdirs();
      }

      InputStream lang;
      FileOutputStream e;
      byte[] buff;
      int n;
      Object buff1;
      if(!fconfig.exists()) {
         lang = Main.class.getResourceAsStream("/config.yml");

         try {
            e = new FileOutputStream(fconfig);
            buff = new byte[65536];

            while((n = lang.read(buff)) > 0) {
               e.write(buff, 0, n);
               e.flush();
            }

            e.close();
            buff1 = null;
            if(SystemUtils.IS_OS_WINDOWS) {
               winConvert(fconfig);
            }
         } catch (Exception var7) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + var7);
         }
      }

      if(!flang.exists()) {
         lang = Main.class.getResourceAsStream("/lang.yml");

         try {
            e = new FileOutputStream(flang);
            buff = new byte[65536];

            while((n = lang.read(buff)) > 0) {
               e.write(buff, 0, n);
               e.flush();
            }

            e.close();
            buff1 = null;
            if(SystemUtils.IS_OS_WINDOWS) {
               winConvert(flang);
            }
         } catch (Exception var6) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error: " + var6);
         }
      }

      Main.config = YamlConfiguration.loadConfiguration(fconfig);
      Lang.load(YamlConfiguration.loadConfiguration(flang));
   }
}
