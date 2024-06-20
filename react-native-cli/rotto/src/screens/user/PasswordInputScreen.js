import { View, TextInput, Text, StyleSheet, Alert } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import Title from '../../components/user/Title';
import CustomButton from '../../components/common/CustomButton';
import Colors from '../../constants/Colors';
import { useEffect, useState } from 'react';
import UserTopBar from '../../components/user/UserTopBar';
import { signUp, signIn } from '../../utils/userApi';
import { useSelector } from 'react-redux';
import TokenService from '../../utils/token';
import ProgressBar from '../../components/user/ProgressBar';

const PasswordInputScreen = ({navigation}) => {
  const reg = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*]).{8,}$/;

  const signupInfo = useSelector(state => state.signupInfo);

  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [isChecked, setIsChecked] = useState(null);
  const [isValidPassword, setIsValidPassword] = useState(true);

  const passwordInputHandler = (enteredText) => {
    setPassword(enteredText);
  };

  const passwordConfirmInputHandler = (enteredText) => {
    setPasswordConfirm(enteredText);
  };

  useEffect(() => {
    if (password) {
      setIsChecked(password === passwordConfirm);
    }
  }, [password, passwordConfirm]);

  useEffect(() => {
    if (password) {
      setIsValidPassword(reg.test(password));
    }
  }, [password])

  const pressConfirmHandler = async () => {
    const signUpRes = await signUp({
      name: signupInfo.name,
      juminNo: signupInfo.personId.slice(0, 6),
      phoneNum: signupInfo.phoneNumber,
      sex: parseInt(signupInfo.personId.slice(-1)) % 2 === 1 ? "M" : "F",
      password,
      email: signupInfo.email
    });

    if (signUpRes.status === 201) {
      const res = await signIn({
        phoneNum: signupInfo.phoneNumber,
        password
      });
      if (res.status === 200) {
        await TokenService.setToken(res.data.accessToken, res.data.refreshToken);
        navigation.navigate('PINSetting');   
      } else {
        return Alert.alert('로그인에 실패했습니다.', '', [{text: '다시하기', onPress: () => navigation.navigate('SignIn')}])
      }
    } else {
      return Alert.alert('회원가입에 실패했습니다.', '', [{text: '다시하기', onPress: () => navigation.navigate('Onboarding')}])
    }
  };

  return (
    <View style={styles.container}>
      <UserTopBar navigation={navigation} />
      <View style={styles.dataContainer}>
        <Title>회원가입</Title>
        <ProgressBar step={4} />
        <View style={styles.inputsContainer}>
          <View>
            <Text style={styles.description}>비밀번호를 입력하세요</Text>
            <Text style={styles.title}>비밀번호</Text>
            <View style={[styles.inputContainer , { marginBottom: 8 }]}>
              <TextInput 
                style={[styles.inputText, {flex: 1, padding: 0}]} 
                autoCorrect={false}
                autoCapitalize="none"
                underlineColorAndroid="transparent"
                onChangeText={passwordInputHandler}
                value={password}
                secureTextEntry={true}
                maxLength={20}
              />
              {
                password.length ?
                <MaterialIcons name="cancel" size={18} color={Colors.iconGray} onPress={() => setPassword('')} /> : <></>
              }
            </View>
            <Text style={[styles.warnText, isValidPassword && { color: 'green' }]}>8자 이상 20자 이하. 숫자/소문자/대문자/특수문자를 모두 포함.</Text>
            <Text style={[styles.title, { marginTop: 8 }]}>비밀번호 재확인</Text>
            <View style={[styles.inputContainer, { marginBottom: 16 }]}>
              <TextInput 
                style={[styles.inputText, {flex: 1, padding: 0}]} 
                autoCorrect={false}
                autoCapitalize="none"
                underlineColorAndroid="transparent"
                onChangeText={passwordConfirmInputHandler}
                value={passwordConfirm}
                secureTextEntry={true}
                maxLength={20}
              />
              {
                passwordConfirm.length ?
                <MaterialIcons name="cancel" size={18} color={Colors.iconGray} onPress={() => setPasswordConfirm('')} /> : <></>
              }
            </View>
            { isChecked === false && <Text style={styles.warnText}>비밀번호가 일치하지 않습니다. 다시 확인해주세요.</Text> }
            { isChecked === true && <Text style={[styles.warnText, { color: 'green' }]}>비밀번호가 일치합니다.</Text> }
          </View>
        </View>
        <CustomButton onPress={pressConfirmHandler} disabled={!isChecked || !isValidPassword}>확인</CustomButton>
      </View>
    </View>
  )
}

export default PasswordInputScreen;

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
    marginBottom: 44,
    marginTop: 32
  },
  inputText: {
    fontFamily: 'pretendard-regular',
    fontSize: 14
  },
  description: {
    fontFamily: 'pretendard-semiBold',
    fontSize: 16,
    marginBottom: 18
  },
  title: {
    fontFamily: 'pretendard-regular',
    fontSize: 12,
    color: Colors.fontGray,
    marginBottom: 8
  },
  inputContainer: {
    width: '100%',
    borderBottomColor: Colors.fontGray,
    borderBottomWidth: 1,
    alignItems: 'center',
    flexDirection: 'row',
    height: 28
  },
  warnText: {
    color: 'red',
    fontSize: 12
  }
});