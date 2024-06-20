import { View, TextInput, StyleSheet, Alert } from "react-native";
import CustomButton from "../../components/common/CustomButton";
import Colors from "../../constants/Colors";
import { useEffect, useState } from "react";
import { createRequest } from "../../utils/settingApi";
import TokenService from "../../utils/token";
import DetailTopBar from "../../components/common/DetailTopBar";

const InquiryCreateScreen = ({navigation}) => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const titleInputHandler = (enteredText) => {
    setTitle(enteredText);
  };

  const contentInputHandler = (enteredText) => {
    setContent(enteredText);
  };

  const submitHandler = async () => {
    if (title && content) {
      const res = await createRequest({ title, content });
      navigation.navigate('inquiryDetail', {
        reqBoardCode: res.reqBoardCode
      });
    } else {
      Alert.alert('제목과 내용을 입력해주세요');
    }
  };

  return (
    <View style={styles.container}>
      <DetailTopBar title='문의 작성' navigation={navigation} />
      <View style={styles.contentContainer}>
        <TextInput 
          placeholder="제목을 입력해주세요.(20자 이내)"
          placeholderTextColor={Colors.fontGray}
          autoCapitalize="none"
          autoCorrect={false}
          maxLength={20}
          style={[styles.inputBox, { paddingVertical: 5 }]}
          onChangeText={titleInputHandler}
          value={title}
        />
        <TextInput 
          placeholder="내용을 입력해주세요.(1,000자 이내)"
          placeholderTextColor={Colors.fontGray}
          autoCapitalize="none"
          autoCorrect={false}
          maxLength={1000}
          multiline={true}
          numberOfLines={7}
          style={[styles.inputBox, { textAlignVertical: 'top' }]}
          onChangeText={contentInputHandler}
          value={content}
        />
        <CustomButton onPress={submitHandler}>작성 완료</CustomButton>
      </View>
    </View>
  )
}

export default InquiryCreateScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center'
  },
  contentContainer: {
    width: '82%',
    gap: 12,
    marginVertical: 12
  },
  inputBox: {
    borderRadius: 10,
    backgroundColor: '#ECECEC',
    width: '100%',
    fontFamily: 'pretendard-medium',
    fontSize: 12,
    padding: 10
  }
});