import React from "react";
import styles from "../css/auth/SocialLogin.module.scss";
import {FaGithub, FaGoogle, FaVk} from "react-icons/fa";
import Button from "../../atoms/Button";

const SocialLogin = () => {
    const assetBase = window.location.pathname.startsWith("/Lab4") ? "/Lab4" : "";

    const playSound = (src) => {
        const audio = new Audio(`${assetBase}/${src}`);
        audio.play();
    }

    return (
        <>
            <div className={styles.divider}>
                <div className={styles.dividerLine}></div>
                <span>or continue with</span>
                <div className={styles.dividerLine}></div>
            </div>
            <div className={styles.socialLogin}>
                <Button type="button" className={styles.neuSocial} onClick={() => playSound('ClashRoyale.mp3')}>
                    <FaVk size={30} />
                </Button>
                <Button type="button" className={styles.neuSocial} onClick={() => playSound('Fah.mp3')}>
                    <FaGithub size={30} />
                </Button>
                <Button type="button" className={styles.neuSocial} onClick={() => playSound('oi.mov')}>
                    <FaGoogle size={30} />
                </Button>
            </div>
        </>

    );
};

export default SocialLogin;
