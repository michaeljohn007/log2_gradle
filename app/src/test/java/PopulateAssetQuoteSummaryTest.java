import PortfolioSystem.AssetQuote;
import PortfolioSystem.PortfolioSystemOperations;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Set;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PopulateAssetQuoteSummaryTest {


  @Test
  public void populateAssetQuoteSummaryTest_OneAssetOneHolding_SameDetailsReturned() throws IOException {

    //Arrange
    PortfolioSystemOperations testPortfolioSystem=new PortfolioSystemOperations();
    AssetQuote.assetQuotes.clear();
    AssetQuote expectedResult = new AssetQuote("AAPL", "Apple", 1625504878, 2799.2, 20,"EQUITY");
    AssetQuote.assetQuotes.add(expectedResult);

    AssetQuote actualResult = new AssetQuote();

    //Act
    LinkedHashMap<String, AssetQuote> assetQuoteSummary=testPortfolioSystem.populateAssetQuoteSummary();

    Set<String> keys = assetQuoteSummary.keySet();
    for(String key: keys){
      actualResult= assetQuoteSummary.get(key);
    }

    //Assert
    Assert.assertEquals(actualResult.getAssetSymbol(), expectedResult.getAssetSymbol());
    Assert.assertEquals(actualResult.getValue(), expectedResult.getValue());
    Assert.assertEquals(actualResult.getStockAmount(), expectedResult.getStockAmount());


  }

  @Test
  public void populateAssetQuoteSummaryTest_OneAssetSeveralHolding_SummedDetailsReturned() throws IOException {

    //Arrange

    PortfolioSystemOperations testPortfolioSystem=new PortfolioSystemOperations();
    AssetQuote.assetQuotes.clear();
    AssetQuote a1 = new AssetQuote("AAPL", "Apple", 1625504878, 3000, 10,"EQUITY");
    AssetQuote a2 = new AssetQuote("AAPL", "Apple", 1625504879, 1500, 5, "EQUITY");
    AssetQuote a3 = new AssetQuote("AAPL", "Apple", 1625504880, 6000, 20,"EQUITY");


    AssetQuote.assetQuotes.add(a1);
    AssetQuote.assetQuotes.add(a2);
    AssetQuote.assetQuotes.add(a3);


    AssetQuote actualResult = new AssetQuote();
    double expectedStockAmount=35.0;
    double expectedValue=10500.0;


    //Act
    LinkedHashMap<String, AssetQuote> assetQuoteSummary=testPortfolioSystem.populateAssetQuoteSummary();

    Set<String> keys = assetQuoteSummary.keySet();
    for(String key: keys){
      actualResult= assetQuoteSummary.get(key);
    }

    //Assert
    Assert.assertEquals(actualResult.getValue(), expectedValue);
    Assert.assertEquals(actualResult.getStockAmount(), expectedStockAmount);


  }
}