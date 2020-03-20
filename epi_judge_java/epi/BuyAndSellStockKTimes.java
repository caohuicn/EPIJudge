package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class BuyAndSellStockKTimes {
  @EpiTest(testDataFile = "buy_and_sell_stock_k_times.tsv")

  public static double buyAndSellStockKTimes(List<Double> prices, int k) {
    if (k == 0) {
      return 0.0;
    } else if ( 2 * k >= prices.size()) {
      return unlimitedBuyAndSells(prices);
    }
    //dp[k][n] = max(dp[k-1][n], p(n) + max(dp[k-1][m]) - p(m+1)
    //m: 0 to n-2
    return buyAndSellStockKTimesDP4(prices, k);

  }


  public static double buyAndSellStockKTimesDP4(List<Double> prices, int k) {
    List<Double> minPrices = new ArrayList<>(Collections.nCopies(k + 1, Double.MAX_VALUE));
    List<Double> maxProfits =  new ArrayList<>(Collections.nCopies(k + 1, 0.0));
    for (Double price: prices) {
      for (int i = 1; i <= k; i++) {
        maxProfits.set(i, Math.max(maxProfits.get(i), price - minPrices.get(i)));
        minPrices.set(i, Math.min(minPrices.get(i), price - maxProfits.get(i - 1)));
      }
    }
    return maxProfits.get(k);
  }

  public static double buyAndSellStockKTimesDP3(List<Double> prices, int k) {
    double[] prev = new double[prices.size()];
    double[] dp = new double[prices.size()];
    for (int i = 1; i <= k; i++) {
      double sellOnJCost = prices.get(0);
      for (int j = 1; j < prices.size(); j++) {
        double price = prices.get(j);
        sellOnJCost = Math.min(prices.get(j - 1) - prev[j - 1], sellOnJCost);
        dp[j] = Math.max(dp[j - 1], price - sellOnJCost);
      }
      prev = Arrays.copyOf(dp, dp.length);
    }
    return dp[prices.size() - 1];
  }

  public static double buyAndSellStockKTimesDP2(List<Double> prices, int k) {
    double[][] dp = new double[k+1][prices.size()];
    for (int i = 0; i <= k; i++) {
      dp[i][0] = 0;
    }
    for (int i = 0; i < prices.size(); i++) {
      dp[0][i] = 0;
    }
    for (int i = 1; i <= k; i++) {
      double sellOnJCash = -prices.get(0);
      for (int j = 1; j < prices.size(); j++) {
        double price = prices.get(j);
        sellOnJCash = Math.max(dp[i - 1][j - 1] - prices.get(j - 1), sellOnJCash);
        dp[i][j] = Math.max(dp[i][j - 1], sellOnJCash + price);
      }
    }
    return dp[k][prices.size() - 1];
  }

  public static double buyAndSellStockKTimesDP(List<Double> prices, int k) {
    double[][] dp = new double[k+1][prices.size() + 1];
    for (int i = 0; i <= k; i++) {
      dp[i][0] = 0;
    }
    for (int i = 0; i <= prices.size(); i++) {
      dp[0][i] = 0;
    }
    for (int i = 1; i <= k; i++) {
      for (int j = 0; j < prices.size(); j++) {
        double price = prices.get(j);
        double sellOnJProfit = 0;
        for (int m = 0; m < j; m++) {
          sellOnJProfit = Math.max(price - prices.get(m) + dp[i - 1][m], sellOnJProfit);
        }
        dp[i][j + 1] = Math.max(dp[i][j], sellOnJProfit);
      }
    }
    return dp[k][prices.size()];
  }

  private static double unlimitedBuyAndSells(List<Double> prices) {
    double sum = 0;
    for(int i = 1; i < prices.size(); i++) {
      sum += Math.max(0.0, prices.get(i) - prices.get(i-1));
    }
    return sum;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "BuyAndSellStockKTimes.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
//    System.out.println(buyAndSellStockKTimes(Arrays.asList(5.0, 6.0, 8.0, 1.0, 9.0), 2));
  }
}
