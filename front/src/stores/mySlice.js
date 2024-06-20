import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isBankModalOpen: false,
  selectedBank: null,
  connectedAccount: null,
  transactionMode: null,
  isFilterModalOpen: null,
  selectedFilter: '전체',
  fundingAccount: null,
  isDisconnectModalOpen: false
}

export const mySlice = createSlice({
  name: "myPageInfo",
  initialState,
  reducers: {
    setBankModal: (state, action) => {
      state.isBankModalOpen = action.payload;
    },
    setSelectedBank: (state, action) => {
      state.selectedBank = action.payload;
    },
    setConnectedAccount: (state, action) => {
      state.connectedAccount = action.payload;
    },
    setTransactionMode: (state, action) => {
      state.transactionMode = action.payload;
    },
    setFilterModal: (state, action) => {
      state.isFilterModalOpen = action.payload;
    },
    setSelectedFilter: (state,action) => {
      state.selectedFilter = action.payload;
    },
    setFundingAccount: (state, action) => {
      state.fundingAccount = action.payload;
    },
    setDisconnectModal: (state, action) => {
      state.isDisconnectModalOpen = action.payload;
    }
  }
});

export const { 
  setBankModal, 
  setSelectedBank, 
  setConnectedAccount, 
  setTransactionMode, 
  setFilterModal,
  setSelectedFilter,
  setFundingAccount,
  setDisconnectModal
} = mySlice.actions;

export default mySlice.reducer;