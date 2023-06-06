import clearPage from '../../../utils/render';
import displayErrorMessage from '../../../utils/error';
import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser, isAuthenticated, isHelper } from '../../../utils/auths';

const getStatusLabel = (status) => {
  switch (status) {
    case 'PROPOSED':
      return 'Proposé';
    case 'ACCEPTED':
      return 'Accepté';
    case 'REFUSED':
      return 'Refusé';
    case 'WORKSHOP':
      return 'En atelier';
    case 'SHOP':
      return 'En magasin';
    case 'ON_SALE':
      return 'En vente';
    case 'SOLD':
      return 'Vendu';
    case 'REMOVED':
      return 'Retiré';
    default:
      return 'Inconnu';
  }
};

const ProfileIndexPage = async () => {
  clearPage();

  if (isAuthenticated()) {
    const currentUser = getAuthenticatedUser();

    const main = document.querySelector('main');
    const mainDiv = document.createElement('div');
    mainDiv.classList.add('min-h-[calc(100vh_-_120px)]', 'bg-gradient-green-large');
    main.appendChild(mainDiv);

    const secondaryDiv = document.createElement('div');
    secondaryDiv.classList.add('bg-noise-base', 'mx-4', 'md:mx-24');

    const user = document.createElement('div');
    user.classList.add(
      'flex',
      'gap-10',
      'p-10',
      'items-center',
      'flex-col',
      'md:flex-row',
      'justify-center',
    );

    const userImage = document.createElement('div');

    const userImageTag = document.createElement('img');
    userImageTag.classList.add(
      'rounded-full',
      'md:h-40',
      'md:w-40',
      'lg:h-64',
      'lg:w-64',
      'h-64',
      'w-64',
      'aspect-square',
    );
    userImageTag.setAttribute('src', `/api/images/${currentUser.photo}`);
    userImage.appendChild(userImageTag);

    const userInfo = document.createElement('div');

    const name = document.createElement('div');
    name.classList.add('flex', 'gap-2', 'pb-4');

    const lastname = document.createElement('p');
    lastname.classList.add('text-4xl', 'font-bold', 'text-swamp-green');
    lastname.textContent = currentUser.lastname;
    name.appendChild(lastname);

    const firstname = document.createElement('p');
    firstname.classList.add('text-4xl', 'font-bold', 'text-swamp-green');
    firstname.textContent = currentUser.firstname;
    name.appendChild(firstname);
    userInfo.appendChild(name);

    const email = document.createElement('p');
    email.classList.add('text-xl', 'text-gray-500');
    email.textContent = currentUser.email;
    userInfo.appendChild(email);

    const phone = document.createElement('p');
    phone.classList.add('text-xl', 'text-gray-500');
    phone.textContent = currentUser.phoneNumber;
    userInfo.appendChild(phone);

    const editBtn = document.createElement('button');
    editBtn.classList.add(
      'my-2',
      'px-4',
      'py-1',
      'bg-orange-400',
      'text-white',
      'cursor-pointer',
      'rounded',
    );
    editBtn.textContent = 'Edit';
    editBtn.addEventListener('click', () => {
      Navigate('/profile/edit');
    });
    userInfo.appendChild(editBtn);

    user.appendChild(userImage);
    user.appendChild(userInfo);

    secondaryDiv.appendChild(user);

    const info = document.createElement('div');
    info.classList.add(
      'flex',
      'gap-10',
      'lg:gap-64',
      'justify-center',
      'pb-8',
      'lg:flex-row',
      'flex-col',
    );

    const objectsInfo = document.createElement('div');
    objectsInfo.classList.add('p-4');

    const objectsTitle = document.createElement('h2');
    objectsTitle.classList.add('text-4xl', 'font-bold', 'text-swamp-green', 'pb-8');
    objectsTitle.textContent = 'Objets';
    objectsInfo.appendChild(objectsTitle);

    const listObjects = document.createElement('ul');
    listObjects.setAttribute(
      'class',
      '[&>:nth-child(even)]:bg-noise-base [&>:nth-child(odd)]:bg-gradient-gris',
    );

    const request = await fetch(`/api/users/${currentUser.id}/objects`, {
      method: 'GET',
      headers: {
        Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
      },
    });

    const response = await request.json();
    if (!request.ok) {
      displayErrorMessage(response.message, main);
    } else {
      response.forEach((object) => {
        const link = document.createElement('a');

        link.addEventListener('click', () => {
          Navigate(`/object?id=${object.id}`);
        });

        const leftDiv = document.createElement('div');
        leftDiv.setAttribute('class', 'flex flex-row items-center mb-4');

        const img = document.createElement('img');
        img.setAttribute('class', 'w-14 h-14 sm:w-20 sm:h-20 ml-8 mr-4 my-2');
        img.setAttribute('src', `/api/images/${object?.photo}`);

        leftDiv.appendChild(img);

        const contentObject = document.createElement('div');
        contentObject.setAttribute('class', 'flex flex-col justify-between');
        leftDiv.appendChild(contentObject);

        const typeName = document.createElement('div');

        const typeObject = document.createElement('p');
        typeObject.setAttribute('class', 'font-bold text-orange-400');
        typeObject.textContent = object.fkObjectType.typeName;
        typeName.appendChild(typeObject);

        const offeringMember = document.createElement('p');
        offeringMember.setAttribute('class', 'text-gray-400');
        offeringMember.textContent = `${getAuthenticatedUser().lastname} ${
          getAuthenticatedUser().firstname
        }`;

        typeName.appendChild(offeringMember);

        const description = document.createElement('p');
        description.setAttribute('class', 'text-gray-400');
        description.textContent = object.description;

        typeName.appendChild(description);

        const status = document.createElement('p');
        status.setAttribute('class', 'text-gray-400');
        status.textContent = `${getStatusLabel(object.status)}`;

        typeName.appendChild(status);

        contentObject.appendChild(typeName);

        const prixObject = document.createElement('p');
        prixObject.setAttribute('class', 'font-bold text-swamp-green text-xl');
        if (object.sellingPrice === 0) {
          prixObject.textContent = 'Pas en vente';
        } else {
          prixObject.textContent = `${object.sellingPrice} €`;
        }
        contentObject.appendChild(prixObject);

        link.appendChild(leftDiv);

        listObjects.appendChild(link);
      });
    }

    objectsInfo.appendChild(listObjects);
    info.appendChild(objectsInfo);

    const notificationsInfo = document.createElement('div');
    notificationsInfo.classList.add('p-4');

    const notificationsTitle = document.createElement('h2');
    notificationsTitle.classList.add('text-4xl', 'font-bold', 'text-swamp-green');
    notificationsTitle.textContent = 'Notifications';
    notificationsInfo.appendChild(notificationsTitle);

    const listNotifications = document.createElement('ul');

    displayNotifications(listNotifications, currentUser);

    notificationsInfo.appendChild(listNotifications);
    info.appendChild(notificationsInfo);

    secondaryDiv.appendChild(info);
    mainDiv.appendChild(secondaryDiv);
  } else {
    Navigate('/');
  }
};

