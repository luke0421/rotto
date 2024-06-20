import { View, Pressable, TextInput, Text, StyleSheet } from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import Title from '../../components/user/Title';
import CustomButton from '../../components/common/CustomButton';
import Colors from '../../constants/Colors';
import InputBox from '../../components/user/InputBox';
import { useRef, useState } from 'react';
import UserTopBar from '../../components/user/UserTopBar';
import { useDispatch } from 'react-redux';
import { inputName, inputPersonId } from '../../stores/signUpSlice';
import ProgressBar from '../../components/user/ProgressBar';

const NameIdInputScreen = ({navigation}) => {
  const emptyCells = '●●●●●●●';
  const [name, setName] = useState('');
  const [personId, setPersonId] = useState('');
  const idInputRef = useRef();
  const dispatch = useDispatch();

  const nameInputHandler = (enteredText) => {
    setName(enteredText);
  };

  const idInputHandler = (enteredText) => {
    setPersonId(enteredText.replace(/[^0-9]/g, ''));
  };

  const pressNextHandler = () => {
    dispatch(inputName(name));
    dispatch(inputPersonId(personId));
    navigation.navigate('PhoneNumberInput');
  };

  return (
    <View style={styles.container}>
      <UserTopBar navigation={navigation} />
      <View style={styles.dataContainer}>
        <Title>회원가입</Title>
        <ProgressBar step={1} />
        <View style={styles.inputsContainer}>
          <InputBox 
            description="이름을 입력해주세요"
            title="이름"
          >
            <TextInput 
              style={[styles.inputText, {flex: 1, padding: 0}]} 
              autoCorrect={false}
              autoCapitalize="none"
              underlineColorAndroid="transparent"
              onChangeText={nameInputHandler}
              value={name}
              placeholder="2자 이상, 20자 이하 입력"
            />
            {
              name.length ?
              <MaterialIcons name="cancel" size={18} color={Colors.iconGray} onPress={() => setName('')} /> : <></>
            }
          </InputBox>
          <InputBox 
            description="주민등록번호를 입력해주세요"
            title="주민등록번호"
          >
            <Pressable style={styles.inputContainer} onPress={() => idInputRef.current.focus()}>
              <TextInput 
                style={{ position: 'absolute', top: -1000, left: -1000 }} 
                autoCorrect={false}
                underlineColorAndroid="transparent"
                maxLength={7}
                keyboardType="number-pad"
                ref={idInputRef}
                onChangeText={idInputHandler}
                value={personId}
                selectionColor="white"
              />
              <Text>{personId.replace(/[^0-9]/g, '').replace(/^(\d{0,6})(\d{0,7})$/g, '$1-$2').replace(/-{1,2}$/g, '')}</Text>
              <Text style={styles.emptyCellText}>
                {
                  personId.length !== 7 &&
                  emptyCells.slice(personId.length, -1) + '-' + emptyCells.slice(-1)
                }
              </Text>
              <Text style={styles.inputText}>●●●●●●</Text>
            </Pressable>
            {
              personId.length ?
              <MaterialIcons name="cancel" size={18} color={Colors.iconGray} onPress={() => setPersonId('')} /> : <></>
            }
          </InputBox>
        </View>
        <CustomButton disabled={!name || !personId || name.length < 2} onPress={pressNextHandler}>다음</CustomButton>
      </View>
    </View>
  )
}

export default NameIdInputScreen;

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
  emptyCellText: {
    fontFamily: 'pretendard-regular',
    fontSize: 14,
    color: Colors.iconGray
  },
  inputContainer: {
    flexDirection: 'row',
    flex: 1,
    alignItems: 'center'
  }
});