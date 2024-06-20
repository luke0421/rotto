import {
  ActivityIndicator,
  Dimensions,
  FlatList,
  Modal,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { useEffect, useRef, useState } from "react";
import { getFarmList } from "../../utils/farmAPi";
import { Ionicons } from "@expo/vector-icons";

import Colors from "../../constants/Colors";
import StackHeader from "../../components/common/StackHeader";
import FarmDetail from "../../components/discovery/FarmDetail";
import FilterButton from "../../components/discovery/FilterButton";
import ResetButton from "../../components/discovery/ResetButton";

let sortData = [
  { index: 0, name: "기본순", value: null },
  { index: 1, name: "최근 수익률 높은 순", value: "rate" },
  { index: 2, name: "마감 기한 빠른 순", value: "deadline" },
  { index: 3, name: "공모가 높은 순", value: "highPrice" },
  { index: 4, name: "공모가 낮은 순", value: "lowPrice" },
];

let fundingData = [
  { index: 0, name: "선택 안함", value: null },
  { index: 1, name: "진행 예정", value: 0 },
  { index: 2, name: "진행중", value: 1 },
  { index: 3, name: "종료", value: 2 },
];

let beanData = [
  { index: 0, name: "선택 안함", value: null },
  { index: 1, name: "자메이카 블루마운틴", value: "자메이카 블루마운틴" },
  { index: 2, name: "예멘 모카 아이리시", value: "예멘 모카 아이리시" },
  { index: 3, name: "예멘 애플 마티니", value: "예멘 애플 마티니" },
  { index: 4, name: "하와이 코나", value: "하와이 코나" },
  { index: 5, name: "파나마 게이샤", value: "파나마 게이샤" },
];

const { width } = Dimensions.get("window");

const FarmListScreen = () => {
  const [farms, setFarms] = useState([]);
  const [selectedSort, setSelectedSort] = useState(sortData[0]); // 기본순 선택
  const [fundingStatus, setFundingStatus] = useState(fundingData[0]); // 청약 진행 여부 선택
  const [selectedBean, setSelectedBean] = useState(beanData[0]); // 원두 종류 선택
  const [isLiked, setIsLiked] = useState(null);
  const [minPrice, setMinPrice] = useState(null); // 최저 가격 선택
  const [maxPrice, setMaxPrice] = useState(null); // 최고 가격 선택
  const [selectedCategory, setSelectedCategory] = useState(null); // 바텀 시트 표시 내용 선택
  const [keyword, setKeyword] = useState("");
  const [modalVisible, setModalVisible] = useState(false);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(null);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  

  const getList = async (
    page,
    sort,
    keyword,
    isLiked,
    subsStatus,
    minPrice,
    maxPrice,
    beanType
  ) => {
    if (!page) return;
    const res = await getFarmList(
      page,
      sort,
      keyword,
      isLiked,
      subsStatus,
      minPrice,
      maxPrice,
      beanType
    );
    setFarms(res.farms);
    setPage((prevPage) => prevPage + 1);
    setTotalPages(res.totalPages);
  };

  const loadFarms = async () => {
    if (page > totalPages) return;
    if (loading || !hasMore) return;

    setLoading(true);
    const newFarms = await getFarmList(
      page,
      selectedSort.value,
      keyword,
      isLiked ? true : null,
      fundingStatus.value,
      minPrice,
      maxPrice,
      selectedBean.value
    );
    if (newFarms.farms.length > 0) {
      setFarms((prevFarms) => [...prevFarms, ...newFarms.farms]);
      setPage((prevPage) => prevPage + 1);
    } else {
      setHasMore(false); // 더 이상 데이터가 없을 경우
    }
    setLoading(false);
  };

  const renderFooter = () => {
    if (!loading) return null;
    return <ActivityIndicator size="large" color={Colors.bgOrg} />;
  };

  const applyFilter = (category, name, id) => {
    if (category === "sort") {
      setSelectedSort(sortData.find((item) => item.index === id));
    } else if (category === "funding") {
      setFundingStatus(fundingData.find((item) => item.index === id));
    } else if (category === "bean") {
      setSelectedBean(beanData.find((item) => item.index === id));
    } else if (category === "search") {
    }
  };

  useEffect(() => {
    setPage(1)
    getList(
      1,
      selectedSort.value,
      keyword,
      isLiked ? true : null,
      fundingStatus.value,
      minPrice,
      maxPrice,
      selectedBean.value
    );
  }, [
    selectedSort,
    keyword,
    isLiked,
    fundingStatus,
    minPrice,
    maxPrice,
    selectedBean,
  ]);

  const renderFarmList = (itemData) => {
    return <FarmDetail data={itemData.item} />;
  };

  const handlePressResetButton = () => {
    setSelectedSort(sortData[0]);
    setFundingStatus(fundingData[0]);
    setSelectedBean(beanData[0]);
    setIsLiked(null);
    setMinPrice(null);
    setMaxPrice(null);
  };

  return (
    <View style={styles.screen}>
      <StackHeader
        screenName={"discovery"}
        title={"농장 검색"}
        color={Colors.bgOrg}
        search={true}
        onPress={() => {
          setModalVisible(true);
          setSelectedCategory("search");
        }}
      />
      <ScrollView
        contentContainerStyle={styles.topScroll}
        horizontal={true}
        showsHorizontalScrollIndicator={false}
      >
        <View style={styles.filterContainer}>
          <Pressable
            style={styles.sortButton}
            onPress={() => {
              setModalVisible(true);
              setSelectedCategory("sort");
            }}
          >
            <Text style={styles.filterFont}>{selectedSort.name}</Text>
            <Ionicons
              style={styles.filterIcon}
              name="chevron-down"
              size={20}
              color={Colors.iconGray}
            />
          </Pressable>
          {(selectedSort != sortData[0] ||
            selectedBean != beanData[0] ||
            minPrice != null ||
            maxPrice != null ||
            fundingStatus != fundingData[0] ||
            isLiked == true) && (
            <ResetButton
              filterName={"초기화"}
              onPress={handlePressResetButton}
            />
          )}
          <FilterButton
            filterName={fundingStatus.index != 0 ? fundingStatus.name : "청약"}
            isChecked={fundingStatus.name != fundingData[0].name}
            onPress={() => {
              setModalVisible(true);
              setSelectedCategory("funding");
            }}
          />
          <FilterButton
            filterName={selectedBean.index != 0 ? selectedBean.name : "원두"}
            isChecked={selectedBean.name !== beanData[0].name}
            onPress={() => {
              setModalVisible(true);
              setSelectedCategory("bean");
            }}
          />
          <FilterButton
            filterName={"관심 농장"}
            isChecked={isLiked}
            onPress={() => {
              setIsLiked(!isLiked);
            }}
          />
          <FilterButton
            filterName={
              !minPrice && !maxPrice
                ? "가격"
                : !minPrice
                ? `가격 ${maxPrice}원 이하`
                : !maxPrice
                ? `가격 ${minPrice}원 이상`
                : `가격 ${minPrice}원 ~ ${maxPrice}원`
            }
            isChecked={minPrice || maxPrice}
            onPress={() => {
              setModalVisible(true);
              setSelectedCategory("price");
            }}
          />
        </View>
      </ScrollView>
      {farms && (
        <FlatList
          contentContainerStyle={styles.farmsContainer}
          showsVerticalScrollIndicator={false}
          data={farms}
          keyExtractor={(item) => item.farmCode}
          renderItem={renderFarmList}
          onEndReached={loadFarms}
          onEndReachedThreshold={0.5}
          ListFooterComponent={renderFooter}
        />
      )}
      <Modal
        animationType="fade"
        transparent={true}
        visible={modalVisible}
      >
        <Pressable
          style={styles.modalBack}
          onPress={() => setModalVisible(false)}
        >
          <View style={styles.modal}>
            <View style={styles.selectContainer}>
              <ScrollView>
                {selectedCategory === "sort" &&
                  sortData.map((sortItem) => (
                    <View key={sortItem.index} style={styles.filterItem}>
                      <Pressable
                        onPress={() =>
                          applyFilter("sort", sortItem.name, sortItem.index)
                        }
                        style={styles.sortItem}
                      >
                        <Text
                          style={[
                            styles.sortItemText,
                            sortItem.name === selectedSort.name && {
                              fontFamily: "pretendard-bold",
                            },
                          ]}
                        >
                          {sortItem.name}
                        </Text>
                      </Pressable>
                      {sortItem.name === selectedSort.name && (
                        <Ionicons
                          style={{ paddingRight: 25 }}
                          name="checkmark"
                          size={20}
                          color="black"
                        />
                      )}
                    </View>
                  ))}
                {selectedCategory === "funding" &&
                  fundingData.map((item) => (
                    <View style={styles.filterItem} key={item.index}>
                      <Pressable
                        onPress={() =>
                          applyFilter("funding", item.name, item.index)
                        }
                        style={styles.sortItem}
                      >
                        <Text
                          style={[
                            styles.filterText,
                            item.name === fundingStatus.name && {
                              fontFamily: "pretendard-bold",
                            },
                          ]}
                        >
                          {item.name}
                        </Text>
                      </Pressable>
                      {item.name === fundingStatus.name && (
                        <Ionicons
                          style={{ paddingRight: 25 }}
                          name="checkmark"
                          size={20}
                          color="black"
                        />
                      )}
                    </View>
                  ))}
                {selectedCategory === "bean" &&
                  beanData.map((item) => (
                    <View key={item.index} style={styles.filterItem}>
                      <Pressable
                        onPress={() =>
                          applyFilter("bean", item.name, item.index)
                        }
                        style={styles.sortItem}
                      >
                        <Text
                          style={[
                            styles.sortItemText,
                            item.name === selectedBean.name && {
                              fontFamily: "pretendard-bold",
                            },
                          ]}
                        >
                          {item.name}
                        </Text>
                      </Pressable>
                      {item.name === selectedBean.name && (
                        <Ionicons
                          style={{ paddingRight: 25 }}
                          name="checkmark"
                          size={20}
                          color="black"
                        />
                      )}
                    </View>
                  ))}
                {selectedCategory === "price" && (
                  <View style={styles.priceContainer}>
                    <View style={styles.titleContainer}>
                      <Text>가격</Text>
                    </View>
                    <View style={styles.inputsContainer}>
                      <View style={styles.inputContainer}>
                        <TextInput
                          maxLength={10}
                          autoCapitalize="none"
                          autoCorrect={false}
                          onChangeText={(minPrice) => {
                            // 입력된 값이 숫자인지 확인합니다.
                            if (!isNaN(parseInt(minPrice))) {
                              // 입력된 값이 숫자라면, 앞에 0을 제거하고 setMinPrice를 호출합니다.
                              setMinPrice(parseInt(minPrice).toString());
                            } else {
                              // 입력된 값이 숫자가 아니라면, 빈 문자열을 setMinPrice를 호출하여 입력을 무시합니다.
                              setMinPrice("");
                            }
                          }}
                          placeholder="최소 가격"
                          keyboardType="number-pad"
                          value={minPrice}
                        />
                        <Text> 원</Text>
                      </View>
                      <Text> ~ </Text>
                      <View style={styles.inputContainer}>
                        <TextInput
                          maxLength={10}
                          autoCapitalize="none"
                          autoCorrect={false}
                          onChangeText={(maxPrice) => {
                            // 입력된 값이 숫자인지 확인합니다.
                            if (!isNaN(parseInt(maxPrice))) {
                              // 입력된 값이 숫자라면, 앞에 0을 제거하고 setMinPrice를 호출합니다.
                              setMaxPrice(parseInt(maxPrice).toString());
                            } else {
                              // 입력된 값이 숫자가 아니라면, 빈 문자열을 setMinPrice를 호출하여 입력을 무시합니다.
                              setMaxPrice("");
                            }
                          }}
                          placeholder="최대 가격"
                          keyboardType="number-pad"
                          value={maxPrice}
                        />
                        <Text> 원</Text>
                      </View>
                    </View>
                  </View>
                )}
                {selectedCategory === "search" && (
                  <View
                    style={[styles.searchContainer, { width: width * 0.9 }]}
                  >
                    <View style={styles.searchInputContainer}>
                      <Ionicons
                        name="search"
                        size={24}
                        color={Colors.fontGray}
                        style={{ marginRight: 10 }}
                      />
                      <TextInput
                        maxLength={10}
                        autoCapitalize="none"
                        autoCorrect={false}
                        onChangeText={(keyword) => setKeyword(keyword)}
                        value={keyword}
                        placeholder="검색"
                        style={{ width: "83%" }}
                      />
                      {keyword && (
                        <Ionicons
                          name="close-circle-outline"
                          size={24}
                          color={Colors.fontGray}
                          onPress={() => setKeyword(null)}
                        />
                      )}
                    </View>
                  </View>
                )}
              </ScrollView>
            </View>
          </View>
          <View style={styles.buttonContainer}>
            <Text style={styles.modalCloseMenu}>
              {selectedCategory === "price" ? "확인" : "닫기"}
            </Text>
          </View>
        </Pressable>
      </Modal>
    </View>
  );
};

export default FarmListScreen;

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: "white",
  },
  topScroll: {
    height: "auto",
    paddingRight: 15,
  },
  sortButton: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    height: 40,
    marginLeft: 10,
  },
  filterContainer: {
    marginTop: 10,
    height: 50,
    flexDirection: "row",
  },
  filterIcon: {
    marginLeft: 5,
  },
  filterFont: {
    marginBottom: 3,
    marginLeft: 8,
    fontFamily: "pretendard-bold",
    fontSize: 15,
  },
  filterItem: {
    width: "100%",
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  filterFontEmp: {
    marginBottom: 3,
    marginLeft: 8,
    fontFamily: "pretendard-bold",
    fontSize: 15,
  },
  farmsContainer: {
    minHeight: 800,
    alignItems: "center",
    marginTop: 0,
  },
  sortItem: {
    paddingVertical: 10,
    paddingHorizontal: 25,
    // borderBottomWidth: 1,
    // borderBottomColor: Colors.borderGray,
  },
  sortItemText: {
    fontSize: 15,
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
    width: "98%",
    marginTop: 20,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  priceContainer: {
    marginTop: 10,
    marginHorizontal: 25,
    alignItems: "center",
  },
  titleContainer: {
    width: "100%",
    alignItems: "flex-start",
  },
  searchContainer: {
    marginTop: 10,
    marginHorizontal: 20,
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
  },
  modalCloseMenu: {
    textAlign: "center",
    fontSize: 14,
    fontFamily: "pretendard-medium",
    paddingVertical: 12,
    color: Colors.fontGray,
  },
  modalBack: {
    justifyContent: "flex-end",
    flexGrow: 1,
    backgroundColor: "rgba(0, 0, 0, 0.25)",
  },
  modal: {
    backgroundColor: "white",
    borderTopRightRadius: 20,
    borderTopLeftRadius: 20,
    alignItems: "center",
  },
  selectContainer: {
    width: "100%",
    paddingVertical: 20,
    alignItems: "flex-start",
    gap: 18,
  },
  buttonContainer: {
    width: "100%",
    borderTopColor: Colors.borderGray,
    borderTopWidth: 1,
    backgroundColor: "white",
  },
  filterText: {
    fontSize: 15,
    fontFamily: "pretendard-regular",
  },
});
