import { View, Text, StyleSheet, Alert, Image } from 'react-native';
import DetailTopBar from '../../components/common/DetailTopBar';
import { useEffect, useState } from 'react';
import { chargeAccount as charge, sendMoney as send } from '../../utils/accountApi';
import { useDispatch, useSelector } from 'react-redux';
import { setTransactionMode } from '../../stores/mySlice';
import CustomButton from '../../components/common/CustomButton';
import LottieView from 'lottie-react-native';

const TransactionResultScreen = ({navigation, route}) => {
  const transactionBalance = route.params.amount;
  const transactionMode = useSelector(state => state.myPageInfo.transactionMode);
  const dispatch = useDispatch();

  const [isLoading, setIsLoading] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);

  useEffect(() => {
    const chargeAccount = async () => {
      const res = await charge({ transactionBalance });
      if (res.status !== 200) {
        setIsLoading(true);
        dispatch(setTransactionMode(null));
        return Alert.alert('거래가 실패했습니다.', '', [{
          text: '돌아가기',
          onPress: () => navigation.navigate('mypage')
        }])
      } else {
        setIsLoading(true);
        setIsSuccess(true);
      }
    };

    const sendMoney = async () => {
      const res = await send({ transactionBalance });
      if (res.status !== 200) {
        setIsLoading(true);
        dispatch(setTransactionMode(null));
        return Alert.alert('거래가 실패했습니다.', '', [{
          text: '돌아가기',
          onPress: () => navigation.navigate('mypage')
        }])
      } else {
        setIsLoading(true);
        setIsSuccess(true);
      }
    };

    if (transactionMode === 'charge') {
      chargeAccount();
    } else {
      sendMoney();
    }
  }, [navigation])

  return (
    <View style={styles.container}>
      {
        isLoading ?
        isSuccess && (
          <>
            <View style={styles.innerContainer}>
              <Text style={styles.text}>
                {parseInt(transactionBalance).toLocaleString('ko-KR')}원 {transactionMode === 'charge' ? '채우기' : '보내기'} 완료
              </Text>
              <Image 
                style={styles.img} 
                resizeMode='contain'
                source={
                transactionMode === 'charge' ? 
                require('../../../assets/images/deposit.png') 
                : 
                require('../../../assets/images/withdraw.png')} 
              />
              <CustomButton onPress={() => {
                navigation.navigate('account');
                dispatch(setTransactionMode(null));
              }}>
                확인
              </CustomButton>
            </View>
          </>
        )
        :
        <View style={styles.loadingContainer}>
          <LottieView
            autoPlay
            style={{
              width: '100%',
              height: '80%',
            }}
            source={require('../../../assets/transactionLoading.json')}
          />
        </View>
      }
    </View>
  )
}

export default TransactionResultScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center'
  },
  text: {
    fontSize: 24,
    fontFamily: 'pretendard-semiBold',
    textAlign: 'center'
  },
  innerContainer: {
    width: '78%',
    gap: 100,
    flex: 1,
    justifyContent: 'center'
  },
  img: {
    width: '100%'
  },
  loadingContainer: {
    width: '70%',
    flex: 1,
    justifyContent: 'center'
  }
})