import React from "react";
import styles from "../css/auth/LoginHeader.module.scss";

const LoginHeader = (props) => {
    return (
        <>
            <div className={styles.loginHeader}>
                <div className={styles.neuIcon}>
                    <div className={styles.iconInner}>
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                            <circle cx="12" cy="7" r="4"/>
                        </svg>
                    </div>
                </div>
                <h2>{props.value}</h2>
                <p>{props.text}</p>
            </div>
        </>


    );
};

export default LoginHeader;