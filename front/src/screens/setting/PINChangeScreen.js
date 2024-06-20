import { View, SafeAreaView, StyleSheet, Alert, Text } from 'react-native';
import ReactNativePinView from 'react-native-pin-view';
import { useEffect, useState, useRef } from 'react';
import KeyService from '../../utils/pinCode';
import { Ionicons } from '@expo/vector-icons';
import Colors from '../../constants/Colors';
import DetailTopBar from '../../components/common/DetailTopBar';

const PINChangeScreen = ({navigation}) => {
  const pinView = useRef(null)
  const [showRemoveButton, setShowRemoveButton] = useState(false)
  const [showCompletedButton, setShowCompletedButton] = useState(false)
  const [enteredPin, setEnteredPin] = useState("")
  const [step2Pin, setStep2Pin] = useState("")
  const [step, setStep] = useState('1');
  const [description, setDescription] = useState('');

  useEffect(() => {
    switch (step) {
      case '1':
        setDescription('기존 비밀번호를 입력해주세요');
        break;
      case '2':
        setDescription('변경할 비밀번호를 입력해주세요');
        break;
      case '3':
        setDescription('변경할 비밀번호를 한번 더 입력해주세요')
    }
  }, [step])

  useEffect(() => {
    if (enteredPin.length > 0) {
      setShowRemoveButton(true);
    } else {
      setShowRemoveButton(false);
    }
    if (enteredPin.length === 6) {
      setShowCompletedButton(true);
    } else {
      setShowCompletedButton(false);
    }
  }, [enteredPin]);

  const resetPinCode = () => {
    setEnteredPin('');
    pinView.current.clearAll();
  };

  const pinCodeHandler = async () => {
    if(step === '1'){
      const storedPin = await KeyService.getPinCode();
      if (storedPin === enteredPin) {
        setStep('2');
      } else {
        Alert.alert('잘못 입력하셨습니다. 다시 입력해주세요.', '', [{text: '재입력', onPress: resetPinCode}])
      } 
      setEnteredPin('');
      pinView.current.clearAll();
    } else if (step === '2') {
      setStep('3');
      setStep2Pin(enteredPin);
      resetPinCode();
    } else {
      if (step2Pin === enteredPin){
        setPinCode();
      } else{
        Alert.alert('불일치합니다. 다시 입력해주세요.', '', [{text: '재입력', onPress: resetPinCode}]);
      }
    }
  };

  const setPinCode = async () => {
    await KeyService.setPinCode(enteredPin);
    navigation.navigate('setting');
  };

  return (
    <SafeAreaView style={styles.safeAreaView}>
      <DetailTopBar navigation={navigation} title='간편 비밀번호 변경' />
      <View style={styles.pinContainer}>
        <Text style={styles.description}>{description}</Text>
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
              pinCodeHandler();
            }
          }}
          customLeftButton={showRemoveButton ? <Ionicons name={"arrow-back-outline"} size={36} color={"black"} /> : undefined}
          customRightButton={showCompletedButton ? <Ionicons name={"checkmark-outline"} size={36} color={"black"} /> : undefined}
        />
      </View>
    </SafeAreaView>
  )
}

export default PINChangeScreen;

const styles = StyleSheet.create({
  safeAreaView: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center'
  },
  pinContainer: {
    width: '82%',
    flex: 1,
    justifyContent: 'center'
  },
  description: {
    fontFamily: 'pretendard-semiBold',
    fontSize: 16,
    textAlign: 'center',
    marginBottom: 32
  }
});