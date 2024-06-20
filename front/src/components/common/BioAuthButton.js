import { Pressable, Text, StyleSheet, Platform } from 'react-native';
import Colors from '../../constants/Colors';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useEffect, useState } from 'react';
import SettingService from '../../utils/setting';

const BioAuthButton = ({style}) => {
  const [isActive, setIsActive] = useState(false);

  useEffect(() => {
    const getisEnabled = async () => {
      const isEnabled = await SettingService.getBiometricEnabled();
      setIsActive(() => isEnabled ? isEnabled : false);
    };

    const loadInitialData = async () => {
      await Promise.all([getisEnabled()]);
    };

    loadInitialData();
  }, [])

  const setBioAuth = async () => {
    setIsActive(pre => !pre);
    await SettingService.setBiometricEnabled(isActive.toString());
  };

  return (
    <Pressable
      onPress={setBioAuth} 
      style={[styles.container, { borderColor: isActive ? Colors.btnBlue : Colors.fontGray }, style]}
    >
      <MaterialCommunityIcons 
        name={ Platform.OS === 'android' ? 'fingerprint' : 'face-recognition' } 
        size={14}
        color={ isActive ? Colors.btnBlue : Colors.fontGray }
      />
      <Text 
        style={[styles.text, { color: isActive ? Colors.btnBlue : Colors.fontGray }]}
      >
        {
          Platform.OS === 'android' ?
          '지문 인식 사용'
          :
          'Face ID 사용'
        }
      </Text>
    </Pressable>
  )
}

export default BioAuthButton;

const styles = StyleSheet.create({
  container: {
    height: 26,
    width: 110,
    borderRadius: 24,
    borderWidth: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8
  },
  text: {
    fontFamily: 'pretendard-medium',
    fontSize: 12
  }
});