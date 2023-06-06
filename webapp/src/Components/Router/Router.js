import routes from './routes';
import Navbar from '../Navbar/Navbar';
import Navigate from './Navigate';
import displayErrorMessage from '../../utils/error';
import LoginPage from '../Pages/Authentification/LoginPage';

const Router = () => {
  onFrontendLoad();
  onHistoryChange();
};

function onHistoryChange() {
  window.addEventListener('popstate', () => {
    const uri = window.location.pathname;

    renderComponent(uri);
  });
}

function onFrontendLoad() {
  window.addEventListener('load', async () => {
    const uri = window.location.pathname;

    await getUserOnRefresh(uri);

    renderComponent(uri);
  });
}

function renderComponent(uri) {
  const componentToRender = routes[uri];

  if (componentToRender === undefined) {
    Navigate('/');
  }

  componentToRender();
  Navbar();
}

async function getUserOnRefresh(uri) {
  const main = document.querySelector('main');

  if (localStorage.getItem('token') || sessionStorage.getItem('token')) {
    const request = await fetch('/api/auths/user', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
      },
    });

    if (!request.ok) {
      displayErrorMessage(request.json().message, main);
      Navigate('/logout');

      if (uri.split('/')[1] === 'dashboard') {
        Navigate('/login');
        await LoginPage();
      }
    } else {
      const response = await request.json();
      sessionStorage.setItem('user', JSON.stringify(response));
    }
  }
}

export default Router;
