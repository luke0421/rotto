import { View, Text, FlatList, StyleSheet, Pressable, Alert } from 'react-native';
import Colors from '../../constants/Colors';
import dayjs from 'dayjs';
import { useCallback, useState } from 'react';
import { useFocusEffect } from '@react-navigation/native';
import { getApplyHistory } from '../../utils/investApi';
import { Ionicons } from '@expo/vector-icons';
import { cancelFunding as cancel } from '../../utils/fundingApi';

const ApplyList = ({navigation}) => {
  const [data, setData] = useState();

  useFocusEffect(
    useCallback(() => {
      const getApplyData = async () => {
        const res = await getApplyHistory();
        setData(res.userApplyHistoryListGetDtos);
      };

      getApplyData();
    }, [])
  );

  const cancelFunding = async (subscriptionCode) => {
    const res = await cancel(subscriptionCode);
    if (res.status === 200) {
      setData(pre => pre.filter(el => el.subscriptionCode !== subscriptionCode));
      return Alert.alert('해지 완료되었습니다.');
    } else {
      return Alert.alert('해지에 실패하셨습니다. 다시시도 해주세요.')
    }
  };

  const cancelHandler = async (subscriptionCode) => {
    Alert.alert('', '정말 해지하시겠습니까?', 
      [
        { text: '해지', onPress: () => cancelFunding(subscriptionCode), style: 'destructive' },
        { text: '취소', onPress: () => {}, style: 'cancel' }
      ],
      {
        cancelable: true,
        onDismiss: () => {}
      }
    );
  };

  return(
    data &&
    <FlatList 
      showsVerticalScrollIndicator={false}
      data={data}
      renderItem={itemData => {
        return (
          <Pressable 
            style={styles.cardContainer}
            onPress={() => {
              if (dayjs(itemData.item.endTime) >= dayjs()) {
                navigation.navigate('Routers', {
                  screen: '청약',
                  params: {
                    screen: 'fundingDetail',
                    params: { subscriptionCode: itemData.item.subscriptionCode }
                  }
                })
              } else {
                navigation.navigate('Routers', {
                  screen: '발견',
                  params: {
                    screen: 'farm',
                    params: { farmCode: itemData.item.farmCode }
                  }
                })
              }
            }}
          >
            <View style={styles.topContainer}>
              <Text style={styles.farmName}>{itemData.item.farmName}</Text>
              <View style={styles.rightContainer}>
                <Text style={styles.menu}>{dayjs(itemData.item.startedTime).add(9, 'hour').format('YYYY.MM.DD')} - {dayjs(itemData.item.endTime).add(9, 'hour').format('YYYY.MM.DD')}</Text>
                <Pressable onPress={() => cancelHandler(itemData.item.subscriptionCode)}>
                  <Ionicons name="close-circle-outline" size={20} color={Colors.fontGray} />
                </Pressable>
              </View>
            </View>
            <View style={styles.midContainer}>
              <View style={styles.contentContainer}>
                <Text style={styles.menu}>신청률</Text>
                <Text style={styles.content}>{itemData.item.totalApplyCount} / {itemData.item.totalTokenCount} ROT ({Math.round(itemData.item.totalApplyCount / itemData.item.totalTokenCount * 100 * 100) / 100}%)</Text>
              </View>
            </View>
            <View style={styles.bottomContainer}>
              <View style={styles.contentContainer}>
                <Text style={styles.menu}>잔여 수량 환불 예정일</Text>
                <Text style={styles.content}>{itemData.item.refundDate ? dayjs(itemData.item.refundDate).add(9, 'hour').format('YYYY.MM.DD') : '미정' }</Text>
              </View>
              <Text style={styles.state}>
                {
                  dayjs(itemData.item.endTime) >= dayjs() ? 
                  '청약 진행중'
                  :
                  '청약 마감'
                }
              </Text>
            </View>
          </Pressable>
        )
      }}
      keyExtractor={(item) => {
        return item.applyHistoryCode
      }}
      contentContainerStyle={{ flexGrow: 1 }}
    />
  )
}

export default ApplyList;

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
  topContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  farmName: {
    fontSize: 14,
    fontFamily: 'pretendard-semiBold'
  },
  contentContainer: {
    gap: 3
  },
  midContainer: {
    flexDirection: 'row',
    gap: 24
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
  },
  rightContainer: {
    flexDirection: 'row',
    gap: 6,
    alignItems: 'center'
  }
});