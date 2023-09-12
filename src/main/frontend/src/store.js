import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { reducer as layoutReducer } from "./layout";
import { reducer as authReducer, logout } from "./auth";
import { api as userApi } from "./users";
import { api as projectApi } from "./projects";
import { api as taskApi } from "./tasks";

const appReducer = combineReducers({
  auth: authReducer,
  layout: layoutReducer,
  [userApi.reducerPath]: userApi.reducer,
  [projectApi.reducerPath]: projectApi.reducer,
  [taskApi.reducerPath]: taskApi.reducer

});

const rootReducer = (state, action) => {
  if (logout.match(action)) {
    state = undefined;
  }
  return appReducer(state, action);
};

export const store = configureStore({
  reducer: rootReducer,
  middleware: getDefaultMiddleware => getDefaultMiddleware()
    .concat(userApi.middleware)
    .concat(projectApi.middleware)
    .concat(taskApi.middleware)
});