import Logo from "../atoms/Logo";
import BasicButton from "../atoms/Button";
import styles from "./css/Header.module.scss";
import StartHeaderInfo from "../molecules/start/StartHeaderInfo";
import {useNavigate} from "react-router-dom";
import React from "react";
import {useDispatch} from "react-redux";





function StartHeader() {
    const navigate = useNavigate();
    const dispatchForm = useDispatch();

    const handleButton = () => {
        if (localStorage.getItem('token')) {
            navigate('/Lab4/main')
        } else {
            dispatchForm({type: 'LogIn'})
            navigate('/Lab4/login')
        }

    }

    const handleLink = () => {
        if (localStorage.getItem('token')) {
            navigate('/Lab4/main')
        } else {
            dispatchForm({type: 'SignUp'})
            navigate('/Lab4/register')
        }

    }

    return (
            <header className={styles.header}>
                <div className={styles.pill}>
                    <div className={styles.brand}>
                        <Logo className={styles.logo} aria-label="InstaMax"/>
                    </div>
                    <div className={styles.headerInfo}>
                        <StartHeaderInfo name={'Суворов Станислав Денисович'} group={"P3215"}
                                         variant={"300097"}></StartHeaderInfo>
                    </div>
                    <nav className={styles.nav} aria-label="Primary">
                        <button className={styles.link} type="button" onClick={handleLink}>Sign In</button>
                        <BasicButton className={styles.button} type="button" onClick={handleButton}>
                            <span>Log in</span>
                        </BasicButton>
                    </nav>
                </div>
            </header>
    );
}

export default StartHeader;
