import { View, Text, StyleSheet } from 'react-native';
import TokenService from '../../utils/token';
import { useEffect } from 'react';
import Colors from '../../constants/Colors';
import LottieView from 'lottie-react-native';
import KeyService from '../../utils/pinCode';

const SplashScreen = ({navigation}) => {
  useEffect(() => {
    const checkToken = async () => {
      try {
        await new Promise(resolve => setTimeout(resolve, 2000));
        
        const accessToken = await TokenService.getAccessToken();
        const refreshToken = await TokenService.getRefreshToken();
        const pinCode = await KeyService.getPinCode();

        if (accessToken && refreshToken) {
          if (pinCode) {
            navigation.replace('BioAuth');
          } else {
            navigation.replace('PINSetting');
          }
        } else {
          navigation.replace('Onboarding');
        }
      } catch (err) {
        //console.error(err);
      }
    };

    checkToken();
  }, [navigation]);

  return (
    <View style={styles.container}>
      <LottieView
        autoPlay
        style={{
          width: '100%',
          height: '100%',
        }}
        source={require('../../../assets/splash_rotto.json')}
      />
    </View>
  )
}

export default SplashScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.bgOrg
  }
});