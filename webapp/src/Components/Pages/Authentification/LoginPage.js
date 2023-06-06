import Navigate from '../../Router/Navigate';
import clearPage from '../../../utils/render';
import displayErrorMessage from '../../../utils/error';

const LoginPage = () => {
  clearPage();
  const main = document.querySelector('main');

  const maindiv = document.createElement('div');
  maindiv.setAttribute(
    'class',
    'flex justify-center items-center min-h-[calc(100vh_-_120px)] bg-gradient-green-large',
  );

  const formLogin = document.createElement('form');
  formLogin.setAttribute(
    'class',
    'bg-noise-base p-8 flex flex-col sm:w-3/5 h-2/4 justify-center max-w-2xl',
  );

  const formLoginTitle = document.createElement('h1');
  formLoginTitle.textContent = 'CONNEXION';
  formLoginTitle.setAttribute('class', 'text-center font-bold mb-8 text-swamp-green text-4xl');

  formLogin.appendChild(formLoginTitle);

  const sectionTop = document.createElement('section');
  sectionTop.setAttribute('class', 'flex flex-col justify-center items-center md:mx-20 xl:mx-40');

  const emailLabel = document.createElement('label');
  emailLabel.setAttribute('for', 'email');
  emailLabel.textContent = 'Adresse email';
  emailLabel.setAttribute('class', 'flex flex-col text-swamp-green w-full');

  const emailInput = document.createElement('input');
  emailInput.setAttribute('type', 'email');
  emailInput.setAttribute('id', 'email');
  emailInput.setAttribute('required', 'required');
  emailInput.setAttribute('class', 'w-2/4 rounded mt-2 mb-2 p-1 text-orange-400 w-full');

  emailLabel.appendChild(emailInput);
  sectionTop.appendChild(emailLabel);

  const passwordLabel = document.createElement('label');
  passwordLabel.setAttribute('for', 'password');
  passwordLabel.textContent = 'Mot de passe';
  passwordLabel.setAttribute('class', 'flex flex-col text-swamp-green w-full');

  const passwordInput = document.createElement('input');
  passwordInput.setAttribute('type', 'password');
  passwordInput.setAttribute('id', 'password');
  passwordInput.setAttribute('required', 'required');
  passwordInput.setAttribute('class', 'w-2/4 rounded mt-2 p-1 text-orange-400 w-full');

  passwordLabel.appendChild(passwordInput);
  sectionTop.appendChild(passwordLabel);

  const rememberMeLabel = document.createElement('label');
  rememberMeLabel.setAttribute('for', 'rememberMe');
  rememberMeLabel.textContent = 'Se souvenir de moi';
  rememberMeLabel.setAttribute('class', 'text-orange-400 mt-8');

  const rememberMeInput = document.createElement('input');
  rememberMeInput.setAttribute('type', 'checkbox');
  rememberMeInput.setAttribute('id', 'rememberMe');
  rememberMeInput.setAttribute('class', 'ml-4 form-checkbox');

  rememberMeLabel.appendChild(rememberMeInput);
  sectionTop.appendChild(rememberMeLabel);

  formLogin.appendChild(sectionTop);

  const sectionBottom = document.createElement('section');
  sectionBottom.setAttribute('class', 'flex flex-col lg:flex-row justify-center items-center');

  const noAccount = document.createElement('button');
  noAccount.setAttribute('type', 'button');
  noAccount.addEventListener('click', () => {
    Navigate('/register');
  });
  noAccount.textContent = 'Pas encore de compte ?';
  noAccount.setAttribute('class', 'lg:mr-8 mt-8 underline text-orange-400');

  sectionBottom.appendChild(noAccount);

  const loginButton = document.createElement('button');
  loginButton.setAttribute('type', 'submit');
  loginButton.textContent = 'CONNEXION';
  loginButton.setAttribute(
    'class',
    'bg-swamp-green text-white font-bold py-2 px-4 rounded mt-8 self-center',
  );

  sectionBottom.appendChild(loginButton);

  formLogin.appendChild(sectionBottom);

  formLogin.addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = emailInput.value;
    const password = passwordInput.value;
    const rememberMe = rememberMeInput.checked;

    const request = await fetch('/api/auths/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email,
        password,
      }),
    });

    const result = await request.json();
    if (!request.ok) {
      displayErrorMessage(result.message, main);
      formLogin.reset();
    } else {
      if (rememberMe) {
        localStorage.setItem('token', result.token);
      } else {
        sessionStorage.setItem('token', result.token);
      }
      sessionStorage.setItem('user', JSON.stringify(result.user));
      Navigate('/');
    }
  });

  maindiv.appendChild(formLogin);
  main.appendChild(maindiv);
};

export default LoginPage;
