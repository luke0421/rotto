import API from "./Api";

const URL = '/account';

export const getAccountInfo = async () => {
  try {
    const res = await API.get(URL);
    return res.data;
  } catch (err) {
    //console.error(err);
    return null;
  }
};

export const getRealAccountInfo = async () => {
  try {
    const res = await API.get(URL + '/real');
    return res.data;
  } catch (err) {
    if (err.response.status === 500) {
      return null;
    }
  }
};

export const disConnectAccount = async (accountCode) => {
  try {
    const res = await API.delete(URL + `/real/remove/${accountCode}`);
    return res;
  } catch (err) {
    //console.error(err);
    return err;
  }
};

export const connectAccount = async (data) => {
  try {
    const res = await API.post(URL + '/connection', data);
    return res;
  } catch (err) {
    //console.error('계좌 연결 실패 ' + err);
    return err.response;
  }
};

export const chargeAccount = async (data) => {
  try {
    const res = await API.patch(URL + '/deposit', data);
    return res;
  } catch (err) {
    //console.error('금액 충전 실패 ' + err.response.data);
    return err.response;
  }
};

export const sendMoney = async (data) => {
  try {
    const res = await API.patch(URL + '/withdrawal', data);
    return res;
  } catch (err) {
    //console.error('보내기 실패' + err.response);
    return err.response;
  }
};

export const getAccountHistory = async (accountCode) => {
  try {
    const res = await API.get(`/account-history/${accountCode}`);
    return res.data;
  } catch (err) {
    //console.error('입출금내역 조회 실패' + err)
    return null;
  }
};

export const getAccountDeposit = async (accountCode) => {
  try {
    const res = await API.get(`/account-history/deposit/${accountCode}`);
    return res.data;
  } catch (err) {
    //console.error('입금내역 조회 실패' + err)
    return null;
  }
};

export const getAccountWithdrawal = async (accountCode) => {
  try {
    const res = await API.get(`/account-history/withdrawal/${accountCode}`);
    return res.data;
  } catch (err) {
    //console.error('출금내역 조회 실패' + err)
    return null;
  }
};