import Utils from "./Utils";
import { combineReducers, applyMiddleware, createStore } from "redux";
import { createLogger } from "redux-logger";

const userConstants = {
    LOGIN: 'USER_LOGIN',
    LOGOUT: 'USER_LOGOUT',
};

const alertConstants = {
    ERROR: 'ERROR',
    CLEAR: 'CLEAR',
};

export const userActions = {
    login,
    logout
};

function login(user) {
    Utils.saveUser(user)
    return { type: userConstants.LOGIN, user }
}

function logout() {
    Utils.removeUser()
    return { type: userConstants.LOGOUT }
}

export const alertActions = {
    error,
    clear
};

function error(msg) {
    return { type: alertConstants.ERROR, msg }
}

function clear() {
    return { type: alertConstants.CLEAR }
}

/* REDUСERS */

function alert(state =null, action) {
    console.log("alert")
    switch (action.type) {
        case alertConstants.ERROR:
            return { msg: action.msg };
        case alertConstants.CLEAR:
            return null;
        default:
            return state
    }
}

let user =  Utils.getUser()
const initialState = user ? { user } : {}

function authentication(state = initialState, action) {
    console.log("authentication")
    switch (action.type) {
        case userConstants.LOGIN:
            return { user: action.user };
        case userConstants.LOGOUT:
            return { };
        default:
            return state
    }
}

/* STORE */

const rootReducer = combineReducers({
    authentication, alert
});

const loggerMiddleware = createLogger();

export const store = createStore(
    rootReducer,
    applyMiddleware( loggerMiddleware )
);