import PortfolioSystem.PortfolioSystemOperations;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddFundsTest {
  @Test
  public void addFunds() {
    PortfolioSystemOperations testPortfolioSystemOperations =new PortfolioSystemOperations();
    testPortfolioSystemOperations.setStartingBalance(10);
    testPortfolioSystemOperations.addFunds(200);
    double expectedFunds = 210;
    Assert.assertEquals(testPortfolioSystemOperations.getAvailableFunds(), expectedFunds );
  }
}