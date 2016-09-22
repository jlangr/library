package controller;

import domain.core.Branch;

public class BranchRequest {
   private String name;
   private String id;

   public BranchRequest() {
   }

   public BranchRequest(Branch branch) {
      this.name = branch.getName();
      this.id = branch.getScanCode();
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}