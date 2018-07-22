// 设置axios发送post请求时，参数按照url方式提交
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
// 拦截axios请求，进行规范化操作
axios.interceptors.request.use(function (config) {
    if (config.params) {
        config.params = Object.assign({}, config.params);
        // 删除无效参数
        for (let key in config.params) {
            if (!config.params.hasOwnProperty(key)) {
                continue;
            }
            if (config.params[key] === null || config.params[key] === '') {
                delete config.params[key];
            }
        }
    }
    if (config.method === 'post' && config.data) {
        config.data = Object.assign({}, config.data);
        // 删除无效参数
        for (let key in config.data) {
            if (!config.data.hasOwnProperty(key)) {
                continue;
            }
            if (config.data[key] === null || config.data[key] === '') {
                delete config.data[key];
            }
        }
        // 将参数对象转换为key=value形式
        config.data = Qs.stringify(config.data)
    }
    return config;
}, function (error) {
    Vue.prototype.$message.error('请求服务端失败:' + error);
    return Promise.reject(error);
});
// 验证服务端返回结果
axios.interceptors.response.use(function (response) {
    if (response.status === 200) {
        var result = response.data;
        if (result.code === 'common-0004') {
            // 未登录，则跳转到登录页面
            Vue.prototype.$confirm('未登录或登录已超时，是否重新登录？', '警告')
                .then(function () {
                    window.location.href = 'login.html';
                });
            return Promise.reject(response);
        }
        return result;
    } else {
        Vue.prototype.$message.error(response.status + ':' + response.statusText);
        return Promise.reject(response);
    }
}, function (error) {
    Vue.prototype.$message.error('请求服务端失败:' + error);
    return Promise.reject(error);
});
