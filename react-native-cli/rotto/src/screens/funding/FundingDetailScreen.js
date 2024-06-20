import { View, Text, ScrollView, StyleSheet, Pressable, Image } from 'react-native';
import DetailTopBar from '../../components/common/DetailTopBar';
import { useFocusEffect } from '@react-navigation/native';
import { useCallback, useEffect, useState } from 'react';
import { getFundingDetail } from '../../utils/fundingApi';
import dayjs from 'dayjs';
import Colors from '../../constants/Colors';
import { MaterialCommunityIcons, Ionicons, FontAwesome5 } from '@expo/vector-icons';
import CustomButton from '../../components/common/CustomButton';
import { useDispatch, useSelector } from 'react-redux';
import { setApplyModal, setFundingData } from '../../stores/fundingSlice';
import ApplyModal from '../../components/funding/ApplyModal';
import { S3URL } from "@env"

const FundingDetailScreen = ({navigation, route}) => {
  const subscriptionCode = route.params.subscriptionCode;
  const dispatch = useDispatch();
  const fundingData = useSelector(state => state.fundingInfo.fundingData);
  const [error, setError] = useState(false);

  useFocusEffect(
    useCallback(() => {
      const getDetailedData = async () => {
        const res = await getFundingDetail(subscriptionCode);
        //console.log(res);
        dispatch(setFundingData(res));
      };

      getDetailedData();
      setError(false);
    }, [subscriptionCode])
  );

  const getState = (state, startedTime, endTime) => {
    switch (state) {
      case 0:
        return {
          text: `D-${Math.floor(dayjs(startedTime).add(9, 'hour').diff(dayjs(), "day", true))}`,
          color: Colors.btnYellow
        };
      case 1:
        return {
          text: '청약 진행중',
          color: Colors.btnBlue,
          endText: `청약 마감 D-${Math.floor(dayjs(endTime).add(9, 'hour').diff(dayjs(), "day", true))}`
        }
      case 2:
        return {
          text: '마감',
          color: Colors.fontGray
        }
    }
  };

  return (
    <View style={styles.container}>
      <DetailTopBar navigation={navigation} />
      {
        fundingData && fundingData.subscriptionCode === subscriptionCode &&
        <ScrollView style={styles.scrollContainer} contentContainerStyle={{ rowGap: 14 }} showsVerticalScrollIndicator={false}>
          <View style={styles.badgeContainer}>
            <View style={[styles.badge, { backgroundColor: getState(fundingData.subsStatus, fundingData.startedTime, fundingData.endTime).color }]}>
              <Text style={styles.badgeText}>{getState(fundingData.subsStatus, fundingData.startedTime, fundingData.endTime).text}</Text>
            </View>
            {
              fundingData.subsStatus === 1 &&
              <View style={[styles.badge, { backgroundColor: 'black' }]}>
                <Text style={styles.badgeText}>{getState(fundingData.subsStatus, fundingData.startedTime, fundingData.endTime).endText}</Text>
              </View>
            }
          </View>
          <Text style={styles.farmName}>{fundingData.farmName}</Text>
          <View style={styles.imgContainer}>
            <Image 
              style={styles.img} 
              source = {
                error ?
                require('../../../assets/images/discovery/coffeefarm2.png')
                :
                { uri: `${S3URL}/farm_img/${fundingData.farmCode}/${fundingData.farmImg}` }
              }
              onError={() => setError(true)}
            />
            <Image style={styles.img} source={require('../../../assets/images/discovery/coffeefarm2.png')} />
          </View>
          <View style={styles.contentContainer}>
            <View style={styles.menuContainer}>
              <MaterialCommunityIcons name="calendar-blank" size={18} color="black" />
              <Text style={styles.menutext}>공모 기한</Text>
            </View>
            <Text style={styles.contentText}>
              {dayjs(fundingData.startedTime).add(9, 'hour').format('YYYY.MM.DD')} - {dayjs(fundingData.endTime).add(9, 'hour').format('YYYY.MM.DD')}
            </Text>
          </View>
          <View style={styles.contentContainer}>
            <View style={styles.menuContainer}>
              <MaterialCommunityIcons name="registered-trademark" size={18} color="black" />
              <Text style={styles.menuText}>청약 목표 수량</Text>
            </View>
            <Text style={styles.contentText}>{fundingData.totalTokenCount.toLocaleString('ko-KR')} ROTTO</Text>
          </View>
          <View style={styles.contentContainer}>
            <View style={styles.menuContainer}>
              <MaterialCommunityIcons name="registered-trademark" size={18} color="black" />
              <Text style={styles.menuText}>청약 최대 수량</Text>
            </View>
            <Text style={styles.contentText}>{fundingData.limitNum} ROTTO</Text>
          </View>
          <View style={styles.contentContainer}>
            <View style={styles.menuContainer}>
              <Ionicons name="pricetags" size={18} color="black" />
              <Text style={styles.menuText}>가격</Text>
            </View>
            <Text style={styles.contentText}>{fundingData.confirmPrice.toLocaleString('ko-KR')}원 / 1 ROTTO</Text>
          </View>
          {
            fundingData.subsStatus === 1 &&
            <View style={styles.contentContainer}>
              <View style={styles.menuContainer}>
                <FontAwesome5 name="chart-pie" size={18} />
                <Text style={styles.menuText}>신청률</Text>
              </View>
              <Text style={styles.contentText}>
              {fundingData.applyCount.toLocaleString('ko-KR')} / {fundingData.totalTokenCount.toLocaleString('ko-KR')} ROT ({Math.round(fundingData.applyCount / fundingData.totalTokenCount * 100 * 100) / 100}%)
              </Text>
            </View>
          }
          <View style={styles.contentContainer}>
            <View style={styles.menuContainer}>
              <MaterialCommunityIcons name="chart-line" size={18} color="black" />
              <Text style={styles.menuText}>지난 청약 수익률</Text>
            </View>
            <Text style={[styles.contentText, { color: fundingData.returnRate < 0 ? 'blue' : 'red' }]}>{fundingData.returnRate > 0 && '+'}{fundingData.returnRate}%</Text>
          </View>
          <View style={styles.contentContainer}>
            <View style={styles.menuContainer}>
              <Ionicons name="information-circle-outline" size={18} color="black" />
              <Text style={styles.menuText}>상세 정보</Text>
            </View>
            <Pressable onPress={() => navigation.navigate('Routers', {
              screen: '발견',
              params: {
                screen: 'farm',
                params: { farmCode: fundingData.farmCode, returnTo: '청약' }
              }
            })}>
              <Text style={styles.contentText}>농장 상세 정보 {'>'}</Text>
            </Pressable>
          </View>
        </ScrollView>
      }
      {
        fundingData && fundingData.subscriptionCode === subscriptionCode && fundingData.subsStatus === 1 &&
        <View style={styles.buttonContainer}>
          <CustomButton 
            onPress={() => dispatch(setApplyModal(true))}
            disabled={!!fundingData.isApply}
          >
            {
              fundingData.isApply ?
              '이미 신청하셨습니다'
              :
              '신청하기'
            }
          </CustomButton>
        </View>
      }
      <ApplyModal />
    </View>
  )
}

export default FundingDetailScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    backgroundColor: 'white'
  },
  scrollContainer: {
    width: '85%',
    marginBottom: 16
  },
  buttonContainer: {
    width: '85%',
    paddingBottom: 16,
    paddingTop: 8
  },
  badgeContainer: {
    flexDirection: 'row',
    gap: 6,
    alignItems: 'center'
  },
  badge: {
    paddingVertical: 4,
    paddingHorizontal: 12,
    borderRadius: 25
  },
  badgeText: {
    fontSize: 12,
    fontFamily: 'pretendard-semiBold',
    color: 'white'
  },
  farmName: {
    fontSize: 20,
    fontFamily: 'pretendard-semiBold'
  },
  contentContainer: {
    gap: 5
  },
  menuContainer: {
    flexDirection: 'row',
    gap: 8,
    alignItems: 'center'
  },
  menuText: {
    fontSize: 14,
    fontFamily: 'pretendard-medium'
  },
  contentText: {
    fontSize: 12,
    fontFamily: 'pretendard-medium',
    color: Colors.fontGray
  },
  imgContainer: {
    height: 190,
    borderRadius: 5,
    overflow: 'hidden'
  },
  img: {
    height: '100%',
    width: '100%'
  }
})