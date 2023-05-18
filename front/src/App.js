import './App.css';
import React from "react";
import {BrowserRouter, Route, Routes, Navigate} from "react-router-dom";
import NavigationBar from "./components/NavigationBar";
import Home from "./components/Home";
import Login from "./components/Login";
import Utils from "./utils/Utils";
import { connect } from 'react-redux';

const ProtectedRoute = ({children}) => {
    let user = Utils.getUser();
    return user ? children : <Navigate to={'/login'} />
};

function App(props) {
return (
    <div className="App">
        <BrowserRouter>
            <NavigationBar />
            <div className="container-fluid"> 
                {props.error_message && <div className="alert alert-danger m-1">{props.error_message}</div>} 
                <Routes>
                    <Route path="home" element={<ProtectedRoute><Home/></ProtectedRoute>}/>
                    <Route path="login" element={<Login />}/>
                </Routes>
            </div>
        </BrowserRouter>
    </div>
    );
}

const mapStateToProps = (state) => {
    const { alert } = state;
    const error_message = alert && alert.msg ? alert.msg : "";
    return { error_message };
};

export default connect(mapStateToProps)(App);
