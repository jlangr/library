package com.loc.material.api;

public enum MaterialType {
   Book(21), AudioCassette(14), VinylRecording(14), MicroFiche(7),
   AudioCD(7), SoftwareCD(7), DVD(3), BluRay(3), VideoCassette(7);

   private int checkoutPeriod;

   MaterialType(int checkoutPeriod) {
      this.checkoutPeriod = checkoutPeriod;
   }

   public int getCheckoutPeriod() {
      return checkoutPeriod;
   }
}
