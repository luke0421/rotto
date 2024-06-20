import { View, Pressable, Text, StyleSheet } from 'react-native';
import { AntDesign } from '@expo/vector-icons';

const DetailTopBar = ({title, navigation, destination}) => {
  return (
    <View style={styles.topBar}>
      <Pressable onPress={() => {
        if (destination) {
          navigation.navigate(destination);
        } else {
          navigation.goBack();
        }
      }}>
        <AntDesign name="left" size={24} />
      </Pressable>
      <Text style={styles.title}>{title}</Text>
      <View style={styles.spacer}></View>
    </View>
  )
}

export default DetailTopBar;

const styles = StyleSheet.create({
  topBar: {
    width: '100%',
    height: '12%',
    paddingHorizontal: 24,
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
  },
  title: {
    fontSize: 20,
    fontFamily: 'pretendard-medium',
  },
  spacer: {
    width: 24
  }
});