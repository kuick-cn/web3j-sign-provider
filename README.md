# web3j sign provider

A simple web3 standard provider that signs sendTransaction payloads.

## Install

```
repositories {
    maven {
        url "https://dl.bintray.com/ethereum/maven/"
    }
    maven {
        url "http://repo.kuick.cn/artifactory/libs-release-local"
    }

    jcenter()
    mavenCentral()
}

dependencies {
    compile(group: 'cn.kuick.blockchain', name: 'web3j-sign-provider', version: '1.1.0')
}
```

## Usage

```java
import cn.kuick.blockchain.web3.signer.Signer;
import cn.kuick.blockchain.web3.signer.PrivateKeySigner;
import cn.kuick.blockchain.web3.provider.Web3jSignerProvider;

//...

//initialization
Signer signer = new PrivateKeySigner(privateKey);
Web3jService web3jService = new Web3jSignerProvider(URL, signer);

//web3j method
Web3j web3j = Web3j.build(web3jService);

/**
* create transaction
* fromAddress Not Null
* nonce Allow empty
* gasPrice default = 5000000000
* gasLimit default = 21000
* receiveAddress Not Null
* value Not Null 1 eth = 1*10^18 value
* data Allow empty
*/
Transaction transaction = new Transaction(fromAddress,nonce,gasPrice,gasLimit,receiveAddress,value,data);

//use web3j
EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();

if (ethSendTransaction == null) {
    throw new RuntimeException("No response.");
}

if(ethSendTransaction.getError() != null){
    throw new RuntimeException(ethSendTransaction.getError().getMessage());
}

// poll for transaction response via org.web3j.protocol.Web3j.ethGetTransactionReceipt(<txHash>)
String transactionHash = ethSendTransaction.getTransactionHash();
```
