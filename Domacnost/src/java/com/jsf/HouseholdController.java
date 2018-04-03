package com.jsf;

import com.dataquery.DataQuery;
import com.entity.Household;
import com.entity.User;
import com.entity.Userinhousehold;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "Household")
@SessionScoped
public class HouseholdController implements Serializable {
  //
  @ManagedProperty(value = "#{applicationManagers}")
  ApplicationManagers manager;
  private List<Household> householders;
  private int userId;
  private String name = "";
  private Household selectedHousehold;

  public HouseholdController() {
//    System.err.println("uz sme dalej");
    userId = SessionUtils.getUserId();
    householders = DataQuery.getInstance().getHousholdersByUser(userId);
    System.err.println(householders.size());

  }

  public List<Household> getHouseholders() {
    System.out.println("geter");
    return householders;
  }

  public void setHouseholders(List<Household> householders) {
    this.householders = householders;
  }

  public ApplicationManagers getManager() {
    return manager;
  }

  public void setManager(ApplicationManagers manager) {
    this.manager = manager;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Household getSelectedHousehold() {
    selectedHousehold = manager.getSelectHousehold(userId);
    return selectedHousehold;
  }

  public void setSelectedHousehold(Household selectedHousehold) {
    this.selectedHousehold = selectedHousehold;
  }

  public void addHousehold() {
    System.out.println("com.jsf.HouseholdController.addHousehold()");
    User user = DataQuery.getInstance().getUserById(userId);
    Household newHousehold = new Household(name, user);
    newHousehold = DataQuery.getInstance().addHousehold(newHousehold);
    System.err.println("je to tu ");
    setName("");
    householders.add(newHousehold);
    setHouseholders(householders);
  }

  public void deleteHousehold(Household household) {
    System.out.println("com.jsf.HouseholdController.deleteHousehold()");
    if (DataQuery.getInstance().deleteHousehold(household)) {
      householders.remove(household);
    }
  }

  public String selectHousehold(Household h) {
    if (manager.setHousehold(userId, h)) {
      selectedHousehold = h;
      return "";
//      return "household";
    }
    return "";
  }

  public void deleteUserFromHousehold(Userinhousehold householdUser) {
    DataQuery.getInstance().deleteUserFromHousehold(householdUser);
  }
}
