import { View, Text, StyleSheet, Modal, Pressable, TextInput, TouchableWithoutFeedback } from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { setBeanType, setFilterModal, setMaxPrice, setMinPrice, setSortBy, setSubsStatus } from '../../stores/fundingSlice';
import Colors from '../../constants/Colors';
import { Ionicons } from '@expo/vector-icons';
import { useState } from 'react';

const FilterModal = () => {
  const modalVisible = useSelector(state => state.fundingInfo.isFilterModalOpen);
  const { filterData, selectedFilter, sortBy, subsStatus, beanType } = useSelector(state => state.fundingInfo);
  const dispatch = useDispatch();

  const [inputMinPrice, setInputMinPrice] = useState();
  const [inputMaxPrice, setInputMaxPrice] = useState();

  const minPriceHandler = (enteredText) => {
    setInputMinPrice(enteredText);
  };

  const maxPriceHandler = (enteredText) => {
    setInputMaxPrice(enteredText);
  };

  const selectFilter = (value) => {
    switch (selectedFilter) {
      case 'sortBy':
        dispatch(setSortBy(value));
        break;
      case 'subsStatus':
        dispatch(setSubsStatus(value));
        break;
      case 'beanType':
        dispatch(setBeanType(value));
        break;
      case 'price':
        dispatch(setMinPrice(value.minPrice));
        dispatch(setMaxPrice(value.maxPrice));
        break;
    }

    dispatch(setFilterModal(false));
  };

  const isSelected = (value) => {
    switch (selectedFilter) {
      case 'sortBy':
        return sortBy === value; 
      case 'subsStatus':
        return subsStatus === value;
      case 'beanType':
        return beanType === value;
      default:
        return false;
    }
  };


  return (
    <Modal
      animationType='fade'
      transparent={true}
      visible={modalVisible}
      onRequestClose={() => dispatch(setFilterModal(false))}          
    >  
      <Pressable style={styles.modalBack} onPress={() => dispatch(setFilterModal(false))}>
        <View style={styles.modal}>
          <View style={styles.selectContainer}>
            { 
              filterData ?
              filterData.map(el => (
                <Pressable key={el.text} style={styles.filterContainer} onPress={() => selectFilter(el.value)}>
                  <Text 
                    style={[
                      styles.filterText,
                      isSelected(el.value) && { fontFamily: 'pretendard-semiBold' }
                    ]}
                  >
                    {el.text}
                  </Text>
                  {
                    isSelected(el.value) &&
                    <Ionicons name="checkmark" size={20} color="black" />
                  }
                </Pressable>
              ))
              :
              <View>
                <Text>가격</Text>
                <TouchableWithoutFeedback>
                  <View style={styles.inputsContainer}>
                    <View style={styles.inputContainer}>
                      <TextInput
                        maxLength={10}
                        autoCapitalize="none"
                        autoCorrect={false}
                        placeholder="최소 가격"
                        keyboardType="number-pad"
                        style={styles.inputText}
                        onChangeText={minPriceHandler}
                        value={inputMinPrice}
                      />
                      <Text> 원</Text>
                    </View>
                    <Text> ~ </Text>
                    <View style={styles.inputContainer}>
                      <TextInput
                        maxLength={10}
                        autoCapitalize="none"
                        autoCorrect={false}
                        placeholder="최대 가격"
                        keyboardType="number-pad"
                        style={styles.inputText}
                        onChangeText={maxPriceHandler}
                        value={inputMaxPrice}
                      />
                      <Text> 원</Text>
                    </View>
                  </View>
                </TouchableWithoutFeedback>
              </View>
            }
          </View>
          <View style={styles.line} />
          <Pressable 
            style={styles.buttonContainer}
            onPress={() => {
              if (selectedFilter === 'price') {
                selectFilter({
                  minPrice: inputMinPrice,
                  maxPrice: inputMaxPrice
                });
              } else {
                dispatch(setFilterModal(false))
              }
            }}
          >
            <Text style={styles.modalCloseMenu}>{selectedFilter === 'price' ? '확인' : '닫기'}</Text>
          </Pressable>
        </View>
      </Pressable>
    </Modal>
  )
}

export default FilterModal;

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
  priceContainer: {
  },
  inputContainer: {
    flexDirection: "row",
    alignItems: "center",
    borderRadius: 3,
    borderColor: Colors.borderGray,
    borderWidth: 1,
    padding: 5,
    paddingHorizontal: 15,
    width: 150,
    justifyContent: "space-between",
  },
  inputsContainer: {
    width: '100%',
    marginTop: 20,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  inputText: {
    flex: 1
  },
  buttonContainer: {
    width: '100%'
  }
});