import { View, TextInput, Text, StyleSheet } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import Title from '../../components/user/Title';
import CustomButton from '../../components/common/CustomButton';
import Colors from '../../constants/Colors';
import InputBox from '../../components/user/InputBox';
import { useState } from 'react';
import UserTopBar from '../../components/user/UserTopBar';
import { useDispatch } from 'react-redux';
import { inputEmail } from '../../stores/signUpSlice';
import { checkEmail } from '../../utils/userApi';
import ProgressBar from '../../components/user/ProgressBar';

const EmailInputScreen = ({navigation}) => {
  const [email, setEmail] = useState('');
  const [isValidEmail, setIsValidEmail] = useState();
  const [isChecked, setIsChecked] = useState();
  const dispatch = useDispatch();

  const emailInputHandler = (enteredText) => {
    const enteredEmail = enteredText.replace(/\s/g, '');
    setEmail(enteredEmail);
    setIsValidEmail(() => /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/.test(enteredEmail));
    setIsChecked();
  };

  const checkEmailValidity = async () => {
    if (email) {
      const isExist = await checkEmail({ email}) ;
      setIsChecked(!isExist);
    }
  };

  const pressNextHandler = () => {
    dispatch(inputEmail(email));
    navigation.navigate('PasswordInput');
  };

  return (
    <View style={styles.container}>
      <UserTopBar navigation={navigation} />
      <View style={styles.dataContainer}>
        <Title>회원가입</Title>
        <ProgressBar step={3} />
        <View style={styles.inputsContainer}>
          <InputBox 
            description="이메일을 입력해주세요"
            title="이메일"
          >
            <TextInput 
              style={[styles.inputText, {flex: 1, padding: 0}]} 
              underlineColorAndroid="transparent"
              onChangeText={emailInputHandler}
              autoCorrect={false}
              autoCapitalize="none"
              value={email}
            />
            {
              email.length ?
              <MaterialIcons style={{ marginRight: 4 }} name="cancel" size={18} color={Colors.iconGray} onPress={() => setEmail('')} /> : <></>
            }
            <CustomButton disabled={!isValidEmail} onPress={checkEmailValidity} style={{ backgroundColor: 'black', width: '18%', height: 22 }} btnColor='black'>중복 확인</CustomButton>
          </InputBox>
        </View>
        {
          isValidEmail === false ?
          <Text style={[styles.warnText, styles.caution]}>이메일 형식에 맞지 않습니다.</Text>
          :
          <>
            {isChecked === false && <Text style={[styles.warnText, styles.caution]}>이미 가입된 이메일입니다.</Text>}
            {isChecked === undefined && <Text style={styles.warnText}>중복 확인을 해주세요.</Text>}
          </>
        }
        <CustomButton onPress={pressNextHandler} disabled={!isChecked}>다음</CustomButton>
      </View>
    </View>
  )
}

export default EmailInputScreen;

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