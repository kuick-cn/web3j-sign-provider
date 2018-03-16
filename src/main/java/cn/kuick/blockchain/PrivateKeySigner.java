package cn.kuick.blockchain;

import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;
import org.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;

public class PrivateKeySigner implements Signer{


    private String toAddress;
    private BigInteger privateKey;


    public PrivateKeySigner(String toAddress, BigInteger privateKey) throws Exception {
        if (privateKey == null) {
            throw new Exception("privateKey is null");
        }
        if (toAddress == null) {
            throw new Exception("toAddress is null");
        }

        this.toAddress = toAddress;
        this.privateKey = privateKey;
    }

    @Override
    public String sign(Transaction transaction) throws Exception {
        String value;
        String nonce;
        String gasPrice;
        String gasLimit;
        String data;


        if (transaction.getValue().length() % 2 == 0) {
            value = transaction.getValue().replace("0x", "");
        } else {
            value = transaction.getValue().replace("0x", "0");
        }
        if (transaction.getNonce().length() % 2 == 0) {
            nonce = transaction.getNonce().replace("0x","");
        } else {
            nonce = transaction.getNonce().replace("0x","0");
        }
        if (transaction.getGasPrice().length() % 2 == 0) {
            gasPrice = transaction.getGasPrice().replace("0x","");
        } else {
            gasPrice = transaction.getGasPrice().replace("0x","0");
        }
        if (transaction.getGas().length() % 2 == 0) {
            gasLimit = transaction.getGas().replace("0x","");
        } else {
            gasLimit = transaction.getGas().replace("0x","0");
        }

        if (transaction.getData() == null) {
            data = "";
        } else if (transaction.getData().length() % 2 == 0) {
            data = transaction.getData().replace("0x","");
        } else {
            data = transaction.getData().replace("0x","0");
        }

        if (gasLimit == null) {
            gasLimit = "21000"; //min
        }
        if (gasPrice == null) {
            gasPrice = "5000000000"; //min
        }
        if (value == null) {
            throw new Exception("Value is null");
        }

        byte[] receiveAddress = Hex.decode(toAddress.replace("0x", ""));
        byte[] gasPriceByte = Hex.decode(gasPrice.replace("0x", ""));
        byte[] gasLimitByte = Hex.decode(gasLimit.replace("0x", ""));
        byte[] valueByte = Hex.decode(value);
        byte[] dataByte = Hex.decode(data.replace("0x", ""));
        byte[] nonceByte = Hex.decode(nonce.replace("0x", ""));

        org.ethereum.core.Transaction transactionObj = new org.ethereum.core.Transaction(nonceByte, gasPriceByte, gasLimitByte, receiveAddress, valueByte, dataByte);
        transactionObj.sign(ECKey.fromPrivate(privateKey));

        return "0x" + Hex.toHexString(transactionObj.getEncoded());
    }

}
