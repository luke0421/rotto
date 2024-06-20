import { View, TextInput, Text, StyleSheet } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import Title from '../../components/user/Title';
import CustomButton from '../../components/common/CustomButton';
import Colors from '../../constants/Colors';
import { useEffect, useState } from 'react';
import UserTopBar from '../../components/user/UserTopBar';
import { getUserInfo, signIn } from '../../utils/userApi';
import TokenService from '../../utils/token';

// 나중에 회원가입 진행사항 progress bar로 보여주기

const SignInScreen = ({navigation}) => {
  const [phoneNumber, setPhoneNumber] = useState('');
  const [password, setPassword] = useState('');
  const [errMsg, setErrMsg] = useState('');

  const phoneNumberInputHandler = (enteredText) => {
    setPhoneNumber(enteredText
      .replace(/[^0-9]/g, '')
      .replace(/^(\d{0,4})(\d{0,4})$/g, "$1-$2").replace(/(\-{1})$/g, "")
    );
  };

  const passwordInputHandler = (enteredText) => {
    setPassword(enteredText);
  };

  const signInHandler = async () => {
    const res = await signIn({
      phoneNum: '010' + phoneNumber.replace(/-/g, ''),
      password
    });
    if (res.status === 200) {
      await TokenService.setToken(res.data.accessToken, res.data.refreshToken);
      const userInfoRes = await getUserInfo();
      await TokenService.setUserCode(userInfoRes.data.userCode.toString());
      navigation.reset({
        index: 1,
        routes: [{ name: 'Routers' }]
      });
      setErrMsg('');
    } else {
      setErrMsg('전화번호와 비밀번호를 다시 확인해주세요');
    }
  };

  return (
    <View style={styles.container}>
      <UserTopBar navigation={navigation} destination="Onboarding" />
      <View style={styles.dataContainer}>
        <Title>로그인</Title>
        <View style={styles.inputsContainer}>
          <View>
            <Text style={styles.title}>전화번호</Text>
            <View style={[styles.inputContainer , { marginBottom: 8 }]}>
              <Text style={styles.inputText}>010-</Text>
              <TextInput 
                // style={[styles.inputText, {flex: 1}]} 
                style={[styles.inputText, {flex: 1, padding: 0}]} 
                autoCorrect={false}
                autoCapitalize="none"
                underlineColorAndroid="transparent"
                keyboardType="number-pad"
                onChangeText={phoneNumberInputHandler}
                value={phoneNumber}
                maxLength={9}
              />
              {
                phoneNumber.length ?
                <MaterialIcons name="cancel" size={18} color={Colors.iconGray} onPress={() => setPhoneNumber('')} /> : <></>
              }
            </View>
            <Text style={[styles.title, { marginTop: 8 }]}>비밀번호</Text>
            <View style={[styles.inputContainer, { marginBottom: 16 }]}>
              <TextInput 
                style={[styles.inputText, {flex: 1, padding:0}]} 
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
            { errMsg && <Text style={styles.warnText}>{errMsg}</Text>}
          </View>
        </View>
        <CustomButton onPress={signInHandler} disabled={!phoneNumber || !password}>로그인</CustomButton>
      </View>
    </View>
  )
}

export default SignInScreen;

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