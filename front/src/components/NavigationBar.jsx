import React from 'react';
import { Navbar, Nav } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faHome } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom';
import './NavbarStyle.css'

class NavigationBarClass extends React.Component {

    constructor(props) {
        super(props);
    }

    render() { 
        return (
            <Navbar bg="dark" expand="lg" variant="dark">
                <Navbar.Brand className="ms-2 me-1"><FontAwesomeIcon icon={faHome} size='xs' />{' '}РПО</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link href={"/"} >Главная</Nav.Link>
                        <Nav.Link onClick={() =>{ this.props.navigate("Home") }}>Дом</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        );
    }
}

const NavigationBar = props => {
    const navigate = useNavigate()
    return <NavigationBarClass navigate={navigate} {...props} />
}

export default  NavigationBar;