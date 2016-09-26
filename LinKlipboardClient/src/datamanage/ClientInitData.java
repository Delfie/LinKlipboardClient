package datamanage;

import java.io.Serializable;
import java.util.Vector;

import contents.Contents;

public class ClientInitData implements Serializable{
   private static final long serialVersionUID = -6494489325228283099L;
   
   Vector<Contents> history;
   Vector<String> clients;

   public Vector<Contents> getHistory() {
      return this.history;
   }

   public Vector<String> getClients() {
      return this.clients;
   }
}