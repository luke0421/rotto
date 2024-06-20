import { ScrollView, View, Pressable, Text, StyleSheet } from "react-native";
import Colors from "../../constants/Colors";
import { Ionicons } from '@expo/vector-icons';
import FilterButton from "../discovery/FilterButton";
import { useDispatch, useSelector } from "react-redux";
import { setFilterData, setFilterModal, setFilter, setSortBy, setSubsStatus, setBeanType, setMinPrice, setMaxPrice, setKeyword } from "../../stores/fundingSlice";
import ResetButton from "../discovery/ResetButton";

const FilterBar = () => {
  const { 
    sortBy, 
    subsStatus, 
    minPrice, 
    maxPrice, 
    beanType,
    keyword
  } = useSelector(state => state.fundingInfo);

  const dispatch = useDispatch();

  const filters = {
    sortBy: [
      { value:  null, text: '기본순'},
      { value: 'rate', text: '수익률 높은 순' },
      { value: 'deadline', text: '마감 기한 빠른 순' },
      { value: 'highPrice', text: '공모가 높은 순' },
      { value: 'lowPrice', text: '공모가 낮은 순' },
      { value: 'highApplyPercent', text: '신청률 높은 순' }
    ],
    subsStatus: [
      { value: null, text: '전체' },
      { value: 0, text: '청약 예정' },
      { value: 1, text: '청약 진행중' },
      { value: 2, text: '청약 마감' }
    ],
    beanType: [
      { text: '선택 안함', value: null },
      { text: '예멘 모카 아이리시', value: '예멘 모카 아이리시' },
      { text: '예멘 애플 마티니', value: '예멘 애플 마티니' },
      { text: '자메이카 블루마운틴', value: '자메이카 블루마운틴' },
      { text: '파나마 게이샤', value: '파나마 게이샤' },
      { text: '하와이 코나', value: '하와이 코나' }
    ]
  };

  const resetHandler = () => {
    dispatch(setSubsStatus(null));
    dispatch(setBeanType(null));
    dispatch(setMinPrice(null));
    dispatch(setMaxPrice(null));
    dispatch(setKeyword(null));
  };

  return (
    <View style={styles.container}>
      <ScrollView horizontal={true} showsHorizontalScrollIndicator={false}>
        <View style={styles.filterContainer}>
          <Pressable
            style={styles.sortButton}
            onPress={() => {
              dispatch(setFilterData(filters.sortBy));
              dispatch(setFilter('sortBy'));
              dispatch(setFilterModal(true));
            }}
          >
            <Text style={styles.filterFont}>{sortBy ? filters.sortBy.find(el => el.value === sortBy).text : '기본순'}</Text>
            <Ionicons
              style={styles.filterIcon}
              name="chevron-down"
              size={20}
              color={Colors.iconGray}
            />
          </Pressable>
          {
            (subsStatus !== null || beanType || minPrice || maxPrice || keyword) &&
            <ResetButton filterName={"초기화"} onPress={resetHandler} />
          }
          <FilterButton 
            filterName={subsStatus !== null ? filters.subsStatus.find(el => el.value === subsStatus).text : '진행 여부'} 
            isChecked={subsStatus !== null} 
            onPress={() => {
              dispatch(setFilterData(filters.subsStatus));
              dispatch(setFilter('subsStatus'));
              dispatch(setFilterModal(true));
            }} 
          />
          <FilterButton 
            filterName={beanType ? filters.beanType.find(el => el.value === beanType).text : '원두'} 
            isChecked={beanType} 
            onPress={() => {
              dispatch(setFilterData(filters.beanType));
              dispatch(setFilter('beanType'));
              dispatch(setFilterModal(true));
            }} 
          />
          <FilterButton 
            filterName={
              (!minPrice && !maxPrice) ? "가격" 
              : (!minPrice) ? `가격 ${maxPrice}원 이하`
              : (!maxPrice) ? `가격 ${minPrice}원 이상`
              : `가격 ${minPrice}원 ~ ${maxPrice}원`
            } 
            isChecked={minPrice || maxPrice} 
            onPress={() => {
              dispatch(setFilterData(null));
              dispatch(setFilter('price'));
              dispatch(setFilterModal(true));
            }} 
          />
          {
            keyword &&
            <Pressable style={styles.filterButton}>
              <Text style={styles.filterButtonText}>검색: {keyword}</Text>
              <Ionicons name="close-circle-outline" size={18} onPress={() => dispatch(setKeyword(null))}/>
            </Pressable>
          }
        </View>
      </ScrollView>
    </View>
  )
}

export default FilterBar;

const styles = StyleSheet.create({
  container: {
    height: 35,
    marginTop: 10
  },
  sortButton: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    height: 35,
    marginLeft: 10,
    borderRadius: 5,
    backgroundColor: "white",
    borderColor: Colors.borderGray,
    borderWidth: 1,
  },
  filterContainer: {
    flexDirection: "row",
    height: 35
  },
  filterIcon: {
    marginLeft: 5,
    paddingRight: 4
  },
  filterFont: {
    marginBottom: 3,
    marginLeft: 8,
    fontFamily: "pretendard-bold",
    fontSize: 15,
  },
  filterButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    height: 35,
    borderRadius: 5,
    backgroundColor: 'white',
    borderWidth: 1,
    marginLeft: 10,
    paddingHorizontal: 10
  },
  filterButtonText: {
    marginBottom: 3,
    marginRight: 4,
    fontFamily: 'pretendard-bold',
    fontSize: 15
  }
});