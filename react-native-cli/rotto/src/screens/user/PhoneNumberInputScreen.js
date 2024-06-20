import { View, TextInput, Text, StyleSheet } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import Title from '../../components/user/Title';
import CustomButton from '../../components/common/CustomButton';
import Colors from '../../constants/Colors';
import InputBox from '../../components/user/InputBox';
import { useState } from 'react';
import UserTopBar from '../../components/user/UserTopBar';
import { useDispatch } from 'react-redux';
import { inputPhoneNumber } from '../../stores/signUpSlice';
import { checkPhoneNumber } from '../../utils/userApi';
import ProgressBar from '../../components/user/ProgressBar';

const PhoneNumberInputScreen = ({navigation}) => {
  const [phoneNumber, setPhoneNumber] = useState('');
  const [isChecked, setIsChecked] = useState();
  const dispatch = useDispatch();

  const phoneNumberInputHandler = (enteredText) => {
    const enteredNum = enteredText
      .replace(/[^0-9]/g, '')
      .replace(/^(\d{0,4})(\d{0,4})$/g, "$1-$2").replace(/(\-{1})$/g, "");
    setPhoneNumber(enteredNum);
    setIsChecked();
  };

  const checkPhoneNumberValidity = async () => {
    if (phoneNumber) {
      const isExist = await checkPhoneNumber({
        phoneNum: '010' + phoneNumber.replace(/-/g, '')
      }) ;
      setIsChecked(!isExist);
    }
  };

  const pressNextHandler = () => {
    dispatch(inputPhoneNumber('010' + phoneNumber.replace(/-/g, '')));
    navigation.navigate('EmailInput');
  };

  return (
    <View style={styles.container}>
      <UserTopBar navigation={navigation} />
      <View style={styles.dataContainer}>
        <Title>회원가입</Title>
        <ProgressBar step={2} />
        <View style={styles.inputsContainer}>
          <InputBox 
            description="전화번호를 입력해주세요"
            title="전화번호"
          >
            <Text style={styles.inputText}>010-</Text>
            <TextInput 
              style={[styles.inputText, {flex: 1, padding: 0}]} 
              autoCorrect={false}
              underlineColorAndroid="transparent"
              onChangeText={phoneNumberInputHandler}
              keyboardType="number-pad"
              value={phoneNumber}
              maxLength={9}
            />
            {
              phoneNumber.length ?
              <MaterialIcons style={{ marginRight: 4 }} name="cancel" size={18} color={Colors.iconGray} onPress={() => setPhoneNumber('')} /> : <></>
            }
            <CustomButton disabled={phoneNumber.length !== 9} onPress={checkPhoneNumberValidity} style={{ backgroundColor: 'black', width: '18%', height: 22 }} btnColor='black'>중복 확인</CustomButton>
          </InputBox>
        </View>
        { isChecked === false && <Text style={[styles.warnText, styles.caution]}>이미 가입된 번호입니다.</Text> }
        { isChecked === undefined && <Text style={styles.warnText}>중복 확인을 해주세요</Text> }
        <CustomButton onPress={pressNextHandler} disabled={!isChecked || phoneNumber.length !== 9}>다음</CustomButton>
      </View>
    </View>
  )
}

export default PhoneNumberInputScreen;

const styles = StyleSheet.create({
  container: {
    backgroundColor: 'white',
    flex: 1,
    alignItems: 'center'
  },
  dataContainer: {
    width: '80%'
  },
  inputsContainer: {
    gap: 40,
    marginBottom: 16,
    marginTop: 32
  },
  inputText: {
    fontFamily: 'pretendard-regular',
    fontSize: 14
  },
  warnText: {
    fontSize: 12,
    textAlign: 'center',
    marginBottom: 16
  },
  caution: {
    color: 'red'
  }
});