import { configureStore } from "@reduxjs/toolkit";
import signUpSlice from "./signUpSlice";
import mySlice from "./mySlice";
import fundingSlice from "./fundingSlice";

export const store = configureStore({
  reducer: {
    signupInfo: signUpSlice,
    myPageInfo: mySlice,
    fundingInfo: fundingSlice
  }
});