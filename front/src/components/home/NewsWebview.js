import React from 'react';
import {SafeAreaView, StyleSheet, Dimensions, ref} from 'react-native';
import WebView from 'react-native-webview';

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

const NewsWebview = ({route}) => {
  const {newsDetailLink} = route.params;
  //console.log(newsDetailLink);

  return (
    <SafeAreaView style={styles.container}>
      <WebView
        ref={ref}
        style={styles.webview}
        source={{uri: newsDetailLink}}
      />
    </SafeAreaView>
  );
};

export default NewsWebview;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  webview: {
    flex: 1,
    width: windowWidth,
    height: windowHeight,
  },
});