import clearPage from '../../../../utils/render';
import displayErrorMessage from '../../../../utils/error';
import Navigate from '../../../Router/Navigate';
import { isAuthenticated, isHelper, isManager } from '../../../../utils/auths';

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
  if (isAuthenticated()) {
    if (isManager() || isHelper()) {
      clearPage();

      const main = document.querySelector('main');
      const mainDiv = document.createElement('div');
      mainDiv.classList.add('min-h-[calc(100vh_-_120px)]', 'bg-gradient-green-large');
      main.appendChild(mainDiv);

      const secondaryDiv = document.createElement('div');
      secondaryDiv.classList.add('bg-noise-base', 'mx-4', 'md:mx-24');

      const user = document.createElement('div');
      user.classList.add('flex', 'gap-10', 'p-10', 'items-center', 'flex-col', 'md:flex-row');

      const userImage = document.createElement('div');

      const userId = new URLSearchParams(window.location.search).get('id');

      const userRequest = await fetch(`/api/users/${userId}`, {
        method: 'GET',
        headers: {
          Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
        },
      });

      const userResponse = await userRequest.json();
      let currentUser;
      if (!userRequest.ok) {
        displayErrorMessage(userResponse.message, main);
      } else {
        currentUser = userResponse;
      }

      const userImageTag = document.createElement('img');
      userImageTag.classList.add('rounded-full', 'w-64', 'h-64');
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

      user.appendChild(userImage);
      user.appendChild(userInfo);

      secondaryDiv.appendChild(user);

      const info = document.createElement('div');

      const objectsTitle = document.createElement('h2');
      objectsTitle.classList.add(
        'text-4xl',
        'font-bold',
        'text-swamp-green',
        'pb-8',
        'text-center',
      );
      objectsTitle.textContent = 'Objets';
      info.appendChild(objectsTitle);

      const listObjects = document.createElement('ul');
      listObjects.setAttribute(
        'class',
        '[&>:nth-child(even)]:bg-noise-base [&>:nth-child(odd)]:bg-gradient-gris grid grid-cols-1 sm:grid-cols-2',
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
            Navigate(`/dashboard/objects/edit?id=${object.id}`);
          });

          const objectLine = document.createElement('li');
          objectLine.setAttribute('class', 'flex flex-row items-center h-full relative');

          const img = document.createElement('img');
          img.setAttribute('class', 'w-14 h-14 sm:w-20 sm:h-20 ml-8 mr-4 my-2');
          img.setAttribute('src', `/api/images/${object?.photo}`);

          objectLine.appendChild(img);

          const contentObject = document.createElement('div');
          contentObject.setAttribute('class', 'flex flex-col justify-between');
          objectLine.appendChild(contentObject);

          const typeName = document.createElement('div');

          const typeObject = document.createElement('p');
          typeObject.setAttribute('class', 'font-bold text-orange-400');
          typeObject.textContent = object.fkObjectType.typeName;
          typeName.appendChild(typeObject);

          const offeringMember = document.createElement('p');
          offeringMember.setAttribute('class', 'text-gray-400');

          if (object.fkOfferingMember.email) {
            offeringMember.textContent = `${object.fkOfferingMember.lastname} ${object.fkOfferingMember.firstname}`;
          } else {
            offeringMember.textContent = `${object.unknownUserPhoneNumber}`;
          }

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

          link.appendChild(objectLine);

          listObjects.appendChild(link);
        });
      }

      info.appendChild(listObjects);

      secondaryDiv.appendChild(info);
      mainDiv.appendChild(secondaryDiv);
    } else {
      Navigate('/');
    }
  } else {
    Navigate('/');
  }
};

export default ProfileIndexPage;
