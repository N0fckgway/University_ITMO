import React from "react";
import Logo from "../atoms/Logo";
import BasicButton from "../atoms/Button";
import styles from "./css/Header.module.scss";
import StartHeaderInfo from "../molecules/startPage/StartHeaderInfo";

function StartHeader() {
    return (
        <>
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
                        <a className={styles.link} href="#pricing">Sign In</a>
                        <BasicButton className={styles.button} type="button">
                            <span>Log in</span>
                        </BasicButton>
                    </nav>
                </div>
            </header>

        </>


    );
}

export default StartHeader;
