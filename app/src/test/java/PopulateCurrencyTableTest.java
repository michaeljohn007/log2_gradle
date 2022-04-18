import PortfolioSystem.PortfolioSystemOperations;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PopulateCurrencyTableTest {



  @Test
  public void populateCurrencyMap_CheckForKeyCurrencies_KeysInTable() throws IOException, ParseException {

    //Arrange
    PortfolioSystemOperations testPortfolioSystem=new PortfolioSystemOperations();
    LinkedHashMap<String, Double> exchangeRates = new LinkedHashMap<String, Double>();

    Boolean expectedResult = true;
    Boolean actualResultEuro,actualResultCadDollar,actualResultPound;

    //Act
    exchangeRates=testPortfolioSystem.populateCurrencyMap();


    // Check if some specific currencies are in the table
    actualResultEuro=exchangeRates.containsKey("EUR/USD");
    actualResultCadDollar=exchangeRates.containsKey("CAD/USD");
    actualResultPound=exchangeRates.containsKey("GBP/USD");


    Assert.assertEquals(actualResultEuro,expectedResult);
    Assert.assertEquals(actualResultCadDollar,expectedResult);
    Assert.assertEquals(actualResultPound,expectedResult);

  }

}
