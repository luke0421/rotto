import AsyncStorage from "@react-native-async-storage/async-storage";

const StorageKeys = {
  BIOMETRIC_ENABLED: 'bioMetric',
  PUSH_ENABLED: 'push'
};

const SettingService = {
  // 생체 인식 사용 여부 저장
  setBiometricEnabled: async (isEnabled) => {
    try {
      await AsyncStorage.setItem(StorageKeys.BIOMETRIC_ENABLED, JSON.stringify(isEnabled));
      return true;
    } catch (e) {
      //console.log(e);
      return false;
    }
  },

  // 생체 인식 사용 여부 불러오기
  getBiometricEnabled: async () => {
    try {
      const value = await AsyncStorage.getItem(StorageKeys.BIOMETRIC_ENABLED);
      return value !== null ? JSON.parse(value) : null;
    } catch(e) {
      //console.log(e);
      return null;
    }
  },

  // push 알람 사용 여부 저장
  setPushEnabled: async (isEnabled) => {
    try {
      await AsyncStorage.setItem(StorageKeys.PUSH_ENABLED, JSON.stringify(isEnabled));
      return true;
    } catch (e) {
      //console.log(e);
      return false;
    }
  },

  // push 알람 사용 여부 불러오기
  getPushEnabled: async () => {
    try {
      const value = await AsyncStorage.getItem(StorageKeys.PUSH_ENABLED);
      return value !== null ? JSON.parse(value) : null;
    } catch(e) {
      //console.log(e);
      return null;
    }
  }

};

export default SettingService;
