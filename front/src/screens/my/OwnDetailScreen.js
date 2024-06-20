import { View, Text, ScrollView, StyleSheet } from 'react-native';
import DetailTopBar from '../../components/common/DetailTopBar';
import { useFocusEffect } from '@react-navigation/native';
import { useCallback, useState } from 'react';
import { getOwnDetail } from '../../utils/investApi';
import Colors from '../../constants/Colors';

const OwnDetailScreen = ({navigation, route}) => {
  const subscriptionCode = route.params.subscriptionCode;
  const [data, setData] = useState();

  useFocusEffect(
    useCallback(() => {
      const getDetailData = async () => {
        const res = await getOwnDetail(subscriptionCode);
        //console.log(res);
        setData(res);
      };

      getDetailData();
    }, [subscriptionCode])
  )

  return (
    <View style={styles.container}>
      <DetailTopBar title='정산 상세 내역' navigation={navigation} />
      {
        data &&
        <View style={styles.innerContainer}>
          <View style={styles.resultContainer}>
            <View style={styles.contentContainer}>
              <Text style={styles.menu}>원두 판매 금액</Text>
              <Text style={styles.content}>{data.totalSoldPrice.toLocaleString('ko-KR')}원</Text>
            </View>
            <View style={styles.contentContainer}>
              <Text style={styles.menu}>지출 금액</Text>
              <Text style={styles.content}>{data.totalExpense.toLocaleString('ko-KR')}원</Text>
            </View>
            <View style={styles.line} />
            <View style={styles.contentContainer}>
              <Text style={styles.menu}>경매 결과</Text>
              <Text style={[styles.content, { color: data.totalProceed < 0 ? 'blue' : 'red' }]}>
                {data.totalProceed > 0 && '+' }{(data.totalProceed).toLocaleString('ko-KR')}원
              </Text>
            </View>
            <View style={styles.contentContainer}>
              <Text style={styles.menu}>내 지분</Text>
              <Text style={styles.content}>{data.tradeNum}ROT ({Math.round(data.tradeNum / data.totalTokenCount * 100 * 100) / 100}%)</Text>
            </View>
            <View style={styles.line} />
            <View style={styles.contentContainer}>
              <Text style={styles.menu}>수익 금액</Text>
              <Text style={[styles.content, { color: data.myProceed < 0 ? 'blue' : 'red' }]}>
                {data.myProceed > 0 && '+'}{data.myProceed.toLocaleString('ko-KR')}원
              </Text>
            </View>
          </View>
          <View style={styles.detailContainer}>
            <Text style={styles.title}>경매 상세 내역</Text>
            <ScrollView contentContainerStyle={{rowGap: 12}} showsVerticalScrollIndicator={false}>
              <Text style={styles.subTitle}>판매 수익</Text>
              <View style={styles.contentContainer}>
                <Text style={styles.menu}>판매 금액</Text>
                <Text style={styles.content}>{data.totalSoldPrice.toLocaleString('ko-KR')}원</Text>
              </View>
              <Text style={styles.subTitle}>지출 금액</Text>
              {
                data.tradeHistoryExpenseDetailOfSubDtoList && data.tradeHistoryExpenseDetailOfSubDtoList.map(el => (
                  <View style={styles.contentContainer} key={el.expenditureContent}>
                    <Text style={styles.menu}>{el.expenditureContent}</Text>
                    <Text style={styles.content}>-{el.expenses.toLocaleString('ko-KR')}원</Text>
                  </View>
                ))
              }
              <Text style={styles.subTitle}>수수료</Text>
              <View style={styles.contentContainer}>
                <Text style={styles.menu}>거래 수수료</Text>
                <Text style={styles.content}>-{data.fee ? data.fee.toLocaleString('ko-KR') : 0 }원</Text>
              </View>
            </ScrollView>
          </View>
        </View>
      }
    </View>
  )
}

export default OwnDetailScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center'
  },
  innerContainer: {
    width: '85%',
    gap: 35,
    flex: 1
  },
  resultContainer: {
    gap: 12
  },
  contentContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  menu: {
    fontSize: 16,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray 
  },
  content: {
    fontSize: 16,
    fontFamily: 'pretendard-medium'
  },
  line: {
    height: 1,
    borderBottomColor: Colors.fontGray,
    borderBottomWidth: 1
  },
  title: {
    fontSize: 18,
    fontFamily: 'pretendard-medium'
  },
  subTitle: {
    fontSize: 16,
    fontFamily: 'pretendard-medium'
  },
  detailContainer: {
    flex: 1,
    gap: 16,
    paddingBottom: 16
  }
});