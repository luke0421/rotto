import { View, Text, SafeAreaView, StyleSheet, Alert, Platform, Pressable } from 'react-native';
import ReactNativePinView from 'react-native-pin-view';
import { useEffect, useState, useRef } from 'react';
import KeyService from '../../utils/pinCode';
import { Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import Colors from '../../constants/Colors';

const PINInputScreen = ({navigation}) => {
  const pinView = useRef(null)
  const [showRemoveButton, setShowRemoveButton] = useState(false)
  const [showCompletedButton, setShowCompletedButton] = useState(false)
  const [enteredPin, setEnteredPin] = useState("")

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

  const checkPinCode = async () => {
    const storedPin = await KeyService.getPinCode();
    if (storedPin === enteredPin) {
      navigation.reset({
        index: 1,
        routes: [{ name: 'Routers' }]
      });
    } else {
      Alert.alert('잘못 입력하셨습니다. 다시 입력해주세요.', '', [{text: '재입력', onPress: resetPinCode}])
      setEnteredPin('');
      pinView.current.clearAll();
    } 
  }

  return (
    <SafeAreaView style={styles.safeAreaView}>
      <View style={styles.pinContainer}>
        <Text style={styles.description}>간편 비밀번호를 입력해주세요</Text>
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
              checkPinCode();
            }
          }}
          customLeftButton={showRemoveButton ? <Ionicons name={"arrow-back-outline"} size={36} color={"black"} /> : undefined}
          customRightButton={showCompletedButton ? <Ionicons name={"checkmark-outline"} size={36} color={"black"} /> : undefined}
        />
        {/* <Pressable style={styles.bioButton} onPress={() => navigation.navigate('BioAuth')}>
          <MaterialCommunityIcons name={ Platform.OS === 'android' ? 'fingerprint' : 'face-recognition' } size={12} />
          <Text style={styles.bioText}>생체 인증</Text>
        </Pressable> */}
      </View>
    </SafeAreaView>
  )
}

export default PINInputScreen;

const styles = StyleSheet.create({
  safeAreaView: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center'
  },
  pinContainer: {
    width: '80%',
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
  description: {
    fontFamily: 'pretendard-semiBold',
    fontSize: 16,
    textAlign: 'center',
    marginBottom: 32
  },
  bioButton: {
    flexDirection: 'row',
    gap: 8,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 1,
    width: 100,
    height: 30,
    borderRadius: 15,
    marginTop: 16
  },
  bioText: {
    fontFamily: 'pretendard-medium',
    fontSize: 12
  }
});