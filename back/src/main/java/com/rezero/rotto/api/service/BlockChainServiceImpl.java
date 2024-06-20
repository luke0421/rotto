package com.rezero.rotto.api.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.StaticGasProvider;

import com.google.api.Http;
import com.rezero.rotto.contracts.TokenManager;
import com.rezero.rotto.dto.request.CreateTokenRequest;
import com.rezero.rotto.dto.request.DistributeRequest;
import com.rezero.rotto.dto.request.PayTokensRequest;
import com.rezero.rotto.dto.request.RefundsTokenRequest;
import com.rezero.rotto.entity.Subscription;
import com.rezero.rotto.entity.TradeHistory;
import com.rezero.rotto.entity.User;
import com.rezero.rotto.repository.SubscriptionRepository;
import com.rezero.rotto.repository.TradeHistoryRepository;
import com.rezero.rotto.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockChainServiceImpl implements BlockChainService{

	@Autowired
	private final Web3j web3j;

	@Autowired
	private final Credentials credentials;

	private final SubscriptionRepository subscriptionRepository;
	private final UserRepository userRepository;
	private final TradeHistoryRepository tradeHistoryRepository;

	private TokenManager tokenManager = null;

	@Value("${CHAIN_TOKEN_MANAGER}")
	private String tokenManagerAddress;

	private final Logger logger = LoggerFactory.getLogger(BlockChainServiceImpl.class);
	private static final BigInteger GAS_LIMIT = BigInteger.valueOf(9_007_199_254_740_991L);
	private static final BigInteger GAS_PRICE = BigInteger.ZERO;

	@Override
	public ResponseEntity<?> createToken(CreateTokenRequest request) {
		if(tokenManager == null) initContract();
		Subscription subscription = subscriptionRepository.findBySubscriptionCode(request.getCode());
		if(subscription == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청하신 청약을 찾을 수 없습니다.");

		TokenManager.Subscription requestSubscription = changeVariable(subscription);
		BigInteger amount = BigInteger.valueOf(request.getAmount());

		logger.info("ROTTO 생성 이전,  code: " + requestSubscription.code + ", amount: " + amount);

		try {
			TransactionReceipt transactionReceipt = tokenManager.createToken(requestSubscription, amount).send();
			if(transactionReceipt.isStatusOK()) {
				logger.info("ROTTO 생성 완료. amount: " + amount);
				return ResponseEntity.status(HttpStatus.CREATED).body("ROTTO 생성 완료");
			}
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ROTTO 생성 실패");
		} catch (Exception ex){
			Throwable cause = ex.getCause();
			if(cause instanceof TransactionException){
					String revertReason = getRevertReason((TransactionException)cause);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(revertReason);
			}
			String errorMessage = (cause != null ? cause.getMessage() : ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}


	@Override
	public ResponseEntity<?> distributeTokens(DistributeRequest request) {
		if(tokenManager == null) initContract();
		User user = userRepository.findByUserCode(request.getUserCode());
		if(request.getSubscription() == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청하신 청약을 찾을 수 없습니다.");
		else if(request.getAmount() > request.getSubscription().getLimitNum())
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("요청하신 수량이 1인당 가질 수 있는 개수를 초과하였습니다.");
		else if(user == null || !WalletUtils.isValidAddress(user.getBcAddress())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않는 주소입니다.");
		}
		TokenManager.Subscription requestSubscription = changeVariable(request.getSubscription());
		BigInteger amount = BigInteger.valueOf(request.getAmount());

		ResponseEntity<?> check = checkWhiteList(user.getBcAddress());
		if(check.getStatusCode() == HttpStatus.OK){
			boolean isWhitelisted = (boolean)check.getBody();
			if(!isWhitelisted)  InsertWhiteList(user.getBcAddress());
		}
		else ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("작업 중 오류가 발생하였습니다.");

		try {
			TransactionReceipt transactionReceipt = tokenManager.distributeToken(requestSubscription,
				user.getBcAddress(), amount).send();

			if(transactionReceipt.isStatusOK()){
				TradeHistory history = new TradeHistory();
				history.setSubscriptionCode(request.getSubscription().getSubscriptionCode());
				history.setBcAddress(user.getBcAddress());
				history.setUserCode(user.getUserCode());
				history.setRefund(0);
				history.setTradeNum(request.getAmount());
				history.setTokenPrice(request.getSubscription().getConfirmPrice());

				tradeHistoryRepository.save(history);

				return ResponseEntity.status(HttpStatus.OK).body("ROTTO 발급 완료");
			}
			else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ROTTO 발급 실패");
		} catch(Exception ex){
			Throwable cause = ex.getCause();
			if(cause instanceof TransactionException){
				String revertReason = getRevertReason((TransactionException)cause);
				logger.info("[distributeToken] revertReason: " + revertReason);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(revertReason);
			}
			String errorMessage = (cause != null ? cause.getMessage() : ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}

	@Override
	public ResponseEntity<?> distributeToken(PayTokensRequest request) {
		if(tokenManager == null) initContract();
		Subscription subscription = subscriptionRepository.findBySubscriptionCode(request.getCode());
		logger.info("[distributeToken] getAddress: " + request.getCode());
		logger.info("[distributeToken] getAddress: " + request.getAddress());
		logger.info("[distributeToken] getAddress: " + request.getAmount());
		Optional<User> user = userRepository.findByBcAddress(request.getAddress());
		if(subscription == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청하신 청약을 찾을 수 없습니다.");
		else if(request.getAmount() > subscription.getLimitNum())
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("요청하신 수량이 1인당 가질 수 있는 개수를 초과하였습니다.");
		else if(user.isEmpty() || !WalletUtils.isValidAddress(request.getAddress())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않는 주소입니다.");
		}
		TokenManager.Subscription requestSubscription = changeVariable(subscription);
		BigInteger amount = BigInteger.valueOf(request.getAmount());

		ResponseEntity<?> check = checkWhiteList(request.getAddress());
		if(check.getStatusCode() == HttpStatus.OK){
			boolean isWhitelisted = (boolean)check.getBody();
			if(!isWhitelisted)  InsertWhiteList(request.getAddress());
		}
		else ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("작업 중 오류가 발생하였습니다.");

		try {
			TransactionReceipt transactionReceipt = tokenManager.distributeToken(requestSubscription,
				request.getAddress(), amount).send();

			if(transactionReceipt.isStatusOK()){
				TradeHistory history = new TradeHistory();
				history.setSubscriptionCode(subscription.getSubscriptionCode());
				history.setBcAddress(request.getAddress());
				history.setUserCode(user.get().getUserCode());
				history.setRefund(0);
				history.setTradeNum(request.getAmount());
				history.setTokenPrice(subscription.getConfirmPrice());

				logger.info("[distributeToken] history: " + history);

				tradeHistoryRepository.save(history);

				return ResponseEntity.status(HttpStatus.OK).body("ROTTO 발급 완료");
			}
			else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ROTTO 발급 실패");
		} catch(Exception ex){
			Throwable cause = ex.getCause();
			if(cause instanceof TransactionException){
				String revertReason = getRevertReason((TransactionException)cause);
				logger.info("[distributeToken] revertReason: " + revertReason);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(revertReason);
			}
			String errorMessage = (cause != null ? cause.getMessage() : ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}

	@Override
	public ResponseEntity<?> RefundsToken(RefundsTokenRequest request) {
		if(tokenManager == null) initContract();

		Subscription subscription = subscriptionRepository.findBySubscriptionCode(request.getCode());
		if(subscription == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청하신 청약을 찾을 수 없습니다.");

		BigInteger code = BigInteger.valueOf(subscription.getSubscriptionCode());

		ResponseEntity<?> check = checkWhiteList(request.getAddress());
		if(check.getStatusCode() == HttpStatus.OK){
			boolean isWhitelisted = (boolean)check.getBody();
			if(!isWhitelisted)  InsertWhiteList(request.getAddress());
		}
		else ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("작업 중 오류가 발생하였습니다.");

		CompletableFuture<TransactionReceipt> transactionReceiptFuture
			= tokenManager.deleteToken(code, request.getAddress()).sendAsync();
		try {
			TransactionReceipt transactionReceipt = transactionReceiptFuture.join();
			if(transactionReceipt.isStatusOK())
				return ResponseEntity.status(HttpStatus.OK).body("ROTTO 환급 완료");
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ROTTO 환급 실패");
		} catch (Exception ex){
			Throwable cause = ex.getCause();
			if(cause instanceof TransactionException){
				String revertReason = getRevertReason((TransactionException)cause);
				logger.info("[RefundsToken] revertReason: " + revertReason);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(revertReason);
			}
			String errorMessage = (cause != null ? cause.getMessage() : ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}

	@Override
	public ResponseEntity<?> InsertWhiteList(String wallet){
		if(tokenManager == null) initContract();

		try {
			TransactionReceipt transactionReceipt = tokenManager.insertList(wallet).send();
			if(transactionReceipt.isStatusOK()){
				return ResponseEntity.ok().body("list 추가 작업 완료");
			}
			else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("list 추가 작업 실패");
		} catch (Exception ex){
			Throwable cause = ex.getCause();
			if(cause instanceof TransactionException){
				String revertReason = getRevertReason((TransactionException)cause);
				logger.info("[InsertWhiteList] revertReason: " + revertReason);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(revertReason);
			}
			String errorMessage = (cause != null ? cause.getMessage() : ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}

	@Override
	public ResponseEntity<?> RemoveWhiteList(String wallet){
		if(tokenManager == null) initContract();
		CompletableFuture<TransactionReceipt> transactionReceiptFuture = tokenManager.removeList(wallet).sendAsync();

		try{
			TransactionReceipt transactionReceipt = transactionReceiptFuture.join();
			if(transactionReceipt.isStatusOK())
				return ResponseEntity.ok().body("list 제거 작업 완료");
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("list 제거 작업 실패");
		} catch (Exception ex) {
			Throwable cause = ex.getCause();
			if(cause instanceof TransactionException){
				String revertReason = getRevertReason((TransactionException)cause);
				logger.info("[RemoveWhiteList] revertReason: " + revertReason);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(revertReason);
			}
			String errorMessage = (cause != null ? cause.getMessage() : ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}

	@Override
	public ResponseEntity<?> checkWhiteList(String address) {
		if(tokenManager == null) initContract();
		CompletableFuture<TransactionReceipt> transactionReceiptFuture = tokenManager.checkWhiteList(address).sendAsync();

		try{
			TransactionReceipt transactionReceipt = transactionReceiptFuture.join();
			if(transactionReceipt.isStatusOK()){
				List<Log> logs = transactionReceipt.getLogs();
				boolean result = false;
				for(Log log : logs){
					String eventSignature = EventEncoder.encode(tokenManager.RESULTCHECK_EVENT);
					if(log.getTopics().get(0).equals(eventSignature)){
						List<Type> results = FunctionReturnDecoder.decode(log.getData(), tokenManager.RESULTCHECK_EVENT.getParameters());
						result = (Boolean) results.get(0).getValue();
						break;
					}
				}
				return ResponseEntity.ok().body(result);
			}
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("list 확인 실패");
		} catch (Exception ex) {
			Throwable cause = ex.getCause();
			if(cause instanceof TransactionException){
				String revertReason = getRevertReason((TransactionException)cause);
				logger.info("[RemoveWhiteList] revertReason: " + revertReason);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(revertReason);
			}
			String errorMessage = (cause != null ? cause.getMessage() : ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}

	@Override
	public ResponseEntity<?> burnToken(Subscription subscription){
		if(tokenManager == null) initContract();

		try {
			BigInteger code = BigInteger.valueOf(subscription.getSubscriptionCode());
			TransactionReceipt transactionReceipt = tokenManager.burnToken(code).send();
			if(transactionReceipt.isStatusOK()) return ResponseEntity.ok().body("신청률 미달 청약 ROTTO burn 완료");
			else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신청률 미달 청약 ROTTO burn 실패");

		} catch (Exception ex){
			Throwable cause = ex.getCause();
			if(cause instanceof TransactionException){
				String revertReason = getRevertReason((TransactionException)cause);
				logger.info("[RemoveWhiteList] revertReason: " + revertReason);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(revertReason);
			}
			String errorMessage = (cause != null ? cause.getMessage() : ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
	}

	private TokenManager.Subscription changeVariable(Subscription subscription){
		BigInteger code = BigInteger.valueOf(subscription.getSubscriptionCode());
		BigInteger confirm_price = BigInteger.valueOf(subscription.getConfirmPrice());
		BigInteger limit_num = BigInteger.valueOf(subscription.getLimitNum());

		return new TokenManager.Subscription(code, confirm_price, limit_num);
	}

	private String decodeRevertMessage(String hexEncoded) {
		String errorMethodId = "0x08c379a0";
		logger.info("[decodeRevertMessage] hexEncoded: " + hexEncoded);
		if(hexEncoded.startsWith(errorMethodId)){
			return hexToASCII(hexEncoded.substring(132).trim());
		}
		return "Transaction failed";
	}

	private String hexToASCII(String hexStr){
		HexFormat hexFormat = HexFormat.of();

		// 16진수 문자열을 바이트 배열로 변환
		byte[] bytes = hexFormat.parseHex(hexStr);

		// 바이트 배열을 UTF-8 문자열로 디코딩
		String decodedString = new String(bytes, StandardCharsets.UTF_8);

		return decodedString.replace("\u0000", "").substring(1);
	}

	private String getRevertReason(TransactionException e)  {
		if(e.getTransactionReceipt().isPresent()){
			TransactionReceipt receipt = e.getTransactionReceipt().get();
			String revertReason = receipt.getRevertReason();
			logger.info("[getRevertReason] revertReason: " + revertReason);
			if(revertReason != null && !revertReason.isEmpty()){
				return decodeRevertMessage(revertReason);
			}
		}
		return "No revert reason provided.";
	}

	private void initContract() {
		this.tokenManager = TokenManager.load(tokenManagerAddress, web3j, credentials, new StaticGasProvider(GAS_PRICE, GAS_LIMIT));
	}
}
