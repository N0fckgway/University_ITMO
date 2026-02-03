import React from "react";
import StartMainInfo from "../molecules/startPage/StartMainInfo";
import StartPartnersInfo from "../molecules/startPage/StartPartnersInfo";
import styles from './css/StartMain.module.scss'

function StartMain() {
    return (
        <>
            <StartMainInfo className={styles.startMain}></StartMainInfo>
            <StartPartnersInfo></StartPartnersInfo>
        </>

    )

}

export default StartMain