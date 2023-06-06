import Navigate from '../Router/Navigate';
import { getAuthenticatedUser, isAuthenticated, isHelper } from '../../utils/auths';
import displayErrorMessage from '../../utils/error';

const Navbar = () => {
  const header = document.querySelector('header');
  header.innerHTML = '';

  const navbar = document.createElement('nav');
  navbar.setAttribute(
    'class',
    'bg-gradient-gris flex items-center justify-between text-orange-400 font-medium',
  );

  const homeIcon = document.createElement('button');
  homeIcon.setAttribute(
    'class',
    'bg-contain bg-no-repeat bg-logo w-40 h-6 mx-4 md:ml-[calc(50%_-_86px)] hover:rotate-2 transition-all',
  );

  homeIcon.addEventListener('click', () => {
    Navigate('/');
  });

  const dropDowns = document.createElement('div');
  dropDowns.setAttribute('class', 'flex items-center');

  const account = document.createElement('div');
  account.setAttribute('id', 'account');
  account.setAttribute('class', 'dropdown');

  const currentUser = getAuthenticatedUser();
  let accountDropDownBtn;
  if (currentUser) {
    accountDropDownBtn = document.createElement('img');
    accountDropDownBtn.setAttribute('src', `/api/images/${currentUser?.photo}`);
    accountDropDownBtn.setAttribute('alt', 'user');
    accountDropDownBtn.setAttribute('class', 'w-8 h-8 rounded-full cursor-pointer m-3');
  } else {
    accountDropDownBtn = document.createElement('button');
    accountDropDownBtn.setAttribute('class', 'font-bold drop-btn-account p-4');
    accountDropDownBtn.textContent = 'Compte';
  }

  account.appendChild(accountDropDownBtn);

  const accountDropDownMenu = document.createElement('div');
  accountDropDownMenu.setAttribute(
    'class',
    'bg-stone-200 dropdown-content dropdown-content-account absolute flex flex-col gap-2 transition-all z-10 right-0',
  );

  if (currentUser) {
    accountDropDownMenu.classList.remove('right-0');
  } else {
    accountDropDownMenu.classList.add('right-0');
  }

  if (currentUser) {
    const accountLink = document.createElement('button');
    accountLink.textContent = 'Profil';
    accountLink.setAttribute('id', 'profile');
    accountLink.setAttribute(
      'class',
      'text-lime-700 py-1 px-4 hover:bg-stone-200 hover:text-orange-400 transition-all',
    );

    const logoutLink = document.createElement('button');
    logoutLink.textContent = 'Déconnexion';
    logoutLink.setAttribute('id', 'logout');
    logoutLink.setAttribute(
      'class',
      'text-lime-700 py-1 px-4 hover:bg-stone-200 hover:text-orange-400 transition-all',
    );

    accountDropDownMenu.appendChild(accountLink);

    if (currentUser.role === 'HELPER' || currentUser.role === 'MANAGER') {
      const adminLink = document.createElement('button');
      adminLink.setAttribute('id', 'dashboard');
      adminLink.setAttribute(
        'class',
        'text-lime-700 py-1 px-4 hover:bg-stone-200 hover:text-orange-400 transition-all',
      );
      adminLink.textContent = 'Admin';
      accountDropDownMenu.appendChild(adminLink);
    }

    accountDropDownMenu.appendChild(logoutLink);
  } else {
    const registerLink = document.createElement('button');
    registerLink.textContent = 'Inscription';
    registerLink.setAttribute('id', 'register');
    registerLink.setAttribute(
      'class',
      'text-lime-700 py-1 px-4 hover:bg-stone-200 hover:text-orange-400 transition-all',
    );

    const loginLink = document.createElement('button');
    loginLink.textContent = 'Connexion';
    loginLink.setAttribute('id', 'login');
    loginLink.setAttribute(
      'class',
      'text-lime-700 py-1 px-4 hover:bg-stone-200 hover:text-orange-400 transition-all',
    );

    accountDropDownMenu.appendChild(loginLink);
    accountDropDownMenu.appendChild(registerLink);
  }

  account.appendChild(accountDropDownMenu);
  dropDowns.appendChild(account);

  const notifications = document.createElement('div');
  const notificationsDropDownBtn = document.createElement('button');
  const notificationsDropDownMenu = document.createElement('div');
  if (isAuthenticated()) {
    notifications.setAttribute('class', 'dropdown');

    notificationsDropDownBtn.setAttribute('class', 'font-bold drop-btn-notifications p-4');
    notificationsDropDownBtn.textContent = 'Notifications';

    notificationsDropDownMenu.setAttribute(
      'class',
      'bg-stone-200 dropdown-content dropdown-content-account absolute flex flex-col gap-2 transition-all z-10 right-0',
    );

    notifications.appendChild(notificationsDropDownBtn);
    notifications.appendChild(notificationsDropDownMenu);
    dropDowns.appendChild(notifications);

    displayNotifications(notificationsDropDownMenu, currentUser);
  }

  navbar.appendChild(homeIcon);
  navbar.appendChild(dropDowns);
  header.appendChild(navbar);

  accountDropDownMenu.childNodes.forEach((node) => {
    node.addEventListener('click', () => {
      Navigate(`/${node.id}`);
    });
  });

  document.querySelectorAll('.dropdown-content').forEach((dropdownContent) => {
    dropdownContent.classList.add('hidden');
  });

  accountDropDownBtn.addEventListener('click', () => {
    if (isAuthenticated()) {
      if (!notificationsDropDownMenu.classList.contains('hidden')) {
        notificationsDropDownMenu.classList.toggle('hidden');
      }
    }
    accountDropDownMenu.classList.toggle('hidden');
  });

  if (isAuthenticated()) {
    notificationsDropDownBtn.addEventListener('click', () => {
      if (!accountDropDownMenu.classList.contains('hidden')) {
        accountDropDownMenu.classList.toggle('hidden');
      }
      notificationsDropDownMenu.classList.toggle('hidden');
    });
  }

  document.addEventListener('click', (event) => {
    if (!account.contains(event.target)) {
      accountDropDownMenu.classList.add('hidden');
    }
    if (isAuthenticated()) {
      if (!notifications.contains(event.target)) {
        notificationsDropDownMenu.classList.add('hidden');
      }
    }
  });
};

