import { useEffect, useState } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { getRequestDetail } from '../../utils/settingApi';
import Colors from '../../constants/Colors';
import DetailTopBar from '../../components/common/DetailTopBar';

const InquiryDetailScreen = ({navigation, route}) => {
  const [data, setData] = useState();

  useEffect(() => {
    const getInquiryDetail = async () => {
      const res = await getRequestDetail(route.params.reqBoardCode);
      setData(res);
    };

    getInquiryDetail();
  }, [route.params.reqBoardCode]);

  return (
    <View style={styles.outerContainer}>
      <DetailTopBar title='문의 상세' navigation={navigation} />
      {
        data &&
        <View style={styles.innerContainer}>
          <View style={styles.titleContainer}>
            <Text style={styles.titleText}>Q.</Text>
            <Text style={styles.titleText}>{data.title}</Text>
          </View>
          <View style={styles.contentContainer}>
            <Text style={styles.contentText}>{data.contents}</Text>
          </View>
          <Text style={styles.date}>{new Date(data.createTime).toLocaleString('ko-KR')}</Text>
        </View>
      }
    </View>
  )
}

export default InquiryDetailScreen;

const styles = StyleSheet.create({
  outerContainer: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center'
  },
  innerContainer: {
    width: '82%',
    gap: 12
  },
  titleContainer: {
    flexDirection: 'row',
    gap: 2,
  },
  titleText: {
    fontFamily: 'pretendard-medium',
    fontSize: 18
  },
  contentContainer: {
    borderRadius: 5,
    borderWidth: 0.5,
    borderColor: Colors.fontGray,
    padding: 10
  },
  contentText: {
    fontFamily: 'pretendard-regular',
    fontSize: 14
  },
  date: {
    fontSize: 12,
    fontFamily: 'pretendard-regular',
    color: Colors.fontGray,
    textAlign: 'right'
  }
});