import { combineReducers, configureStore, getDefaultMiddleware } from '@reduxjs/toolkit';
import { reducer as layoutReducer } from './layout';
import { reducer as authReducer, logout } from "./auth";
import { api as userApi } from "./users";

const appReducer = combineReducers({
  auth: authReducer,
  layout: layoutReducer,
  [userApi.reducerPath]: userApi.reducer
});

const rootReducer = (state, action) => {
  if (logout.match(action)) {
    state = undefined;
  }
  return appReducer(state, action);
};

export const store = configureStore({
  reducer: rootReducer,
  middleware: getDefaultMiddleware => getDefaultMiddleware().concat(userApi.middleware)
});