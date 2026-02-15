const defaultState = {
    form: 'logIn'
}

export const reducerForm = (state = defaultState, action) => {
    switch (action.type) {
        case ('SignUp'):
            return {...state, form: 'signUp'}
        case ('LogIn'):
            return {...state, form: 'logIn'}
        default:
            return state
    }
}
