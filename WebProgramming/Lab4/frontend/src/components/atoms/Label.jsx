import React from "react";

function Label(props) {
    return (
        <label htmlFor={props.htmlFor} {...props}></label>
    );
}

export default Label