package cn.kuick.blockchain.web3.provider;

import cn.kuick.blockchain.web3.signer.PrivateKeySigner;
import cn.kuick.blockchain.web3.signer.Signer;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.math.BigInteger;

public class Web3jSignerProviderTest {
    public static void main(String[] args) throws Exception {
        String URL= "https://ropsten.infura.io";
        String toAddress = "0x54720E818DC0f94037b7dfb2D28c9401FC53BfEE";
        
        String fromAddress = "0xF4e8472b3a737F332fbA906845f7184c809aD370";
        String privateKey = "36bc1466c1a66095496b32b871bb1d2dde7b664d5db5d8ca1c562ce8a8509679";
        
        BigInteger gasPrice = new BigInteger("5000000000");
        BigInteger gasLimit = new BigInteger("21000");
        BigInteger value = new BigInteger("1000000000000000000");

        Signer signer = new PrivateKeySigner(privateKey);
        Web3jService web3jService = new Web3jSignerProvider(URL, signer);

        Web3j web3j = Web3j.build(web3jService);

        Transaction transaction = new Transaction(fromAddress,null,gasPrice,gasLimit,toAddress,value, null);

        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();

        if (ethSendTransaction == null) {
            throw new RuntimeException("No response.");
        }

        if(ethSendTransaction.getError() != null){
            throw new RuntimeException(ethSendTransaction.getError().getMessage());
        }

        System.out.println(ethSendTransaction.getTransactionHash());
    }
}
