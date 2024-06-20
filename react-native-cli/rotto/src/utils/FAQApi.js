import API from "./Api";

export const getFAQList = async (page) => {
  try {
    const res = await API.get("/faq", {
      params: {
        page,
      },
    });
    //console.log(res);
    return res.data;
  } catch (err) {
    //console.error(err);
  }
};

export const getFAQDetail = async (code) => {
  try {
    const res = await API.get(`/faq/${code}`);
    return res.data;
  } catch (err) {
    //console.error(err);
  }
};
