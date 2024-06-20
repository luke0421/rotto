import { Text, View, StyleSheet, Pressable, Button } from "react-native";
import { useAccount, useBalance, useDisconnect } from "wagmi";
import { Ionicons } from '@expo/vector-icons';
import { useEffect, useState } from "react";
import Colors from "../../constants/Colors";
import { useDispatch, useSelector } from "react-redux";
import { setDisconnectModal } from "../../stores/mySlice";
import { useWeb3Modal } from "@web3modal/wagmi-react-native";
import { updateWalletAddress } from "../../utils/userApi";
import DisconnectModal from "./DisconnectModal";
import { CONTRACT_ADDRESS } from "@env"
import { CHAIN_ID } from "@env"

const MyWallet = () => {
  const dispatch = useDispatch();

  const { open } = useWeb3Modal();
  const { disconnect } = useDisconnect();

  const { address, isConnected, isDisconnected } = useAccount();
  
  const tokenAddress = CONTRACT_ADDRESS;

  const { data, isError, isLoading } = useBalance({
    address,
    chainId: CHAIN_ID,
    token: tokenAddress,
    watch: true,
    onSuccess: () => {
      //console.log(data);
    },
  })
  
  const disconnectWallet = () => {
    disconnect();
    dispatch(setDisconnectModal(false));
  };
  
  useEffect(() => {
    //console.log("주소", address)
    address && updateWalletAddress({ wallet : address })
    //console.log("토큰 조회 데이터", data)    
  }, [data])

  return (
    <View style={styles.container}>
      {
        isConnected ? 
        <>
          <View style={styles.topContainer}>
            <View style={styles.addressContainer}>
              <Ionicons name="wallet-outline" size={16} />
              <Text style={styles.addressText} numberOfLines={1} ellipsizeMode="tail">{address}</Text>
            </View>
            <Pressable onPress={() => dispatch(setDisconnectModal(true))}>
              <Ionicons name="ellipsis-vertical-outline" size={16} />
            </Pressable>
          </View>
          <View style={styles.balanceContainer}>
            <Text style={styles.balanceText}>{data?.formatted}</Text>
            <Text style={styles.symbolText}>{data?.symbol}</Text>
          </View>
          <DisconnectModal onDisconnect={disconnectWallet} />
        </>
        :
        <Pressable onPress={() => open()}>
          <Text style={styles.connectText}>+ 전자 지갑 연동</Text>
        </Pressable>
      }
    </View>
  );
};

export default MyWallet;

const styles = StyleSheet.create({
  container: {
    width: '90%',
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 18,
    gap: 16,
    minHeight: 100,
    justifyContent: 'center',
    alignItems: 'center'
  },
  topContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 16,
    position: 'relative'
  },
  addressContainer: {
    flexDirection: 'row',
    gap: 4,
    alignItems: 'center',
    flex: 1
  },
  addressText: { 
    fontSize: 12,
    fontFamily: 'pretendard-regular',
    flex: 1
  },
  balanceContainer: {
    flexDirection: 'row',
    gap: 4,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 8
  },
  balanceText: {
    fontSize: 20,
    fontFamily: 'pretendard-regular'
  },
  symbolText: {
    fontSize: 16,
    fontFamily: 'pretendard-regular'
  },
  modal: {
    position: 'absolute',
    paddingVertical: 4,
    paddingHorizontal: 6,
    backgroundColor: 'white',
    borderColor: Colors.fontGray,
    borderWidth: 0.5,
    borderRadius: 5,
    right: 0,
    bottom: -32,
    width: '30%'
  },
  modalMenuText: {
    fontSize: 12,
    color: Colors.fontGray,
    textAlign: 'center'
  },
  connectText: {
    fontSize: 16,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray,
    textAlign: 'center'
  }
})