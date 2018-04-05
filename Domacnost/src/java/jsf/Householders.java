package jsf;

import entity.Household;

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
public class Householders {
  //

  String version = "1.0";

  private Map<Integer, Household> usersHousholder;

  public Householders() {
    usersHousholder = new HashMap<>();
  }

  public void newUser(int id) {
    usersHousholder.put(id, null);
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

}
