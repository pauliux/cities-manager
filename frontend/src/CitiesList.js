import React, {useEffect} from 'react';
import ImageList from '@mui/material/ImageList';
import ImageListItem from '@mui/material/ImageListItem';
import ImageListItemBar from '@mui/material/ImageListItemBar';
import {Paper, TablePagination} from "@mui/material";

export default function CitiesList() {
    const [page, setPage] = React.useState(0);
    const [rowsPerPage, setRowsPerPage] = React.useState(10);
    const [cities, setCities] = React.useState([]);
    const [totalItems, setTotalItems] = React.useState(0);
    const [searchTerm, setSearchTerm] = React.useState('');

    useEffect(() => {
        fetchAllCities(page + 1, rowsPerPage)
    }, []);

    const fetchAllCities = (newPage, rowsPerPage) => {
        fetch(`/api/v1/cities?page=${newPage}&per_page=${rowsPerPage}`)
            .then(response => response.json())
            .then(body => {
                setCities(body.data.items);
                setTotalItems(body.data.total_items);
            });
    };

    const fetchCitiesByName = (name, newPage, rowsPerPage) => {
        fetch(`/api/v1/cities/${name}?page=${newPage}&per_page=${rowsPerPage}`)
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
        fetchCitiesByName(searchTerm, page + 1, rowsPerPage);
    };

    return (
        <Paper>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={searchTerm}
                    onChange={handleSearch}
                    placeholder="Search by city name"
                />
                <button type="submit">Search</button>
                {searchTerm !== "" && (
                    <button type="button" onClick={clearSearch}>
                        Clear
                    </button>
                )}
            </form>
            <ImageList>
                {cities.map((item) => (
                    <ImageListItem key={item.img} style={{margin: 5}}>
                        <img
                            src={item.photo}
                            srcSet={item.photo}
                            alt={item.name}
                            loading="lazy"
                        />
                        <ImageListItemBar
                            title={item.name}
                            position="top"
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
