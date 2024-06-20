import { View, Text, StyleSheet, Image, Pressable, Modal } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import Colors from '../../constants/Colors';
import { useCallback, useEffect, useState } from 'react';
import { useFocusEffect } from '@react-navigation/native';
import { disConnectAccount as disconnect, getRealAccountInfo } from '../../utils/accountApi';
import { useDispatch, useSelector } from 'react-redux';
import { setConnectedAccount } from '../../stores/mySlice';

const ConnectedAccount = ({navigation}) => {
  const dispatch = useDispatch();
  const connectedAccount = useSelector(state => state.myPageInfo.connectedAccount);
  
  const [bankLogo, setBankLogo] = useState(require('../../../assets/images/bank/bokLogo.png'));
  const [bankName, setBankName] = useState('한국');
  const [modalVisible, setModalVisible] = useState(false);

  useFocusEffect(
    useCallback(() => {
      const getRealAccountData = async () => {
        const res = await getRealAccountInfo();
        dispatch(setConnectedAccount(res));
      };

      getRealAccountData();
      //console.log(connectedAccount);
    }, [])
  );

  useEffect(() => {
    if (connectedAccount) {
      switch (connectedAccount.bankName) {
        case '001':
          setBankLogo(require('../../../assets/images/bank/bokLogo.png'));
          setBankName('한국');
          break;
        case '002':
          setBankLogo(require('../../../assets/images/bank/kdbLogo.png'));
          setBankName('산업');
          break;
        case '003':
          setBankLogo(require('../../../assets/images/bank/ibkLogo.png'));
          setBankName('기업');
          break;
        case '004':
          setBankLogo(require('../../../assets/images/bank/kbLogo.png'));
          setBankName('국민');
      }
    }
  }, [connectedAccount]);

  const disconnectAccount = async () => {
    const res = await disconnect(connectedAccount.accountCode);
    if (res.status === 200) {
      dispatch(setConnectedAccount(null));
      setModalVisible(false);
    }
  };

  return (
    <View style={styles.container}>
      {
        connectedAccount ?
        <>
          <View style={styles.innerContainer}>
            <Text style={[styles.mdFont, {color: Colors.fontGray}]}>연결 계좌</Text>
            <View style={styles.infoContainer}>
              <Image style={styles.bankLogo} source={bankLogo} resizeMode="stretch" />
              <Text style={styles.mdFont}>{bankName}</Text>
              <Text style={styles.mdFont}>{connectedAccount.accountNum}</Text>
            </View>
          </View>
          <Pressable onPress={() => setModalVisible(true)}>
            <Ionicons name="ellipsis-vertical-outline" size={16} />
          </Pressable>
          <Modal
            animationType='fade'
            transparent={true}
            visible={modalVisible}
            onRequestClose={() => setModalVisible(false)}          
          >
            <Pressable style={styles.modalBack} onPress={() => setModalVisible(false)}>
              <View style={styles.modal}>
                <Pressable onPress={disconnectAccount}>
                  <Text style={styles.modalMenu}>연결 끊기</Text>
                </Pressable>
                <View style={styles.line} />
                <Pressable onPress={() => setModalVisible(false)}>
                  <Text style={styles.modalCloseMenu}>닫기</Text>
                </Pressable>
              </View>
            </Pressable>
          </Modal>
        </>
        :
        <>
          <Text style={[styles.mdFont, {color: Colors.fontGray}]}>연결된 계좌가 없습니다</Text>
          <Pressable onPress={() => navigation.navigate('connection')}>
            <Ionicons name="add-circle-outline" size={16} color={Colors.fontGray} />
          </Pressable>
        </>
      }
    </View>
  )
}

export default ConnectedAccount;

const styles = StyleSheet.create({
  container: {
    width: '90%',
    backgroundColor: 'white',
    borderRadius: 10,
    padding: 18,
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
    borderWidth: 0.5,
    borderColor: Colors.fontGray
  },
  mdFont: {
    fontSize: 12,
    fontFamily:'pretendard-medium'
  },
  bankLogo: {
    width: 16,
    height: 16,
    borderRadius: 2.5
  },
  innerContainer: {
    gap: 10,
    flexDirection: 'row',
    alignItems: 'center'
  },
  infoContainer: {
    gap: 6,
    flexDirection: 'row',
    alignItems: 'center'
  },
  modalBack : {
    justifyContent: 'flex-end',
    flexGrow: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.25)'
  },
  modal: {
    backgroundColor: 'white',
    borderTopRightRadius: 20,
    borderTopLeftRadius: 20
  },
  line: {
    height: 1,
    borderTopColor: Colors.fontGray,
    borderWidth: 0.5
  },
  modalMenu: {
    fontSize: 15,
    fontFamily: 'pretendard-regular',
    paddingHorizontal: 24,
    paddingVertical: 16
  },
  modalCloseMenu: {
    textAlign: 'center',
    fontSize: 14,
    fontFamily: 'pretendard-medium',
    paddingVertical: 12,
    color: Colors.fontGray
  }
});