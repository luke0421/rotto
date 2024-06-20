import { View, FlatList, Text, StyleSheet, Pressable, ActivityIndicator, Dimensions } from 'react-native';
import { FontAwesome5 } from '@expo/vector-icons';
import Colors from '../../constants/Colors';
import { useCallback, useEffect, useRef, useState } from 'react';
import { getFundingList } from '../../utils/fundingApi';
import dayjs from 'dayjs';
import { useFocusEffect } from '@react-navigation/native';
import { useSelector } from 'react-redux';

const FundingList = ({navigation}) => {
  const { sortBy, subsStatus, beanType, minPrice, maxPrice, keyword } = useSelector(state => state.fundingInfo);
  
  const [data, setData] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [totalPage, setTotalPage] = useState(1);
  const [refreshing, setRefreshing] = useState(false);

  const listRef = useRef(null);

  const getFundingData = async (isInit) => {
    if (isInit || (page <= totalPage && data.length >= 10 * page)) {
      setLoading(true);
      const res = await getFundingList({
        'sort': sortBy,
        'subs-status': subsStatus,
        'bean-type': beanType,
        'min-price': minPrice,
        'max-price': maxPrice,
        'page': isInit ? 1 : page + 1,
        'keyword': keyword
      });
      setData(pre => isInit ? res.subscriptions : [...pre, ...res.subscriptions]);
      setTotalPage(res.totalPages);
      setPage(pre => isInit ? 1 : pre + 1);
      if (isInit) {
        listRef.current.scrollToOffset({ animated: true, offset: 0 });
      }
      setLoading(false);
    }
  };

  const refreshFundingData = async () => {
    setRefreshing(true);
    const res = await getFundingList({
      'sort': sortBy,
      'subs-status': subsStatus,
      'bean-type': beanType,
      'min-price': minPrice,
      'max-price': maxPrice,
      'page': 1,
      'keyword': keyword
    });
    setData(res.subscriptions);
    setTotalPage(res.totalPages);
    setPage(1);
    setRefreshing(false);
  };

  useFocusEffect(
    useCallback(() => {
      getFundingData(true);
    }, [sortBy, subsStatus, beanType, minPrice, maxPrice, keyword])
  );

  const getState = (state, startedTime) => {
    switch (state) {
      case 0:
        return {
          text: `D-${Math.floor(dayjs(startedTime).add(9, 'hour').diff(dayjs(), "day", true))}`,
          color: Colors.btnYellow
        };
      case 1:
        return {
          text: '청약 진행중',
          color: Colors.btnBlue
        }
      case 2:
        return {
          text: '마감',
          color: Colors.fontGray
        }
    }
  };

  const onEndReached = () => {
    if (!loading) {
      getFundingData(false);
    }
  };
  
  const onRefresh = () => {
    if (!refreshing) {
      refreshFundingData();
    }
  }

  return (
    <View style={styles.container}>
      <FlatList 
        ref={listRef}
        data={data}
        showsVerticalScrollIndicator={false}
        renderItem={itemData => {
          return (
            <Pressable 
              style={styles.cardContainer} 
              onPress={() => navigation.navigate('fundingDetail', { 
                subscriptionCode: itemData.item.subscriptionCode 
              })}
            >
              <View style={styles.topContainer}>
                <Text style={styles.date}>
                  {dayjs(itemData.item.startedTime).add(9, 'hour').format('YYYY.MM.DD')} - {dayjs(itemData.item.endTime).add(9, 'hour').format('YYYY.MM.DD')}
                  </Text>
                <View style={[styles.stateContainer, { backgroundColor: getState(itemData.item.subsStatus, itemData.item.startedTime).color }]}>
                  <Text style={styles.state}>{getState(itemData.item.subsStatus, itemData.item.startedTime).text}</Text>
                </View>
              </View>
              <Text style={styles.farmName}>{itemData.item.farmName}</Text>
              <View style={styles.bottomContainer}>
                <View style={styles.contentContainer}>
                  <View style={styles.menuContainer}>
                    <FontAwesome5 name="coins" size={12} />
                    <Text style={styles.menu}>가격</Text>
                  </View>
                  <Text style={styles.content}>{itemData.item.confirmPrice.toLocaleString('ko-KR')} / 1ROT</Text>
                </View>
                {
                  itemData.item.subsStatus === 1 &&
                  <View style={styles.contentContainer}>
                    <View style={styles.menuContainer}>
                      <FontAwesome5 name="chart-pie" size={12} />
                      <Text style={styles.menu}>신청률</Text>
                    </View>
                    <Text style={styles.content}>
                    {itemData.item.applyCount.toLocaleString('ko-KR')} / {itemData.item.totalTokenCount.toLocaleString('ko-KR')} ROT
                    </Text>
                  </View>
                }
                <View style={styles.contentContainer}>
                  <View style={styles.menuContainer}>
                    <FontAwesome5 name="chart-line" size={12} />
                    <Text style={styles.menu}>지난 수익률</Text>
                  </View>
                  <Text style={[styles.content, { color: itemData.item.returnRate < 0 ? 'blue' : 'red'}]}>{itemData.item.returnRate > 0 && '+'}{itemData.item.returnRate}%</Text>
                </View>
              </View>
            </Pressable>
          )
        }}
        keyExtractor={(item) => {
          return item.subscriptionCode
        }}
        contentContainerStyle={{ flexGrow: 1 }}
        onEndReached={onEndReached}
        onEndReachedThreshold={1}
        ListFooterComponent={loading && <ActivityIndicator />}
        onRefresh={onRefresh}
        refreshing={refreshing}
      />
    </View>
  )
}

export default FundingList;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    marginTop: 16,
    borderTopLeftRadius: 25,
    borderTopRightRadius: 25,
    padding: 16
  },
  cardContainer: {
    paddingHorizontal: 16,
    paddingVertical: 10,
    borderRadius: 10,
    borderColor: Colors.fontGray,
    borderWidth: 0.5,
    gap: 12,
    marginBottom: 16
  },
  topContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  bottomContainer: {
    flexDirection: 'row',
    alignContent: 'center',
    gap: (Dimensions.get('window').width - 64) * 0.01 * 0.5,
    width: '100%'
  },
  date: {
    fontSize: 10,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray
  },
  state: {
    fontSize: 10,
    fontFamily: 'pretendard-bold',
    color: 'white'
  },
  stateContainer: {
    height: 20,
    width: 65,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10
  },
  farmName: {
    fontSize: 16,
    fontFamily: 'pretendard-medium'
  },
  menu: {
    fontSize: 14,
    fontFamily: 'pretendard-medium'
  },
  menuContainer: {
    flexDirection: 'row',
    gap: 6,
    alignItems: 'center'
  },
  content: {
    fontSize: 12,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray
  },
  contentContainer: {
    gap: 5,
    width: '33%'
  }
});