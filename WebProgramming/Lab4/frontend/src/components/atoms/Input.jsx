import { TextInput as InputBelle } from "belle";
import React from "react";


function TextInput(props) {
    return (
        <InputBelle type='text' {...props}/>
    );
}

export default TextInput