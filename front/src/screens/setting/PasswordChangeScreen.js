import { View, TextInput, Text, StyleSheet } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import CustomButton from '../../components/common/CustomButton';
import Colors from '../../constants/Colors';
import { useEffect, useState } from 'react';
import { changePassword } from '../../utils/userApi';
import DetailTopBar from '../../components/common/DetailTopBar';

const PasswordChangeScreen = ({navigation}) => {
  const reg = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*]).{8,}$/;

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
    await changePassword({ password });
    navigation.navigate('setting');
  };

  return (
    <View style={styles.container}>
      <DetailTopBar title="비밀번호 변경" navigation={navigation} destination="setting" />
      <View style={styles.dataContainer}>
        <View style={styles.inputsContainer}>
          <View>
            <Text style={styles.title}>새로운 비밀번호</Text>
            <View style={[styles.inputContainer , { marginBottom: 8 }]}>
              <TextInput 
                style={[styles.inputText, {flex: 1}]} 
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
                style={[styles.inputText, {flex: 1}]} 
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
        <CustomButton onPress={pressConfirmHandler} disabled={!isChecked || !isValidPassword}>변경</CustomButton>
      </View>
    </View>
  )
}

export default PasswordChangeScreen;

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