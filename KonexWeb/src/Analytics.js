import React, {useState, useEffect} from 'react';
import './Analytics.css';
import {auth, db} from './firebase';
import {useHistory} from 'react-router-dom';
import Bugs from './Bugs';
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';

function Analytics(props) {

    const history = useHistory();

    const [bugs, setBugs] = useState([]);

    let bug = []

    const [arr, setArr] = useState([])

    const options = ['All', 'Not Resolved', 'In Progress'];

    const defaultOption = options[0];

    useEffect(() => {
        db.ref('Bugs').on("value", snapshot => {
            let bugList = []
            snapshot.forEach(snap => {
                bugList.push(snap.val())
            })
            setBugs({ bugs: bugList})
        })

    }, [])

    const selectHandler = (value) => {
        if (value.value === 'All') {
            bug = bugs.bugs
            console.log(bug);
        }

        else if (value.value === 'Not Resolved') {
            bug = bugs.bugs?.filter(bug => bug.status === 'Not Resolved')
            console.log(bug);
        }

        else if (value.value === 'In Progress') {
            bug = bugs.bugs?.filter(bug => bug.status === 'In Progress')
            console.log(bug);
        }
    }

    const logout = () => {
        auth.signOut().then(() => {
            history.push('/')
        }).catch((err) => {
            console.log(err);
        })
    }
    
    return (
        <div className="analytics">
            <div className="header">
                <button className="logout_button" onClick={logout}>Logout</button>
                <Dropdown className="dropdown" options={options} onChange={selectHandler} value={defaultOption} placeholder="Select an option" />
            </div>
            {bugs.bugs?.map((elem, index) =>
                (
                <Bugs id={index}
                title={elem.bugTitle}
                name={elem.appName}
                description={elem.bugDes}
                device={elem.deviceName}
                size={elem.screenSize}
                resolved={elem.status}
                />
            ))}
        </div>
    )
}

export default Analytics
