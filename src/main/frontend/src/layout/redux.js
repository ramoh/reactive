import { createSlice } from "@reduxjs/toolkit";

const layoutSlice = createSlice({
  name: "layout",
  initialState: {
    drawerOpen: true,
    changePasswordOpen: false,
    newProjectOpen: false,
    openTask: undefined
  },
  reducers: {
    newTask: (state, action = {}) => {
      state.openTask = {
        title: "",
        description: "",
        ...action.payload ?? {}
      };
    },
    clearOpenTask: state => {
      state.openTask = undefined;
    },
    setOpenTask: (state, action) => {
      state.openTask = action.payload;
    },
    openNewProject: state => {
      state.newProjectOpen = true;
    },
    closeNewProject: state => {
      state.newProjectOpen = false;
    },
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
  closeChangePassword,
  openNewProject,
  closeNewProject,
  newTask,
  clearOpenTask,
  setOpenTask
} = layoutSlice.actions;
export const { reducer } = layoutSlice;