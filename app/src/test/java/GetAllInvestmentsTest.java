import PortfolioSystem.AssetQuote;
import PortfolioSystem.PortfolioSystemOperations;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetAllInvestmentsTest {

  @Test
  public void TestGetAllInvestments() throws IOException, ParseException {

    AssetQuote a1 = new AssetQuote("TSLA", "Tesla", 1633108078, 7752.2, 10, "EQUITY");
    AssetQuote a2 = new AssetQuote("AAPL", "Apple", 1625504878, 2799.2, 20,"EQUITY");

    AssetQuote.assetQuotes.add(a1);
    AssetQuote.assetQuotes.add(a2);

    PortfolioSystemOperations testGetAllInvestments = new PortfolioSystemOperations();
    String getAllInvestmentsString = testGetAllInvestments.listAllInvestments();
    Assert.assertTrue(getAllInvestmentsString.contains("| Name: Tesla | Symbol: TSLA | Purchase Price: 775.22 |"));
    Assert.assertTrue(getAllInvestmentsString.contains("| Stock Amount: 10.0 |"));
    Assert.assertTrue(getAllInvestmentsString.contains("| Name: Apple | Symbol: AAPL | Purchase Price: 139.96 |"));
    Assert.assertTrue(getAllInvestmentsString.contains("| Stock Amount: 20.0 |"));

  }

}
