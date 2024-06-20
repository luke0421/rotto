import API from "./Api";

export const getFarmNewsList = async (page) => {
  try {
    const res = await API.get("/news", {
      params: {
        page,
      },
    });
    return res.data.newsList;
  } catch (err) {
    //console.error('FarmNewsApi getFarmNewsList error: ',err);
  }
};

export const getFarmNewsDetail = async (newsCode) => {
  try {
    const res = await API.get(`/news/${newsCode}`);
    return res;
  } catch (err) {
    //console.error('FarmNewsApi getFarmNewsDetail error: ',err);
  }
};
