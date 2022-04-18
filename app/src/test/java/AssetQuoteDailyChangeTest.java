import org.testng.Assert;
import org.testng.annotations.Test;
import PortfolioSystem.*;

import java.io.IOException;

public class AssetQuoteDailyChangeTest {


  @Test
  public void gainLossOnAsset_TestLossOf50() throws IOException {

    //Arrange
    double expectedResult = -50;
    double actualResult;
    AssetQuote quoteForDate = new AssetQuote();

    quoteForDate.setValue(100.00);
    quoteForDate.setOpeningValue(150.00);

    //Act
    actualResult = quoteForDate.getDaysGainOrLossValue();

    //Assert
    Assert.assertEquals(actualResult, expectedResult);
  }

  @Test
  public void gainLossOnAsset_TestGainOf50() throws IOException {

    //Arrange
    double expectedResult = 50;
    double actualResult;
    AssetQuote quoteForDate = new AssetQuote();

    quoteForDate.setValue(150.00);
    quoteForDate.setOpeningValue(100.00);

    //Act
    actualResult = quoteForDate.getDaysGainOrLossValue();

    //Assert
    Assert.assertEquals(actualResult, expectedResult);
  }


  @Test
  public void percentGainLossOnAsset_TestLossOf50Percent() throws IOException {
    //Arrange
    double expectedResult = -50;
    double actualResult;
    AssetQuote  quoteForDate = new AssetQuote();

    quoteForDate.setValue(100.00);
    quoteForDate.setOpeningValue(200.00);

    //Act
    actualResult = quoteForDate.getDaysGainOrLossPercent();

    //Assert
    Assert.assertEquals(actualResult, expectedResult);
  }

  @Test
  public void percentGainLossOnAsset_TestGainOf50Percent() throws IOException {

    //Arrange
    double expectedResult = 50;
    double actualResult;
    AssetQuote quoteForDate = new AssetQuote();

    quoteForDate.setValue(1500.00);
    quoteForDate.setOpeningValue(1000.00);

    //Act
    actualResult = quoteForDate.getDaysGainOrLossPercent();

    //Assert
    Assert.assertEquals(actualResult, expectedResult);
  }

  @Test
  public void gainLossOnAsset_NoChangeValue() throws IOException {

    double expectedResult = 0;
    double actualResult;
    AssetQuote quoteForDate = new AssetQuote();

    quoteForDate.setValue(100.00);
    quoteForDate.setOpeningValue(100.00);

    //Act
    actualResult = quoteForDate.getDaysGainOrLossPercent();

    //Assert
    Assert.assertEquals(actualResult, expectedResult);
  }

  @Test
  public void percentGainLossOnAsset_NoChangeValuePercent() throws IOException {

    double expectedResult = 0;
    double actualResult;
    AssetQuote quoteForDate = new AssetQuote();

    quoteForDate.setValue(1000.00);
    quoteForDate.setOpeningValue(1000.00);

    //Act
    actualResult = quoteForDate.getDaysGainOrLossPercent();

    //Assert
    Assert.assertEquals(actualResult, expectedResult);
  }
}
