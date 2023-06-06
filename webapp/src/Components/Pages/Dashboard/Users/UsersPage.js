import clearPage from '../../../../utils/render';
import displayErrorMessage from '../../../../utils/error';
import DashboardNavbar from '../Navbar/DashboardNavbar';
import { isAuthenticated, isManager } from '../../../../utils/auths';
import Navigate from '../../../Router/Navigate';

const promoteUser = async (id) => {
  const main = document.querySelector('main');

  const requestButton = await fetch(`/api/users/${id}/promote`, {
    method: 'PATCH',
    headers: {
      Authorization: localStorage.getItem('token') || sessionStorage.getItem('token'),
    },
  });

  const responseButton = await requestButton.json();

  if (!requestButton.ok) {
    displayErrorMessage(responseButton.message, main);
  }
};

const displayRole = (role) => {
  switch (role) {
    case 'HELPER':
      return 'Aidant';
    case 'MANAGER':
      return 'Responsable';
    default:
      return 'Utilisateur';
  }
};

const UsersPage = async () => {
  clearPage();

  if (isAuthenticated()) {
    if (isManager()) {
      const main = document.querySelector('main');

      const maindiv = document.createElement('div');
      maindiv.classList.add('flex', 'flex-col', 'lg:flex-row', 'min-h-[calc(100vh_-_120px)]');
      maindiv.appendChild(DashboardNavbar());

      const content = document.createElement('div');
      content.classList.add('flex', 'flex-col', 'flex-grow');

      const searchDiv = document.createElement('div');

      const searchContainer = document.createElement('form');
      searchContainer.classList.add(
        'flex',
        'flex-row',
        'justify-center',
        'w-fit',
        'mx-auto',
        'my-4',
        'gap-4',
      );

      const searchInput = document.createElement('input');
      searchInput.setAttribute('type', 'text');
      searchInput.setAttribute('placeholder', 'Rechercher un utilisateur');
      searchInput.classList.add(
        'border',
        'border-gray-300',
        'rounded-md',
        'p-2',
        'w-64',
        'md:w-96',
      );

      const listUsersCompletion = document.createElement('div');
      listUsersCompletion.classList.add(
        'absolute',
        'mt-12',
        'mr-10',
        'z-10',
        'bg-white',
        'w-96',
        'rounded-md',
        'shadow-md',
        'border',
        'border-gray-300',
        'hidden',
      );

      const searchBtn = document.createElement('button');
      searchBtn.setAttribute('type', 'submit');

      const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
      svg.setAttribute('height', '16');
      svg.setAttribute('width', '16');
      svg.setAttribute('viewBox', '0 0 512 512');
      svg.setAttribute('xmlns', 'http://www.w3.org/2000/svg');
      svg.classList.add('ml-2');

      const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
      path.setAttribute(
        'd',
        'M376.324,312.508c49.638-78.774,40.238-184.326-28.306-252.871c-79.507-79.515-208.872-79.515-288.388,0   c-79.507,79.516-79.507,208.873,0,288.379c68.536,68.544,174.115,77.935,252.88,28.306l135.668,135.676L512,448.186   L376.324,312.508z M296.543,296.542c-51.121,51.139-134.308,51.139-185.439,0c-51.121-51.121-51.112-134.299,0.009-185.43   c51.122-51.121,134.309-51.13,185.43-0.008C347.665,162.243,347.665,245.421,296.543,296.542z',
      );

      svg.appendChild(path);
      searchBtn.appendChild(svg);

      searchContainer.appendChild(searchInput);
      searchContainer.appendChild(searchBtn);
      searchDiv.appendChild(searchContainer);

      const centerDiv = document.createElement('div');
      centerDiv.setAttribute('class', 'flex flex-col flex-grow');

      const title = document.createElement('h2');
      title.setAttribute('class', 'pl-4 font-bold text-xl pt-5 text-swamp-green');
      title.textContent = 'Récemment inscrits';
      centerDiv.appendChild(title);

      const listUsers = document.createElement('ul');
      listUsers.setAttribute(
        'class',
        '[&>:nth-child(even)]:bg-noise-base [&>:nth-child(odd)]:bg-gradient-gris flex-grow',
      );

      const request = await fetch('/api/users', {
        method: 'GET',
        headers: {
          Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
        },
      });

      const response = await request.json();
      if (!request.ok) {
        displayErrorMessage(response.message, main);
      } else {
        displayUsers(response, listUsers);
      }

      centerDiv.appendChild(listUsers);
      content.appendChild(searchDiv);
      content.appendChild(centerDiv);
      maindiv.appendChild(content);
      main.appendChild(maindiv);

      searchInput.addEventListener('input', (e) => {
        const search = e.target.value;

        if (search.length === 0) {
          listUsersCompletion.classList.add('hidden');
          return;
        }

        const users = response.filter(
          (user) =>
            user.firstname.toLowerCase().startsWith(search.toLowerCase()) ||
            user.lastname.toLowerCase().startsWith(search.toLowerCase()) ||
            `${user.lastname} ${user.firstname}`.toLowerCase().startsWith(search.toLowerCase()),
        );

        if (users.length > 0) {
          listUsersCompletion.classList.remove('hidden');
          listUsersCompletion.innerHTML = '';
          users.forEach((user) => {
            const userCompletion = document.createElement('div');
            userCompletion.classList.add(
              'p-2',
              'border-b',
              'border-gray-300',
              'cursor-pointer',
              'hover:bg-gray-200',
            );
            userCompletion.textContent = `${user.lastname} ${user.firstname}`;
            userCompletion.addEventListener('click', () => {
              searchInput.value = userCompletion.textContent;
              listUsersCompletion.classList.add('hidden');
            });
            listUsersCompletion.appendChild(userCompletion);
          });
          searchInput.parentNode.appendChild(listUsersCompletion);
        }
      });

      searchContainer.addEventListener('submit', (e) => {
        e.preventDefault();

        const search = searchInput.value;

        let users = response;

        if (search) {
          users = response.filter(
            (user) =>
              user.firstname.toLowerCase().startsWith(search.toLowerCase()) ||
              user.lastname.toLowerCase().startsWith(search.toLowerCase()) ||
              `${user.lastname} ${user.firstname}`.toLowerCase().startsWith(search.toLowerCase()),
          );
        }

        listUsers.innerHTML = '';
        displayUsers(users, listUsers);
      });
    } else {
      Navigate('/');
    }
  } else {
    Navigate('/');
  }
};

