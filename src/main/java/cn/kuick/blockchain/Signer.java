package cn.kuick.blockchain;

import org.web3j.protocol.core.methods.request.Transaction;

public interface Signer {

    String sign(Transaction transaction) throws Exception;

}
