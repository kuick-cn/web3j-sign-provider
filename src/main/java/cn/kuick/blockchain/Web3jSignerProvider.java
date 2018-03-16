package cn.kuick.blockchain;

import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Async;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class Web3jSignerProvider implements Web3jService,org.web3j.protocol.Web3jService{


    private HttpService service;
    private Signer signer;

    public Web3jSignerProvider(String url, Signer signer) throws Exception {
        if (url == null) {
            throw new Exception("url is null");
        }
        if (signer == null) {
            throw new Exception("Please initialization signer");
        }
        this.service = new HttpService(url);
        this.signer = signer;
    }

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        if (request.getMethod().equals("eth_sendTransaction")) {
            Transaction transactoin = (Transaction) request.getParams().get(0);
            try {
                //Reconstruction Requset For Find Nonce
                Request requestCount = new Request<>(
                        "eth_getTransactionCount",
                        Arrays.asList(transactoin.getFrom(), DefaultBlockParameterName.LATEST.getValue()),
                        service,
                        EthGetTransactionCount.class);

                EthGetTransactionCount count = (EthGetTransactionCount) requestCount.send();
                BigInteger nonce = count.getTransactionCount();

                //Reconstruction Transaction For Signer
                Transaction signerTransaction = new Transaction(
                        transactoin.getFrom(),
                        nonce,
                        new BigInteger(transactoin.getGasPrice().replace("0x",""), 16),
                        new BigInteger(transactoin.getGas().replace("0x",""), 16),
                        transactoin.getTo(),
                        new BigInteger(transactoin.getValue().replace("0x",""), 16),
                        transactoin.getData()
                );

                //Signer
                String newTrans = signer.sign(signerTransaction);

                //Reconstruction Requset For Send
                Request requestRaw = new Request<>(
                        "eth_sendRawTransaction",
                        Collections.singletonList(newTrans),
                        service,
                        EthSendTransaction.class);

                //sendRawTransaction
                return (T) requestRaw.send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }  else {
           return service.send(request, responseType);
        }
    }

    @Override
    public <T extends Response> CompletableFuture<T> sendAsync(Request request, Class<T> responseType) {
        if (request.getMethod().equals("eth_sendTransaction")) {
            return Async.run(() -> send(request, responseType));
        } else {
            return service.sendAsync(request, responseType);
        }
    }
}
