/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Livia
 */
@WebService(serviceName = "Prepocet")
public class Prepocet {

  /**
   * Web service operation
   */
  @WebMethod(operationName = "prepocet")
  public List<Double> prepocet1(@WebParam(name = "oldPortions") double oldPortions, @WebParam(name = "newPortion") double newPortion, @WebParam(name = "ingredient") List<Double> ingredient) {
    List<Double> result = new ArrayList<>();
    for (int i = 0; i < ingredient.size(); i++) {
      double value = ingredient.get(i) / oldPortions * newPortion;
      result.add(value);
    }
    return result;
  }
}