async function displayNotifications(notificationsDropDownMenu, currentUser) {
  const userNotifications = await getUserNotifications(currentUser.id);

  userNotifications
    .filter((notification) => !notification.read)
    .forEach((notification) => {
      const notificationLink = document.createElement('button');
      notificationLink.setAttribute(
        'class',
        'text-lime-700 py-1 px-4 hover:bg-stone-200 hover:text-orange-400 transition-all',
      );

      notificationLink.addEventListener('click', async () => {
        await fetch(
          `/api/users/${currentUser.id}/notifications/${notification.fkNotification.id}`,
          {
            method: 'PATCH',
            headers: {
              Authorization: localStorage.getItem('token') || sessionStorage.getItem('token'),
            },
          },
        );

        if (isHelper()) {
          Navigate(
            `/dashboard/objects/edit?id=${notification.fkNotification.fkConcernedObject.id}`,
          );
        } else {
          Navigate(`/object?id=${notification.fkNotification.fkConcernedObject.id}`);
        }
      });

      notificationLink.textContent = `${notification.fkNotification.textNotification}`;

      notificationsDropDownMenu.appendChild(notificationLink);
    });
}

async function getUserNotifications(id) {
  const request = await fetch(`/api/users/${id}/notifications`, {
    method: 'GET',
    headers: {
      Authorization: localStorage.getItem('token') || sessionStorage.getItem('token'),
    },
  });

  const response = await request.json();
  const listNotifications = [];
  if (!request.ok) {
    displayErrorMessage(response.message, document.querySelector('main'));
  } else {
    response.forEach((object) => {
      listNotifications.push(object);
    });
  }

  return listNotifications;
}

export default Navbar;
