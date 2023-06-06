import Navigate from '../../Router/Navigate';
import clearPage from '../../../utils/render';
import displayErrorMessage from '../../../utils/error';

const RegisterPage = () => {
  clearPage();
  const main = document.querySelector('main');

  const maindiv = document.createElement('div');
  maindiv.setAttribute(
    'class',
    'flex justify-center items-center min-h-[calc(100vh_-_120px)] bg-gradient-green-large',
  );

  const formRegister = document.createElement('form');
  formRegister.setAttribute('enctype', 'multipart/form-data');
  formRegister.setAttribute('novalidate', 'novalidate');
  formRegister.setAttribute(
    'class',
    'bg-noise-base p-8 sm:px-14 md:px-28 xl:px-56 flex flex-col w-3/5 h-5/6 justify-center max-w-4xl items-center',
  );

  const formRegisterTitle = document.createElement('h1');
  formRegisterTitle.textContent = 'INSCRIPTION';
  formRegisterTitle.setAttribute('class', 'text-center font-bold mb-8 text-swamp-green text-4xl');

  formRegister.appendChild(formRegisterTitle);

  const photoLabel = document.createElement('label');
  photoLabel.setAttribute('for', 'photo');
  photoLabel.setAttribute(
    'class',
    'flex flex-col h-24 w-24 mb-4 justify-center items-center cursor-pointer border-2 rounded-full overflow-hidden',
  );

  const photoInput = document.createElement('input');
  photoInput.setAttribute('type', 'file');
  photoInput.setAttribute('id', 'photo');
  photoInput.setAttribute('accept', 'image/png, image/jpeg, image/jpg, image/gif');
  photoInput.setAttribute('style', 'display: none;');
  photoInput.setAttribute('data-maxsize', '5242880');
  photoLabel.style.backgroundImage = `url(/api/images/default-user.png)`;
  photoLabel.style.backgroundSize = 'cover';

  photoInput.addEventListener('click', () => {
    photoInput.value = '';
    photoLabel.style.backgroundImage = `url(https://media.tenor.com/UnFx-k_lSckAAAAC/amalie-steiness.gif)`;
    document.body.onfocus = () => {
      if (photoInput.value === '') {
        photoLabel.style.backgroundImage = `url(/api/images/default-user.png)`;
      }
    };
  });

  photoInput.addEventListener('change', () => {
    const { files } = photoInput;
    if (files.length === 0) {
      // check if no files were selected
      photoLabel.style.backgroundImage = `url(/api/images/default-user.png)`;
      return;
    }
    const reader = new FileReader();
    reader.onload = (e) => {
      if (photoInput.files[0].size > photoInput.dataset.maxsize) {
        photoLabel.style.backgroundImage = `url(/api/images/default-user.png)`;
        displayErrorMessage("La taille de l'image ne doit pas dépasser 5Mo", main);
        return;
      }
      photoLabel.style.backgroundImage = `url(${e.target.result})`;
    };
    reader.readAsDataURL(photoInput.files[0]);
  });

  photoLabel.appendChild(photoInput);

  const photoSpan = document.createElement('span');
  photoSpan.addEventListener('click', () => {
    photoInput.click();
  });

  photoLabel.appendChild(photoSpan);
  photoLabel.appendChild(photoInput);

  formRegister.appendChild(photoLabel);

  const lastnameLabel = document.createElement('label');
  lastnameLabel.setAttribute('for', 'lastname');
  lastnameLabel.textContent = 'Nom';
  lastnameLabel.setAttribute('class', 'flex flex-col text-swamp-green w-full');

  const lastnameInput = document.createElement('input');
  lastnameInput.setAttribute('type', 'text');
  lastnameInput.setAttribute('id', 'lastname');
  lastnameInput.setAttribute('required', 'required');
  lastnameInput.setAttribute('class', 'w-2/4 p-1 rounded mt-2  mb-2 text-orange-400 w-full');

  lastnameLabel.appendChild(lastnameInput);
  formRegister.appendChild(lastnameLabel);

  const firstnameLabel = document.createElement('label');
  firstnameLabel.setAttribute('for', 'firstname');
  firstnameLabel.textContent = 'Prénom';
  firstnameLabel.setAttribute('class', 'flex flex-col text-swamp-green w-full');

  const firstnameInput = document.createElement('input');
  firstnameInput.setAttribute('type', 'text');
  firstnameInput.setAttribute('id', 'firstname');
  firstnameInput.setAttribute('required', 'required');
  firstnameInput.setAttribute('class', 'w-2/4 p-1 rounded mt-2 mb-2 text-orange-400 w-full');

  firstnameLabel.appendChild(firstnameInput);
  formRegister.appendChild(firstnameLabel);

  const emailLabel = document.createElement('label');
  emailLabel.setAttribute('for', 'email');
  emailLabel.textContent = 'Adresse email';
  emailLabel.setAttribute('class', 'flex flex-col text-swamp-green w-full');

  const emailInput = document.createElement('input');
  emailInput.setAttribute('type', 'email');
  emailInput.setAttribute('id', 'email');
  emailInput.setAttribute('required', 'required');
  emailInput.setAttribute('class', 'w-2/4 p-1 rounded mt-2 mb-2 text-orange-400 w-full');

  emailLabel.appendChild(emailInput);
  formRegister.appendChild(emailLabel);

  const gsmLabel = document.createElement('label');
  gsmLabel.setAttribute('for', 'gsm');
  gsmLabel.textContent = 'Numéro de GSM';
  gsmLabel.setAttribute('class', 'flex flex-col text-swamp-green w-full');

  const gsmInput = document.createElement('input');
  gsmInput.setAttribute('type', 'tel');
  gsmInput.setAttribute('pattern', '[0-9]{4}/[0-9]{2}.[0-9]{2}.[0-9]{2}');
  gsmInput.setAttribute('id', 'gsm');
  gsmInput.setAttribute('required', 'required');
  gsmInput.setAttribute('class', 'w-2/4 p-1 rounded mt-2 mb-2 text-orange-400 w-full');

  gsmLabel.appendChild(gsmInput);
  formRegister.appendChild(gsmLabel);

  const passwordLabel = document.createElement('label');
  passwordLabel.setAttribute('for', 'password');
  passwordLabel.textContent = 'Mot de passe';
  passwordLabel.setAttribute('class', 'flex flex-col text-swamp-green w-full');

  const passwordInput = document.createElement('input');
  passwordInput.setAttribute('type', 'password');
  passwordInput.setAttribute('id', 'password');
  passwordInput.setAttribute('required', 'required');
  passwordInput.setAttribute('class', 'w-2/4 p-1 rounded mt-2 mb-2 text-orange-400 w-full');

  passwordLabel.appendChild(passwordInput);
  formRegister.appendChild(passwordLabel);

  const passwordConfirmLabel = document.createElement('label');
  passwordConfirmLabel.setAttribute('for', 'passwordConfirm');
  passwordConfirmLabel.textContent = 'Répéter le mot de passe';
  passwordConfirmLabel.setAttribute('class', 'flex flex-col text-swamp-green w-full');

  const passwordConfirmInput = document.createElement('input');
  passwordConfirmInput.setAttribute('type', 'password');
  passwordConfirmInput.setAttribute('id', 'passwordConfirm');
  passwordConfirmInput.setAttribute('required', 'required');
  passwordConfirmInput.setAttribute('class', 'w-2/4 p-1 rounded mt-2 mb-2 text-orange-400 w-full');

  passwordConfirmLabel.appendChild(passwordConfirmInput);
  formRegister.appendChild(passwordConfirmLabel);

  const sectionBottom = document.createElement('section');
  sectionBottom.setAttribute(
    'class',
    'w-fit flex flex-col lg:flex-row items-center justify-center',
  );

  const alreadyRegistered = document.createElement('button');
  alreadyRegistered.setAttribute('type', 'button');
  alreadyRegistered.addEventListener('click', () => {
    Navigate('/login');
  });
  alreadyRegistered.textContent = 'Déjà inscrit ?';
  alreadyRegistered.setAttribute('class', 'mt-4 text-orange-400 mr-4 underline');

  sectionBottom.appendChild(alreadyRegistered);

  const registerButton = document.createElement('button');
  registerButton.setAttribute('type', 'submit');
  registerButton.textContent = "S'INSCRIRE";
  registerButton.setAttribute(
    'class',
    'bg-swamp-green text-white font-bold py-2 px-4 rounded mt-4 self-center',
  );

  sectionBottom.appendChild(registerButton);

  formRegister.appendChild(sectionBottom);

  formRegister.addEventListener('submit', async (e) => {
    e.preventDefault();

    const lastname = lastnameInput.value;
    const firstname = firstnameInput.value;
    const email = emailInput.value;
    const gsm = gsmInput.value;
    const password = passwordInput.value;
    const passwordConfirm = passwordConfirmInput.value;

    const photo = photoInput.files[0];

    if (!lastname || !firstname || !email || !gsm || !password || !passwordConfirm) {
      displayErrorMessage('Veuillez remplir tous les champs', main);
      return;
    }

    if (!email.match('\\b[\\w.%-]+@[\\w.-]+\\.[A-Za-z]{2,}\\b')) {
      displayErrorMessage('Adresse email invalide', main);
      return;
    }

    if (password !== passwordConfirm) {
      displayErrorMessage('Les mots de passe ne correspondent pas', main);
      return;
    }

    let isError = false;
    let error = 'Votre mot de passe doit être composé de: ';
    if (password.length < 8) {
      error += '8 caractères minimum, ';
      isError = true;
    }

    if (password.indexOf(' ') >= 0) {
      error += "pas d'espace, ";
      isError = true;
    }

    if (!password.match('(?=.*\\d)(?=.*[^\\w\\s]).+')) {
      error += 'au moins un chiffre et un caractère spécial, ';
      isError = true;
    }

    if (isError) {
      error = error.slice(0, -2);
      error += '.';
      displayErrorMessage(error, main);
      return;
    }

    const formData = new FormData();
    formData.append('file', photo);
    formData.append('lastname', lastname);
    formData.append('firstname', firstname);
    formData.append('email', email);
    formData.append('gsm', gsm);
    formData.append('password', password);

    const request = await fetch('/api/auths/register', {
      method: 'POST',
      body: formData,
    });

    const result = await request.json();
    if (!request.ok) {
      displayErrorMessage(result.message, main);
    } else {
      sessionStorage.setItem('user', JSON.stringify(result.user));
      sessionStorage.setItem('token', result.token);
      Navigate('/');
    }
  });

  maindiv.appendChild(formRegister);
  main.appendChild(maindiv);
};

export default RegisterPage;
