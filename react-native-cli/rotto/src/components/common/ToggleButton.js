import { useEffect, useState } from 'react';
import { TouchableOpacity, Animated, Easing, StyleSheet } from 'react-native';
import Colors from '../../constants/Colors';

const ToggleButton = ({onToggle, isOn}) => {
  const [animatedValue] = useState(new Animated.Value(isOn ? 1 : 0));

  useEffect(() => {
    Animated.timing(animatedValue, {
      toValue: isOn ? 1 : 0,
      duration: 200,
      easing: Easing.linear,
      useNativeDriver: false
    }).start();
  }, [isOn, animatedValue]);

  const translateX = animatedValue.interpolate({
    inputRange: [0, 1],
    outputRange: [2, 17]
  });

  const color = isOn ? Colors.btnBlue : '#D9D9D9';

  return (
    <TouchableOpacity
      onPress={onToggle}
      style={[styles.container, { backgroundColor: color }]}
    >
      <Animated.View 
        style={[styles.toggleWheel, { transform: [{translateX}]}]} 
      />
    </TouchableOpacity>
  )
}

export default ToggleButton;

const styles = StyleSheet.create({
  container: {
    width: 34,
    height: 19,
    borderRadius: 10,
    justifyContent: 'center',
  },
  toggleWheel: {
    width: 15,
    height: 15,
    borderRadius: 99,
    backgroundColor: 'white'
  }
});