// 防止多个并发 401 重复跳转
let isRedirecting = false;

function toLoginPage() {
  if (isRedirecting) return;
  isRedirecting = true;
  localStorage.removeItem('loginUser');
  const page = window.location.pathname.split('/').pop() || 'dept_test.html';
  window.location.href = 'login.html?redirect=' + encodeURIComponent(page);
}

// axios 请求拦截器：每次请求自动携带 token
axios.interceptors.request.use(config => {
  const user = localStorage.getItem('loginUser');
  if (user) {
    try {
      const token = JSON.parse(user).token;
      if (token) config.headers.token = token;
    } catch (e) {}
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// axios 响应拦截器：token 失效跳转登录页
axios.interceptors.response.use(response => {
  return response;
}, error => {
  if (error.response && error.response.status === 401) {
    toLoginPage();
  }
  return Promise.reject(error);
});
