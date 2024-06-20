import { View, Text, Pressable, Switch, StyleSheet, Alert } from 'react-native';
import Colors from '../../constants/Colors';
import { useCallback, useEffect, useState } from 'react';
import ToggleButton from '../../components/common/ToggleButton';
import SettingService from '../../utils/setting';
import { logout, resign } from '../../utils/userApi';
import TokenService from '../../utils/token';
import KeyService from '../../utils/pinCode';
import { useFocusEffect } from '@react-navigation/core';

const SettingScreen = ({navigation}) => {
  const [isCheckedBio, setIsCheckedBio] = useState();
  const [isCheckedPush, setIsCheckedPush] = useState();
  const [isRegisteredPinCode, setIsRegisteredPinCode] = useState(true);
  const [isLoaded, setIsLoaded] = useState(false);
  
  const toggleBio = async () => {
    await SettingService.setBiometricEnabled(!isCheckedBio);
    setIsCheckedBio(!isCheckedBio);
  };

  const togglePush = async () => {
    await SettingService.setPushEnabled(!isCheckedPush);
    setIsCheckedPush(!isCheckedPush);
  };

  useFocusEffect(
    useCallback(() => {
      const getBiometricEnabled = async () => {
        const isEnabled = await SettingService.getBiometricEnabled();
        setIsCheckedBio(() => isEnabled ? isEnabled : false);
      };
  
      const getPushEnabled = async () => {
        const isEnabled = await SettingService.getPushEnabled();
        setIsCheckedPush(() => isEnabled ? isEnabled : false);
      };
  
      const getPinCode = async () => {
        const pinCode = await KeyService.getPinCode();
        setIsRegisteredPinCode(pinCode !== null);
      };
  
      const getToken = async () => {
        const token = await TokenService.getAccessToken();
        const refresh = await TokenService.getRefreshToken();
        //console.log(token);
        //console.log(refresh);
      };
  
      getToken();
  
      const loadInitialData = async () => {
        await Promise.all([
          getBiometricEnabled(),
          getPushEnabled(),
          getPinCode()
        ]);
        setIsLoaded(true);
      };
  
      loadInitialData();
    }, [])
  )

  const logoutHandler = async () => {
    await logout();
    await TokenService.clearAllData();
    navigation.reset({
      index: 0,
      routes: [{ name: "Onboarding" }],
    });
  };

  const resignHandelr = () => {
    const resignUser = async () => {
      await resign();
      navigation.reset({
        index: 0,
        routes: [{ name: "Onboarding" }]
      });
    };

    Alert.alert('', '탈퇴하시겠습니까?', 
      [
        { text: '취소', onPress: () => {}, style: 'cancel' },
        { text: '삭제', onPress: resignUser, style: 'destructive' }
      ],
      {
        cancelable: true,
        onDismiss: () => {}
      }
    );
  }

  return (
    <View style={styles.screen}>
      <View style={styles.topBar}>
        <Text style={styles.title}>설정</Text>
      </View>
      {
        isLoaded &&
        <View style={styles.container}>
          <Text style={styles.subTitle}>보안</Text>
          <Pressable 
            style={styles.menuContainer} 
            onPress={() => navigation.navigate('bioAuth')}
          >
            <Text style={styles.menuText}>비밀번호 변경</Text>
          </Pressable>
          <Pressable 
            style={styles.menuContainer} 
            onPress={() => {
              if (isRegisteredPinCode) {
                navigation.navigate('pinChange');
              } else {
                navigation.navigate('PINSetting');
              }
            }}
          >
            <Text style={styles.menuText}>{ isRegisteredPinCode ? '간편 비밀번호 변경' : '간편 비밀번호 등록' }</Text>
          </Pressable>
          <View style={styles.menuContainer}>
            <Text style={styles.menuText}>생체 인증 등록/해제</Text>
            <ToggleButton 
              onToggle={toggleBio}
              isOn={isCheckedBio}
            />
          </View>
          <View style={styles.line} />
          <View style={styles.menuContainer}>
            <Text style={styles.menuText}>버전 정보</Text>
            <Text style={styles.menuText}>v1.0.0</Text>
          </View>
          <View style={styles.menuContainer}>
            <Text style={styles.menuText}>알림 설정</Text>
            <ToggleButton 
              onToggle={togglePush}
              isOn={isCheckedPush}
            />
          </View>
          <View style={styles.line} />
          <Pressable style={styles.menuContainer} onPress={() => navigation.navigate('inquiry')}>
            <Text style={styles.menuText}>문의하기</Text>
          </Pressable>
          <Pressable style={styles.menuContainer} onPress={() => navigation.navigate('terms')}>
            <Text style={styles.menuText}>이용약관</Text>
          </Pressable>
          <View style={styles.line} />
          <Pressable onPress={logoutHandler} style={styles.menuContainer}>
            <Text style={[styles.menuText, { color: 'red' }]}>로그아웃</Text>
          </Pressable>
          <Pressable style={styles.menuContainer} onPress={resignHandelr}>
            <Text style={[styles.menuText, { color: '#888888' }]}>탈퇴하기</Text>
          </Pressable>
        </View>
      }
    </View>
  )
}

export default SettingScreen;

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center'
  },
  topBar: {
    width: '100%',
    height: '12%',
    justifyContent: 'center',
    alignItems: 'center'
  },
  container: {
    width: '82%',
    gap: 12
  },
  title: {
    fontFamily: 'pretendard-medium',
    fontSize: 20
  },
  subTitle: {
    fontFamily: 'pretendard-medium',
    fontSize: 18,
    color: Colors.fontGray
  },
  menuContainer: {
    width: '100%',
    justifyContent: 'space-between',
    flexDirection: 'row',
    alignItems: 'center'
  },
  menuText: {
    fontFamily: 'pretendard-medium',
    fontSize: 16,
  },
  line: {
    width: '100%',
    borderBottomColor: Colors.fontGray,
    borderBottomWidth: 0.5
  }
});