import PortfolioSystem.Asset;
import PortfolioSystem.AssetQuote;
import PortfolioSystem.PortfolioSystem;
import PortfolioSystem.PortfolioSystemOperations;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
public class PurchaseAssetMethodTest {
 @Test
 // public void testPurchaseAsset method in PortfolioSystemOperations class
 public void checkAssets1_purchaseAsset() throws IOException, ParseException {
  AssetQuote newAsset = new AssetQuote("TST-Asset", "Test Asset", 1644688177,
      111.11 );

  boolean expectedResult = true;
  boolean actualResult;
  PortfolioSystemOperations pso = new PortfolioSystemOperations();

  String assetSym = "AMD";
  double amount = 5;

  // Act
  AssetQuote.setAvailableFunds(10000);
  actualResult = pso.purchaseAsset("AMD", 5);

  // Assert
  Assert.assertEquals(actualResult, expectedResult);

  // Act
  actualResult = pso.purchaseAsset("NVDA", 10);

  // Assert
  Assert.assertEquals(actualResult, expectedResult);
 }
 // the funds are set to 10000 so there are funds to buy assets and the tests should succeed!!!
 @Test
 // public void testPurchaseAsset method in PortfolioSystemOperations class
 public void checkAssets2_purchaseAsset() throws IOException, ParseException {

  boolean expectedResult = true;
  boolean actualResult;
  PortfolioSystemOperations pso = new PortfolioSystemOperations();
  AssetQuote.setAvailableFunds(10000);

  // Act
  actualResult = pso.purchaseAsset("AAPL", 10);

  // Assert
  Assert.assertEquals(actualResult, expectedResult);
 }
 @Test
 // public void testPurchaseAsset method in PortfolioSystemOperations class
 public void amountExeedingFunds_purchaseAsset() throws IOException, ParseException {
  AssetQuote.setAvailableFunds(10000);
  boolean expectedResult = false;
  boolean actualResult;
  PortfolioSystemOperations pso = new PortfolioSystemOperations();


  // Act
  actualResult = pso.purchaseAsset("MSFT", 1000);
  // message You don't have enough funds to buy the assets!!! will be displayed because the prive of NVDA
  // stock is

  // Assert
  Assert.assertEquals(actualResult, expectedResult);


 }


}
