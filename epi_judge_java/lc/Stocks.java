package lc;

public class Stocks {
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) return 0;
        int days = prices.length;
        int[] minPrices = new int[days + 1];
        int[] maxProfits = new int[days + 1];
        minPrices[0] = Integer.MAX_VALUE;
        for (int i = 1; i <= days; i++) {
            maxProfits[i] = Math.max(maxProfits[i - 1], i > 1 ? prices[i - 1] - minPrices[i - 1] : 0);
            minPrices[i] = Math.min(minPrices[i - 1], i > 1 ? prices[i - 1] - maxProfits[i - 2] : prices[i - 1] - 0);
        }
        return maxProfits[days];
    }

    public static void main(String[] args) {
        System.out.println(maxProfit(new int[]{2,1}));
    }
}
