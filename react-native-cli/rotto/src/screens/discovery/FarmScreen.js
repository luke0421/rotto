import { useEffect, useState } from "react";
import {
  FlatList,
  Image,
  Modal,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  useWindowDimensions,
} from "react-native";
import { getFarmDetail } from "../../utils/farmAPi";
import { addLike, removeLike } from "../../utils/FarmILikeApi";
import { Ionicons } from "@expo/vector-icons";

import StackHeader from "../../components/common/StackHeader";
import Colors from "../../constants/Colors";
import { S3URL } from "@env"

const FarmScreen = ({ route }) => {
  const { width, height } = useWindowDimensions();
  const [farm, setFarm] = useState(null);
  const farmImagesCount = Array.from({ length: 10 }, (_, i) =>
    (i + 1).toString().padStart(3, "0")
  );
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedImage, setSelectedImage] = useState(null);

  const openModal = (imageUri) => {
    setSelectedImage(imageUri);
    setModalVisible(true);
  };

  const closeModal = () => {
    setModalVisible(false);
    setSelectedImage(null);
  };

  const getDetail = async () => {
    const res = await getFarmDetail(route.params.farmCode);
    setFarm(res);
    //console.log(res);
  };

  useEffect(() => {
    getDetail();
  }, [route.params.farmCode]);

  const handlePressHeart = () => {
    farm.isLiked ? removeLike(farm.farmCode) : addLike(farm.farmCode);
    setFarm((prevFarm) => ({
      ...prevFarm,
      isLiked: !farm.isLiked,
      likeCount: farm.isLiked ? farm.likeCount - 1 : farm.likeCount + 1,
    }));
  };

  return (
    <>
      {farm && (
        <View style={styles.screen}>
          <StackHeader
            screenName="farmList"
            title={farm.farmName}
            detail={true}
            isLiked={farm.isLiked}
            onPressHeart={handlePressHeart}
            returnTo={route.params.returnTo}
          />
          <ScrollView style={styles.scrollView}>
            <View style={styles.mainContainer}>
              <View style={styles.badgeContainer}>
                {farm.isFunding && (
                  <View
                    style={[styles.badge, { backgroundColor: Colors.btnBlue }]}
                  >
                    <Text style={styles.badgeText}>청약진행중</Text>
                  </View>
                )}
                {farm.isFunding && (
                  <View style={[styles.badge, { backgroundColor: "black" }]}>
                    <Text style={styles.badgeText}>마감 D-{farm.deadline}</Text>
                  </View>
                )}
              </View>
              <View style={styles.rowConatainer}>
                <View style={styles.rowConatainer}>
                  <Image
                    source={{
                      uri: `${S3URL}/farm_img/1/${farm.farmLogoPath}`,
                    }}
                    style={styles.farmLogo}
                  />
                  <Text style={styles.title}>{farm.farmName}</Text>
                </View>
                <View style={styles.rowConatainer}>
                  <Text style={styles.likeCountText}>{farm.likeCount}</Text>
                  <Pressable
                    style={styles.iconContainer}
                    onPress={handlePressHeart}
                  >
                    {farm.isLiked ? (
                      <Ionicons name="heart" size={20} color="red" />
                    ) : (
                      <Ionicons name="heart-outline" size={20} color="red" />
                    )}
                  </Pressable>
                </View>
              </View>
              <View style={styles.content}>
                <Text>
                  {farm.farmIntroduce}
                </Text>
              </View>
            </View>
            <View style={styles.container}>
              <Text style={styles.title}>농장 정보</Text>
              <View style={styles.content}>
                <View style={{ flexDirection: "row", marginBottom: 30 }}>
                  <View style={{ flex: 1 }}>
                    <Text style={styles.infoTitle}>지난 수익률</Text>
                    <Text
                      style={
                        farm.returnRate > 0
                          ? [styles.infoContent, styles.returnRatePlus]
                          : [styles.infoContent, styles.returnRateMinus]
                      }
                    >
                      {farm.returnRate > 0 ? "+" : "-"}
                      {farm.returnRate}%
                    </Text>
                  </View>
                  <View style={{ flex: 1 }}>
                    <Text style={styles.infoTitle}>재배 원두</Text>
                    <Text style={styles.infoContent}>{farm.beanName}</Text>
                  </View>
                </View>
                <View style={{ flexDirection: "row" }}>
                  <View style={{ flex: 1 }}>
                    <Text style={styles.infoTitle}>농장 시작일</Text>
                    <Text style={styles.infoContent}>
                      {farm ? farm.farmStartedDate.split("T")[0] : ""}
                    </Text>
                  </View>
                  <View style={{ flex: 1 }}>
                    <Text style={styles.infoTitle}>규모</Text>
                    <Text style={styles.infoContent}>{farm.farmScale} ha</Text>
                  </View>
                </View>
              </View>
            </View>
            <View style={styles.container}>
              <Text style={styles.title}>수상내역</Text>
              <View style={styles.content}>
                <Text>{farm.awardHistory}</Text>
              </View>
            </View>
            <View style={styles.container}>
              <Text style={styles.title}>주소</Text>
              <View style={styles.content}>
                <Text>{farm.farmAddress}</Text>
              </View>
            </View>
            <View style={styles.imageContainer}>
              <Text style={styles.imageTitle}>농장 둘러보기</Text>
              <View style={styles.imageContent}>
                {farmImagesCount.map((item) => (
                  <TouchableOpacity
                    key={item}
                    onPress={() =>
                      openModal(
                        `${S3URL}/farm_img/${farm.farmCode}/farm${item}.jpg`
                      )
                    }
                  >
                    <Image
                      source={{
                        uri: `${S3URL}/farm_img/${farm.farmCode}/farm${item}.jpg`,
                      }}
                      style={{
                        width: (width / 100) * 32,
                        height: (width / 100) * 32,
                        marginBottom: 5,
                      }}
                      key={item}
                    />
                  </TouchableOpacity>
                ))}
              </View>
            </View>
          </ScrollView>
          {selectedImage && (
            <Modal
              visible={modalVisible}
              transparent={true}
              animationType="fade"
            >
              <View style={styles.modalBackground}>
                <TouchableOpacity
                  style={styles.modalContainer}
                  onPress={closeModal}
                >
                  <Image
                    source={{ uri: selectedImage }}
                    style={styles.modalImage}
                  />
                </TouchableOpacity>
              </View>
            </Modal>
          )}
        </View>
      )}
    </>
  );
};

