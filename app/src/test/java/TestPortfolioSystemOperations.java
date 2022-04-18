import PortfolioSystem.AssetQuote;
import PortfolioSystem.PortfolioSystemOperations;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;


public class TestPortfolioSystemOperations {


  @Test
  public void setStartingBalance() {
    PortfolioSystemOperations testWithdrawFunds=new PortfolioSystemOperations();
    testWithdrawFunds.setStartingBalance(10);
    double currentBalance = 10;
    Assert.assertEquals(testWithdrawFunds.getAvailableFunds(), currentBalance);

    double newBalance = 20;
    Assert.assertNotEquals(testWithdrawFunds.getAvailableFunds(), newBalance);
  }
  @Test
  public void getExchangeSummary() throws IOException, ParseException {
    PortfolioSystemOperations testGetExchangeSummary = new PortfolioSystemOperations();
    String actual =testGetExchangeSummary.getExchangeSummary("US","SNP");
    System.out.println(actual);
    //Assert.assertTrue(actual.contains("Exchange Time Zone Name= America/New_York"));
    //Assert.assertTrue(actual.contains("Full Exchange Name= SNP"));
    //Assert.assertTrue(actual.contains("Symbol= ^GSPC"));

  }
  @Test
  public void addFunds() {
    PortfolioSystemOperations testPortfolioSystemOperations =new PortfolioSystemOperations();
    testPortfolioSystemOperations.setStartingBalance(10);
    testPortfolioSystemOperations.addFunds(200);
    double expectedFunds = 210;
    Assert.assertEquals(testPortfolioSystemOperations.getAvailableFunds(), expectedFunds );
  }

  @Test
  public void withdrawFunds() {
    PortfolioSystemOperations testWithdrawFunds=new PortfolioSystemOperations();
    boolean actualResult;
    testWithdrawFunds.setStartingBalance(10);
    actualResult =  testWithdrawFunds.withdrawFunds(5);
    Assert.assertEquals(actualResult, true);

    actualResult =  testWithdrawFunds.withdrawFunds(10);
    Assert.assertEquals(actualResult, false);
  }




  @Test
  public void getAssetInformation() throws IOException, ParseException {
    PortfolioSystemOperations testGetAssetInformation=new PortfolioSystemOperations();

    List<String> assetInformation = Arrays.asList("AAPL,TSLA");
    List<AssetQuote> actualAssetInformation;

    actualAssetInformation = testGetAssetInformation.getAssetInformation(assetInformation);
    Assert.assertEquals(actualAssetInformation.get(0).getAssetSymbol(), "AAPL");
    Assert.assertEquals(actualAssetInformation.get(1).getAssetSymbol(), "TSLA");
  }

}
