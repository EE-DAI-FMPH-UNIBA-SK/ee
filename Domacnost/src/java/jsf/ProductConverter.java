package jsf;

import entity.Product;

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
@ManagedBean(name = "productConverterBean")
@FacesConverter(value = "productConverter")
public class ProductConverter implements Converter {
  //

  @PersistenceContext(unitName = "DomacnostPU")
  private transient EntityManager em;

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }

    try {
      return em.find(Product.class, Integer.valueOf(value));
    } catch (NumberFormatException e) {
      throw new ConverterException(new FacesMessage(value + " is not a valid Product ID"), e);
    }
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, Object value) {
    if (value == null) {
      return "";
    }

    if (value instanceof Product) {
      return String.valueOf(((Product) value).getId());
    } else {
      throw new ConverterException(new FacesMessage(value + " is not a valid Product"));
    }
  }

}
