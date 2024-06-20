import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  name: "",
  personId: "",
  phoneNumber: "",
  eamil: ""
}

export const signUpSlice = createSlice({
  name: "signUpInfo",
  initialState,
  reducers: {
    inputName: (state, action) => {
      state.name = action.payload;
    },
    inputPersonId: (state, action) => {
      state.personId = action.payload;
    },
    inputPhoneNumber: (state, action) => {
      state.phoneNumber = action.payload;
    },
    inputEmail: (state, action) => {
      state.email = action.payload;
    }
  }
});

export const { inputName, inputPersonId, inputPhoneNumber, inputEmail } = signUpSlice.actions;

export default signUpSlice.reducer;