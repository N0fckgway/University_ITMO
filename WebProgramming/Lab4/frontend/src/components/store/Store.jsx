import {combineReducers, configureStore} from "@reduxjs/toolkit";
import { reducerEmail } from './EmailCheckReducer'
import { reducerForm } from './FormAuthReducer'

const rootReducer = combineReducers({
       email: reducerEmail,
       formAuth: reducerForm,
});

const store = configureStore({
       reducer: rootReducer,
});

export default store;
