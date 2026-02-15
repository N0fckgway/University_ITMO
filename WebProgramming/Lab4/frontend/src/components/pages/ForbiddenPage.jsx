import React from "react";
import {useNavigate} from "react-router-dom";

const ForbiddenPage = (props) => {
    const navigate = useNavigate();

    return (
        <main style={{padding: "24px"}}>
            <h1>{props.error}</h1>
            <p>{props.description}</p>
            <button type="button" onClick={() => navigate("/Lab4/login")}>
                Перейти к логину
            </button>
        </main>
    );
};

export default ForbiddenPage;
