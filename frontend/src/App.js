import {Component} from "react";
import './App.css';
import CitiesList from "./CitiesList";
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';

class App extends Component {
    render() {
        return (
            <Box sx={{flexGrow: 1}}>
                <AppBar position="static">
                    <Toolbar>
                        <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
                            Cities manager
                        </Typography>
                    </Toolbar>
                </AppBar>
                <Container>
                    <CitiesList/>
                </Container>
            </Box>
        );
    }
}

export default App;