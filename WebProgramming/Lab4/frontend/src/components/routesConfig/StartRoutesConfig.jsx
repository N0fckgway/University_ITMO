import {Route, Routes} from "react-router-dom";
import StartPage from "../pages/StartPage";
import AuthPage from "../pages/AuthPage";
import MainPage from "../pages/MainPage";
import ForbiddenPage from "../pages/ForbiddenPage";


const hasAuthToken = () => {
    const token = localStorage.getItem("token");
    return !(!token || token === "undefined" || token === "null");
}

const ProtectedMainRoute = () => {
    if (!hasAuthToken()) {
        return <ForbiddenPage error={'403'} description={'Вы не авторизованы! За логиньтесь!'} />;
    } else return <MainPage />;

};

const StartRoutesConfig = () => {
    return (
        <Routes>
            <Route path={'/Lab4'} element={<StartPage/>}/>

            <Route path={'/Lab4/login'} element={<AuthPage />}></Route>

            <Route path={'/Lab4/register'} element={<AuthPage />}></Route>

            <Route path={'/Lab4/main'} element={<ProtectedMainRoute />}></Route>
        </Routes>
    )

}

export default StartRoutesConfig
