import { View, Text, Pressable, StyleSheet } from 'react-native';
import Colors from '../../constants/Colors';

const CustomButton = ({children, onPress, style, btnColor, disabled, fontFamily}) => {
  return (
    <View style={[styles.buttonOuterContainer, style]}>
      <Pressable 
        style={
          ({pressed}) => 
            pressed ? 
            [styles.buttonInnerContainer, { ...styles.pressed, backgroundColor: btnColor ? btnColor : Colors.btnBlue }] 
            : 
            [styles.buttonInnerContainer, { backgroundColor: disabled ? '#B3B3B3' : btnColor ? btnColor : Colors.btnBlue }]
        } 
        onPress={onPress} 
        disabled={disabled === undefined ? false : disabled }
      >
        <Text style={[styles.buttonText, { fontFamily: fontFamily ? fontFamily : 'pretendard-bold' }]}>{children}</Text>
      </Pressable>
    </View>
  )
}

export default CustomButton;

const styles = StyleSheet.create({
  buttonOuterContainer: {
    width: '100%',
    borderRadius: 5,
    overflow: 'hidden',
    height: 32,
  },
  buttonInnerContainer: {
    backgroundColor: Colors.btnBlue,
    elevation: 2,
    height: '100%',
    justifyContent: 'center'
  },
  buttonText: {
    color: 'white',
    textAlign: 'center',
    marginBottom: 2,
    fontSize: 12
  },
  pressed: {
    opacity: 0.75
  }
})