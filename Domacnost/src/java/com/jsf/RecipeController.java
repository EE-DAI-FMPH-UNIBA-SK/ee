package com.jsf;

import com.entity.Household;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "Recipe")
@SessionScoped
public class RecipeController {
  //
  @ManagedProperty(value = "#{applicationManagers}")
  ApplicationManagers manager;
  private Household selectedHousehold;
  private int userId;

  public RecipeController() {
    userId = SessionUtils.getUserId();
  }

  public ApplicationManagers getManager() {
    return manager;
  }

  public void setManager(ApplicationManagers manager) {
    this.manager = manager;
  }

  public Household getSelectedHousehold() {
    selectedHousehold = manager.getSelectHousehold(userId);
    return selectedHousehold;
  }

  public void setSelectedHousehold(Household selectedHousehold) {
    this.selectedHousehold = selectedHousehold;
    manager.setHousehold(userId, selectedHousehold);
  }

}
