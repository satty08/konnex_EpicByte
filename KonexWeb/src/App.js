import './App.css';
import React, {useState} from 'react';
import Login from './Login';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Analytics from './Analytics';
import SignIn from './SignIn';

function App() {

  const [isLoggedIn, setLoggedIn] = useState(false);

  const loginHandler = (email, password) => {
    // We should of course check email and password
    // But it's just a dummy/ demo anyways
    setLoggedIn(true);
  };

  const logoutHandler = () => {
    setLoggedIn(false);
  };

  return (
    <Router>
      <div className="App">
        <Switch>
        <Route path="/analytics">
            {isLoggedIn && <Analytics onLogout={logoutHandler} />}
          </Route>
          <Route path="/signIn">
            <SignIn onLogin={loginHandler} />
          </Route>
          <Route path="/">
            <Login onLogin={loginHandler} />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
