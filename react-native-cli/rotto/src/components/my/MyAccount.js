import { useCallback, useEffect, useState } from 'react';
import { View, Text, StyleSheet, Image, Pressable, Alert } from 'react-native';
import { getAccountInfo } from '../../utils/accountApi';
import { MaterialIcons } from '@expo/vector-icons';
import CustomButton from '../common/CustomButton';
import Colors from '../../constants/Colors';
import { useFocusEffect } from '@react-navigation/native';
import { setFundingAccount, setTransactionMode } from '../../stores/mySlice';
import { useDispatch, useSelector } from 'react-redux';

const MyAccount = ({navigation, detail}) => {
  const dispatch = useDispatch();
  const fundingAccount = useSelector(state => state.myPageInfo.fundingAccount);
  const connectedAccount = useSelector(state => state.myPageInfo.connectedAccount);

  useFocusEffect(
    useCallback(() => {
      const getFundingAccount = async () => {
        const res = await getAccountInfo();
        //console.log(res)
        dispatch(setFundingAccount(res));
      };
      
      getFundingAccount();
    }, [])
  );

  return (
    <View style={[styles.container, detail && styles.border]}>
      {
        fundingAccount &&
        <>
          <View style={styles.topContainer}>
            <View style={styles.accountInfoContainer}>
              <Image style={styles.bankLogo} source={require("../../../assets/images/ssafyLogo.png")} resizeMode="stretch" />
              <Text style={styles.bankName}>싸피 은행</Text>
              <Text style={styles.accountNum}>{fundingAccount.accountNum}</Text>
            </View>
            {
              !detail &&
              <Pressable onPress={() => navigation.navigate('account')}>
                <MaterialIcons name="arrow-forward-ios" size={16} />
              </Pressable>
            }
          </View>
          <Text style={styles.balanceText}>{fundingAccount.accountBalance.toLocaleString('ko-KR')} 원</Text>
          <View style={[styles.buttonContainer, detail && { justifyContent: 'space-between' }]}>
            <CustomButton 
              style={{ width: detail ? '48%' : '16%', height: detail ? 30 : 24 }} 
              fontFamily='pretendard-medium'
              onPress={() => {
                if (connectedAccount) {
                  dispatch(setTransactionMode('charge'));
                  navigation.navigate('transaction');
                } else {
                  return Alert.alert('', '연결 계좌가 없습니다.', [
                    { text: '연결하기', onPress: () => navigation.navigate('connection'), style: 'destructive' },
                    { text: '확인', onPress: () => null, style: 'cancel' }
                  ])
                }
              }}
            >
              채우기
            </CustomButton>
            <CustomButton 
              style={{ width: detail ? '48%' : '16%', height: detail ? 30 : 24 }} 
              fontFamily='pretendard-medium' 
              btnColor='black'
              onPress={() => {
                if (connectedAccount) {
                  dispatch(setTransactionMode('send'));
                  navigation.navigate('transaction');
                } else {
                  return Alert.alert('', '연결 계좌가 없습니다.', [
                    { text: '연결하기', onPress: () => navigation.navigate('connection'), style: 'destructive' },
                    { text: '확인', onPress: () => null, style: 'cancel' }
                  ])
                }
              }}
            >
              보내기
            </CustomButton>
          </View>
        </>
      }
    </View>
  )
}

export default MyAccount;

const styles = StyleSheet.create({
  container: {
    width: '90%',
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 18,
    gap: 16
  },
  border: {
    borderColor: Colors.fontGray,
    borderWidth: 0.5
  },
  topContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    position: 'relative'
  },
  accountInfoContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6
  },
  bankLogo: {
    height: 20,
    width: 20
  },
  bankName: {
    fontSize: 16,
    fontFamily: 'pretendard-semiBold'
  },
  accountNum: {
    fontSize: 12,
    fontFamily: 'pretendard-regular'
  },
  balanceText: {
    fontSize: 20,
    fontFamily: 'pretendard-regular',
    textAlign: 'center'
  },
  buttonContainer: {
    flexDirection: 'row',
    gap: 6,
    justifyContent: 'flex-end'
  }
});