import { View, Text, ScrollView, StyleSheet } from 'react-native';
import { useEffect, useState } from 'react';
import { getTerms } from '../../utils/settingApi';
import DetailTopBar from '../../components/common/DetailTopBar';

const TermsScreen = ({navigation}) => {
  const [terms, setTerms] = useState();

  useEffect(() => {
    const getTermsData = async () => {
      const res = await getTerms();
      setTerms(res.content);
    };

    getTermsData();
  }, [navigation])

  return (
    <View style={styles.container}>
      <DetailTopBar title="이용 약관" navigation={navigation} />
      {
        terms &&
        <ScrollView style={styles.contentContainer} showsVerticalScrollIndicator={false}>
          <Text>{terms}</Text>
        </ScrollView>
      }
    </View>
  )
}

export default TermsScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    alignItems: 'center'
  },
  contentContainer: {
    width: '82%',
    paddingHorizontal: 16
  }
});