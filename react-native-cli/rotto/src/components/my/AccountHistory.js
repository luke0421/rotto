import { View, Text, StyleSheet, ScrollView, Pressable, TouchableWithoutFeedback } from 'react-native';
import { useDispatch, useSelector } from 'react-redux';
import { Ionicons, FontAwesome } from '@expo/vector-icons';
import Colors from '../../constants/Colors';
import { useFocusEffect } from '@react-navigation/native';
import { useCallback, useEffect, useRef, useState } from 'react';
import FilterModal from './FilterModal';
import { setFilterModal, setSelectedFilter } from '../../stores/mySlice';
import { getAccountDeposit, getAccountHistory, getAccountWithdrawal } from '../../utils/accountApi';
import dayjs from 'dayjs';

const AccountHistory = () => {
  const dispatch = useDispatch();
  const fundingAccount = useSelector(state => state.myPageInfo.fundingAccount);
  const filter = useSelector(state => state.myPageInfo.selectedFilter);

  const scrollRef = useRef(null);

  const [data, setData] = useState();
  const [sections, setSections] = useState({});

  const getTransactionData = async () => {
    if (filter === '전체') {
      const res = await getAccountHistory(fundingAccount.accountCode);
      setData(res.accountHistoryListDtoss);
    } else if (filter === '입금') {
      const res = await getAccountDeposit(fundingAccount.accountCode);
      setData(res.accountHistoryListDtoss);
    } else {
      const res = await getAccountWithdrawal(fundingAccount.accountCode);
      setData(res.accountHistoryListDtoss);
    }
    scrollRef.current.scrollTo({ animated: true, y: 0 })
  };

  useFocusEffect(
    useCallback(() => {
      getTransactionData();
    }, [filter])
  );

  const devideSection = () => {
    const sections = {};

    data.forEach((el) => {
      const date = dayjs(el.accountTime).add(9, 'hour').format("YYYY년 MM월 DD일");
      if (sections[date]) {
        sections[date].push(el);
      } else {
        sections[date] = [el];
      }
    });

    return sections;
  };

  useEffect(() => {
    if (data) {
      setSections(() => devideSection());
    }
  }, [data])

  return (
    <View style={styles.container}>
      <View style={styles.filterContainer}>
        <Pressable style={styles.filter} onPress={() => dispatch(setFilterModal(true))}>
          <Text style={styles.filterText}>{filter}</Text>
          <Ionicons name="chevron-down" size={20} color={Colors.fontGray} />
        </Pressable>
        <Pressable onPress={getTransactionData}>
          <Ionicons name="reload" size={24} color={Colors.fontGray} />
        </Pressable>
      </View>
      <View style={styles.scrollContainer}>
        <ScrollView contentContainerStyle={{rowGap: 16}} ref={scrollRef} showsVerticalScrollIndicator={false}>
          {
            Object.entries(sections).map(([day, content]) => (
              <View style={styles.dayContainer} key={day}>
                <Text style={styles.dayText}>{day}</Text>
                {
                  content.map((el) => (
                    <View style={styles.contentContainer} key={el.accountTime}>
                      <View style={styles.circle}>
                        <FontAwesome name="won" size={18} color="white" />
                      </View>
                      <View style={styles.infoContainer}>
                        <Text style={styles.transferName}>{el.transferName}</Text>
                        <Text style={styles.accountTime}>{dayjs(el.accountTime).add(9, 'hour').format("HH:mm")}</Text>
                      </View>
                      <Text style={[styles.amount, el.depositOrWithdrawal === 1 && { color: Colors.btnBlue }]}>{el.depositOrWithdrawal === 1 ? '+' : '-'}{el.amount.toLocaleString('ko-KR')} 원</Text>
                    </View>
                  ))
                }
              </View>
            ))
          }
        </ScrollView>
      </View>
      <FilterModal />
    </View>
  )
}

export default AccountHistory;

const styles = StyleSheet.create({
  container: {
    width: '90%',
    gap: 16,
    paddingTop: 12,
    flex: 1
  },
  filterContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  filter: {
    borderWidth: 1,
    borderColor: Colors.fontGray,
    borderRadius: 25,
    paddingHorizontal: 14,
    paddingVertical: 4,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 6
  },
  filterText: {
    fontSize: 14,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray
  },
  dayContainer: {
    gap: 16
  },
  circle: {
    width: 35,
    height: 35,
    borderRadius: 17.5,
    paddingTop: 2,
    backgroundColor: Colors.btnBlue,
    alignItems: 'center',
    justifyContent: 'center'
  },
  dayText: {
    fontSize: 12,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray
  },
  contentContainer: {
    flexDirection: 'row',
    gap: 10,
    alignItems: 'center'
  },
  infoContainer: {
    gap: 8,
    flex: 1
  },
  transferName: {
    fontSize: 14,
    fontFamily: 'pretendard-semiBold'
  },
  accountTime: {
    fontSize: 12,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray
  },
  amount: {
    fontSize: 14,
    fontFamily: 'pretendard-semiBold',
    color: '#4F4F4F'
  },
  scrollContainer: {
    width: '95%',
    marginHorizontal: 'auto',
    flex: 1,
    paddingBottom: 16
  }
});