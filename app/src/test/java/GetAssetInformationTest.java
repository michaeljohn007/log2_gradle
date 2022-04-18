import PortfolioSystem.AssetQuote;
import PortfolioSystem.PortfolioSystemOperations;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetAssetInformationTest {

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
