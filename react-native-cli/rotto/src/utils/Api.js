import axios from "axios";
import TokenService from "./token";
import { useNavigation } from "@react-navigation/native";
import { DOMAIN_URL } from "@env"
import { AccessibilityInfo } from "react-native";

const DOMAIN = DOMAIN_URL

const API = axios.create({
  baseURL: DOMAIN + "/api",
  withCredentials: true,
});

const reissueAPI = axios.create({
  baseURL: DOMAIN + "/api",
  withCredentials: true,
});

API.interceptors.request.use(
  async (config) => {
    const accessToken = await TokenService.getAccessToken();
    // console.log(accessToken)
    if (accessToken) {
      config.headers['Authorization'] = 'Bearer ' + accessToken;
    }
    return config;
  },
  (err) => {
    return Promise.reject(err);
  }
);

let isRefreshing = false;
let refreshPromise = null;
let storedRefreshToken = null;

API.interceptors.response.use(
  (res) => {
    return res;
  },
  async (err) => {
    if (err.response && err.response.status === 401) {
      if (!isRefreshing) {
        isRefreshing = true;        

        storedRefreshToken = await getToken();

        //console.log('accessToken 갱신 과정에서 불러오는 refreshtoken: ' + storedRefreshToken);

        refreshPromise = reissueAPI.post('/auth/refresh', null, {
          headers: {
            Authorization: 'Bearer ' + storedRefreshToken
          }
        })
          .then(async (res) => {
            const access = res.data.accessToken;
            const refresh = res.data.refreshToken;
            await TokenService.setToken(access, refresh);
            return Promise.resolve();
          })
          .catch(async (err) => {
            if (err.response.status === 401) {
              //console.log('refresh 토큰이 만료되어 로그아웃');
              const accessToken = await TokenService.getAccessToken();
              const refreshToken = await TokenService.getRefreshToken();
              const params = { accessToken, refreshToken };
              await API.post('/auth/logout', { params })
                .then(async () => {
                  await TokenService.clearAllData();
                })
                .catch((err) => {
                  //console.error(err);
                })
            }
            //console.log('accessToken 갱신 실패');
            return Promise.reject(err);
          })
          .finally(() => {
            isRefreshing = false;
            refreshPromise = null;
          });
      }

      if (refreshPromise) {
        return refreshPromise.then(() => {
          return API(err.config);
        })
      } else {
        return Promise.reject(err);
      }
    }
    return Promise.reject(err);
  }
);

export default API;

const getToken = async () => {
  const res = await TokenService.getRefreshToken();
  return res;
};