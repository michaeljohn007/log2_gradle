import PortfolioSystem.PortfolioSystemOperations;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SetStartingBalanceTest {

  @Test
  public void setStartingBalance() {
    PortfolioSystemOperations testWithdrawFunds=new PortfolioSystemOperations();
    testWithdrawFunds.setStartingBalance(10);
    double currentBalance = 10;
    Assert.assertEquals(testWithdrawFunds.getAvailableFunds(), currentBalance);

    double newBalance = 20;
    Assert.assertNotEquals(testWithdrawFunds.getAvailableFunds(), newBalance);
  }
}
