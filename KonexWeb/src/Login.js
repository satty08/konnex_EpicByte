import React, {useState} from 'react';
import './Login.css';
import { Link, useHistory } from 'react-router-dom';
import { auth } from './firebase';

function Login(props) {

    const history = useHistory();

    const[email, setEmail] = useState('');
    const[password, setPassword] = useState('');

    const signIn = e => {
        e.preventDefault();
        props.onLogin(email, password)

        //Firebase logging
        auth.signInWithEmailAndPassword(email, password)
            .then(auth => {
                history.push('/analytics')
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
                    
                    <button className="login__signInButton" type="submit" onClick={signIn}>Sign In</button>
                </form>
                <p>
                    Demo Account:
                    Username: konnex@gmail.com
                    Password: 12345678
                </p>

                <p>
                Note: If you already have an account click on Sign In and if not click on Register Below.
                </p>
                <Link to="/signIn">
                    <button className="login__registerButton" >Register</button>
                </Link>
            </div>
        </div>
    )
}

export default Login
