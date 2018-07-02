// 设置axios发送post请求时，参数按照url方式提交
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.interceptors.request.use(function (config) {
    if (config.method === 'post') {
        config.data = Qs.stringify(config.data)
    }
    return config;
});
