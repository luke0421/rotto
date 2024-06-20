import {useState, useEffect} from 'react';
import {StyleSheet, Text, View, Image, Dimensions} from 'react-native';
import Colors from '../../constants/Colors';
import {getMyInvestment} from '../../utils/myInvestmentAmount';

const {width} = Dimensions.get('window');

const MyDeposit = () => {
  const [MyInvestment, setMyInvestment] = useState([]);

  const getList = async () => {
    const res = await getMyInvestment();
    setMyInvestment(res);
    //console.log('MyDeposit mounted', res);
  };

  useEffect(() => {
    getList();
  }, []);

  return (
    <View style={styles.componentContainer}>
      <Text style={styles.header}>나의 자산</Text>
      <View style={styles.boxContainer}>
        <View style={styles.depositContainer}>
          <View style={{flex: 1, alignItems: 'baseline'}}>
            <Text style={styles.content}>총 투자금액</Text>
            <Text>
              <Text style={styles.highlight}>
                {MyInvestment && MyInvestment.totalInvestAmount
                  ? MyInvestment.totalInvestAmount.toLocaleString('ko-KR')
                  : '-'}
              </Text>{' '}
              원
            </Text>
            <View style={{flexDirection: 'row'}}>
              {/* <Text> */}
              <Text style={styles.variance}>
                ( +{' '}
                {MyInvestment && MyInvestment.totalPercent
                  ? MyInvestment.totalPercent.toFixed(2)
                  : '-'}
                % )
              </Text>
              {/* </Text> */}
            </View>
          </View>

          {/* <View style={{flex: 1, alignItems: 'flex-end'}}>
            <View style={{flexDirection: 'row'}}>
              <Text>
                손익
                <Text style={styles.variance}>
                  {MyInvestment && MyInvestment.totalProceed
                    ? MyInvestment.totalProceed.toLocaleString('ko-KR')
                    : '-'}
                </Text>
                원
              </Text>
            </View>
          </View> */}
        </View>
        <View style={styles.line}></View>
        <View>
          <View>
            <Text style={styles.content}>투자 현황</Text>
          </View>
          {MyInvestment &&
            MyInvestment.tradeHistoryHomeInfoDtoss &&
            MyInvestment.tradeHistoryHomeInfoDtoss.map((token, index) => (
              <View key={index}>
                <View style={styles.itemStyle}>
                  <View style={styles.itemContentStyle}>
                    {token.title === '투자 신청' && (
                      <Image
                        source={require('../../../assets/images/home/submit.png')}
                        style={{width: 40, height: 60, resizeMode: 'contain'}}
                      />
                    )}
                    {token.title === '투자 보유' && (
                      <Image
                        source={require('../../../assets/images/home/coin.png')}
                        style={{width: 40, height: 60, resizeMode: 'contain'}}
                      />
                    )}
                    {token.title === '투자 정산' && (
                      <Image
                        source={require('../../../assets/images/home/money.png')}
                        style={{width: 40, height: 60, resizeMode: 'contain'}}
                      />
                    )}
                    <View style={{marginLeft: 10}}>
                      <Text style={styles.itemContent}>{token.title}</Text>
                      <Text style={styles.itemSubContent}>
                        {token.tokenNum} ROTTO{' '}
                      </Text>
                    </View>
                  </View>
                  <View>
                    <Text style={{marginRight: 10}}></Text>
                    <Text style={{marginRight: 5}}>
                      {token.expenses.toLocaleString('ko-KR')}원
                    </Text>
                  </View>
                </View>
              </View>
            ))}
        </View>
      </View>
    </View>
  );
};

export default MyDeposit;

const styles = StyleSheet.create({
  componentContainer: {
    marginTop: 20,
    position: 'relative',
  },
  header: {
    fontFamily: 'pretendard-extraBold',
    fontSize: 18,
    marginHorizontal: 25,
    color: 'white',
  },
  boxContainer: {
    marginTop: 10,
    marginHorizontal: 20,
    marginBottom: 10,
    // height: 200,
    borderRadius: 15,
    padding: 25,
    backgroundColor: 'white',
  },
  depositContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    alignContent: 'space-between',
    flex: 1,
  },
  iconContainer: {
    marginTop: 15,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontFamily: 'pretendard-bold',
    fontSize: 17,
    marginBottom: 10,
  },
  content: {
    fontFamily: 'pretendard-bold',
    fontSize: 18,
    marginBottom: 10,
  },
  highlight: {
    fontFamily: 'pretendard-extraBold',
    fontSize: 25,
  },
  variance: {
    fontFamily: 'pretendard-regular',
    fontSize: 13,
    color: 'red',
  },
  depositDetail: {
    marginTop: 20,
    // backgroundColor: "#f1ebeb",
  },
  line: {
    // height:1,
    marginVertical: 20,
    borderColor: Colors.fontGray,
    borderWidth: 0.5,
  },
  itemStyle: {
    marginBottom: 10,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  itemContentStyle: {
    marginHorizontal: 10,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  itemContent: {
    fontFamily: 'pretendard-regular',
    fontSize: 12,
    coloc: 'grey',
  },
  itemSubContent: {
    fontFamily: 'pretendard-bold',
    fontSize: 16,
  },
});
