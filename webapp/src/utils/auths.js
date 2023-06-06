const getAuthenticatedUser = () => JSON.parse(sessionStorage.getItem('user'));

const isAuthenticated = () => sessionStorage.getItem('user') !== null;

const isHelper = () =>
  JSON.parse(sessionStorage.getItem('user')).role === 'HELPER' ||
  JSON.parse(sessionStorage.getItem('user')).role === 'MANAGER';

const isManager = () => JSON.parse(sessionStorage.getItem('user')).role === 'MANAGER';

const clearAuthenticatedUser = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  sessionStorage.removeItem('token');
  sessionStorage.removeItem('user');
};

export { getAuthenticatedUser, isAuthenticated, isHelper, isManager, clearAuthenticatedUser };
