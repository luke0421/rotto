import API from "./Api";

export const getBeanList = async () => {
    try {
      const res = await API.get("/bean");
      //console.log(res.data);
      return res.data;
    } catch (err) {
      //console.error(err);
    }
  };

  export const getBeanDetail = async (code) => {
    try {
      const res = await API.get(`/bean/${code}`);
      //console.log(res.data);
      return res.data;
    } catch (err) {
      //console.error(err);
    }
  };