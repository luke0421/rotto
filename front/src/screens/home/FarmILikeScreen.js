import { useNavigation } from "@react-navigation/native";
import { useEffect, useState } from "react";
import {
    FlatList,
    Pressable,
    StyleSheet,
    View,
    Image
} from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { Text } from "react-native-paper";
import Colors from "../../constants/Colors";
import { getFarmILikeList } from "../../utils/FarmILikeApi";

const FarmILikeListScreen = () => {

    const [farmILikeList, setfarmILikeList] = useState([]);

    const getList = async () => {
        const res = await getFarmILikeList();
        //console.log(res.data.farms);
        setfarmILikeList(res.data.farms);
    };

    useEffect(() => {
        getList();
    }, []);

    const Navigation = useNavigation();

    const rendeFarmILikeList = (farm) => {
        return (
        <View style={styles.container}>
            <View key={farm.farmCode} style={styles.itemStyle}>
                <View style={styles.itemContentStyle}>
                    <Image
                        source={require("../../../assets/images/farmfarm.png")}
                        style={{ width: 60, height: 60 }}
                    />
                    <View
                        style={{ marginLeft: 10 }}
                    >
                        <Text style={styles.content}>
                            {farm.farmName}
                        </Text>
                        <Text style={styles.subContent}>
                            {farm.beanName}
                        </Text>
                    </View>
                </View>
                <View>
                    <Text style={{ marginRight: 10 }}>청약 진행 중</Text>
                </View>
            </View>
        </View>
        );
    };

    return (
        <View style={styles.screen}>
            <Pressable
                style={styles.iconContainer}
                onPress={() => Navigation.navigate("home")}
            >
                <Ionicons name="chevron-back" size={30} color={Colors.iconGray} />
            </Pressable>
            <View
                style={[
                    {
                        marginTop: 10,
                        flexDirection: "row",
                        alignItems: "center",
                        justifyContent: "center",
                    },
                ]}
            >
                <Text style={styles.header}>관심 농장</Text>
            </View>
            {farmILikeList && <FlatList
                data={farmILikeList}
                keyExtractor={(item, index) => index.toString()}
                renderItem={rendeFarmILikeList}
            />}
            <Pressable
                style={styles.itemStyle}
                onPress={() => Navigation.navigate("farmList")}
            >
                <View style={styles.itemContentStyle}>
                    <View style={{ backgroundColor: "#F8F8F8", height: 40, width: 40, borderRadius: 25, alignItems: "center", justifyContent: "center", marginLeft: 10 }}>
                        <Ionicons name="add" size={24} color="#5492F7" />
                    </View>
                    <Text style={{ marginLeft: 20, color: "#BBBBBB", fontFamily: "pretendard-bold", fontSize: 16 }}>
                        추가하기
                    </Text>
                </View>
            </Pressable>
        </View>
    );
};

export default FarmILikeListScreen;

const styles = StyleSheet.create({
    container: {
        flexDirection: "row",
        alignContent: "center",
        margin: 10,
    },
    screen: {
        flex: 1,
        backgroundColor: "white",
    },
    header: {
        fontFamily: "pretendard-extraBold",
        fontSize: 26,
        paddingBottom: 20,
    },
    title: {
        fontFamily: "pretendard-regular",
        fontSize: 16,
        margin: 10,
        alignContent: "stretch",
        justifyContent: "center",
    },
    content: {
        marginTop: 5,
    },
    iconContainer: {
        margin: 15,
        position: "absolute",
        justifyContent: "flex-start",
        alignItems: "center",
    },
    qText: {
        alignItems: "center",
        justifyContent: "center",
        fontSize: 16,
        color: "grey",
        margin: 10,
    },
    myIconStyle: {
        margin: 10,
        color: "grey",
        justifyContent: "center",
        alignContent: "center",
    },
    myContent: {
        marginHorizontal: 40,
        marginVertical: 5,
        padding: 10,
    }
});
