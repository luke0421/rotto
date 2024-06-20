import { View, Text, StyleSheet, FlatList, TouchableWithoutFeedback, Pressable } from 'react-native';
import Colors from '../../constants/Colors';
import { useCallback, useState } from 'react';
import { useFocusEffect } from '@react-navigation/native';
import { getTradeHistory } from '../../utils/investApi';

const TradeList = ({navigation}) => {
  const [data, setData] = useState();

  useFocusEffect(
    useCallback(() => {
      const getTradeData = async () => {
        const res = await getTradeHistory();
        setData(res.tradeHistoryListDtoss);
      };

      getTradeData();
    }, [])
  )

  return (
    data &&
    <FlatList 
      showsVerticalScrollIndicator={false} 
      data={data}
      renderItem={itemData => {
        return (
          <Pressable 
            style={styles.cardContainer} 
            onPress={() => navigation.navigate('Routers', { 
              screen: '발견',
              params: {
                screen: 'farm',
                params: { farmCode: itemData.item.farmCode }
              }
            })}
          >
            <Text style={styles.farmName}>{itemData.item.farmName}</Text>
            <View style={styles.contentContainer}>
              <Text style={styles.menu}>내 보유 ROTTO</Text>
              <Text style={styles.content}>{itemData.item.tradeNum} / {itemData.item.totalTokenCount} ROT</Text>
            </View>
            <View style={styles.bottomContainer}>
              <View style={styles.contentContainer}>
                <Text style={styles.menu}>내 투자 금액</Text>
                <Text style={styles.content}>{(itemData.item.confirmPrice * itemData.item.tradeNum).toLocaleString('ko-KR')} 원</Text>
              </View>
              <Text style={styles.state}>{itemData.item.refund ? '출하' : '재배중'}</Text>
            </View>
          </Pressable>
        )
      }}
      keyExtractor={(item) => {
        return item.subscriptionCode
      }}
      contentContainerStyle={{ flexGrow: 1 }}
    />
  )
}

export default TradeList;

const styles = StyleSheet.create({
  cardContainer: {
    width: '100%',
    paddingHorizontal: 16,
    paddingVertical: 12,
    justifyContent: 'center',
    borderRadius: 10,
    borderColor: 'black',
    borderWidth: 0.5,
    gap: 3,
    marginBottom: 10,
  },
  farmName: {
    fontSize: 14,
    fontFamily: 'pretendard-semiBold'
  },
  contentContainer: {
    gap: 3
  },
  menu: {
    fontSize: 10,
    color: Colors.fontGray,
    fontFamily: 'pretendard-medium'
  },
  content: {
    fontSize: 12,
    fontFamily: 'pretendard-medium'
  },
  bottomContainer: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    justifyContent: 'space-between'
  },
  state: {
    fontSize: 12,
    fontFamily: 'pretendard-medium'
  }
})