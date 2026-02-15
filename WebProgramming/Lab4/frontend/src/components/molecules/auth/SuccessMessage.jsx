import React from "react";
import styles from '../css/auth/SuccessMessage.module.scss';

const SuccessMessage = ({ show = false, className = "" }) => {
    const classes = [
        styles.successMessage,
        show ? styles.show : "",
        className,
    ].filter(Boolean).join(" ");

    return (
        <div className={classes}>
            <div className={styles.icon}>
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3">
                    <polyline points="20 6 9 17 4 12"/>
                </svg>
            </div>
            <h3>Успешно!</h3>
            <p>Переношу тебя на main</p>
        </div>
    );
};

export default SuccessMessage;
