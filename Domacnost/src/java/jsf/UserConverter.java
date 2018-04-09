package jsf;

import entity.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "userConverterBean")
@FacesConverter(value = "userConverter")
public class UserConverter implements Converter {

  @PersistenceContext(unitName = "DomacnostPU")
  private transient EntityManager em;

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }

    try {
      return em.find(User.class, Integer.valueOf(value));
    } catch (NumberFormatException e) {
      throw new ConverterException(new FacesMessage(value + " is not a valid User ID"), e);
    }
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    if (value == null) {
      return "";
    }

    if (value instanceof User) {
      return String.valueOf(((User) value).getId());
    } else {
      throw new ConverterException(new FacesMessage(value + " is not a valid User"));
    }
  }

}
