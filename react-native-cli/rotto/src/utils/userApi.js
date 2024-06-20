import API from "./Api";
import TokenService from "./token";

export const signUp = async (data) => {
  try {
    const res = await API.post('/user/signup', data);
    return res;
  } catch (err) {
    //console.error('회원가입 오류' + err);
  }
};

export const signIn = async (data) => {
  try {
    const res = await API.post('/auth/login', data);
    return res;
  } catch (err) {
    //console.error('로그인 오류' + err);
    return err;
  }
};

export const getUserInfo = async () => {
  try {
    const res = await API.get('/user');
    return res;
  } catch (err) {
    //console.error('유저 정보 조회 실패' + err);
    return null;
  }
};

export const logout = async () => {
  try {
    const accessToken = await TokenService.getAccessToken();
    const refreshToken = await TokenService.getRefreshToken();
    const data = {
      accessToken: accessToken,
      refreshToken: refreshToken
    };

    await API.post('/auth/logout', data);
  } catch (err) {
    //console.error('로그아웃 에러' + err);
    return err.response;
  }
};

export const checkPhoneNumber = async (data) => {
  try {
    const res = await API.post('/user/phone-num-check', data);
    return res.data.isExist;
  } catch (err) {
    if (err.response.status === 400) {
      return true;
    } else {
      //console.error('전화번호 중복 체크 에러' + err);
      return err;
    }
  }
};

export const resign = async () => {
  try {
    await API.patch('/user');
    return;
  } catch (err) {
    //console.error('회원 탈퇴 실패' + err);
    return err;
  }
};

export const changePassword = async (data) => {
  try {
    await API.patch('/user/modify/password', data);
    return;
  } catch (err) {
    //console.error('비밀번호 변경 에러' + err);
    return err;
  }
};

export const updateWalletAddress = async (data) => {
  try {
    await API.patch('/user/updateAddress', data);
    return;
  } catch (err) {
    //console.error('지갑 주소 업데이트 에러' + err);
    return err;
  }
};

export const checkEmail = async (data) => {
  try {
    const res = await API.post('/user/email-check', data);
    return res.data.isExist;
  } catch (err) {
    if (err.response.status === 400) {
      return true;
    } else {
      //console.error('이메일 중복 체크 에러' + err);
      return err;
    }
  }
};