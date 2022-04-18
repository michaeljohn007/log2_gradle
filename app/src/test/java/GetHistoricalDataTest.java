import PortfolioSystem.AssetQuote;
import PortfolioSystem.PortfolioSystemOperations;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

public class GetHistoricalDataTest {


  @org.testng.annotations.Test
  public void testGetHistoricalData() throws IOException, ParseException {
    PortfolioSystemOperations testGetHistoricalData = new PortfolioSystemOperations();
    List<String> assetSymbols = new ArrayList<>();
    assetSymbols.add("MSFT");
    AssetQuote assetQuote = new AssetQuote("MSFT", "Microsoft", 1645218000, 288.99);
    List<AssetQuote> expectedResult = new ArrayList<>();
    expectedResult.add(assetQuote);
    List<AssetQuote> actualResult;
    actualResult = testGetHistoricalData.getHistoricalData(assetSymbols,"1d", "1mo" );




    for (int i = 0; i < actualResult.size(); i++){
      Double closePrice  = actualResult.get(i).getValue();
      Long timestamp = actualResult.get(i).getTimeStamp();

      Assert.assertTrue(actualResult.get(i) != null);
      Assert.assertEquals(actualResult.get(i).getAssetFullName(),"Microsoft");
      Assert.assertEquals(actualResult.get(i).getAssetSymbol(), "MSFT");
      Assert.assertTrue(closePrice != null);
      Assert.assertTrue(timestamp != null);

    }
  }
}


