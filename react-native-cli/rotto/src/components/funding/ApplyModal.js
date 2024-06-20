import { View, Pressable, Text, StyleSheet, Modal, TextInput, TouchableWithoutFeedback, Alert } from 'react-native';
import Colors from '../../constants/Colors';
import { useDispatch, useSelector } from 'react-redux';
import { setApplyModal, setFundingData } from '../../stores/fundingSlice';
import CustomButton from '../common/CustomButton';
import { useCallback, useState } from 'react';
import { applyFunding } from '../../utils/fundingApi';
import { useFocusEffect } from '@react-navigation/native';
import { setFundingAccount } from '../../stores/mySlice';
import { getAccountInfo } from '../../utils/accountApi';

const ApplyModal = () => {
  const dispatch = useDispatch();
  const modalVisible = useSelector(state => state.fundingInfo.isApplyModalOpen);
  const fundingData = useSelector(state => state.fundingInfo.fundingData);
  const fundingAccount = useSelector(state => state.myPageInfo.fundingAccount);

  const [amount, setAmount] = useState('');
  const [isValidAmount, setIsValidAmount] = useState();

  useFocusEffect(
    useCallback(() => {
      const getFundingAccount = async () => {
        const res = await getAccountInfo();
        dispatch(setFundingAccount(res));
      };
      
      getFundingAccount();
    }, [])
  );

  const amountHandler = (enteredText) => {
    setAmount(enteredText);
    if (parseInt(enteredText) > fundingData.limitNum || parseInt(enteredText) < 1) {
      setIsValidAmount(false);
    } else {
      setIsValidAmount(true);
    }
  };

  const applyHandler = async () => {
    if (fundingAccount.accountBalance < fundingData.confirmPrice * amount) {
      return Alert.alert('', '청약 계좌에 잔액이 부족합니다.', [
        { text: '확인', onPress: () => {
          dispatch(setApplyModal(false));
          setAmount('');
        }}
      ]);
    }
    const res = await applyFunding(fundingData.subscriptionCode, amount);
    return Alert.alert(res.status === 200 ? '청약 신청이 완료되었습니다.' : '청약 신청에 실패하셨습니다.', '', 
    [{
      text: '확인',
      onPress: () => {
        dispatch(setApplyModal(false));
        if (res.status === 200) {
          dispatch(setFundingData({
            ...fundingData,
            "isApply": 1
          }));
        } 
        setAmount('');
      }
    }]);
  };

  return (
    fundingData &&
    <Modal
      animationType='fade'
      transparent={true}
      visible={modalVisible}
      onRequestClose={() => dispatch(setApplyModal(false))}          
    >  
      <Pressable style={styles.modalBack} onPress={() => dispatch(setApplyModal(false))}>
        <View style={styles.modal}>
          <TouchableWithoutFeedback>
            <View style={styles.applyContainer}>
              <Text style={styles.title}>신청 수량</Text>
              <View style={styles.inputContainer}>
                <TextInput 
                  style={styles.inputText}
                  keyboardType='number-pad'
                  value={amount}
                  onChangeText={amountHandler}
                />
                <Text style={[styles.infoText, isValidAmount === false && { color: 'red' }]}> / {fundingData.limitNum} ROTTO</Text>
              </View>
              <CustomButton onPress={applyHandler} disabled={!isValidAmount || !amount}>신청</CustomButton>
            </View>
          </TouchableWithoutFeedback>
          <View style={styles.line} />
          <Pressable onPress={() => dispatch(setApplyModal(false))}>
            <Text style={styles.modalCloseMenu}>닫기</Text>
          </Pressable>
        </View>
      </Pressable>
    </Modal>
  )
}

export default ApplyModal;

const styles = StyleSheet.create({
  modalBack : {
    justifyContent: 'flex-end',
    flexGrow: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.25)'
  },
  modal: {
    backgroundColor: 'white',
    borderTopRightRadius: 20,
    borderTopLeftRadius: 20,
    alignItems: 'center'
  },
  line: {
    height: 1,
    borderTopColor: Colors.fontGray,
    borderWidth: 0.5,
    width: '100%'
  },
  modalCloseMenu: {
    textAlign: 'center',
    fontSize: 14,
    fontFamily: 'pretendard-medium',
    paddingVertical: 12,
    color: Colors.fontGray
  },
  applyContainer: {
    width: '85%',
    paddingVertical: 20,
    gap: 16
  },
  title: {
    fontSize: 16,
    fontFamily: 'pretendard-medium'
  },
  inputContainer: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  inputText: {
    borderBottomColor: Colors.fontGray,
    borderBottomWidth: 1,
    fontSize: 16,
    fontFamily: 'pretendard-medium',
    textAlign: 'center'
  },
  infoText: {
    fontSize: 16,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray
  }
});