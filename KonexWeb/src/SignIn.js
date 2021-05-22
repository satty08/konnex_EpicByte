import React, {useState} from 'react';
import './SignIn.css';
import { useHistory } from 'react-router-dom';
import { auth } from './firebase';

function SignIn(props) {

    const history = useHistory();

    const[email, setEmail] = useState('');
    const[password, setPassword] = useState('');

    const register = e => {
        e.preventDefault();

        //firebase register
        auth.createUserWithEmailAndPassword(email, password)
            .then((auth) => {
                //successfully created new user
                if(auth) {
                    history.push('/')
                }
            })
            .catch(error => alert(error.message))
    }

    return (
        <div className="login">
            <div className="login__container">
                <img 
                    src="https://airbus-h.assetsadobe2.com/is/image/content/dam/events/conference/press-conference/AirbusZEROe-Blended-Wing-Body-Concept.jpg?wid=3626&fit=constrain" 
                    alt=""
                    className="login__logo"
                />
                <h1>Sign In</h1>

                <form>
                    <h5>E-mail</h5>
                    <input type="text" value={email} onChange={e => setEmail(e.target.value)} />

                    <h5>Password</h5>
                    <input type="password" value={password} onChange={e => setPassword(e.target.value)} />
                    
                    <button className="login__signInButton" type="submit" onClick={register}>Register</button>
                </form>
            </div>
        </div>
    )
}

export default SignIn
