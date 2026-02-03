import React from "react";
import {AnimationLogo} from "../atoms/Logo";
import BasicButton from "../atoms/Button";
import styles from "./css/StartMainInfo.module.scss";

function StartMainInfo() {
    return (
        <section className={styles.main} aria-labelledby="hero-title">
            <div className={styles.logoWrap}>
                <AnimationLogo className={styles.logo} aria-hidden="true" />
            </div>
            <h1 id="hero-title" className={styles.title}>
                Ловит даже в лифте без VPN
            </h1>
            <p className={styles.subtitle}>
                От разработчиков <b>Max</b> и <b>Meta</b> — Достойная замена Instagram 🤯
                <strong>Работает с помощью божьей помощи!</strong>
            </p>
            <div className={styles.actions}>
                <BasicButton className={`${styles.button} ${styles.primary}`} type="button">
                    Начать
                </BasicButton>
                <BasicButton className={`${styles.button} ${styles.ghost}`} type="button">
                    Показать тарифы
                </BasicButton>
            </div>
        </section>
    )
}

export default StartMainInfo
