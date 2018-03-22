package cn.kuick.blockchain.web3.signer;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class PrivateKeySigner implements Signer {
    private String privateKey;

    public PrivateKeySigner(String privateKey) throws Exception {
        if (privateKey == null) {
            throw new Exception("privateKey is null");
        }

        this.privateKey = privateKey;
    }

    @Override
    public String sign(Transaction transaction) throws Exception {
        String value = transaction.getValue();
        String nonce = transaction.getNonce();
        String gasPrice = transaction.getGasPrice();
        String gasLimit = transaction.getGas();
        String data = transaction.getData();
        String toAddress = transaction.getTo();

        if (toAddress == null) {
            throw new Exception("toAddress is null");
        }

        if (data == null) {
            data = "";
        }

        BigInteger gasPriceInt = new BigInteger(gasPrice.replace("0x", ""),16);
        BigInteger gasLimitInt = new BigInteger(gasLimit.replace("0x", ""),16);
        BigInteger valueInt = new BigInteger(value.replace("0x", ""),16);
        BigInteger nonceInt = new BigInteger(nonce.replace("0x", ""),16);

        //CreateTransaction
        RawTransaction rawTransaction  = RawTransaction.createTransaction(nonceInt,gasPriceInt,gasLimitInt,toAddress,valueInt,data);
        //Create Credentials
        Credentials credentials = Credentials.create(privateKey);
        //Signer
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }

}
