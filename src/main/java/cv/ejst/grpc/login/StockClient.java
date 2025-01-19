package cv.ejst.grpc.login;

import cv.ejst.grpc.login.protos.generated.stock_quote.Stock;
import cv.ejst.grpc.login.protos.generated.stock_quote.StockQuote;
import cv.ejst.grpc.login.protos.generated.stock_quote.StockQuoteProviderGrpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StockClient {
    private static final Logger logger = Logger.getLogger(StockClient.class.getName());
    private StockQuoteProviderGrpc.StockQuoteProviderBlockingStub blockingStub;
    private StockQuoteProviderGrpc.StockQuoteProviderStub nonBlockingStub;
    private List<Stock> stocks;
    public static void main(String[] args) throws InterruptedException {
        String target = "localhost:50051";
        if (args.length > 0) {
            target = args[0];
        }

        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                .usePlaintext()
                .build();
        try {
            StockClient client = new StockClient(channel);

            client.serverSideStreamingListOfStockPrices();

            client.clientSideStreamingGetStatisticsOfStocks();

            client.bidirectionalStreamingGetListsStockQuotes();

        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
    public StockClient(Channel channel) {
        blockingStub = StockQuoteProviderGrpc.newBlockingStub(channel);
        nonBlockingStub = StockQuoteProviderGrpc.newStub(channel);
        initializeStocks();
    }
    private void initializeStocks() {

        this.stocks = Arrays.asList(Stock.newBuilder().setTickerSymbol("AU").setCompanyName("Auburn Corp").setDescription("Aptitude Intel").build()
                , Stock.newBuilder().setTickerSymbol("BAS").setCompanyName("Bassel Corp").setDescription("Business Intel").build()
                , Stock.newBuilder().setTickerSymbol("COR").setCompanyName("Corvine Corp").setDescription("Corporate Intel").build()
                , Stock.newBuilder().setTickerSymbol("DIA").setCompanyName("Dialogic Corp").setDescription("Development Intel").build()
                , Stock.newBuilder().setTickerSymbol("EUS").setCompanyName("Euskaltel Corp").setDescription("English Intel").build());
    }
    // ...
    public void serverSideStreamingListOfStockPrices() {
        Stock request = Stock.newBuilder()
                .setTickerSymbol("AU")
                .setCompanyName("Austich")
                .setDescription("server streaming example")
                .build();
        Iterator<StockQuote> stockQuotes;
        try {
            logInfo("REQUEST - ticker symbol {0}", request.getTickerSymbol());
            stockQuotes = blockingStub.serverSideStreamingGetListStockQuotes(request);
            for (int i = 1; stockQuotes.hasNext(); i++) {
                StockQuote stockQuote = stockQuotes.next();
                logInfo("RESPONSE - Price #" + i + ": {0}", stockQuote.getPrice());
            }
        } catch (StatusRuntimeException e) {
            logInfo("RPC failed: {0}", e.getStatus());
        }
    }

    public void clientSideStreamingGetStatisticsOfStocks() throws InterruptedException {
        StreamObserver<StockQuote> responseObserver = new StreamObserver<StockQuote>() {
            @Override
            public void onNext(StockQuote summary) {
                logInfo("RESPONSE, got stock statistics - Average Price: {0}, description: {1}", summary.getPrice(), summary.getDescription());
            }

            @Override
            public void onCompleted() {
                logInfo("Finished clientSideStreamingGetStatisticsOfStocks");
            }

            @Override
            public void onError(Throwable throwable) {
                logInfo("Error clientSideStreamingGetStatisticsOfStocks: " + throwable.getMessage());}
        };

        StreamObserver<Stock> requestObserver = nonBlockingStub.clientSideStreamingGetStatisticsOfStocks(responseObserver);
        try {
            for (Stock stock : stocks) {
                logInfo("REQUEST: {0}, {1}", stock.getTickerSymbol(), stock.getCompanyName());
                requestObserver.onNext(stock);
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        requestObserver.onCompleted();
    }
    public void bidirectionalStreamingGetListsStockQuotes() throws InterruptedException{
        StreamObserver<StockQuote> responseObserver = new StreamObserver<StockQuote>() {
            @Override
            public void onNext(StockQuote stockQuote) {
                logInfo("RESPONSE price#{0} : {1}, description:{2}", stockQuote.getOfferNumber(), stockQuote.getPrice(), stockQuote.getDescription());
            }

            @Override
            public void onCompleted() {
                logInfo("Finished bidirectionalStreamingGetListsStockQuotes");
            }
            @Override
            public void onError(Throwable throwable) {
                logInfo("Error bidirectionalStreamingGetListsStockQuotes: " + throwable.getMessage());
            }
        };

        StreamObserver<Stock> requestObserver = nonBlockingStub.bidirectionalStreamingGetListsStockQuotes(responseObserver);
        try {
            for (Stock stock : stocks) {
                logInfo("REQUEST: {0}, {1}", stock.getTickerSymbol(), stock.getCompanyName());
                requestObserver.onNext(stock);
                Thread.sleep(200);
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        requestObserver.onCompleted();
    }
    void logInfo(String message, Object... args) {
        logger.info(String.format(message, args));
    }
}
