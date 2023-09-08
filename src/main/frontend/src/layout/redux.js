import { createSlice } from '@reduxjs/toolkit';

const layoutSlice = createSlice({
  name: 'layout',
  initialState: {
    drawerOpen: true,
    changePasswordOpen: false
  },
  reducers: {
    openChangePassword: state => {
      state.changePasswordOpen = true;
    },
    closeChangePassword: state => {
      state.changePasswordOpen = false;
    },
    toggleDrawer: state => {
      state.drawerOpen = !state.drawerOpen;
    }
  }
});

export const {
  toggleDrawer,
  openChangePassword,
  closeChangePassword
} = layoutSlice.actions;
export const { reducer } = layoutSlice;