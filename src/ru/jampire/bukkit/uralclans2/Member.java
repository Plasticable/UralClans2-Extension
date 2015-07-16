package ru.jampire.bukkit.uralclans2;


public class Member {

   private String name;
   private boolean isModer;


   public Member(String name, boolean isModer) {
      this.name = name;
      this.isModer = isModer;
   }

   public String getName() {
      return this.name;
   }

   public boolean isModer() {
      return this.isModer;
   }

   public void setModer(boolean isModer) {
      this.isModer = isModer;
   }
}
