import {StatusBar} from 'expo-status-bar';
import {StyleSheet, SafeAreaView} from 'react-native';
import {NavigationContainer} from '@react-navigation/native';
import {store} from './src/stores/Store';
import {Provider} from 'react-redux';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import AuthRouters from './src/routers/AuthRouters';
import Constants from 'expo-constants';
import {useFonts} from 'expo-font';
import Colors from './src/constants/Colors';
import '@walletconnect/react-native-compat';
import {WagmiConfig, configureChains, createConfig} from 'wagmi';
import {mainnet} from 'viem/chains';
import {
  Web3Modal,
  createWeb3Modal,
  defaultWagmiConfig,
} from '@web3modal/wagmi-react-native';
import {jsonRpcProvider} from 'wagmi/providers/jsonRpc';
import {PROJECT_ID, CHAIN_ID, RPC_URL} from '@env';
import {useEffect} from 'react';

import SseComponent from './src/components/home/SseComponent';

const customChain = {
  id: CHAIN_ID,
  name: 'ssafy',
  rpcUrls: {
    public: {
      http: [RPC_URL],
    },
  },
  nativeCurrency: {
    name: 'Ether',
    symbol: 'ETH',
    decimals: 18,
  },
  testnet: false,
};

const projectId = PROJECT_ID;

const metadata = {
  name: 'rotto',
  description: '커피 STO 투자 증권 앱',
  url: 'rotto://',
  icons: ['../../../assets/images/skyIcon.png'],
  redirect: {
    native: 'rotto://',
  },
};

const chains = [mainnet];

const wagmiConfig = defaultWagmiConfig({chains, projectId, metadata});

const {publicClient} = configureChains(
  [customChain],
  [jsonRpcProvider({rpc: () => ({http: customChain.rpcUrls.public.http})})],
);

createConfig({
  autoConnect: true,
  publicClient,
});

createWeb3Modal({
  projectId,
  chains,
  wagmiConfig,
  enableAnalytics: true,
});

export default function App() {
  const [fontsLoaded] = useFonts({
    'pretendard-thin': require('./assets/fonts/Pretendard-Thin.ttf'),
    'pretendard-extraLight': require('./assets/fonts/Pretendard-ExtraLight.ttf'),
    'pretendard-light': require('./assets/fonts/Pretendard-Light.ttf'),
    'pretendard-regular': require('./assets/fonts/Pretendard-Regular.ttf'),
    'pretendard-medium': require('./assets/fonts/Pretendard-Medium.ttf'),
    'pretendard-semiBold': require('./assets/fonts/Pretendard-SemiBold.ttf'),
    'pretendard-bold': require('./assets/fonts/Pretendard-Bold.ttf'),
    'pretendard-extraBold': require('./assets/fonts/Pretendard-ExtraBold.ttf'),
    'pretendard-black': require('./assets/fonts/Pretendard-Black.ttf'),
  });
  const statusBarStyle = 'light';
  const statusBarColor = Colors.bgOrg;

  return (
    <SafeAreaProvider>
      <Provider store={store}>
        <WagmiConfig config={wagmiConfig}>
          {/* <WagmiConfig config={config}> */}
          <NavigationContainer>
            {/* <StatusBar style="auto" /> */}
            {/* <StatusBar style={styles.statusBar} /> */}
            <StatusBar
              backgroundColor={statusBarColor}
              style={statusBarStyle}
            />
            {fontsLoaded && (
              <SafeAreaView style={styles.safeAreaView}>
                <SseComponent />
                <AuthRouters />
              </SafeAreaView>
            )}
            <Web3Modal />
          </NavigationContainer>
        </WagmiConfig>
      </Provider>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  safeAreaView: {
    flex: 1,
    paddingTop: Constants.statusBarHeight,
  },
});
