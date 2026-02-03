import {Button as BelleButton} from 'belle'
import React from "react";


function BasicButton(props) {
    return (
        <BelleButton type={'button'} {...props}></BelleButton>
    );
}

export default BasicButton
