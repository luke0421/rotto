// import { View, StyleSheet, TouchableOpacity } from "react-native";
// import MyHeader from "../../components/common/MyHeader";
// import MyWallet from "../../components/my/MyWallet";
// import MyAccount from "../../components/my/MyAccount";
// import { useDispatch, useSelector } from "react-redux";
// import { setWalletModal } from "../../stores/mySlice";
// import { useCallback, useEffect } from "react";
// import { useFocusEffect } from "@react-navigation/native";
// import InvestmentList from "../../components/my/InvestmentList";

// const MyScreen = ({navigation}) => {
//   const dispatch = useDispatch();
//   const myPageInfo = useSelector(state => state.myPageInfo);

//   const pressHandler = () => {
//     if (myPageInfo.isWalletModalOpen) {
//       dispatch(setWalletModal(false));
//     }
//   };

//   useFocusEffect(
//     useCallback(() => {
//       return () => {
//         dispatch(setWalletModal(false));
//       }
//     }, [navigation])
//   );

//   return (
//     <MyHeader>
//       <TouchableOpacity style={{ flex: 1 }} onPress={pressHandler} activeOpacity={1}>
//         <View style={styles.container}>
//           <MyAccount navigation={navigation} />
//           <MyWallet />
//           <InvestmentList navigation={navigation} />
//         </View>
//       </TouchableOpacity>
//     </MyHeader>
//   );
// };

// export default MyScreen;

// const styles = StyleSheet.create({
//   container: {
//     alignItems: 'center',
//     marginTop: 28,
//     gap: 20,
//     flex: 1
//   }
// })