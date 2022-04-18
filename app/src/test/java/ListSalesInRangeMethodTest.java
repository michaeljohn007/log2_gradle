import PortfolioSystem.AssetQuote;
import PortfolioSystem.PortfolioSystemOperations;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
public class ListSalesInRangeMethodTest {
    @Test
    public void listSalesInRange()
    {
        AssetQuote a1 = new AssetQuote("TSLA", "Tesla", 1633108078, 7752.2, 10,"EQUITY");
        AssetQuote a2 = new AssetQuote("AAPL", "Apple", 1625504878, 2799.2, 20, "EQUITY");
        AssetQuote a3 = new AssetQuote("NVDA", "Nvidia Corporation", 1618412878, 1833.24,
                12,"EQUITY");
        AssetQuote a4 = new AssetQuote("BTC-USD", "Bitcoin", 1612883278, 2000, 0.0445881,"CRYPTOCURRENCY");

        //ArrayList<AssetQuote> assetQuotes = new ArrayList<AssetQuote>();
        AssetQuote.assetQuotes.add(a1);
        AssetQuote.assetQuotes.add(a2);
        AssetQuote.assetQuotes.add(a3);
        AssetQuote.assetQuotes.add(a4);

        PortfolioSystemOperations pso = new PortfolioSystemOperations();

        double startingBalance = 10000;
        AssetQuote.setAvailableFunds(startingBalance);
        pso.setStartingBalance(startingBalance);

        try {
            pso.sellAsset("TSLA", 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String start, end, output;
        start = "02/02/1970";
        end = "02/11/2021";
        try {
            output = pso.listPortfolioSalesInRange(
                    PortfolioSystemOperations.date2epoch(start),
                    PortfolioSystemOperations.date2epoch(end));

            Assert.assertTrue(output.contains("Symbol: TSLA, Average Purchase Price:"));

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        start = "02/02/1970";
        end = "02/11/2020";
        try {
            output = pso.listPortfolioSalesInRange(
                    PortfolioSystemOperations.date2epoch(start),
                    PortfolioSystemOperations.date2epoch(end));

            Assert.assertEquals(output, "");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }
}
