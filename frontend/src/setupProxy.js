const {createProxyMiddleware} = require('http-proxy-middleware');

module.exports = function (app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: process.env.BACKEND_ENDPOINT ?
                process.env.BACKEND_ENDPOINT
                : 'http://localhost:8080',
            changeOrigin: true,
        })
    );
};