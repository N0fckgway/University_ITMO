import React from "react";
import {TextInput} from "belle";

function Input(props) {
    return (
        <TextInput type='text' {...props}/>
    );
}

export default Input