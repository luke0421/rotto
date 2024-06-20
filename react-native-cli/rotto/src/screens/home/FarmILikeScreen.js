import {useNavigation} from '@react-navigation/native';
import {useEffect, useState} from 'react';
import {
  FlatList,
  Pressable,
  StyleSheet,
  View,
  Image,
  Dimensions,
} from 'react-native';
import {Ionicons} from '@expo/vector-icons';
import {Text} from 'react-native-paper';
import Colors from '../../constants/Colors';
import {getFarmILikeList} from '../../utils/FarmILikeApi';
import StackHeader from '../../components/common/StackHeader';
import {S3URL} from '@env';

const {width} = Dimensions.get('window');

const FarmILikeListScreen = () => {
  const [farmILikeList, setfarmILikeList] = useState([]);

  const getList = async () => {
    const res = await getFarmILikeList();
    setfarmILikeList(res.data.farms);
  };

  useEffect(() => {
    getList();
  }, []);

  const Navigation = useNavigation();

  const rendeFarmILikeList = itemData => {
    return (
      <Pressable
        key={itemData.item.farmCode}
        style={styles.itemStyle}
        onPress={() =>
          Navigation.navigate('발견', {
            screen: 'farmDetail',
            params: {farmCode: itemData.item.farmCode},
          })
        }>
        <View style={styles.itemContentStyle}>
          <Image
            source={{
              uri: `${S3URL}/farm_img/${itemData.item.farmCode}/${itemData.item.farmLogoPath}`,
            }}
            style={{width: 50, height: 50, borderRadius: 25}}
          />
          <View style={{marginLeft: 10}}>
            <Text style={styles.content}>{itemData.item.farmName}</Text>
            <Text style={styles.subContent}>{itemData.item.beanName}</Text>
          </View>
        </View>
        <View>
          <Text style={{marginRight: 10}}>
            {itemData.item.isFunding ? '청약 진행중' : '청약 기간 아님'}
          </Text>
        </View>
      </Pressable>
    );
  };

  return (
    <>
      <StackHeader title={'관심 농장'} screenName={'home'} empty={true} />
      <View style={styles.screen}>
        {farmILikeList && (
          <FlatList
            data={farmILikeList}
            keyExtractor={(item, index) => index.toString()}
            renderItem={rendeFarmILikeList}
          />
        )}
        {/* <Pressable
        style={{height: 40, width: width* 0.9, alignItems: "flex-end"}}
          onPress={() => Navigation.navigate('farmList')}>
          <View>
            <Text
              style={{
                marginLeft: 20,
                color: '#BBBBBB',
                fontFamily: 'pretendard-bold',
                fontSize: 16,
              }}>
              농장 목록으로 이동
            </Text>
          </View>
        </Pressable> */}
      </View>
    </>
  );
};

export default FarmILikeListScreen;

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignContent: 'center',
    margin: 10,
  },
  screen: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center',
  },
  header: {
    fontFamily: 'pretendard-extraBold',
    fontSize: 26,
    paddingBottom: 20,
  },
  title: {
    fontFamily: 'pretendard-regular',
    fontSize: 16,
    margin: 10,
    alignContent: 'stretch',
    justifyContent: 'center',
  },
  content: {
    marginTop: 5,
  },
  iconContainer: {
    margin: 15,
    position: 'absolute',
    justifyContent: 'flex-start',
    alignItems: 'center',
  },
  qText: {
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 16,
    color: 'grey',
    margin: 10,
  },
  myIconStyle: {
    margin: 10,
    color: 'grey',
    justifyContent: 'center',
    alignContent: 'center',
  },
  myContent: {
    marginHorizontal: 40,
    marginVertical: 5,
    padding: 10,
  },
  subContent: {
    fontFamily: 'pretendard-regular',
    fontSize: 12,
    color: 'grey',
  },
  itemContentStyle: {
    marginHorizontal: 10,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  itemStyle: {
    marginBottom: 20,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    width: width * 0.9,
  },
});
