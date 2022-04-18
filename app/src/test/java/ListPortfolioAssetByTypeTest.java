import PortfolioSystem.AssetQuote;
import PortfolioSystem.PortfolioSystemOperations;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ListPortfolioAssetByTypeTest {

  @Test
  public void TestListPortfolioAssetsByType() throws IOException, ParseException {

    AssetQuote a1 = new AssetQuote("TSLA", "Tesla", 1633108078, 7752.2, 10, "EQUITY");
    AssetQuote a2 = new AssetQuote("AAPL", "Apple", 1625504878, 2799.2, 20, "EQUITY");
    AssetQuote a3 = new AssetQuote("BTC-USD", "Bitcoin", 1612883278, 2000, 0.0445881, "CRYPTOCURRENCY");

    AssetQuote.assetQuotes.add(a1);
    AssetQuote.assetQuotes.add(a2);
    AssetQuote.assetQuotes.add(a3);

    PortfolioSystemOperations testListPortfolioAssetsByType = new PortfolioSystemOperations();

    String testListPortfolioAssetsByTypeString = testListPortfolioAssetsByType.listPortfolioAssetsByType("Crypto");
    Assert.assertTrue(testListPortfolioAssetsByTypeString.contains("| Name: Bitcoin | Symbol: BTC-USD | Purchase Price: 44855.02 |"));
    Assert.assertTrue(testListPortfolioAssetsByTypeString.contains("| Stock Amount: 0.0445881 |"));




  }

}
