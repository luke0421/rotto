import { View, Pressable, Text, StyleSheet, Modal, Image } from 'react-native';
import Colors from '../../constants/Colors';
import { useDispatch, useSelector } from 'react-redux';
import { setBankModal, setSelectedBank } from '../../stores/mySlice';

const BankSelectModal = () => {
  const dispatch = useDispatch();
  const modalVisible = useSelector(state => state.myPageInfo.isBankModalOpen);

  const bankList = [
    {
      bankName: '한국은행',
      bankCode: '001',
      imgUrl: require('../../../assets/images/bank/bokLogo.png')
    },
    {
      bankName: '산업은행',
      bankCode: '002',
      imgUrl: require('../../../assets/images/bank/kbLogo.png')
    },
    {
      bankName: '기업은행',
      bankCode: '003',
      imgUrl: require('../../../assets/images/bank/ibkLogo.png')
    },
    {
      bankName: '국민은행',
      bankCode: '004',
      imgUrl: require("../../../assets/images/bank/kbLogo.png")
    }
  ];

  const selectBank = (idx) => {
    dispatch(setSelectedBank(bankList[idx]));
    dispatch(setBankModal(false));
  };

  return (
    <Modal
      animationType='fade'
      transparent={true}
      visible={modalVisible}
      onRequestClose={() => dispatch(setBankModal(false))}          
    >  
      <Pressable style={styles.modalBack} onPress={() => dispatch(setBankModal(false))}>
        <View style={styles.modal}>
          <View style={styles.selectContainer}>
            {
              bankList.map((el, idx) => (
                <Pressable 
                  style={styles.bankContainer} key={el.bankCode}
                  onPress={() => selectBank(idx)}
                >
                  <View style={styles.logoBox}>
                    <Image style={styles.bankLogo} source={el.imgUrl} resizeMode='stretch' />
                  </View>
                  <Text style={styles.bankName}>{el.bankName}</Text>
                </Pressable>
              ))
            }
          </View>
          <View style={styles.line} />
          <Pressable onPress={() => dispatch(setBankModal(false))}>
            <Text style={styles.modalCloseMenu}>닫기</Text>
          </Pressable>
        </View>
      </Pressable>
    </Modal>
  )
}

export default BankSelectModal;

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
  selectContainer: {
    width: '90%',
    paddingVertical: 20,
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between'
  },
  bankContainer: {
    width: '22%',
    alignItems: 'center',
    height: 70,
    justifyContent: 'space-between'
  },
  logoBox: {
    width: '55%',
    height: 45,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 10,
    overflow: 'hidden'
  },
  bankLogo: {
    width: '100%',
    height: '100%'
  },
  bankName: {
    fontSize: 14,
    fontFamily:'pretendard-medium'
  }
});