import API from "./Api";

export const getFarmList = async (page, sort, keyword, isLiked, subsStatus, minPrice, maxPrice, beanType) => {
  try {
    const res = await API.get("/farm",{
      params: {
        "page" : page,
        "sort" : sort,
        "keyword" : keyword, 
        "is-liked" : isLiked, 
        "subs-status" : subsStatus, 
        "min-price" : minPrice, 
        "max-price" : maxPrice, 
        "bean-type" : beanType
      },
    });
    return res.data;
  } catch (err) {
  }
};

export const getFarmDetail = async (code) => {
  try {
    const res = await API.get(`/farm/${code}`);
    return res.data;
  } catch (err) {
  }
};

export const getFarmTOP10 = async () => {
  try {
    const res = await API.get("/farm/top-ten");
    return res.data;
  } catch (err) {
  }
};