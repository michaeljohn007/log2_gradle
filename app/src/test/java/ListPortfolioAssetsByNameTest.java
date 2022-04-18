
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import PortfolioSystem.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class ListPortfolioAssetsByNameTest {


    @Test
    public void listPortfolioAssetsByName_EmptyListPassedIn_EmptyString() throws IOException,ParseException {

        //Arrange
        PortfolioSystemOperations  testPortfolioSystem=new PortfolioSystemOperations();
        AssetQuote.assetQuotes.clear();
        ArrayList<String> assetList = new ArrayList<>();


        String expectedResult="";
        String actualResult;

        //Act
        actualResult= testPortfolioSystem.listPortfolioAssetsByName(assetList);
        

        //Assert
        Assert.assertEquals(actualResult,expectedResult);

    }

    @Test
    public void listPortfolioAssetsByName_AssetNotInPortfolio_EmptyString() throws IOException,ParseException {

        //Arrange
        PortfolioSystemOperations  testPortfolioSystem=new PortfolioSystemOperations();
        AssetQuote.assetQuotes.clear();
        //Add some Assets to assetQuotes.
        AssetQuote a1 = new AssetQuote("TSLA", "Tesla", 1633108078, 7752.2, 10, "EQUITY");
        AssetQuote a2 = new AssetQuote("AAPL", "Apple", 1625504878, 2799.2, 20, "EQUITY");

        //ArrayList<AssetQuote> assetQuotes = new ArrayList<AssetQuote>();
        AssetQuote.assetQuotes.add(a1);
        AssetQuote.assetQuotes.add(a2);

        ArrayList<String> assetList = new ArrayList<>();
        assetList.add("DNUT") ;
        assetList.add("Ocu");  // Partial name match for Ocugen, Inc.


        String expectedResult="";
        String actualResult;

        //Act
        actualResult=testPortfolioSystem.listPortfolioAssetsByName(assetList);


        //Assert
        Assert.assertEquals(actualResult,expectedResult);

    }

    @Test
    public void listPortfolioAssetsByName_AssetSymbolInPortfolio_FormattedString() throws IOException,ParseException {

        //Arrange
        PortfolioSystemOperations  testPortfolioSystem=new PortfolioSystemOperations();

        AssetQuote.assetQuotes.clear();

        //Add some Assets to assetQuotes.
        AssetQuote a1 = new AssetQuote("TSLA", "Tesla", 1633108078, 7752.2, 10,"EQUITY");
        AssetQuote a2 = new AssetQuote("AAPL", "Apple", 1625504878, 2799.2, 20,"EQUITY");
        AssetQuote a3 = new AssetQuote("NVDA", "Nvidia Corporation", 1618412878, 1833.24,
                12,"EQUITY");
        AssetQuote a4 = new AssetQuote("BTC-USD", "Bitcoin", 1612883278, 2000, 0.0445881,"CRYPTOCURRENCY");
        AssetQuote a5 = new AssetQuote("KRP","Kimbell Royalty Partners", System.currentTimeMillis() / 1000L,15.00,100.0, "EQUITY");
        //ArrayList<AssetQuote> assetQuotes = new ArrayList<AssetQuote>();
        AssetQuote.assetQuotes.add(a1);
        AssetQuote.assetQuotes.add(a2);
        AssetQuote.assetQuotes.add(a3);
        AssetQuote.assetQuotes.add(a4);
        AssetQuote.assetQuotes.add(a5);

        ArrayList<String> assetList = new ArrayList<>();
        assetList.add("TSL");   // Partial symbol for Tesla
        System.out.println();


        String expectedResult = "Tesla";
        String actualResult;


        //Act
        actualResult= testPortfolioSystem.listPortfolioAssetsByName(assetList);


        //Assert
        Assert.assertTrue(actualResult.contains(expectedResult));

    }


    @Test
    public void listPortfolioAssetsByName_AssetNameInPortfolio_FormattedStringInResult() throws IOException,ParseException {

        //Arrange
        PortfolioSystemOperations  testPortfolioSystem=new PortfolioSystemOperations();

        AssetQuote.assetQuotes.clear();


        AssetQuote a1 = new AssetQuote("AAPL", "Apple", 1625504878, 1500.00, 10,"EQUITY");
        AssetQuote a2 = new AssetQuote("KRP","Kimbell Royalty Partners", System.currentTimeMillis() / 1000L,14.00,100.0,"EQUITY");
        AssetQuote.assetQuotes.add(a1);
        AssetQuote.assetQuotes.add(a2);


        ArrayList<String> assetList = new ArrayList<>();
        assetList.add("App");   // Partial name for Apple


        // Formatted output for a1
        String expectedResult = "Apple";

        String actualResult;

        //Act
        actualResult= testPortfolioSystem.listPortfolioAssetsByName(assetList);


        //Assert
        Assert.assertTrue(actualResult.contains(expectedResult));
        AssetQuote.assetQuotes.clear();
    }


    @Test
    public void listPortfolioAssetsByName_MultipleAssetSymbolInPortfolio_FormattedString() throws IOException, ParseException {

        //Arrange
        PortfolioSystemOperations  testPortfolioSystem=new PortfolioSystemOperations();

        AssetQuote.assetQuotes.clear();

        //Add some Assets to assetQuotes.
        AssetQuote a1 = new AssetQuote("TSLA", "Tesla", 1633108078, 7752.2, 10,"EQUITY");
        AssetQuote a2 = new AssetQuote("AAPL", "Apple", 1625504878, 2799.2, 20,"EQUITY");
        AssetQuote a3 = new AssetQuote("NVDA", "Nvidia Corporation", 1618412878, 1833.24,
                12,"EQUITY");
        AssetQuote a4 = new AssetQuote("BTC-USD", "Bitcoin", 1612883278, 2000, 0.0445881,"CRYPTOCURRENCY");
        AssetQuote a5 = new AssetQuote("KRP","Kimbell Royalty Partners", System.currentTimeMillis() / 1000L,14.00,100.0,"EQUITY");
        //ArrayList<AssetQuote> assetQuotes = new ArrayList<AssetQuote>();
        AssetQuote.assetQuotes.add(a1);
        AssetQuote.assetQuotes.add(a2);
        AssetQuote.assetQuotes.add(a3);
        AssetQuote.assetQuotes.add(a4);
        AssetQuote.assetQuotes.add(a5);

        ArrayList<String> assetList = new ArrayList<>();

        assetList.add("NVD"); // Partial name symbol for Nvidia
        assetList.add("KR");  // Partial name symbol for Kimbell Royalty Partners
        assetList.add("Bit"); // Partial name for Bitcoin
        assetList.add("MSF"); // Partial name for Microsoft - not in portfolio

        String actualResult;

        // 3 expected results
        String expectedResult1="Bitcoin";
        String expectedResult2="Kimbell Royalty Partners";
        String expectedResult3="Nvidia Corporation";

        //Act
        actualResult=testPortfolioSystem.listPortfolioAssetsByName(assetList);


        //Assert - Check 3 expected results are returned
        Assert.assertTrue(actualResult.contains(expectedResult1));
        Assert.assertTrue(actualResult.contains(expectedResult2));
        Assert.assertTrue(actualResult.contains(expectedResult3));

    }





}