async function displayNotifications(list, currentUser) {
  const userNotifications = await getUserNotification(currentUser.id);

  // eslint-disable-next-line
  list.innerHTML = '';

  userNotifications.sort((a, b) => {
    if (a.read === true && b.read === false) {
      return 1;
    }
    if (b.read === true && a.read === false) {
      return -1;
    }
    return 0;
  });

  userNotifications.forEach((notification) => {
    const notificationLi = document.createElement('li');
    notificationLi.classList.add('py-2');

    const notificationLink = document.createElement('a');
    notificationLink.setAttribute(
      'class',
      'text-lime-700 text-md hover:text-orange-400 transition-all cursor-pointer',
    );
    if (!notification.read) {
      notificationLink.classList.add('font-bold');
    }

    notificationLink.addEventListener('click', async () => {
      await fetch(`/api/users/${currentUser.id}/notifications/${notification.fkNotification.id}`, {
        method: 'PATCH',
        headers: {
          Authorization: localStorage.getItem('token') || sessionStorage.getItem('token'),
        },
      });

      if (isHelper()) {
        Navigate(`/dashboard/objects/edit?id=${notification.fkNotification.fkConcernedObject.id}`);
      } else {
        Navigate(`/object?id=${notification.fkNotification.fkConcernedObject.id}`);
      }
    });

    notificationLink.textContent = `${notification.fkNotification.textNotification}`;

    notificationLi.appendChild(notificationLink);
    list.appendChild(notificationLi);
  });
}

async function getUserNotification(id) {
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

export default ProfileIndexPage;
