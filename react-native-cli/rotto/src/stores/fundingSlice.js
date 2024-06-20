import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isApplyModalOpen: false,
  fundingData: null,
  sortBy: null,
  subsStatus: null,
  minPrice: null,
  maxPrice: null,
  beanType: null,
  isFilterModalOpen: false,
  filterData: null,
  selectedFilter: null,
  isSearchModalOpen: false,
  keyword: null
}

export const fundingSlice = createSlice({
  name: "fundingInfo",
  initialState,
  reducers: {
    setApplyModal: (state, action) => {
      state.isApplyModalOpen = action.payload;
    },
    setFundingData: (state, action) => {
      state.fundingData = action.payload;
    },
    setSortBy: (state, action) => {
      state.sortBy = action.payload;
    },
    setSubsStatus: (state, action) => {
      state.subsStatus = action.payload;
    },
    setMinPrice: (state, action) => {
      state.minPrice = action.payload;
    },
    setMaxPrice: (state, action) => {
      state.maxPrice = action.payload;
    },
    setBeanType : (state, action) => {
      state.beanType = action.payload;
    },
    setFilterModal: (state, action) => {
      state.isFilterModalOpen = action.payload;
    },
    setFilterData: (state, action) => {
      state.filterData = action.payload;
    },
    setFilter: (state, action) => {
      state.selectedFilter = action.payload;
    },
    setSearchModal: (state, action) => {
      state.isSearchModalOpen = action.payload;
    },
    setKeyword: (state, action) => {
      state.keyword = action.payload;
    }
  }
});

export const { 
  setApplyModal,
  setFundingData,
  setSortBy,
  setSubsStatus,
  setMinPrice,
  setMaxPrice,
  setBeanType,
  setFilterModal,
  setFilterData,
  setFilter,
  setSearchModal,
  setKeyword
} = fundingSlice.actions;

export default fundingSlice.reducer;