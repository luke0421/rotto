import * as SecureStore from 'expo-secure-store'

const StorageKeys = {
  PinCode: 'pinCode'
};

const KeyService = {
  // 핀번호 저장
  setPinCode: async (pinCode) => {
    try {
      await SecureStore.setItemAsync(StorageKeys.PinCode, pinCode);
    } catch (error) {
      //console.error('핀번호 저장 실패', error);
    }
  },

  // 핀번호 조회
  getPinCode: async () => {
    try {
      return await SecureStore.getItemAsync(StorageKeys.PinCode);
    } catch (error) {
      //console.error('핀번호 조회 실패', error);
      return null;
    }
  },

  // 핀번호 삭제
  clearPinCode: async () => {
    try {
      await SecureStore.deleteItemAsync(StorageKeys.PinCode);
    } catch (error) {
      //console.error(error);
    }
  },
};


export default KeyService;
