import {Image, Pressable, ScrollView, StyleSheet, Text, View} from 'react-native';
import StackHeader from '../../components/common/StackHeader';
import {useEffect, useState} from 'react';
import {useNavigation} from '@react-navigation/native';
import {getFarmNewsList} from '../../utils/FarmNewsApi';

const NewsScreen = () => {
  const [farmNewsList, setfarmNewsList] = useState([]);
  const Navigation = useNavigation();

  const getList = async () => {
    try {
      const res = await getFarmNewsList();
      setfarmNewsList(res);
    } catch (err) {
      //console.error("FarmNews getList error: ", error);
    }
  };

  useEffect(() => {
    getList();
  }, []);

  return (
    <>
      <StackHeader title={'농장 소식'} empty={true} screenName={'home'} />
      <ScrollView style={styles.screen} showsVerticalScrollIndicator={false}>
        {farmNewsList &&
          farmNewsList.map((item, index) => (
            <Pressable
              onPress={() =>
                Navigation.navigate('NewsWebview', {
                  newsDetailLink: item.newsDetailLink,
                })
              }
              key={item.newsCode}>
              <View style={styles.itemStyle}>
                <View style={{flexShrink: 1}}>
                  <Text>{item.category}</Text>
                  <Text style={styles.title}>{item.title}</Text>
                  <View
                    style={{
                      flex: 1,
                      flexDirection: 'row',
                      alignContent: 'space-between',
                    }}>
                    <Text style={{flex: 1}}>{item.postTime}</Text>
                    <Text style={{flex: 1}}>{item.author}</Text>
                  </View>
                </View>

                <Image
                  source={{
                    uri: `${item.imgLink}`,
                  }}
                  style={{width: 90, height: 90, resizeMode: 'stretch'}}
                />
              </View>
            </Pressable>
          ))}
      </ScrollView>
    </>
  );
};

export default NewsScreen;

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    marginHorizontal: 10,
    backgroundColor: 'white',
    paddingHorizontal: 20,
  },
  header: {
    margin: 10,
    fontFamily: 'pretendard-extraBold',
    fontSize: 23,
  },
  itemStyle: {
    margin: 10,
    flexDirection: 'row',
    alignItems: 'center',
  },
  title: {
    fontFamily: 'pretendard-bold',
    fontSize: 14,
    marginRight: 20,
  },
});
