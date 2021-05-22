import React from 'react';
import './Bugs.css';

function Bugs({ name, description, title, device, size, resolved, id }) {

    const statusChange = () => {
        
    }

    return (
        <div className="bugs">
            <p>ID: {id}</p>
            <p>App Name: {name}</p>
            <p>Bug Title: {title}</p>
            <p>Description: {description}</p>
            <p>Device Name: {device}</p>
            <p>Device Size: {size}</p>
            <p>Status: {resolved}</p>
            {resolved === "In Progress" ? 
                <button className="sendBugDisabled" disabled onClick={statusChange}>Send Bug</button> 
            : 
                <button className="sendBug" onClick={statusChange}>Send Bug</button>}
        </div>
    )
}

export default Bugs
