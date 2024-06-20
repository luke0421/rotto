import { Pressable, View, Text, StyleSheet } from 'react-native';
import Colors from '../../constants/Colors';

const InquiryCard = ({navigation, data}) => {

  return (
    <Pressable style={styles.container} onPress={() => navigation.navigate('inquiryDetail', { reqBoardCode: data.reqBoardCode })}>
      <View style={styles.titleContainer}>
        <Text style={styles.mdFont}>Q.</Text>
        <Text style={[styles.mdFont, { flex: 1 }]}>{data.title}</Text>
      </View>
      <Text style={styles.date}>{new Date(data.createTime).toLocaleDateString("ko-KR")} {new Date(data.createTime).toLocaleTimeString("ko-KR")}</Text>
    </Pressable>
  )
}

export default InquiryCard;

const styles = StyleSheet.create({
  container: {
    width: '100%',
    borderColor: Colors.fontGray,
    borderWidth: 0.5,
    borderRadius: 5,
    padding: 10,
    gap: 8
  },
  titleContainer: {
    flexDirection: 'row',
    gap: 2
  },
  mdFont: {
    fontSize: 12,
    fontFamily: 'pretendard-medium'
  },
  date: {
    fontSize: 10,
    fontFamily: 'pretendard-regular',
    color: Colors.fontGray,
    flex: 1,
    textAlign: 'right'
  }
});