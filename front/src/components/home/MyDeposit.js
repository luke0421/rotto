import { useState, useEffect } from "react";
import { StyleSheet, Text, View, Image, Dimensions } from "react-native";
import Colors from "../../constants/Colors";
import { getMyInvestment } from "../../utils/myInvestmentAmount";

const { width } = Dimensions.get("window");

const MyDeposit = () => {
  const [MyInvestment, setMyInvestment] = useState([]);

  const getList = async () => {
    const res = await getMyInvestment();
    setMyInvestment(res);
  };

  useEffect(() => {
    getList();
  }, []);



  return (
    <View style={styles.componentContainer}>
      <Text style={styles.header}>나의 자산</Text>
      <View style={styles.boxContainer}>
        <View style={styles.depositContainer}>
          <View style={{ flex: 1, alignItems: "baseline" }}>
            <Text style={styles.content}>순 자산</Text>
            <Text>
              <Text style={styles.highlight}>
                {MyInvestment.totalInvestAmount}
              </Text>{" "}
              원
            </Text>
          </View>

          <View style={{ flex: 1, alignItems: "flex-end" }}>
            <View style={{ flexDirection: "row" }}>
              <Text>손익</Text>
              <Text style={styles.variance}>{MyInvestment.totalProceed}</Text>
            </View>
            <View style={{ flexDirection: "row" }}>
              <Text>수익률</Text>
              <Text style={styles.variance}>{MyInvestment.totalPercent}</Text>
            </View>
          </View>
        </View>
        <View style={styles.line}></View>
        <View>
          <View>
            <Text style={styles.content}> 투자 현황</Text>
          </View>
          {MyInvestment.tradeHistoryHomeInfoDtoss && MyInvestment.tradeHistoryHomeInfoDtoss.map((token, index) => (
            <View>
            <View style={styles.itemStyle}>
              <View style={styles.itemContentStyle}>
                <Image
                  source={require("../../../assets/images/farmfarm.png")}
                  style={{ width: 60, height: 60 }}
                />
                <View style={{ marginLeft: 10 }}>
                  <Text style={styles.itemContent}>{token.title}</Text>
                  <Text style={styles.itemSubContent}>{token.tokenNum} ROTTO </Text>
                </View>
              </View>
              <View>
                <Text style={{ marginRight: 10 }}></Text>
                <Text style={{ marginRight: 5 }}>{token.expenses}원</Text>
              </View>
            </View>
          </View>            
          ))}
          <View>
            <Text>상세 보기</Text>
          </View>
        </View>
      </View>
    </View>
  );
};

export default MyDeposit;

const styles = StyleSheet.create({
  componentContainer: {
    marginTop: 20,
    position: "relative",
  },
  header: {
    fontFamily: "pretendard-extraBold",
    fontSize: 18,
    marginHorizontal: 25,
    color: "white",
  },
  boxContainer: {
    marginTop: 10,
    marginHorizontal: 20,
    marginBottom: 10,
    // height: 200,
    borderRadius: 15,
    padding: 25,
    backgroundColor: "white",
  },
  depositContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    alignContent: "space-between",
    flex: 1,
  },
  iconContainer: {
    marginTop: 15,
    justifyContent: "center",
    alignItems: "center",
  },
  title: {
    fontFamily: "pretendard-bold",
    fontSize: 17,
    marginBottom: 10,
  },
  content: {
    fontFamily: "pretendard-regular",
    fontSize: 18,
    margin: 10,
  },
  highlight: {
    fontFamily: "pretendard-bold",
    fontSize: 25,
  },
  variance: {
    fontFamily: "pretendard-regular",
    fontSize: 15,
    color: "red",
  },
  depositDetail: {
    marginTop: 20,
    // backgroundColor: "#f1ebeb",
  },
  line: {
    // height:1,
    marginTop: 20,
    borderColor: Colors.fontGray,
    borderWidth: 0.5,
  },
  itemStyle: {
    marginBottom: 10,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  itemContentStyle: {
    marginHorizontal: 10,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  itemContent: {
    fontFamily: "pretendard-regular",
    fontSize: 12,
    coloc: "grey",
  },
  itemSubContent: {
    fontFamily: "pretendard-bold",
    fontSize: 16,
  },
});
