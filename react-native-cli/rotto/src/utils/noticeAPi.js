import API from "./Api";

export const getNoticeList = async (page) => {
  try {
    const res = await API.get("/notice", {
      params: {
        page,
      },
    });
    //console.log(res.data);
    return res.data;
  } catch (err) {
    //console.error(err);
  }
};

export const getNoticeDetail = async (code) => {
  try {
    const res = await API.get(`/notice/${code}`);
    //console.log(res.data);
    return res.data;
  } catch (err) {
    //console.error(err);
  }
};
