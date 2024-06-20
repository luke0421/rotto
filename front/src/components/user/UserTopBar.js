import { View, Pressable, StyleSheet } from 'react-native';
import { AntDesign } from '@expo/vector-icons';

const UserTopBar = ({navigation, destination}) => {
  return (
    <View style={styles.topBar}>
      <Pressable 
        style={styles.backButton} 
        onPress={() => {
          if (destination) {
            navigation.navigate(destination);
          } else {
            navigation.goBack();
          }
        }}
      >
        <AntDesign name="left" size={24} />
      </Pressable>
    </View>
  )
};

export default UserTopBar;

const styles = StyleSheet.create({
  topBar: {
    width: '100%',
    backgroundColor: 'white'
  },
  backButton: {
    marginLeft: 24,
    marginVertical: 16
  }
});