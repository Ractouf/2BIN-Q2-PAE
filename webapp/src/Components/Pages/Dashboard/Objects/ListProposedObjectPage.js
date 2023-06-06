import clearPage from '../../../../utils/render';
import Navigate from '../../../Router/Navigate';
import displayErrorMessage from '../../../../utils/error';
import DashboardNavbar from '../Navbar/DashboardNavbar';
import { isAuthenticated, isManager } from '../../../../utils/auths';

let currentObject = null;
const ListProposedObjectPage = async () => {
  clearPage();

  if (isAuthenticated() && isManager()) {
    const main = document.querySelector('main');

    const mainDiv = document.createElement('div');
    mainDiv.classList.add('flex', 'flex-col', 'lg:flex-row', 'min-h-[calc(100vh_-_120px)]');
    mainDiv.appendChild(DashboardNavbar());
    main.appendChild(mainDiv);

    const secondDiv = document.createElement('div');
    secondDiv.classList.add('mt-8', 'mx-auto');
    mainDiv.appendChild(secondDiv);

    const title = document.createElement('h2');
    title.classList.add('my-5', 'text-2xl', 'text-swamp-green', 'text-center', 'md:text-left');
    title.innerText = 'Proposition';
    secondDiv.appendChild(title);

    const form = document.createElement('div');
    form.classList.add('flex', 'flex-col', 'md:flex-row');
    secondDiv.appendChild(form);

    const title2 = document.createElement('h2');
    title2.classList.add('my-5', 'text-2xl', 'text-swamp-green', 'text-center', 'md:text-left');
    title2.innerText = 'Proposés';
    secondDiv.appendChild(title2);

    const listProposed = document.createElement('ul');
    listProposed.setAttribute('class', 'grid grid-cols-2 flex-grow py-2');

    const request = await fetch('/api/objects/proposed', {
      method: 'GET',
      headers: {
        Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
      },
    });

    const response = await request.json();
    if (!request.ok) {
      displayErrorMessage(response.message, main);
    } else {
      if (currentObject == null) {
        [currentObject] = response;
      }
      response.forEach((object, index) => {
        const link = document.createElement('a');

        const isEven = index % 4 === 1 || index % 4 === 2;
        if (object.id === currentObject.id) {
          link.classList.add('bg-swamp-green');
        } else {
          link.classList.add(isEven ? 'bg-gradient-gris' : 'bg-noise-base');
        }

        if (object !== currentObject) {
          link.addEventListener('click', () => {
            currentObject = object;
            Navigate(`/dashboard/objects/proposed`);
          });
        }

        const proposedObject = document.createElement('li');
        proposedObject.setAttribute('class', 'flex flex-row items-center h-full relative');

        const img = document.createElement('img');
        img.setAttribute('class', 'w-14 h-14 sm:w-20 sm:h-20 ml-8 mr-4 my-2');
        img.setAttribute('src', `http://localhost:3000/images/${object?.photo}`);

        proposedObject.appendChild(img);

        const contentObject = document.createElement('div');
        contentObject.setAttribute('class', 'flex flex-col justify-between');
        proposedObject.appendChild(contentObject);

        const typeName = document.createElement('div');

        const typeObject = document.createElement('p');
        typeObject.setAttribute('class', 'font-bold text-orange-400');
        typeObject.textContent = object.fkObjectType.typeName;
        typeName.appendChild(typeObject);

        const offeringMember = document.createElement('p');
        offeringMember.setAttribute('class', 'text-gray-400');

        if (object.fkOfferingMember.id === 0) {
          offeringMember.textContent = object.unknownUserPhoneNumber;
        } else {
          offeringMember.textContent = `${object.fkOfferingMember.lastname} ${object.fkOfferingMember.firstname}`;
        }

        typeName.appendChild(offeringMember);

        const buttonAccept = document.createElement('button');
        buttonAccept.textContent = 'Accepter';
        buttonAccept.classList.add('bg-swamp-green', 'text-white', 'cursor-pointer', 'px-2');
        buttonAccept.addEventListener('click', async () => {
          const request1 = await fetch(`/api/objects/${object.id}/accept`, {
            method: 'PATCH',
            headers: {
              Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
            },
          });
          if (!request1.ok) {
            displayErrorMessage(`Error while accepting proposition of object ${object.id}`, main);
          } else {
            currentObject = null;
            Navigate('/dashboard/objects/proposed');
          }
        });
        typeName.appendChild(buttonAccept);

        contentObject.appendChild(typeName);
        link.appendChild(proposedObject);
        listProposed.appendChild(link);
      });
    }
    const currentImg = document.createElement('img');
    currentImg.setAttribute('src', `/api/images/${currentObject?.photo}`);
    currentImg.setAttribute('alt', 'aucun objet proposé pour le moment');
    currentImg.classList.add(
      'w-96',
      'h-96',
      'm-auto',
      'text-center',
      'bg-white',
      'text-gray-500',
      'font-bold',
      'relative',
    );
    form.appendChild(currentImg);

    const currentSide = document.createElement('div');
    currentSide.classList.add('flex', 'flex-col', 'px-14', 'sm:px-20', 'md:p-4');
    form.appendChild(currentSide);

    const formInfo = document.createElement('div');
    formInfo.classList.add('flex', 'flex-col');
    currentSide.appendChild(formInfo);

    const nameSeller = document.createElement('p');
    nameSeller.classList.add('text-lg', 'font-bold', 'text-swamp-green', 'mb-0', 'md:mb-4');
    if (currentObject.fkOfferingMember.id === 0) {
      nameSeller.textContent = `Proposant: ${currentObject.unknownUserPhoneNumber}`;
    } else {
      nameSeller.innerText = `Proposant: ${currentObject.fkOfferingMember.lastname} ${currentObject.fkOfferingMember.firstname}`;
    }
    formInfo.appendChild(nameSeller);

    const objectTypeLabel = document.createElement('p');
    objectTypeLabel.classList.add('text-lg', 'font-bold', 'text-swamp-green', 'mb-0', 'md:mb-4');
    objectTypeLabel.innerText = `Type: ${currentObject.fkObjectType.typeName}`;
    formInfo.appendChild(objectTypeLabel);

    const objectDescription = document.createElement('p');
    objectDescription.classList.add('text-lg', 'font-bold', 'text-gray-400', 'mb-0', 'md:mb-4');
    objectDescription.innerText = `${currentObject.description}`;
    formInfo.appendChild(objectDescription);

    const acceptBtn = document.createElement('input');
    acceptBtn.classList.add(
      'my-1',
      'md:my-1.5',
      'md:px-4',
      'py-1',
      'bg-swamp-green',
      'text-white',
      'cursor-pointer',
    );
    acceptBtn.type = 'submit';
    acceptBtn.name = 'accept';
    acceptBtn.value = 'Accepter';
    acceptBtn.addEventListener('click', async () => {
      const requestAccept = await fetch(`/api/objects/${currentObject.id}/accept`, {
        method: 'PATCH',
        headers: {
          Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
        },
      });

      if (!requestAccept.ok) {
        displayErrorMessage(
          `Error while accepting proposition of object ${currentObject.id}`,
          main,
        );
      } else {
        currentObject = null;
        Navigate(`/dashboard/objects/proposed`);
      }
    });
    currentSide.appendChild(acceptBtn);

    const formRefuse = document.createElement('form');
    currentSide.appendChild(formRefuse);

    const refuseBtn = document.createElement('input');
    refuseBtn.classList.add(
      'md:my-2',
      'md:px-4',
      'py-1',
      'bg-rose-900',
      'text-white',
      'cursor-pointer',
      'w-full',
    );
    refuseBtn.type = 'submit';
    refuseBtn.name = 'refuse';
    refuseBtn.value = 'Refuser';
    formRefuse.appendChild(refuseBtn);

    const formInput = document.createElement('textarea');
    formInput.placeholder = 'raison de refus';
    formInput.classList.add('max-h-36', 'px-2', 'mt-1.5');
    currentSide.appendChild(formInput);

    formRefuse.addEventListener('submit', async (e) => {
      e.preventDefault();

      const refusalReason = formInput.value;

      const requestRefuse = await fetch(`/api/objects/${currentObject.id}/refuse`, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
        },
        body: JSON.stringify({
          refusalReason,
        }),
      });

      const responseRefuse = await requestRefuse.json();
      if (!requestRefuse.ok) {
        displayErrorMessage(responseRefuse.message, main);
      } else {
        currentObject = null;
        Navigate(`/dashboard/objects/proposed`);
      }
    });

    secondDiv.appendChild(listProposed);
  } else {
    Navigate('/');
  }
};

export default ListProposedObjectPage;
