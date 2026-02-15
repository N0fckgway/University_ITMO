const defaultStore = {
    result: null
}

export const reducerEmail = (state = defaultStore, action) => {
    switch (action.type) {
        case "EmailCorrect":
            return {...state, result: true};
        case "EmailUnCorrect":
            return {...state, result: false};
        default:
            return state
    }
}