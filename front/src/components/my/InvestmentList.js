import { View, Text, StyleSheet, Pressable } from 'react-native';
import Colors from '../../constants/Colors';
import { useState } from 'react';
import TradeList from './TradeList';
import ApplyList from './ApplyList';
import CancelList from './CancelList';
import OwnList from './OwnList';

const InvestmentList = ({navigation}) => {
  const [selectedMenu, setSelectedMenu] = useState('보유 내역');

  let ComponentToShow;
  switch (selectedMenu) {
    case '보유 내역':
      ComponentToShow = TradeList;
      break;
    case '신청 내역':
      ComponentToShow = ApplyList;
      break;
    case '정산 내역':
      ComponentToShow = OwnList;
      break;
    case '해지 내역':
      ComponentToShow = CancelList;
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>투자 내역</Text>
      <View style={styles.menuContainer}>
        <Pressable onPress={() => setSelectedMenu('보유 내역')}>
          <Text style={[styles.menu, { color: selectedMenu === '보유 내역' ? 'black' : Colors.fontGray }]}>보유 내역</Text>
        </Pressable>
        <Pressable onPress={() => setSelectedMenu('신청 내역')}>
          <Text style={[styles.menu, { color: selectedMenu === '신청 내역' ? 'black' : Colors.fontGray }]}>신청 내역</Text>
        </Pressable>
        <Pressable onPress={() => setSelectedMenu('정산 내역')}>
          <Text style={[styles.menu, { color: selectedMenu === '정산 내역' ? 'black' : Colors.fontGray }]}>정산 내역</Text>
        </Pressable>
        <Pressable onPress={() => setSelectedMenu('해지 내역')}>
          <Text style={[styles.menu, { color: selectedMenu === '해지 내역' ? 'black' : Colors.fontGray }]}>해지 내역</Text>
        </Pressable>
      </View>
      <View style={{flex: 1}}>
        {ComponentToShow && <ComponentToShow navigation={navigation} />}
      </View>
    </View>
  )
}

export default InvestmentList;

const styles = StyleSheet.create({
  container: {
    width: '100%',
    flex: 1,
    borderTopLeftRadius: 25,
    borderTopRightRadius: 25,
    backgroundColor: 'white',
    paddingHorizontal: 24,
    paddingTop: 16,
    gap: 15
  },
  title: {
    fontSize: 20,
    fontFamily: 'pretendard-bold'
  },
  menuContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between'
  },
  menu: {
    fontSize: 16,
    fontFamily: 'pretendard-medium'
  }
})