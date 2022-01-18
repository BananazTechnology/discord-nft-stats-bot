package com.aaronrenner.discordnftbot.models;

public enum Ticker {
    ETH("eth", "Ξ"),
    WETH("weth", "Ξ"),
    BTC("btc", "₿"),
    DAI("dai", "◈");

    private String ticker;
    private String symbol;

    Ticker(String ticker, String symbol) {
        this.ticker = ticker;
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return ticker;
    }

    public static Ticker fromString(String ticker) {
        if (ticker != null) {
            for (Ticker unit : Ticker.values()) {
                if (ticker.equalsIgnoreCase(unit.ticker)) {
                    return unit;
                }
            }
        }
        return Ticker.valueOf(ticker);
    }
}