package jsf;

import entity.Household;
import entity.Ingredient;
import entity.Product;
import entity.Recipe;
import session.IngredientFacade;
import session.ProductFacade;
import session.RecipeFacade;
import session.SessionUtils;
import webservice.client.Prepocet;
import webservice.client.Prepocet_Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "Recipe")
@SessionScoped
public class RecipeController implements Serializable {
  //
  @Inject
  ApplicationManagers manager;
  private int userId;
  private String message;
  private Recipe selectedRecipe;
  private Household selectedHousehold;
  private List<Recipe> recipes;

  private String name;
  private String description;
  private int countPortions;

  private Product newProduct;
  private double productCount;

  private int portion;
  private List<Ingredient> ingredients;
//
  @EJB
  private RecipeFacade rf;
  @EJB
  private ProductFacade pf;
  @EJB
  private IngredientFacade inf;

  @WebServiceRef(wsdlLocation = "http://localhost:8080/PrepocetWS/Prepocet?wsdl")
  private Prepocet_Service prepocetService;

  public RecipeController() {
    userId = SessionUtils.getUserId();
  }

  @PostConstruct
  private void init() {
    userId = SessionUtils.getUserId();
    selectedRecipe = null;
    manager.setSelectedRecipe(userId, selectedRecipe);
    selectedHousehold = manager.getSelectHousehold(userId);
    recipes = manager.getSelectHousehold(userId).getRecipeList();
  }

  public ApplicationManagers getManager() {
    return manager;
  }

  public void setManager(ApplicationManagers manager) {
    this.manager = manager;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Recipe getSelectedRecipe() {
    return selectedRecipe;
  }

  public void setSelectedRecipe(Recipe selectedRecipe) {
    this.selectedRecipe = selectedRecipe;
    portion = 0;
    if (selectedRecipe != null) {
      ingredients = selectedRecipe.getIngredientList();
      countPortions = selectedRecipe.getCountPortions();
    } else {
      countPortions = 0;
    }
  }

  public Household getSelectedHousehold() {
    selectedHousehold = manager.getSelectHousehold(userId);
    return selectedHousehold;
  }

  public void setSelectedHousehold(Household selectedHousehold) {
    this.selectedHousehold = selectedHousehold;
    manager.setHousehold(userId, selectedHousehold);
  }

  public List<Recipe> getRecipes() {
    return recipes;
  }

  public void setRecipes(List<Recipe> recipes) {
    this.recipes = recipes;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getCountPortions() {
    return countPortions;
  }

  public void setCountPortions(int countPortions) {
    this.countPortions = countPortions;
  }

  public Product getNewProduct() {
    return newProduct;
  }

  public void setNewProduct(Product newProduct) {
    this.newProduct = newProduct;
  }

  public double getProductCount() {
    return productCount;
  }

  public void setProductCount(double productCount) {
    this.productCount = productCount;
  }

  public int getPortion() {
    return portion;
  }

  public void setPortion(int portion) {
    this.portion = portion;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public List<Product> getAllProducts() {
    List<Product> result = pf.getAllProduct();
    result.removeAll(selectedRecipe.getIngredientList().stream().map(i -> i.getProduct()).collect(Collectors.toList()));
    result.remove(selectedHousehold.getAdmin());
    return result;
  }

  public void addRecipe() {
    setMessage("");
    if (name != "" && description != "" && countPortions != 0) {
      Recipe newRecipe = new Recipe(name, description, countPortions, selectedHousehold);
      rf.create(newRecipe);
      selectedHousehold.addRecipe(newRecipe);
      selectedRecipe = null;
      name = "";
      description = "";
      countPortions = 0;
    } else {
      setMessage("You can not create a recipe. Check the correctness of the data");
    }
  }

  public void deleteRecipe(Recipe r) {
    setMessage("");
    try {
      rf.deleteRecipe(r);
      selectedHousehold.deleteRecipe(r);
    } catch (Exception e) {
      setMessage("Unable to delete the selected recipe");
    }
  }

  public void addIngredient() {
    Ingredient newIngredient = new Ingredient(selectedRecipe, newProduct, productCount);
    inf.create(newIngredient);
    selectedRecipe.addIngredient(newIngredient);
    newProduct = null;
    productCount = 0;
  }

  public void deleteIngredient(Ingredient ingredient) {
    inf.deleteIngredient(ingredient);
    selectedRecipe.deleteIngredient(ingredient);
    ingredients.remove(ingredient);
  }

  public void calculateIngredient() {
    Prepocet prepocet = prepocetService.getPrepocetPort();
    List<Double> ingredients = this.ingredients.stream().map(i -> i.getCount()).collect(Collectors.toList());
    ingredients = prepocet.prepocet(countPortions, portion, ingredients);
    countPortions = portion;
    for (int i = 0; i < this.ingredients.size(); i++) {
      this.ingredients.get(i).setCount(ingredients.get(i));
    }
  }
}
