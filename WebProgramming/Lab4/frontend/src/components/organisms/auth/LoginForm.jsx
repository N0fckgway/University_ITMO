import styles from './LoginForm.module.scss'
import LoginHeader from "../../molecules/auth/LoginHeader";
import SuccessMessage from "../../molecules/auth/SuccessMessage";
import LoginOptions from "../../molecules/auth/LoginOptions";
import SocialLogin from "../../molecules/auth/SocialLogin";
import BasicButton from "../../atoms/Button";
import Input from "../../atoms/Input";
import Label from "../../atoms/Label";
import {useDispatch, useSelector} from "react-redux";
import React, {useState} from "react";
import {useNavigate} from "react-router-dom";


const LoginForm = () => {
    const isCorrect = useSelector(state => (state.email.result));
    const dispatcher = useDispatch();
    const navigation = useNavigate();
    const [submitError, setSubmitError] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);



    const handleChangeEmail = (e) => {
        setSubmitError("");
        setShowSuccess(false);
        const email = e.target.value;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            dispatcher({type: 'EmailUnCorrect'});
        } else dispatcher({type: 'EmailCorrect'})
    }


    const handleChangeForm = () => {
        dispatcher({type: 'SignUp'})
        navigation('/Lab4/register')
    }

    async function handleSubmit(e) {
        setSubmitError("");
        setShowSuccess(false);
        setIsSubmitting(true);
        try {
            e.preventDefault();
            const formData = new FormData(e.currentTarget);
            const email = String(formData.get("email") || "").trim();
            const password = String(formData.get("password") || "");

            const resp = await fetch(`/Lab4/api/auth/login`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({email, password})
            });

            const data = await resp.json();
            if (!resp.ok) {
                throw new Error(data?.message || "Данные не дошли до сервера!");
            }
            localStorage.setItem("token", data.token);
            setShowSuccess(true);
            setTimeout(() => {
                navigation('/Lab4/main');
            }, 700);
        } catch (error) {
            setSubmitError(error?.message || "Ошибка входа");
            setShowSuccess(false);
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <>
            <div className={styles.loginContainer}>
                <div className={styles.loginCard}>
                    <div className={styles.toogle}>
                        <button type="button" className={styles.toggleButton} onClick={handleChangeForm}>
                            Sign up
                        </button>
                    </div>
                    <LoginHeader value="Вход"
                                 text={
                                    `Пожалуйста войдите в аккаунт`
                                }
                    />
                    <form className={styles.form} onSubmit={handleSubmit}>
                        <div className={styles.formGroup}>
                            <div>
                                <div className={`${styles.neuInput} ${isCorrect === false ? styles.show : ''}`}>
                                    <Input type={'email'} placeholder={'Email'} name={'email'} id={'email'}
                                           onChange={handleChangeEmail} autoComplete={'true'}/>
                                    <Label htmlFor={'email'}>Email</Label>
                                </div>
                                {isCorrect === false && (
                                    <div className={styles.errorMessage} role="alert">
                                        Введите корректный email
                                    </div>
                                )}
                            </div>
                            <div className={styles.neuInput}>
                                <input
                                    type={'password'}
                                    placeholder={'Password'}
                                    name={'password'}
                                    id={'password'}
                                    onChange={() => {
                                        setSubmitError("");
                                        setShowSuccess(false);
                                    }}
                                />
                                <Label htmlFor={'password'}>Password</Label>
                            </div>
                            <LoginOptions/>
                        </div>
                        {submitError && (
                            <div className={styles.formFeedbackError} role="alert">
                                {submitError}
                            </div>
                        )}
                        <BasicButton primary className={styles.button} type="submit" disabled={isSubmitting}>
                            {isSubmitting ? "Входим..." : "Войти"}
                        </BasicButton>
                    </form>
                    <SocialLogin/>
                    <SuccessMessage show={showSuccess}/>
                </div>
            </div>

        </>
    )

}

export default LoginForm
