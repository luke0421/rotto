import { View, Pressable, Text, StyleSheet, Modal, TouchableWithoutFeedback } from 'react-native';
import Colors from '../../constants/Colors';
import { useDispatch, useSelector } from 'react-redux';
import { setDisconnectModal } from '../../stores/mySlice';

const DisconnectModal = ({onDisconnect}) => {
  const dispatch = useDispatch();
  const modalVisible = useSelector(state => state.myPageInfo.isDisconnectModalOpen);

  return (
    <Modal
      animationType='fade'
      transparent={true}
      visible={modalVisible}
      onRequestClose={() => dispatch(setDisconnectModal(false))}          
    >  
      <Pressable style={styles.modalBack} onPress={() => dispatch(setDisconnectModal(false))}>
        <View style={styles.modal}>
          <TouchableWithoutFeedback>
            <View style={styles.selectContainer}>
              <Pressable style={styles.filterContainer} onPress={onDisconnect}>
                <Text style={styles.filterText}>연결 끊기</Text>
              </Pressable>
            </View>
          </TouchableWithoutFeedback>
          <View style={styles.line} />
          <Pressable style={styles.buttonContainer} onPress={() => dispatch(setDisconnectModal(false))}>
            <Text style={styles.modalCloseMenu}>닫기</Text>
          </Pressable>
        </View>
      </Pressable>
    </Modal>
  )
}

export default DisconnectModal;

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
    width: '85%',
    paddingVertical: 20,
    alignItems: 'center',
    gap: 18
  },
  filterText: {
    fontSize: 15,
    fontFamily: 'pretendard-regular',
  },
  filterContainer: {
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between'
  },
  buttonContainer: {
    width: '100%'
  }
});