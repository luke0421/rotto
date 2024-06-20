import { View, Text, StyleSheet } from 'react-native';
import Colors from '../../constants/Colors';

const ProgressBar = ({step}) => {
  return (
    <View style={styles.container}>
      <View style={[styles.circle, step >= 1 && { backgroundColor: Colors.btnBlue }]}>
        <Text style={styles.step}>1</Text>
      </View>
      <View style={[styles.line, step >= 1 && { borderBottomColor: Colors.btnBlue }]}></View>
      <View style={[styles.circle, step >= 2 && { backgroundColor: Colors.btnBlue }]}>
        <Text style={styles.step}>2</Text>
      </View>
      <View style={[styles.line, step >= 2 && { borderBottomColor: Colors.btnBlue }]}></View>
      <View style={[styles.circle, step >= 3 && { backgroundColor: Colors.btnBlue }]}>
        <Text style={styles.step}>3</Text>
      </View>
      <View style={[styles.line, step >= 3 && { borderBottomColor: Colors.btnBlue }]}></View>
      <View style={[styles.circle, step >= 4 && { backgroundColor: Colors.btnBlue }]}>
        <Text style={styles.step}>4</Text>
      </View>
    </View>
  )
}

export default ProgressBar;

const styles = StyleSheet.create({
  container: {
    marginTop: 16,
    width: '100%',
    flexDirection: 'row',
    alignItems: 'center'
  },
  circle: {
    width: 24,
    height: 24,
    borderRadius: 12,
    backgroundColor: Colors.fontGray,
    alignItems: 'center',
    justifyContent: 'center'
  },
  line: {
    height: 0.5,
    borderBottomColor: Colors.fontGray,
    borderBottomWidth: 0.5,
    flex: 1
  },
  step: {
    color: 'white',
    fontSize: 14,
    fontFamily: 'pretendard-medium'
  }
});