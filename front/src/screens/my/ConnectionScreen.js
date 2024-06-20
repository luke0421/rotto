import { View, Text, StyleSheet, TextInput, Modal, Pressable, Alert } from 'react-native';
import DetailTopBar from '../../components/common/DetailTopBar';
import { useCallback, useEffect, useState } from 'react';
import Colors from '../../constants/Colors';
import { Ionicons } from '@expo/vector-icons';
import CustomButton from '../../components/common/CustomButton';
import BankSelectModal from '../../components/my/BankSelectModal';
import { useDispatch, useSelector } from 'react-redux';
import { setBankModal, setSelectedBank } from '../../stores/mySlice';
import { connectAccount } from '../../utils/accountApi';
import { useIsFocused } from '@react-navigation/native';

const ConnectionScreen = ({navigation}) => {
  const dispatch = useDispatch();
  const selectedBank = useSelector(state => state.myPageInfo.selectedBank);

  const [step, setStep] = useState(1);
  const [accountNum, setAccountNum] = useState('');
  const [password, setPassword] = useState('');

  const isFocused = useIsFocused();

  useEffect(() => {
    if (!isFocused) {
      setStep(1);
      setAccountNum('');
      setPassword('');
      dispatch(setSelectedBank(null));
    }
  }, [isFocused])

  const accountNumHandler = (enteredText) => {
    setAccountNum(enteredText);
  };

  const passwordHandler = (enteredText) => {
    setPassword(enteredText);
  };

  const resetInfo = () => {
    setStep(1);
    setAccountNum('');
    setPassword('');
    dispatch(setSelectedBank(null));
  };

  const buttonHandler = async () => {
    if (step === 1) {
      setStep(pre => pre + 1);
    } else {
      const res = await connectAccount({
        bankName: "002",
        accountNum: "0025683504300707"
      });

      if (res.status === 200) {
        dispatch(setSelectedBank(null));
        navigation.navigate('account');
      } else if (res.status == 302) {
        navigation.navigate('account');
      } else {
        Alert.alert('계좌번호와 비밀번호를 다시 확인해주세요', '', [{text: '재입력', onPress: resetInfo}]);
      }
    }
  };

  return (
    <View style={styles.container}>
      <DetailTopBar title='계좌 연결' navigation={navigation} destination='account' />
      <View style={styles.innerContainer}>
        <View style={styles.stepContainer}>
          <View style={[styles.stepCircle, step !== 1 && { backgroundColor: Colors.fontGray }]}>
            {
              step === 1 ?
              <Text style={styles.stepNumber}>1</Text>
              :
              <Ionicons name='checkmark-sharp' size={12} color='white' />
            }
          </View>
          <Text style={styles.stepText}>계좌 번호 입력</Text>
        </View>
        {
          step === 1 ?
          <>
            <Pressable onPress={() => dispatch(setBankModal(true))}>
              <View style={styles.inputContainer}>
                <Text style={[styles.inputText, { color: selectedBank ? 'black' : Colors.fontGray }]}>
                  { selectedBank ? selectedBank.bankName : '은행' }
                </Text>
                <Ionicons name="chevron-expand-sharp" size={20} color={Colors.fontGray} />
              </View>
            </Pressable>
            <View style={styles.inputContainer}>
              <TextInput 
                style={[styles.inputText, { flex: 1 }]} 
                placeholder='계좌번호' 
                value={accountNum} 
                keyboardType='number-pad'
                autoCorrect={false}
                onChangeText={accountNumHandler}
              />
            </View>
            <BankSelectModal />
          </>
          :
          <>
            <View style={styles.stepContainer}>
              <View style={styles.stepCircle}>
                <Text style={styles.stepNumber}>2</Text>
              </View>
              <Text style={styles.stepText}>계좌 비밀번호 입력</Text>
            </View>
            <View style={styles.inputContainer}>
              <TextInput 
                style={styles.inputText}
                placeholder='계좌 비밀번호'
                value={password}
                keyboardType='number-pad'
                autoCorrect={false}
                onChangeText={passwordHandler}
                secureTextEntry={true}
                maxLength={4}
              />
            </View>
          </>
        }
        <CustomButton
          disabled={
            step === 1 ?
            !selectedBank || accountNum === ''
            :
            password === ''
          }
          onPress={buttonHandler}
        >
          { step === 1 ? '다음' : '완료' }
        </CustomButton>
      </View>
    </View>
  )
}

export default ConnectionScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: 'white',
  },
  innerContainer: {
    width: '80%',
    gap: 10
  },
  stepContainer: {
    flexDirection: 'row',
    gap: 5,
    alignItems: 'center'
  },
  stepCircle: {
    backgroundColor: Colors.btnBlue,
    height: 20,
    width: 20,
    borderRadius: 10,
    justifyContent: 'center',
    alignItems: 'center'
  },
  stepNumber: {
    fontSize: 12,
    fontFamily: 'pretendard-medium',
    color: 'white'
  },
  stepText: {
    fontSize: 16,
    fontFamily: 'pretendard-medium'
  },
  inputContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 18,
    paddingVertical: 12,
    borderColor: Colors.fontGray,
    borderRadius: 10,
    borderWidth: 0.5
  },
  inputText: {
    fontSize: 14,
    fontFamily: 'pretendard-medium'
  }
});