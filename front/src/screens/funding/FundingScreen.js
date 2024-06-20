import { StyleSheet } from "react-native";
import MyHeader from "../../components/common/MyHeader";
import FilterBar from "../../components/funding/FilterBar";
import FundingList from "../../components/funding/FundingList";
import FilterModal from "../../components/funding/FilterModal";
import SearchModal from "../../components/funding/SearchModal";

const FundingScreen = ({navigation}) => {
  
  return (
    <MyHeader isSearch={true}>
      <FilterBar />
      <FundingList navigation={navigation} />
      <FilterModal />
      <SearchModal />
    </MyHeader>
  )
}

export default FundingScreen;

const styles = StyleSheet.create({
  
});
