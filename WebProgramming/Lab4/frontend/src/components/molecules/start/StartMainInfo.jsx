import React, { useState } from "react";
import {AnimationLogo} from "../../atoms/Logo";
import BasicButton from "../../atoms/Button";
import styles from "../css/start/StartMainInfo.module.scss";
import {useNavigate} from "react-router-dom";

function StartMainInfo() {
    const [btnTariff, setBtnTariff] = useState(false);
    const assetBase = window.location.pathname.startsWith("/Lab4") ? "/Lab4" : "";
    const navigation = useNavigate();

    const handleClick = () => {
        const token = localStorage.getItem("token");
        if (token && token !== "undefined" && token !== "null") {
            navigation("/Lab4/main");
        } else {
            navigation("/Lab4/login");
        }
    }

    return (
        <>
            <section className={styles.main} aria-labelledby="hero-title">
                <div className={styles.logoWrap}>
                    <AnimationLogo className={styles.logo} aria-hidden="true"/>
                </div>
                <h1 id="hero-title" className={styles.title}>
                    Ловит даже в лифте без VPN
                </h1>
                <p className={styles.subtitle}>
                    От разработчиков <b>Max</b> и <b>Meta</b> — Достойная замена Instagram 🤯
                    <strong>Работает с помощью божьей помощи!</strong>
                </p>
                <div className={styles.actions}>
                    <BasicButton className={`${styles.button} ${styles.primary}`} type="button" onClick={handleClick}>
                        Начать
                    </BasicButton>
                    <BasicButton className={`${styles.button} ${styles.ghost}`} type="button"
                                 onClick={() => setBtnTariff(true)}>
                        Показать тарифы
                    </BasicButton>
                </div>
            </section>
            {btnTariff && (
                <div className={`${styles.overlay} ${styles.show}`} onClick={() => setBtnTariff(false)}>
                    <div className={styles.modal}>
                        <img
                            className={styles.modalImage}
                            src={`${assetBase}/Sidim_ne_rypiemsya.jpg`}
                            alt="Сидим не рыпаемся"
                        />
                    </div>
                </div>
            )}
        </>



    )
}

export default StartMainInfo
