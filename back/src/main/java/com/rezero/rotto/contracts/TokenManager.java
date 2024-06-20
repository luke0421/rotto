package com.rezero.rotto.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.3.
 */
@SuppressWarnings("rawtypes")
public class TokenManager extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b5061002d61002261003260201b60201c565b61003a60201b60201c565b6100fe565b600033905090565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b6113608061010d6000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c80638da5cb5b116100665780638da5cb5b1461011d578063d37fc7e61461013b578063d67069fe14610157578063ed8c24b314610173578063f2fde38b1461018f5761009e565b806339e899ee146100a357806359b910d6146100bf57806360a20c49146100db57806365d9b0dc146100f7578063715018a614610113575b600080fd5b6100bd60048036038101906100b89190610d09565b6101ab565b005b6100d960048036038101906100d49190610d09565b610260565b005b6100f560048036038101906100f09190610d09565b61031d565b005b610111600480360381019061010c9190610e9d565b610509565b005b61011b6105a8565b005b6101256105bc565b6040516101329190610eec565b60405180910390f35b61015560048036038101906101509190610f07565b6105e5565b005b610171600480360381019061016c9190610d09565b6107d4565b005b61018d60048036038101906101889190610f47565b6108dd565b005b6101a960048036038101906101a49190610d09565b610acf565b005b80600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff160361021b576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016102129061101d565b60405180910390fd5b81600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050565b80600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16036102d0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016102c79061101d565b60405180910390fd5b6102d8610b52565b81600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050565b806000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639cce37c6836040518263ffffffff1660e01b815260040161037b9190610eec565b602060405180830381865afa158015610398573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906103bc9190611075565b9050806103fe576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016103f590611114565b60405180910390fd5b82600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff160361046e576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016104659061101d565b60405180910390fd5b610476610b52565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166360a20c49856040518263ffffffff1660e01b81526004016104d19190610eec565b600060405180830381600087803b1580156104eb57600080fd5b505af11580156104ff573d6000803e3d6000fd5b5050505050505050565b610511610b52565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16631b2ef1ca8360000151836040518363ffffffff1660e01b8152600401610572929190611143565b600060405180830381600087803b15801561058c57600080fd5b505af11580156105a0573d6000803e3d6000fd5b505050505050565b6105b0610b52565b6105ba6000610bd0565b565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b806000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639cce37c6836040518263ffffffff1660e01b81526004016106439190610eec565b602060405180830381865afa158015610660573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906106849190611075565b9050806106c6576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106bd90611114565b60405180910390fd5b82600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610736576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161072d9061101d565b60405180910390fd5b61073e610b52565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663fcd3533c86866040518363ffffffff1660e01b815260040161079b92919061116c565b600060405180830381600087803b1580156107b557600080fd5b505af11580156107c9573d6000803e3d6000fd5b505050505050505050565b80600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610844576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161083b9061101d565b60405180910390fd5b61084c610b52565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663d67069fe836040518263ffffffff1660e01b81526004016108a79190610eec565b600060405180830381600087803b1580156108c157600080fd5b505af11580156108d5573d6000803e3d6000fd5b505050505050565b816000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16639cce37c6836040518263ffffffff1660e01b815260040161093b9190610eec565b602060405180830381865afa158015610958573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061097c9190611075565b9050806109be576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016109b590611114565b60405180910390fd5b83600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610a2e576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a259061101d565b60405180910390fd5b610a36610b52565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166399eed1eb8787876040518463ffffffff1660e01b8152600401610a95939291906111f5565b600060405180830381600087803b158015610aaf57600080fd5b505af1158015610ac3573d6000803e3d6000fd5b50505050505050505050565b610ad7610b52565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610b46576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b3d9061129e565b60405180910390fd5b610b4f81610bd0565b50565b610b5a610c94565b73ffffffffffffffffffffffffffffffffffffffff16610b786105bc565b73ffffffffffffffffffffffffffffffffffffffff1614610bce576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610bc59061130a565b60405180910390fd5b565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b600033905090565b6000604051905090565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610cd682610cab565b9050919050565b610ce681610ccb565b8114610cf157600080fd5b50565b600081359050610d0381610cdd565b92915050565b600060208284031215610d1f57610d1e610ca6565b5b6000610d2d84828501610cf4565b91505092915050565b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b610d8482610d3b565b810181811067ffffffffffffffff82111715610da357610da2610d4c565b5b80604052505050565b6000610db6610c9c565b9050610dc28282610d7b565b919050565b6000819050919050565b610dda81610dc7565b8114610de557600080fd5b50565b600081359050610df781610dd1565b92915050565b600063ffffffff82169050919050565b610e1681610dfd565b8114610e2157600080fd5b50565b600081359050610e3381610e0d565b92915050565b600060608284031215610e4f57610e4e610d36565b5b610e596060610dac565b90506000610e6984828501610de8565b6000830152506020610e7d84828501610de8565b6020830152506040610e9184828501610e24565b60408301525092915050565b60008060808385031215610eb457610eb3610ca6565b5b6000610ec285828601610e39565b9250506060610ed385828601610de8565b9150509250929050565b610ee681610ccb565b82525050565b6000602082019050610f016000830184610edd565b92915050565b60008060408385031215610f1e57610f1d610ca6565b5b6000610f2c85828601610de8565b9250506020610f3d85828601610cf4565b9150509250929050565b600080600060a08486031215610f6057610f5f610ca6565b5b6000610f6e86828701610e39565b9350506060610f7f86828701610cf4565b9250506080610f9086828701610de8565b9150509250925092565b600082825260208201905092915050565b7fec98acebb094eba5b4eca78020ec958aec9d8020eca3bcec868cec9e85eb8b8860008201527feb8ba42e00000000000000000000000000000000000000000000000000000000602082015250565b6000611007602483610f9a565b915061101282610fab565b604082019050919050565b6000602082019050818103600083015261103681610ffa565b9050919050565b60008115159050919050565b6110528161103d565b811461105d57600080fd5b50565b60008151905061106f81611049565b92915050565b60006020828403121561108b5761108a610ca6565b5b600061109984828501611060565b91505092915050565b7fed9788ec9aa9eb9098eca78020ec958aeb8a9420eca780eab091ec9e85eb8b8860008201527feb8ba42e00000000000000000000000000000000000000000000000000000000602082015250565b60006110fe602483610f9a565b9150611109826110a2565b604082019050919050565b6000602082019050818103600083015261112d816110f1565b9050919050565b61113d81610dc7565b82525050565b60006040820190506111586000830185611134565b6111656020830184611134565b9392505050565b60006040820190506111816000830185611134565b61118e6020830184610edd565b9392505050565b61119e81610dc7565b82525050565b6111ad81610dfd565b82525050565b6060820160008201516111c96000850182611195565b5060208201516111dc6020850182611195565b5060408201516111ef60408501826111a4565b50505050565b600060a08201905061120a60008301866111b3565b6112176060830185610edd565b6112246080830184611134565b949350505050565b7f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160008201527f6464726573730000000000000000000000000000000000000000000000000000602082015250565b6000611288602683610f9a565b91506112938261122c565b604082019050919050565b600060208201905081810360008301526112b78161127b565b9050919050565b7f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e6572600082015250565b60006112f4602083610f9a565b91506112ff826112be565b602082019050919050565b60006020820190508181036000830152611323816112e7565b905091905056fea2646970667358221220cb24522ed4a84ff77dd9d52721cbf393fc66f0569418d6f44ad3ca1eaa21f76c64736f6c63430008130033";

    private static String librariesLinkedBinary;

    public static final String FUNC_BURNTOKEN = "burnToken";

    public static final String FUNC_CHECKWHITELIST = "checkWhiteList";

    public static final String FUNC_CREATETOKEN = "createToken";

    public static final String FUNC_DELETETOKEN = "deleteToken";

    public static final String FUNC_DISTRIBUTETOKEN = "distributeToken";

    public static final String FUNC_INSERTLIST = "insertList";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_REMOVELIST = "removeList";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETSTORAGEADDRESS = "setStorageAddress";

    public static final String FUNC_SETWHITELIST = "setWhiteList";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event RESULTCHECK_EVENT = new Event("resultCheck", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
    ;

    @Deprecated
    protected TokenManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TokenManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TokenManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TokenManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<ResultCheckEventResponse> getResultCheckEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(RESULTCHECK_EVENT, transactionReceipt);
        ArrayList<ResultCheckEventResponse> responses = new ArrayList<ResultCheckEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ResultCheckEventResponse typedResponse = new ResultCheckEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.param0 = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ResultCheckEventResponse getResultCheckEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(RESULTCHECK_EVENT, log);
        ResultCheckEventResponse typedResponse = new ResultCheckEventResponse();
        typedResponse.log = log;
        typedResponse.param0 = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ResultCheckEventResponse> resultCheckEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getResultCheckEventFromLog(log));
    }

    public Flowable<ResultCheckEventResponse> resultCheckEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RESULTCHECK_EVENT));
        return resultCheckEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> burnToken(BigInteger code) {
        final Function function = new Function(
                FUNC_BURNTOKEN, 
                Arrays.<Type>asList(new Uint256(code)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> checkWhiteList(String _wallet) {
        final Function function = new Function(
                FUNC_CHECKWHITELIST, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createToken(Subscription subscription, BigInteger amount) {
        final Function function = new Function(
                FUNC_CREATETOKEN, 
                Arrays.<Type>asList(subscription, 
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteToken(BigInteger code, String _wallet) {
        final Function function = new Function(
                FUNC_DELETETOKEN, 
                Arrays.<Type>asList(new Uint256(code),
                new Address(160, _wallet)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> distributeToken(Subscription subscription, String _wallet, BigInteger amount) {
        final Function function = new Function(
                FUNC_DISTRIBUTETOKEN, 
                Arrays.<Type>asList(subscription, 
                new Address(160, _wallet),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> insertList(String _wallet) {
        final Function function = new Function(
                FUNC_INSERTLIST, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeList(String _wallet) {
        final Function function = new Function(
                FUNC_REMOVELIST, 
                Arrays.<Type>asList(new Address(160, _wallet)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setStorageAddress(String _addr) {
        final Function function = new Function(
                FUNC_SETSTORAGEADDRESS, 
                Arrays.<Type>asList(new Address(160, _addr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setWhiteList(String _addr) {
        final Function function = new Function(
                FUNC_SETWHITELIST, 
                Arrays.<Type>asList(new Address(160, _addr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static TokenManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TokenManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TokenManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TokenManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TokenManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TokenManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TokenManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TokenManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TokenManager> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TokenManager.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<TokenManager> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TokenManager.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<TokenManager> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TokenManager.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<TokenManager> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TokenManager.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class Subscription extends StaticStruct {
        public BigInteger code;

        public BigInteger confirm_price;

        public BigInteger limit_num;

        public Subscription(BigInteger code, BigInteger confirm_price, BigInteger limit_num) {
            super(new Uint256(code),
                    new Uint256(confirm_price),
                    new Uint32(limit_num));
            this.code = code;
            this.confirm_price = confirm_price;
            this.limit_num = limit_num;
        }

        public Subscription(Uint256 code, Uint256 confirm_price, Uint32 limit_num) {
            super(code, confirm_price, limit_num);
            this.code = code.getValue();
            this.confirm_price = confirm_price.getValue();
            this.limit_num = limit_num.getValue();
        }
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class ResultCheckEventResponse extends BaseEventResponse {
        public Boolean param0;
    }
}
