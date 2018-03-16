package cn.kuick.blockchain.web3.signer;

import org.web3j.protocol.core.methods.request.Transaction;

public interface Signer {
    String sign(Transaction transaction) throws Exception;
}
