import React, {useEffect} from 'react';
import ImageList from '@mui/material/ImageList';
import ImageListItem from '@mui/material/ImageListItem';
import ImageListItemBar from '@mui/material/ImageListItemBar';
import {Paper, TablePagination, TextField} from "@mui/material";
import IconButton from '@mui/material/IconButton';
import EditIcon from '@mui/icons-material/Edit';
import Alert from '@mui/material/Alert';
import {Button} from "reactstrap";
import ClearIcon from '@mui/icons-material/Clear';
import imageNotFound from './image-not-found.jpg';

const editButtonStyle = {
    backgroundColor: 'white',
    borderRadius: '50%',
    margin: 5
}

export default function CitiesList() {
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const [cities, setCities] = React.useState([]);
    const [totalItems, setTotalItems] = React.useState(0);
    const [searchTerm, setSearchTerm] = React.useState('');
    const [editId, setEditId] = React.useState(null);
    const [itemName, setItemName] = React.useState("");
    const [itemPhoto, setItemPhoto] = React.useState("");
    const [error, setError] = React.useState("");

    const API_PATH = process.env.REACT_APP_BACKEND_ENDPOINT || 'http://localhost:8080';

    useEffect(() => {
        fetchAllCities(page + 1, rowsPerPage)
    }, []);

    const fetchAllCities = (newPage, rowsPerPage) => {
        fetch(API_PATH + `/api/v1/cities?page=${newPage}&per_page=${rowsPerPage}`)
            .then(response => response.json())
            .then(body => {
                setCities(body.data.items);
                setTotalItems(body.data.total_items);
            });
    };

    const fetchCitiesByName = (name, newPage, rowsPerPage) => {
        fetch(API_PATH + `/api/v1/cities/${name}?page=${newPage}&per_page=${rowsPerPage}`)
            .then(response => response.json())
            .then(body => {
                setCities(body.data.items);
                setTotalItems(body.data.total_items);
            });
    };

    const handleChangePage = (event, newPage) => {
        searchTerm
            ? fetchCitiesByName(searchTerm, newPage + 1, rowsPerPage)
            : fetchAllCities(newPage + 1, rowsPerPage);
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        searchTerm
            ? fetchCitiesByName(searchTerm, 1, event.target.value)
            : fetchAllCities(1, event.target.value);
        setRowsPerPage(+event.target.value);
        setPage(0);
    };

    const clearSearch = () => {
        fetchAllCities(1, rowsPerPage);
        setSearchTerm('');
    };

    const handleSearch = (event) => {
        if (!event.target.value) {
            fetchAllCities(1, event.target.value);
        }
        setSearchTerm(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        if (searchTerm) {
            fetchCitiesByName(searchTerm, page + 1, rowsPerPage);
        }
    };

    const handleEdit = (id) => {
        setError("");
        const selectedCity = cities.find(city => city.id === id);
        setEditId(id);
        setItemName(selectedCity.name);
        setItemPhoto(selectedCity.photo);
    };

    const handleSave = (id) => {
        const requestOptions = {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(
                {
                    id: id,
                    name: itemName,
                    photo: itemPhoto
                }
            )
        };

        fetch(API_PATH + '/api/v1/cities/' + id, requestOptions)
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    setError(data.error);
                } else {
                    setError("");
                    const updatedCities = cities.map(city => {
                        if (city.id === id) {
                            return {...city, name: data.name, photo: data.photo, errorOccurred: null};
                        }
                        return city;
                    });
                    setCities(updatedCities);
                    setEditId(null);
                }
            });
    };

    const handleCancel = () => {
        setEditId(null);
        setItemName("");
        setItemPhoto("");
    };

    return (
        <Paper style={{marginTop: 20}}>
            <form onSubmit={handleSubmit}>
                <div style={{margin: 5, display: 'flex', justifyContent: 'space-between'}}>
                    <TextField
                        label="Search by city name"
                        type="text"
                        value={searchTerm}
                        onChange={handleSearch}
                        className="search-field"
                        fullWidth
                        InputProps={{
                            endAdornment: (
                                <IconButton
                                    sx={{visibility: searchTerm ? "visible" : "hidden"}}
                                    onClick={clearSearch}
                                >
                                    <ClearIcon/>
                                </IconButton>
                            ),
                        }}
                    />
                    <div>
                        <Button variant="contained" color="primary" type="submit"
                                style={{marginLeft: 5, height: '100%', minWidth: 150}}>
                            Search
                        </Button>
                    </div>
                </div>
            </form>
            <ImageList>
                {cities.map((item) => (
                    <ImageListItem key={item.img} style={{margin: 5}}>
                        <img
                            src={item.errorOccurred ? imageNotFound : item.photo}
                            srcSet={item.errorOccurred ? imageNotFound : item.photo}
                            alt={item.name}
                            loading="lazy"
                            onError={() => {
                                item.errorOccurred = true;
                                const updatedCities = cities.map(city => {
                                    if (city.id === item.id) {
                                        return item;
                                    }
                                    return city;
                                });
                                setCities(updatedCities);
                            }}
                        />
                        <ImageListItemBar
                            title={
                                editId === item.id ? (
                                    <>
                                        <div>
                                            <input
                                                type="text"
                                                value={itemName}
                                                onChange={e => setItemName(e.target.value)}
                                                onKeyDown={e => {
                                                    if (e.key === 'Enter') {
                                                        handleSave(item.id);
                                                    }
                                                }}
                                            />

                                            <Button variant="contained" color="primary"
                                                    onClick={() => handleSave(item.id)}
                                                    style={{marginLeft: 5, marginRight: 5}}>
                                                Save
                                            </Button>
                                            <Button variant="contained" onClick={handleCancel}>
                                                Cancel
                                            </Button>
                                        </div>
                                        <div style={{paddingTop: 5}}>
                                            <input style={{width: "100%"}}
                                                   type="text"
                                                   value={itemPhoto}
                                                   onChange={e => setItemPhoto(e.target.value)}
                                                   onKeyDown={e => {
                                                       if (e.key === 'Enter') {
                                                           handleSave(item.id);
                                                       }
                                                   }}
                                            />
                                        </div>
                                        <div>
                                            {error && <Alert severity="error">{error}</Alert>}
                                        </div>

                                    </>
                                ) : (
                                    item.name
                                )
                            }
                            position="top"
                            actionIcon={
                                editId !== item.id ? (
                                    <IconButton style={editButtonStyle} onClick={() => handleEdit(item.id)}>
                                        <EditIcon style={{color: 'black'}}/>
                                    </IconButton>
                                ) : null
                            }
                        />
                    </ImageListItem>
                ))}
            </ImageList>
            <TablePagination
                rowsPerPageOptions={[10, 26, 50, 100]}
                component="div"
                count={totalItems}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
        </Paper>
    );
}
