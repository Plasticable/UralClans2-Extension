package ru.jampire.bukkit.uralclans2;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Player;

import ru.jampire.bukkit.uralclans2.Clan;
import ru.jampire.bukkit.uralclans2.Lang;

public class Request {

   private Clan clan;
   private Player player;
   private String sender;
   private long time;
   @SuppressWarnings("rawtypes")
public static ArrayList requests = new ArrayList();


   public Request(Clan clan, Player player, String sender, long time) {
      this.clan = clan;
      this.player = player;
      this.sender = sender;
      this.time = time;
   }

   public Clan getClan() {
      return this.clan;
   }

   public Player getPlayer() {
      return this.player;
   }

   public String getSender() {
      return this.sender;
   }

   public long getTime() {
      return this.time;
   }

   @SuppressWarnings("rawtypes")
public static Request get(Player pl) {
      Iterator var2 = requests.iterator();

      while(var2.hasNext()) {
         Request req = (Request)var2.next();
         if(req.getPlayer().equals(pl)) {
            return req;
         }
      }

      return null;
   }

   @SuppressWarnings("unchecked")
public static void send(Clan clan, Player player, String sender) {
      requests.add(new Request(clan, player, sender, System.currentTimeMillis()));
   }

   public static void accept(Request req) {
      req.getClan().broadcast(Lang.getMessage("clan_join", new Object[]{req.getPlayer().getName()}));
      req.getClan().invite(req.getPlayer().getName());
      requests.remove(req);
   }

   public static void deny(Request req) {
      requests.remove(req);
   }
}
