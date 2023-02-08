## Demo - https://frontend-pauliux.cloud.okteto.net/

## In order to run this project you need to:

### OPTION 1 (easiest):

Navigate to main folder (the one that contains `docker-compose.yml` file) in terminal and
run `docker-compose up --build` command. This will start both frontend and backend. Your
application will be accessible in `localhost:3000`

### OPTION 2:

1. Start backend:
    1. You need to navigate to `\backend\src\main\java\com\application\cities\CitiesApplication.java`
    2. run `main` method in `CitiesApplication.java` class. By doing this, the backend application will:
        * start H2 database
        * run DB migrations that create required DB table and fills that with dump data from `cities.csv` file that is
          located
          in `/resources/db/cities.csv`
2. Start frontend:
    1. you need to make sure that NodeJS is installed on your computer
    2. navigate to `frontend` folder
    3. run `npm install` command in your terminal. This will ensure that the frontend contains all the required packages
    4. run `npm start` to start frontend application
