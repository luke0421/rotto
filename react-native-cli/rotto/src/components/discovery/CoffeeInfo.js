import React, {useRef, useState} from 'react';
import {
  Dimensions,
  Image,
  ImageBackground,
  Pressable,
  StyleSheet,
  Text,
  View,
  useWindowDimensions,
} from 'react-native';
// import { COFFEE_BEAN_INFO } from "../../../assets/data/bean-data";
import Colors from '../../constants/Colors';

export const COFFEE_BEAN_INFO = [
  {
    id: 0,
    name: '자메이카 블루마운틴',
    description:
      '자메이카 블루마운틴은 세계 3대 원두로, 자메이카 블루마운틴 지역 해발 1,200미터가 넘는 고지대에서만 재배되고, 정부의 통제 아래 매년 적은 양만 생산합니다. 커피가 가진 신맛, 단맛, 스모키한 맛을 비롯해 부드러우면서도 깔끔한 맛을 조화롭게 내며, 쓴 맛이 거의 없어 ‘커피의 황제’라고도 불리웁니다.',
    height: '해발 900 ~ 1500 미터',
    harvest: '6월 ~ 7월',
    imageUrl: require('../../../assets/images/discovery/coffeebean3.png'),
  },
  {
    id: 1,
    name: '예멘 모카 아이리시',
    description:
      "아라비아 반도 남서부에 위치한 예멘 국가 마타리에서 생산되는 커피로 '마타리'는 예맨의 최고급 커피 등급으로, 한때 세계 커피 무역의 최고로 꼽혔던 항구인 '모카항'이름을 딴 커피입니다. 세계 3대 원두 중 하나로, 과일맛이 풍부하고 흙 냄새와 다크 초콜릿의 향이 조화롭고, 적절한 쓴맛과 단맛을 지니고 있습니다.",
    height: '해발 1800 ~ 2000 미터',
    harvest: '12월 ~ 2월',
    imageUrl: require('../../../assets/images/discovery/coffeebean8.png'),
  },
  {
    id: 2,
    name: '예멘 애플 마티니',
    height: '해발 2350 미터',
    harvest: '12월 ~ 2월',
    description:
      '예멘의 고지대인 퀴마에서 생산되는 커피로, 이름에서 알 수 있듯이 애플 마티니와 같은 상큼한 사과 향을 가지고 있습니다. 또한, 카라멜과 같은 달콤한 맛과 함께 복합적인 향미를 즐길 수 있습니다. 맛의 밸런스가 뛰어나며, 부드럽고 긴 여운이 남습니다.',
    imageUrl: require('../../../assets/images/discovery/coffeebean1.png'),
  },
  {
    id: 3,
    name: '하와이 코나',
    description:
      '하와이 코나는 하와이 코나섬에서 재배, 수확한 커피를 말하는데 여기서도 최고 등급인 엑스트라 펜시라는 등급에 한해서만 세계 3대 원두로 분류됩니다. 커피에 꽃향과 브라과일향이 은은하게 나며, 적당한 신맛이 일품이라 평가받습니다.',
    height: '해발 150 ~ 900 미터',
    harvest: '8월 ~ 1월',
    imageUrl: require('../../../assets/images/discovery/coffeebean9.png'),
  },
  {
    id: 4,
    name: '파나마 게이샤',
    description:
      '파나마 지협에 있는 파나마는 고지대, 비옥한 화산 토양, 풍부한 강우량등의 조건이 좋아 훌륭한 커피를 생산해 내기로 유명하며, 파나마의 게이샤는 최고급 스페셜티 커피로 꽃의 향기와 좋은 산미, 바디감으로 커피 마니아들 사이에서 사랑받고 있다.',
    height: '해발 1400 ~ 1800 미터',
    harvest: '12월 ~ 4월',
    imageUrl: require('../../../assets/images/discovery/coffeebean11.png'),
  },
];
const {width} = Dimensions.get('window');

const CoffeeInfo = () => {
  const [selectedItem, setSelectedItem] = useState(null);
  const [imagePath, setImagePath] = useState(
    require('../../../assets/images/discovery/coffeebean3.png'),
  );

  const updateSelect = id => {
    if (id === selectedItem) {
      setSelectedItem(null);
      setImagePath(null);
    } else {
      setImagePath(COFFEE_BEAN_INFO[id].imageUrl);
      setSelectedItem(id);
    }
  };

  const renderBeanInfo = itemData => {
    const isSelected = selectedItem === itemData.item.id;
    return (
      <Pressable
        style={[
          styles.item,
          isSelected && styles.selectedName, // 선택된 아이템의 테두리 색상 변경
        ]}
        onPress={() => updateSelect(itemData.item.id)}>
        <Text style={styles.title}>{itemData.item.name}</Text>
      </Pressable>
    );
  };

  return (
    <View style={styles.container}>
      <ImageBackground
        source={
          imagePath
            ? imagePath
            : require('../../../assets/images/discovery/coffeebean3.png')
        }
        style={{width: width - 30, height: 300}}
        resizeMode="cover"
      />
      <View style={styles.flatListContainer}>
        {COFFEE_BEAN_INFO.map(item => (
          <View key={item.id} style={styles.flatListItemContainer}>
            {renderBeanInfo({item})}
          </View>
        ))}
        {selectedItem != null && imagePath != null && (
          <View style={styles.description}>
            <>
              <Text style={styles.selectedInfo}>
                해발고도: {COFFEE_BEAN_INFO[selectedItem].height}
              </Text>
              <Text style={styles.selectedInfo}>
                수확시기: {COFFEE_BEAN_INFO[selectedItem].harvest}
              </Text>
              <Text style={styles.descriptionFont}>
                {COFFEE_BEAN_INFO[selectedItem].description}
              </Text>
            </>
          </View>
        )}
      </View>
    </View>
  );
};

export default CoffeeInfo;

const styles = StyleSheet.create({
  container: {
    // width: width-30,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 10,
    borderRadius: 10,
    overflow: 'hidden',
  },
  flatListContainer: {
    backgroundColor: 'white',
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 10,
    paddingBottom: 20,
  },
  flatListItemContainer: {
    width: 130,
    height: 30,
    justifyContent: 'center',
    alignItems: 'center',
    margin: 5,
  },
  item: {
    width: '100%',
    height: '100%',
    justifyContent: 'center',
    alignItems: 'center',
    // borderWidth: 2,
    borderColor: Colors.borderGray,
  },
  description: {
    width: '95%',
    alignItems: 'flex-start',
    marginTop: 10,
    height: width * 0.35,
  },
  descriptionFont: {
    fontFamily: 'pretendard-regular',
    fontSize: 12,
    marginTop: 10,
  },
  title: {
    fontFamily: 'pretendard-semiBold',
    fontSize: 12,
  },
  selectedName: {
    backgroundColor: Colors.bgGray,
  },
  selectedInfo: {
    fontFamily: 'pretendard-regular',
    fontSize: 12,
  },
});
