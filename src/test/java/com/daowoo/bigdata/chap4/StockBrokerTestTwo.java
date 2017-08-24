package com.daowoo.bigdata.chap4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

/**
 * Created by apple on 23/08/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class StockBrokerTestTwo {
    @Mock
    MarketWatcher marketWatcher;
    @Mock
    Portfolio portfolio;
    @Mock
    StockBroker broker;

    @Before
    public void setUp() {
        broker = new StockBroker(marketWatcher);
    }

    @Test
    public void when_ten_percent_gain_then_the_stock_is_sold() {
        //Portfolio's getAvgPrice is stubbed to return $10.00
        when(portfolio.getAvgPrice(isA(Stock.class))).
                thenReturn(new BigDecimal("10.00"));

        //A stock object is created with current price $11.20
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal("11.20"));
        //getQuote method is stubbed to return the stock
        when(marketWatcher.getQuote(anyString())).thenReturn(aCorp);

        //perform method is called, as the stock price increases
        // by 12% the broker should sell the stocks
        broker.perform(portfolio, aCorp);

        //verifying that the broker sold the stocks
        verify(portfolio).sell(aCorp, 10);
    }

    @Test  public void verify_no_more_interaction() {
        Stock noStock = null;
        portfolio.getAvgPrice(noStock);
        portfolio.sell(null, 0);
        verify(portfolio).getAvgPrice(eq(noStock));
        //this will fail as the sell method was invoked
        verifyNoMoreInteractions(portfolio);
    }

    @Test
    public void argument_matcher() {
        when(portfolio.getAvgPrice(isA(Stock.class))).
                thenReturn(new BigDecimal("10.00"));

        Stock blueChipStock = new Stock("FB", "FB Corp", new BigDecimal(1000.00));
        Stock otherStock = new Stock("XY", "XY Corp", new BigDecimal(5.00));

        when(marketWatcher.getQuote(argThat(new BlueChipStockMatcher()))).thenReturn(blueChipStock);
        when(marketWatcher.getQuote(argThat(new OtherStockMatcher()))).thenReturn(otherStock);

        broker.perform(portfolio, blueChipStock);
        verify(portfolio).sell(blueChipStock,10);

        broker.perform(portfolio, otherStock);
        verify(portfolio, never()).sell(otherStock,10);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsException() throws Exception {
        when(portfolio.getAvgPrice(isA(Stock.class))).thenThrow(new IllegalStateException("Database down"));

        portfolio.getAvgPrice(new Stock(null, null, null));
    }

    @Test(expected = IllegalStateException.class)
    public void throwsException_void_methods() throws Exception {
        doThrow(new IllegalStateException()).
                when(portfolio).buy(isA(Stock.class));
        portfolio.buy(new Stock(null, null, null));
    }

    @Test
    public void consecutive_calls() throws Exception {
        Stock stock = new Stock(null, null, null);
        when(portfolio.getAvgPrice(stock)).thenReturn(BigDecimal.TEN, BigDecimal.ZERO);
        assertEquals(BigDecimal.TEN, portfolio.getAvgPrice(stock));
        assertEquals(BigDecimal.ZERO, portfolio.getAvgPrice(stock));
        assertEquals(BigDecimal.ZERO, portfolio.getAvgPrice(stock));
    }

    Map<String, List<Stock>> stockMap = new HashMap<String, List<Stock>>();
    class BuyAnswer implements Answer<Object> {

        public Object answer(InvocationOnMock invocation) throws Throwable {
            Stock newStock = (Stock) invocation.getArguments()[0];
            List<Stock> stocks = stockMap.get(newStock.getSymbol());
            if (stocks != null) {
                stocks.add(newStock);
            } else {
                stocks = new ArrayList<Stock>();
                stocks.add(newStock);
                stockMap.put(newStock.getSymbol(), stocks);
            }
            return null;
        }
    }

    class TotalPriceAnswer implements Answer<BigDecimal> {

        public BigDecimal answer(InvocationOnMock invocation) throws Throwable  {
            BigDecimal totalPrice = BigDecimal.ZERO;

            for(String stockId: stockMap.keySet()) {
                for(Stock stock:stockMap.get(stockId)) {
                    totalPrice = totalPrice.add(stock.getPrice());
                }
            }
            return totalPrice;
        }
    }


    @Test
    public void answering() throws Exception {
        stockMap.clear();
        doAnswer(new BuyAnswer()).when(portfolio).
                buy(isA(Stock.class));

        when(portfolio.getCurrentValue()).
                then(new TotalPriceAnswer());

        portfolio.buy(new Stock("A", "A", BigDecimal.TEN));
        portfolio.buy(new Stock("B", "B", BigDecimal.ONE));

        assertEquals(new BigDecimal("11"), portfolio.getCurrentValue());
    }

    @Test public void spying() throws Exception {
        Stock realStock = new Stock("A", "Company A", BigDecimal.ONE);
        Stock spyStock = spy(realStock);
        //call real method from  spy
        assertEquals("A", spyStock.getSymbol());

        //Changing value using spy
        spyStock.updatePrice(BigDecimal.ZERO);

        //verify spy has the changed value
        assertEquals(BigDecimal.ZERO, spyStock.getPrice());

        //Stubbing method
        when(spyStock.getPrice()).thenReturn(BigDecimal.TEN);

        //Changing value using spy
        spyStock.updatePrice(new BigDecimal("7"));

        //Stubbed method value 10.00  is returned NOT 7
        assertNotEquals(new BigDecimal("7"), spyStock.getPrice());
        //Stubbed method value 10.00
        assertEquals(BigDecimal.TEN,  spyStock.getPrice());

    }

    @Test
    public void argument_captor() throws Exception {
        when(portfolio.getAvgPrice(isA(Stock.class))).thenReturn(new BigDecimal("10.00"));
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
        when(marketWatcher.getQuote(anyString())).thenReturn(aCorp);
        broker.perform(portfolio, aCorp);

        ArgumentCaptor<String> stockIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(marketWatcher).getQuote(stockIdCaptor.capture());
        assertEquals("A", stockIdCaptor.getValue());

        //Two arguments captured
        ArgumentCaptor<Stock>  stockCaptor = ArgumentCaptor.forClass(Stock.class);
        ArgumentCaptor<Integer> stockSellCountCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(portfolio).sell(stockCaptor.capture(), stockSellCountCaptor.capture());
        assertEquals("A", stockCaptor.getValue().getSymbol());
        assertEquals(10, stockSellCountCaptor.getValue().intValue());
    }

    @Test public void inorder() throws Exception {
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
        portfolio.getAvgPrice(aCorp);
        portfolio.getCurrentValue();
        marketWatcher.getQuote("X");
        portfolio.buy(aCorp);

        InOrder inOrder=inOrder(portfolio,marketWatcher);
        inOrder.verify(portfolio).getAvgPrice(isA(Stock.class));
        inOrder.verify(portfolio).getCurrentValue();
        inOrder.verify(marketWatcher).getQuote(anyString());
        inOrder.verify(portfolio).buy(isA(Stock.class));
    }

    @Test
    public void changing_default() throws Exception {
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));
        Portfolio pf = Mockito.mock(Portfolio.class);
        //default null is returned
        assertNull(pf.getAvgPrice(aCorp));
        Portfolio pf1 = Mockito.mock(Portfolio.class, Mockito.RETURNS_SMART_NULLS);
        //a smart null is returned
        System.out.println("#1 "+pf1.getAvgPrice(aCorp));
        assertNotNull(pf1.getAvgPrice(aCorp));

        Portfolio pf2 = Mockito.mock(Portfolio.class, Mockito.RETURNS_MOCKS);
        //a mock is returned
        System.out.println("#2 "+pf2.getAvgPrice(aCorp));
        assertNotNull(pf2.getAvgPrice(aCorp));

        Portfolio pf3 = Mockito.mock(Portfolio.class, Mockito.RETURNS_DEEP_STUBS);
        //a deep stubbed mock is returned
        System.out.println("#3 "+pf3.getAvgPrice(aCorp));
        assertNotNull(pf3.getAvgPrice(aCorp));
    }

    @Test
    public void resetMock() throws Exception {
        Stock aCorp = new Stock("A", "A Corp", new BigDecimal(11.20));

        Portfolio portfolio = Mockito.mock(Portfolio.class);
        when(portfolio.getAvgPrice(eq(aCorp))).
                thenReturn(BigDecimal.ONE);
        assertNotNull(portfolio.getAvgPrice(aCorp));

        Mockito.reset(portfolio);
        //Resets the stub, so getAvgPrice returns NULL
        assertNull(portfolio.getAvgPrice(aCorp));
    }

    Stock globalStock =  when(Mockito.mock(Stock.class).getPrice()).thenReturn(BigDecimal.ONE).getMock();

    @Test
    public void access_global_mock() throws Exception {
        assertEquals(BigDecimal.ONE, globalStock.getPrice());
    }

    @Test
    public void mocking_details() throws Exception {
        Portfolio pf1 = Mockito.mock(Portfolio.class, Mockito.RETURNS_MOCKS);

        BigDecimal result = pf1.getAvgPrice(globalStock);
        assertNotNull(result);
        assertTrue(Mockito.mockingDetails(pf1).isMock());

        Stock myStock = new Stock(null, null, null);
        Stock spy = spy(myStock);
        assertTrue(Mockito.mockingDetails(spy).isSpy());

    }


}
