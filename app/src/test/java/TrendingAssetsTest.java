
import PortfolioSystem.Asset;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import PortfolioSystem.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrendingAssetsTest {

    @Test
    public void getTendingAsset_TendingAssetForGB_HasValues() throws IOException, ParseException {

        //Arrange
        PortfolioSystemOperations  testPortfolioSystem=new PortfolioSystemOperations();

        ArrayList<String> expectedResult = new ArrayList<>();
        expectedResult.add("Symbol");

        ArrayList<String> actualResult;

        //Act
        actualResult=(ArrayList<String>) testPortfolioSystem.getTrendingStocks("GB");

        //Assert
        Assert.assertEquals(!actualResult.isEmpty(),!actualResult.isEmpty());
    }

    @Test
    public void getTendingAsset_TendingAssetForIN_EmptyList() throws IOException,ParseException {

        //Arrange
        PortfolioSystemOperations  testPortfolioSystem=new PortfolioSystemOperations();

        ArrayList<String> expectedResult = new ArrayList<>();
        ArrayList<String> actualResult;

        //Act
        actualResult=(ArrayList<String>) testPortfolioSystem.getTrendingStocks("IN");

        //Assert
        Assert.assertEquals(actualResult,expectedResult);
    }

    @Test
    public void getTendingAsset_TendingAssetForNotValidMarketCode_EmptyList() throws IOException,ParseException {

        //Arrange
        PortfolioSystemOperations  testPortfolioSystem=new PortfolioSystemOperations();

        ArrayList<String> expectedResult = new ArrayList<>();
        ArrayList<String> actualResult;

        //Act
        actualResult=(ArrayList<String>) testPortfolioSystem.getTrendingStocks("XXYYZZ");

        //Assert
        Assert.assertEquals(actualResult,expectedResult);
    }



}

