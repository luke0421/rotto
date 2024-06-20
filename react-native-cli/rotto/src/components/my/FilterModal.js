import { View, Pressable, Text, StyleSheet, Modal, Image } from 'react-native';
import Colors from '../../constants/Colors';
import { useDispatch, useSelector } from 'react-redux';
import { setFilterModal, setSelectedFilter } from '../../stores/mySlice';
import { Ionicons } from '@expo/vector-icons';

const FilterModal = () => {
  const dispatch = useDispatch();
  const modalVisible = useSelector(state => state.myPageInfo.isFilterModalOpen);
  const selectedFilter = useSelector(state => state.myPageInfo.selectedFilter);

  const filterList = [
    {
      id: 0,
      type: '전체'
    },
    {
      id: 1,
      type: '입금'
    },
    {
      id: 2,
      type: '출금'
    }
  ];

  const selectFilter = (type) => {
    dispatch(setSelectedFilter(type));
    dispatch(setFilterModal(false));
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
              filterList.map(el => (
                <Pressable key={el.id} style={styles.filterContainer} onPress={() => selectFilter(el.type)}>
                  <Text 
                    style={[
                      styles.filterText, 
                      selectedFilter === el.type && { fontFamily: 'pretendard-semiBold' }
                    ]}
                  >
                    {el.type}
                  </Text>
                  {
                    selectedFilter === el.type &&
                    <Ionicons name="checkmark" size={20} color="black" />
                  }
                </Pressable>
              ))
            }
          </View>
          <View style={styles.line} />
          <Pressable onPress={() => dispatch(setFilterModal(false))}>
            <Text style={styles.modalCloseMenu}>닫기</Text>
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
  }
});