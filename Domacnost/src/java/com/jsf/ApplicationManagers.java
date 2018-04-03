package com.jsf;

import com.entity.Household;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Livia
 */
@ManagedBean
@ApplicationScoped
public class ApplicationManagers implements Serializable {
  //

  String version = "1.0";

  private Map<Integer, Household> usersHousholder;

  public ApplicationManagers() {
    usersHousholder = new HashMap<>();
  }

  public void newUser(int id) {
    usersHousholder.put(id, null);
//    System.err.println("nastavene");
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public boolean setHousehold(int userId, Household h) {
    if (usersHousholder.containsKey(userId)) {
      usersHousholder.put(userId, h);
      return true;
    }
    return false;
  }

  public Household getSelectHousehold(int userId) {
    if (usersHousholder.containsKey(userId)) {
      return usersHousholder.get(userId);
    }
    return null;
  }
}
