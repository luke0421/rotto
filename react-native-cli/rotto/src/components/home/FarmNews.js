import {useEffect, useState} from 'react';
import {StyleSheet, View, Image, Pressable} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import {Text} from 'react-native-paper';
import {getFarmNewsList} from '../../utils/FarmNewsApi';
import Colors from '../../constants/Colors';

const FarmNews = () => {
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
    <View style={styles.screen}>
      <Text style={styles.header}>농장 소식</Text>
      {farmNewsList &&
        farmNewsList.map(
          (item, index) =>
            index < 3 && (
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
            ),
        )}
      <View
        style={{
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'center',
          margin: 20,
        }}>
        <Pressable
        onPress={() => Navigation.navigate("newsList")}
          style={{
            fontFamily: 'pretendard-bold',
            fontSize: 17,
            color: Colors.bgOrg,
          }}>
          <Text>더 보기</Text>
        </Pressable>
      </View>
    </View>
  );
};

export default FarmNews;

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    marginHorizontal: 10,
    backgroundColor: 'white',
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