export default FarmScreen;

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: Colors.bgGray,
  },
  info: {
    padding: 25,
  },
  container: {
    backgroundColor: "white",
    padding: 25,
    marginBottom: 10,
  },
  mainContainer: {
    backgroundColor: "white",
    paddingHorizontal: 25,
    paddingBottom: 25,
    marginBottom: 10,
  },
  imageContainer: {
    backgroundColor: "white",
    paddingTop: 25,
    marginBottom: 10,
  },
  imageTitle: {
    fontFamily: "pretendard-extraBold",
    fontSize: 20,
    paddingHorizontal: 25,
  },
  imageContent: {
    marginHorizontal: 5,
    marginTop: 20,
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "space-between",
  },
  rowConatainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  title: {
    fontFamily: "pretendard-extraBold",
    fontSize: 20,
  },
  content: {
    marginTop: 20,
  },
  farmLogo: {
    height: 30,
    width: 30,
    resizeMode: "cover",
    borderRadius: 15,
    marginRight: 10,
  },
  infoTitle: {
    fontFamily: "pretendard-extraBold",
    fontSize: 15,
  },
  infoContent: {
    fontFamily: "pretendard-semiBold",
    fontSize: 15,
  },
  badgeContainer: {
    flexDirection: "row",
    gap: 6,
    alignItems: "center",
    paddingBottom: 15,
  },
  badge: {
    paddingVertical: 4,
    paddingHorizontal: 12,
    borderRadius: 25,
  },
  badgeText: {
    fontSize: 12,
    fontFamily: "pretendard-semiBold",
    color: "white",
  },
  iconContainer: {
    marginLeft: 10,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  likeCountText: {
    fontFamily: "pretendard-semiBold",
  },
  modalBackground: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.5)",
    justifyContent: "center",
    alignItems: "center",
  },
  modalContainer: {
    width: "95%",
    height: "100%",
    backgroundColor: "#ffffff00",
    borderRadius: 10,
    overflow: "hidden",
  },
  modalImage: {
    width: "100%",
    height: "100%",
    resizeMode: "contain",
  },
  returnRatePlus: {
    color: "red",
  },
  returnRateMinus: {
    color: "blue",
  },
});
