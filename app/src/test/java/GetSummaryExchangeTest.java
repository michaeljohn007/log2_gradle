import PortfolioSystem.PortfolioSystemOperations;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
public class GetSummaryExchangeTest {
    @Test
    public void getExchangeSummary() throws IOException, ParseException {
        PortfolioSystemOperations testGetExchangeSummary = new PortfolioSystemOperations();
        String actual =testGetExchangeSummary.getExchangeSummary("US","DJI");
        Assert.assertTrue(actual.contains("Exchange Time Zone Name= America/New_York"));
        Assert.assertTrue(actual.contains("Full Exchange Name= DJI"));
        Assert.assertTrue(actual.contains("Symbol= ^DJI"));
    }
}