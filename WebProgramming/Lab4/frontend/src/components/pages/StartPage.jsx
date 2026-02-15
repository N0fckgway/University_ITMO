import React from "react";
import StartHeader from "../organisms/StartHeader";
import StartMain from "../organisms/StartMain";
import StartPartnersInfo from "../molecules/start/StartPartnersInfo";

function StartPage() {
    return (
        <>
            <StartHeader/>
            <StartMain/>
            <StartPartnersInfo/>
        </>
    );
}

export default StartPage;
