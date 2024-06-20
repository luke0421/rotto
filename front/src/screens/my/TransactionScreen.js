import { View, Text, StyleSheet, TextInput } from 'react-native';
import DetailTopBar from '../../components/common/DetailTopBar';
import { getRealAccountInfo } from '../../utils/accountApi';
import { useDispatch, useSelector } from 'react-redux';
import { setConnectedAccount } from '../../stores/mySlice';
import { useFocusEffect } from '@react-navigation/native';
import { useCallback, useState } from 'react';
import Colors from '../../constants/Colors';
import CustomButton from '../../components/common/CustomButton';

const TransactionScreen = ({navigation}) => {
  const dispatch = useDispatch();
  const connectedAccount = useSelector(state => state.myPageInfo.connectedAccount);
  const transactionMode = useSelector(state => state.myPageInfo.transactionMode);

  const [amount, setAmount] = useState(0);

  useFocusEffect(
    useCallback(() => {
      const getRealAccountData = async () => {
        const res = await getRealAccountInfo();
        dispatch(setConnectedAccount(res));
      };

      getRealAccountData();
      //console.log(connectedAccount);
    }, [])
  );

  const amountHandler = (enteredText) => {
    setAmount(enteredText);
  };

  const transactionHandler = () => {
    navigation.navigate('transactionBioAuth', { amount });
    setAmount();
  };
  
  return (
    <View style={styles.container}>
      <DetailTopBar title={transactionMode === 'charge' ? '채우기' : '보내기'} navigation={navigation} />
      <View style={styles.innerContainer}>
        <View style={styles.inputContainer}>
          <TextInput 
            style={styles.inputText}
            placeholder={transactionMode === 'charge' ? '얼마나 채울까요?' : '얼마나 보낼까요?'}
            keyboardType='number-pad'
            onChangeText={amountHandler}
            value={amount}
          />
          <Text style={styles.unitText}>원</Text>
        </View>
        {
          connectedAccount && transactionMode === 'charge' &&
          <View>
            <Text style={styles.balance}>최대 가능 금액: {connectedAccount.accountBalance}원</Text>
          </View>
        }
        <CustomButton onPress={transactionHandler}>{ transactionMode === 'charge' ? '채우기' : '보내기'}</CustomButton>
      </View>
    </View>
  )
}

export default TransactionScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: 'white',
  },
  innerContainer: {
    width: '80%',
    paddingTop: 16,
    gap: 12
  },
  inputContainer: {
    width: '100%',
    borderBottomWidth: 1,
    borderBottomColor: Colors.fontGray,
    padding: 8,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  inputText: {
    fontSize: 20,
    fontFamily: 'pretendard-medium',    
  },
  unitText: {
    fontSize: 20,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray
  },
  balance: {
    fontFamily: 'pretendard-medium',
    fontSize: 12
  }
});