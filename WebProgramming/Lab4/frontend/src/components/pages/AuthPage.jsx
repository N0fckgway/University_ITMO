import React from "react";
import LoginForm from "../organisms/auth/LoginForm";
import {useSelector} from "react-redux";
import SignupForm from "../organisms/auth/SignupForm";
import Logo from "../atoms/Logo";
import styles from "../organisms/auth/LoginForm.module.scss";
import  { useNavigate } from "react-router-dom";

const AuthPage = () => {
    const form = useSelector((state) => (state.formAuth.form))
    const navigation = useNavigate();


    return (
        <>
            <div className={styles.logo}>
                <Logo aria-label="InstaMax" onClick={() => navigation('/Lab4')}/>
            </div>
            {form === 'logIn' ? <LoginForm/> : <SignupForm/>}
        </>
    );
};



export default AuthPage;
