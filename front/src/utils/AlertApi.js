import API from "./Api";

export const getAlertList = async () => {
  try {
    const res = await API.get("/alert");
    //console.log(res.data);
    return res.data;
  } catch (err) {
    //console.error(err);
  }
};

export const getAlertDetail = async (code) => {
  try {
    const res = await API.get(`/alert/${code}`);
    //console.log(res.data);
    return res.data;
  } catch (err) {
    //console.error(err);
  }
};

export const deleteAlert = async (code) => {
  try {
    const res = await API.delete(`/alert/${code}`);
    //console.log(res.data);
    return res.data;
  } catch (err) {
    //console.error(err);
  }
};

export const readAllAlert = async () => {
  try {
    const res = await API.put("/alert/all-read");
    //console.log(res.data);
    return res.data;
  } catch (err) {
    //console.error(err);
  }
};

export const deleteAllAlert = async () => {
    try {
      const res = await API.delete("/alert/all-delete");
      //console.log(res.data);
      return res.data;
    } catch (err) {
      //console.error(err);
    }
  };