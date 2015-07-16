package ru.jampire.bukkit.uralclans2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;

import ru.jampire.bukkit.uralclans2.Main;

public class Lang {

   @SuppressWarnings("rawtypes")
private static Map language = new HashMap();


   @SuppressWarnings({ "rawtypes", "unchecked" })
public static void load(YamlConfiguration langYml) {
      Iterator var2 = langYml.getConfigurationSection(Main.config.getString("language")).getValues(false).entrySet().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         language.put(Main.config.getString("language") + "." + (String)entry.getKey(), String.valueOf(entry.getValue()));
      }

   }

   public static String getMessage(String target) {
      return Main.config.getString("language") == null?"Language not found.":(Main.config.getString("language").equals("")?"String " + target + " is null. Delete lang.yml and turn /c reload.":(String)language.get(Main.config.getString("language") + "." + target));
   }

   public static String getMessage(String target, Object ... arg1) {
      return Main.config.getString("language") == null?"Language not found.":(Main.config.getString("language").equals("")?"String " + target + " is null. Delete lang.yml and turn /c reload.":String.format((String)language.get(Main.config.getString("language") + "." + target), arg1));
   }
}
