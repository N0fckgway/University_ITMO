import React from "react";
import styles from "../css/HeaderInfo.module.scss";

function StartHeaderInfo({name, group, variant}) {
    return (
        <>
            <div className={styles.info}>
                <span className={styles.name}>
                    <strong>{name}</strong>
                </span>
                <span className={styles.divider}>
                    ·
                </span>
                <span className={styles.item}>
                    <span className={styles.label}>
                        <b>Группа:</b>
                    </span>
                    <span className={styles.value}>
                        <strong>{group}</strong>
                    </span>
                </span>
                <span className={styles.divider}>·</span>
                <span className={styles.item}>
                    <span className={styles.label}>
                        <b>Вариант:</b>
                    </span>
                    <span className={styles.value}>
                        <strong>{variant}</strong>
                    </span>
                </span>
            </div>
        </>
    );
}

export default StartHeaderInfo
