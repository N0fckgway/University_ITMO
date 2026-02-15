import React, {useState} from "react";
import styles from '../css/auth/LoginOption.module.scss'

const LoginOptions = () => {
    const [forgetPass, setForgetPass] = useState(false);
    const assetBase = window.location.pathname.startsWith("/Lab4") ? "/Lab4" : "";

    return (
        <>
            <div className={styles.formOptions}>
                <button type="button" className={styles.forgotLink} onClick={() => setForgetPass((prev) => !prev)}>
                    Forgot password?
                </button>
            </div>
            {forgetPass && (
                <div className={styles.inlineImageWrap}>
                    <img
                        className={styles.inlineImage}
                        src={`${assetBase}/Vryadli.jpg`}
                        alt="Вряд-ли"
                    />
                </div>
            )}
        </>

    );
};

export default LoginOptions;
