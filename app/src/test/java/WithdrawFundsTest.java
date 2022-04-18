import PortfolioSystem.PortfolioSystemOperations;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WithdrawFundsTest {

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
}