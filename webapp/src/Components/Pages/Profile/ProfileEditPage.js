import clearPage from '../../../utils/render';
import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser, isAuthenticated } from '../../../utils/auths';
import displayErrorMessage from '../../../utils/error';
import ProfileIndexPage from './ProfileIndexPage';

const ProfileEditPage = async () => {
  clearPage();

  if (isAuthenticated()) {
    const currentUser = getAuthenticatedUser();

    const main = document.querySelector('main');
    const mainDiv = document.createElement('div');
    mainDiv.classList.add('min-h-[calc(100vh_-_120px)]', 'bg-gradient-green-large');
    main.appendChild(mainDiv);

    const secondaryDiv = document.createElement('div');
    secondaryDiv.classList.add('bg-noise-base', 'mx-4', 'md:mx-24');

    const editForm = document.createElement('form');
    editForm.classList.add('flex', 'gap-10', 'p-10', 'justify-center', 'flex-col', 'md:flex-row');

    const imgLabel = document.createElement('label');
    imgLabel.classList.add(
      'md:h-40',
      'md:w-40',
      'lg:h-64',
      'lg:w-64',
      'h-64',
      'w-64',
      'aspect-square',
      'cursor-pointer',
      'rounded-full',
      'bg-cover',
      'bg-no-repeat',
      'text-orange-400',
      'font-bold',
      'relative',
    );
    imgLabel.id = 'label';
    imgLabel.style.backgroundImage = `url(/api/images/${currentUser?.photo})`;

    const imgInput = document.createElement('input');
    imgInput.classList.add('hidden');
    imgInput.name = 'file';
    imgInput.type = 'file';
    imgInput.accept = 'image/png, image/jpeg, image/jpg, image/gif';
    imgInput.dataset.maxsize = '5242880';
    imgInput.alt = 'test';

    imgInput.addEventListener('click', () => {
      imgInput.value = '';
      imgLabel.style.backgroundImage = `url(https://media.tenor.com/UnFx-k_lSckAAAAC/amalie-steiness.gif)`;
      document.body.onfocus = () => {
        if (imgInput.value === '') {
          imgLabel.style.backgroundImage = `url(/api/images/${currentUser?.photo})`;
        }
      };
    });

    imgInput.addEventListener('change', () => {
      const { files } = imgInput;
      if (files.length === 0) {
        // check if no files were selected
        imgLabel.style.backgroundImage = `url(/api/images/${currentUser?.photo})`;
        return;
      }
      const reader = new FileReader();
      reader.onload = (e) => {
        if (imgInput.files[0].size > imgInput.dataset.maxsize) {
          imgLabel.style.backgroundImage = `url(/api/images/${currentUser?.photo})`;
          displayErrorMessage("La taille de l'image ne doit pas dépasser 5Mo", main);
          return;
        }
        imgLabel.style.backgroundImage = `url(${e.target.result})`;
      };
      reader.readAsDataURL(imgInput.files[0]);
    });

    imgLabel.appendChild(imgInput);

    const userInfo = document.createElement('div');
    editForm.classList.add('flex', 'gap-10', 'p-10', 'items-center', 'flex-col');

    const editTitle = document.createElement('h1');
    editTitle.classList.add('text-3xl', 'text-orange-400', 'font-bold', 'm-auto');
    editTitle.textContent = 'Modification du profil';
    userInfo.appendChild(editTitle);

    const name = document.createElement('div');
    name.classList.add('flex', 'gap-2', 'pb-3', 'flex-row', 'w-72', 'lg:w-96');

    const lastname = document.createElement('input');
    lastname.classList.add(
      'text-4xl',
      'font-bold',
      'text-swamp-green',
      'flex',
      'w-1/2',
      'px-1.5',
      'rounded',
    );
    lastname.value = currentUser.lastname;
    lastname.placeholder = 'nom';
    name.appendChild(lastname);

    const firstname = document.createElement('input');
    firstname.classList.add(
      'text-4xl',
      'font-bold',
      'text-swamp-green',
      'flex',
      'w-1/2',
      'px-1.5',
      'rounded',
    );
    firstname.value = currentUser.firstname;
    firstname.placeholder = 'prénom';
    name.appendChild(firstname);
    userInfo.appendChild(name);

    const email = document.createElement('input');
    email.classList.add(
      'text-xl',
      'text-gray-500',
      'flex',
      'mb-1',
      'rounded',
      'px-1.5',
      'w-72',
      'lg:w-96',
    );
    email.value = currentUser.email;
    email.placeholder = 'email';
    userInfo.appendChild(email);

    const phone = document.createElement('input');
    phone.classList.add(
      'text-xl',
      'text-gray-500',
      'flex',
      'm-auto',
      'md:m-0',
      'md:m-0',
      'rounded',
      'px-1.5',
      'flex',
      'w-72',
      'lg:w-96',
    );
    phone.value = currentUser.phoneNumber;
    phone.placeholder = 'gsm';
    userInfo.appendChild(phone);

    const changepassword = document.createElement('p');
    changepassword.classList.add('text-xl', 'text-orange-400', 'font-bold', 'm-auto');
    changepassword.textContent = 'changer mot de passe';
    userInfo.appendChild(changepassword);

    const password = document.createElement('input');
    password.type = 'password';
    password.classList.add(
      'text-xl',
      'text-gray-500',
      'flex',
      'mb-1',
      'rounded',
      'px-1.5',
      'w-72',
      'lg:w-96',
    );
    password.placeholder = 'mot de passe';
    userInfo.appendChild(password);

    const passwordConfirm = document.createElement('input');
    passwordConfirm.type = 'password';
    passwordConfirm.classList.add(
      'text-xl',
      'text-gray-500',
      'flex',
      'rounded',
      'px-1.5',
      'w-72',
      'lg:w-96',
    );
    passwordConfirm.placeholder = 'confirmez le mot de passe';
    userInfo.appendChild(passwordConfirm);

    const formButtons = document.createElement('div');
    formButtons.classList.add('flex', 'gap-2', 'pt-1', 'flex-row', 'w-72', 'lg:w-96');

    const confirmBtn = document.createElement('input');
    confirmBtn.classList.add(
      'w-1/2',
      'md:my-2',
      'md:px-4',
      'py-1',
      'bg-swamp-green',
      'text-white',
      'cursor-pointer',
      'rounded',
    );
    confirmBtn.type = 'submit';
    confirmBtn.name = 'confirm';
    confirmBtn.value = 'Confirmer';
    formButtons.appendChild(confirmBtn);

    const cancelBtn = document.createElement('input');
    cancelBtn.classList.add(
      'text-center',
      'w-1/2',
      'md:my-2',
      'md:px-4',
      'py-1',
      'bg-rose-900',
      'text-white',
      'cursor-pointer',
      'rounded',
    );
    cancelBtn.value = 'Annuler';
    cancelBtn.addEventListener('click', () => {
      Navigate('/profile');
    });
    formButtons.appendChild(cancelBtn);

    const imgSpan = document.createElement('span');
    imgSpan.classList.add('absolute', 'bottom-0', 'right-0', 'text-4xl');
    imgSpan.textContent = '+';
    imgLabel.appendChild(imgSpan);

    imgLabel.appendChild(imgSpan);
    editForm.appendChild(imgLabel);
    editForm.appendChild(userInfo);

    editForm.addEventListener('submit', async (e) => {
      e.preventDefault();

      const photo = imgInput.files[0];

      if (!lastname.value || !firstname.value || !email.value || !phone.value) {
        displayErrorMessage('Veuillez remplir tous les champs', main);
        return;
      }

      if (!email.value.match('\\b[\\w.%-]+@[\\w.-]+\\.[A-Za-z]{2,}\\b')) {
        displayErrorMessage('Adresse email invalide', main);
        return;
      }

      if (password.value !== '' && password.value !== undefined) {
        if (password.value !== passwordConfirm.value) {
          displayErrorMessage('Les mots de passe ne correspondent pas', main);
          return;
        }

        let isError = false;
        let error = 'Votre mot de passe doit être composé de: ';
        if (password.length < 8) {
          error += '8 caractères minimum, ';
          isError = true;
        }

        if (password.value.indexOf(' ') >= 0) {
          error += "pas d'espace, ";
          isError = true;
        }

        if (!password.value.match('(?=.*\\d)(?=.*[^\\w\\s]).+')) {
          error += 'au moins un chiffre et un caractère spécial, ';
          isError = true;
        }

        if (isError) {
          error = error.slice(0, -2);
          error += '.';
          displayErrorMessage(error, main);
          return;
        }
      }

      const formData = new FormData();
      formData.append('file', photo);
      formData.append('lastname', lastname.value);
      formData.append('firstname', firstname.value);
      formData.append('email', email.value);
      formData.append('phone', phone.value);
      formData.append('password', password.value);

      const request = await fetch(`/api/users/${currentUser.id}/edit`, {
        method: 'PATCH',
        body: formData,
        headers: {
          Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
        },
      });

      const result = await request.json();
      if (!request.ok) {
        displayErrorMessage(result.message, main);
      } else {
        sessionStorage.setItem('user', JSON.stringify(result));
        await ProfileIndexPage();
      }
    });

    userInfo.appendChild(formButtons);

    secondaryDiv.appendChild(editForm);

    mainDiv.appendChild(secondaryDiv);
  } else {
    Navigate('/');
  }
};

export default ProfileEditPage;
