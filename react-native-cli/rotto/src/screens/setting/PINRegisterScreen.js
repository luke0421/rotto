import { View, Text, SafeAreaView, StyleSheet, Alert } from 'react-native';
import ReactNativePinView from 'react-native-pin-view';
import { useEffect, useState, useRef } from 'react';
import KeyService from '../../utils/pinCode';
import { Ionicons } from '@expo/vector-icons';
import Colors from '../../constants/Colors';
import BioAuthButton from '../../components/common/BioAuthButton';
import DetailTopBar from '../../components/common/DetailTopBar';

const PINRegisterScreen = ({navigation}) => {
  const pinView = useRef(null)
  const [showRemoveButton, setShowRemoveButton] = useState(false)
  const [showCompletedButton, setShowCompletedButton] = useState(false)
  const [enteredPin, setEnteredPin] = useState("")
  const [step1Pin, setStep1Pin] = useState("")
  const [step, setStep] = useState('1');

  useEffect(() => {
    if (enteredPin.length > 0) {
      setShowRemoveButton(true)
    } else {
      setShowRemoveButton(false)
    }
    if (enteredPin.length === 6) {
      setShowCompletedButton(true)
    } else {
      setShowCompletedButton(false)
    }
  }, [enteredPin]);

  const resetPinCode = () => {
    setEnteredPin('');
    pinView.current.clearAll();
  };

  const setPinCode = async () => {
    await KeyService.setPinCode(enteredPin);
    navigation.navigate('setting');
  };

  return (
    <SafeAreaView style={styles.safeAreaView}>
      <DetailTopBar title='간편 비밀번호 등록' navigation={navigation} />
      <View style={styles.pinContainer}>
        <Text style={styles.description}>
          {
            step === '1' ?
            '간편 비밀번호를 설정해주세요'
            :
            '간편 비밀번호를 한번 더 입력해주세요'
          }
        </Text>
        <BioAuthButton style={{ marginBottom: 16 }} />
        <ReactNativePinView
          inputSize={20}
          ref={pinView}
          pinLength={6}
          buttonSize={60}
          onValueChange={value => setEnteredPin(value)}
          inputAreaStyle={{
            marginBottom: '25%',
          }}
          inputViewEmptyStyle={{
            backgroundColor: '#D9D9D9',
          }}
          inputViewFilledStyle={{
            backgroundColor: Colors.btnBlue,
          }}
          buttonViewStyle={{
            borderWidth: 1,
            borderColor: "black",
          }}
          buttonTextStyle={{
            color: "black"
          }}
          onButtonPress={key => {
            if (key === "custom_left") {
              pinView.current.clear()
            }
            if (key === "custom_right") {
              if(step === '1'){
                setStep('2');
                setStep1Pin(enteredPin);
                setEnteredPin('');
                pinView.current.clearAll();
              }else{
                if(step1Pin === enteredPin){
                  setPinCode();
                }else{
                  Alert.alert('불일치합니다. 다시 입력해주세요.', '', [{text: '재입력', onPress: resetPinCode}]);
                }
              }
            }
          }}
          customLeftButton={showRemoveButton ? <Ionicons name={"arrow-back-outline"} size={36} color={"black"} /> : undefined}
          customRightButton={showCompletedButton ? <Ionicons name={"checkmark-outline"} size={36} color={"black"} /> : undefined}
        />
      </View>
    </SafeAreaView>
  )
}

export default PINRegisterScreen;

const styles = StyleSheet.create({
  safeAreaView: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center'
  },
  pinContainer: {
    width: '82%',
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  description: {
    fontFamily: 'pretendard-semiBold',
    fontSize: 16,
    textAlign: 'center',
    marginBottom: 16
  }
});