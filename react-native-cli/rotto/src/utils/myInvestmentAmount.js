import API from "./Api";

export const getMyInvestment = async () => {
  try {
    const res = await API.get("/trade-history/home");
    return res.data;
  } catch (err) {
    //console.error(err);
  }
};