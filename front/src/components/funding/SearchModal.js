import { View, Text, StyleSheet, Modal, Pressable, TextInput, TouchableWithoutFeedback } from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { setKeyword, setSearchModal } from '../../stores/fundingSlice';
import Colors from '../../constants/Colors';
import { Ionicons } from '@expo/vector-icons';
import { useState } from 'react';

const SearchModal = () => {
  const modalVisible = useSelector(state => state.fundingInfo.isSearchModalOpen);
  const dispatch = useDispatch();

  const [inputKeyword, setInputKeyword] = useState(null);

  const keywordHandler = (enteredText) => {
    setInputKeyword(enteredText);
  };

  return (
    <Modal
      animationType='fade'
      transparent={true}
      visible={modalVisible}
      onRequestClose={() => dispatch(setSearchModal(false))}          
    >  
      <Pressable style={styles.modalBack} onPress={() => {
        dispatch(setSearchModal(false));
        setInputKeyword(null);
      }}>
        <View style={styles.modal}>
          <View style={styles.selectContainer}>
            <View style={styles.searchInputContainer}>
              <Ionicons name="search" size={24} color={Colors.fontGray} style={{ marginRight: 10 }}/>
              <TextInput 
                maxLength={20} 
                autoCapitalize="none" 
                autoCorrect={false} 
                onChangeText={keywordHandler}
                value={inputKeyword}
                placeholder="검색"
                style={{width:"83%"}}
              />
              {inputKeyword && 
                <Ionicons name="close-circle-outline" size={24} color={Colors.fontGray} onPress={() => setInputKeyword(null)}/>
              }
            </View>
          </View>
          <View style={styles.line} />
          <Pressable  style={styles.buttonContainer} onPress={() => {
            dispatch(setKeyword(inputKeyword));
            dispatch(setSearchModal(false));
            setInputKeyword(null);
          }}>
            <Text style={styles.modalCloseMenu}>검색</Text>
          </Pressable>
        </View>
      </Pressable>
    </Modal>
  )
}

export default SearchModal;

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
  buttonContainer: {
    width: '100%'
  },
  searchInputContainer: {
    flexDirection: "row",
    width: "100%",
    borderRadius: 5,
    borderWidth: 1,
    borderColor: Colors.borderGray,
    paddingVertical: 5,
    paddingHorizontal: 10,
    alignItems: "center",
  }
});