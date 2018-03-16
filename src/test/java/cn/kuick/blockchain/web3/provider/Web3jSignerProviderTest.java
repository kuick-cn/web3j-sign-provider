package cn.kuick.blockchain.web3.provider;

import cn.kuick.blockchain.web3.provider.Web3jSignerProvider;
import cn.kuick.blockchain.web3.signer.PrivateKeySigner;
import cn.kuick.blockchain.web3.signer.Signer;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import rx.Subscriber;

import java.math.BigInteger;

public class Web3jSignerProviderTest {
    public static void main(String[] args) throws Exception {
        String URL= "https://ropsten.infura.io";
        String toAddress = "0x54720E818DC0f94037b7dfb2D28c9401FC53BfEE";
        
        String fromAddress = "0xF4e8472b3a737F332fbA906845f7184c809aD370";
        BigInteger privateKey = new BigInteger("36bc1466c1a66095496b32b871bb1d2dde7b664d5db5d8ca1c562ce8a8509679".replace("0x",""), 16);
        
        BigInteger gasPrice = new BigInteger("5000000000");
        BigInteger gasLimit = new BigInteger("21000");
        BigInteger value = new BigInteger("1000000000000000000"); //1000000000000000000
        String data = null;


        Signer signer = new PrivateKeySigner(toAddress, privateKey);
        Web3jService web3jService = new Web3jSignerProvider(URL, signer);

        Web3j web3j = Web3j.build(web3jService);

        Transaction transaction = new Transaction(fromAddress,null,gasPrice,gasLimit,toAddress,value,data);

        web3j.ethSendTransaction(transaction).observable().subscribe(new Subscriber<EthSendTransaction>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(EthSendTransaction ethSendTransaction) {
                String rawResponse = ethSendTransaction.getRawResponse();
                String result = ethSendTransaction.getResult();
                Response.Error error = ethSendTransaction.getError();
                String jsonrpc = ethSendTransaction.getJsonrpc();
                if (rawResponse != null) {
                    System.out.println("rawResponse:" + rawResponse);
                }
                if (result != null) {
                    System.out.println("result:" + result);
                }
                if (error != null) {
                    System.out.println("error:" + error.getMessage());
                }
                if (jsonrpc != null) {
                    System.out.println("jsonrpc:" + jsonrpc);
                }
            }
        });

    }
}