function displayUsers(users, section) {
  users.forEach((user) => {
    const userLine = document.createElement('li');
    userLine.setAttribute('class', 'flex flex-grow justify-between px-4 py-1 items-center');

    const leftDiv = document.createElement('div');
    leftDiv.setAttribute('class', 'flex flex-row items-center');

    const image = document.createElement('img');
    image.src = `/api/images/${user?.photo}`;
    image.setAttribute('class', 'w-11 h-11 rounded-full');
    leftDiv.appendChild(image);

    const userInfo = document.createElement('div');
    userInfo.setAttribute('class', 'pl-4');

    const names = document.createElement('p');
    names.setAttribute('class', 'text-swamp-green font-bold cursor-pointer');
    names.textContent = `${user.lastname} ${user.firstname} - ${displayRole(user.role)}`;
    names.addEventListener('click', () => Navigate(`/dashboard/user?id=${user.id}`));
    userInfo.appendChild(names);

    const otherInfo = document.createElement('p');
    otherInfo.setAttribute('class', 'text-gray-500');
    otherInfo.textContent = `${user.email} ${user.phoneNumber}`;
    userInfo.appendChild(otherInfo);

    leftDiv.appendChild(userInfo);

    userLine.appendChild(leftDiv);
    if (user.role === 'USER' || user.role === 'HELPER') {
      const helperButton = document.createElement('button');
      helperButton.setAttribute('class', 'bg-swamp-green text-white font-bold py-2 px-2');
      helperButton.textContent = 'Promouvoir';
      helperButton.addEventListener('click', () => {
        promoteUser(user.id).then(() => UsersPage());
      });
      userLine.appendChild(helperButton);
    }

    section.appendChild(userLine);
  });
}

export default UsersPage;
