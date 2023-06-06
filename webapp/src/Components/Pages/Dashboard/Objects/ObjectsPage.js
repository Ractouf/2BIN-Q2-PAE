import clearPage from '../../../../utils/render';
import displayErrorMessage from '../../../../utils/error';
import DashboardNavbar from '../Navbar/DashboardNavbar';
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

const displayObjects = (objects, listObjects) => {
  objects.forEach((object, index) => {
    const link = document.createElement('a');

    const isEven = index % 4 === 1 || index % 4 === 2;
    link.classList.add(isEven ? 'bg-gradient-gris' : 'bg-noise-base');

    link.addEventListener('click', () => {
      Navigate(`/dashboard/objects/edit?id=${object.id}`);
    });

    const objectLine = document.createElement('li');
    objectLine.setAttribute('class', 'flex flex-row items-center cursor-pointer');

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
      offeringMember.textContent = `${object.fkOfferingMember.lastname} 
      ${object.fkOfferingMember.firstname}`;
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
};

const ObjectsPage = async () => {
  clearPage();

  if (isAuthenticated()) {
    if (isHelper() || isManager()) {
      const main = document.querySelector('main');
      const mainDiv = document.createElement('div');
      mainDiv.classList.add('flex', 'flex-col', 'lg:flex-row', 'min-h-[calc(100vh_-_120px)]');
      mainDiv.appendChild(DashboardNavbar());
      main.appendChild(mainDiv);

      const content = document.createElement('section');
      content.classList.add('flex', 'flex-col', 'flex-grow');

      const listObjects = document.createElement('ul');
      listObjects.setAttribute('class', 'grid grid-cols-2 flex-grow');

      const searchContainer = document.createElement('form');
      searchContainer.classList.add(
        'flex',
        'flex-col',
        'sm:flex-row',
        'justify-center',
        'w-fit',
        'mx-auto',
        'my-4',
        'gap-4',
      );

      const searchFilter1 = document.createElement('div');
      searchFilter1.classList.add('flex', 'flex-row');

      const typeSelect = document.createElement('div');
      typeSelect.classList.add('dropdown', 'w-min', 'mx-2');

      const typeSelectBtn = document.createElement('button');
      typeSelectBtn.setAttribute('class', 'drop-btn-type bg-white p-2 rounded-md');
      typeSelectBtn.textContent = 'Types';

      typeSelect.appendChild(typeSelectBtn);

      const typeSelectMenu = document.createElement('div');
      typeSelectMenu.setAttribute(
        'class',
        'bg-stone-200 dropdown-type absolute flex flex-col gap-2 transition-all z-10 hidden',
      );

      const requestTypes = await fetch('/api/objectTypes', {
        method: 'GET',
        headers: {
          Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
        },
      });

      const types = await requestTypes.json();
      if (!requestTypes.ok) {
        displayErrorMessage(types.message, main);
      } else {
        types.forEach((type) => {
          const typeContainer = document.createElement('article');
          typeContainer.setAttribute('class', 'flex flex-row items-center mx-4 gap-2');

          const typeText = document.createElement('span');
          typeText.textContent = type.typeName;

          const typeCheckBox = document.createElement('input');
          typeCheckBox.setAttribute('type', 'checkbox');
          typeCheckBox.setAttribute('class', 'type');
          typeCheckBox.setAttribute('id', type.typeName.toLocaleLowerCase());

          typeContainer.appendChild(typeCheckBox);
          typeContainer.appendChild(typeText);

          typeSelectMenu.appendChild(typeContainer);
        });
      }

      typeSelect.appendChild(typeSelectMenu);

      document.querySelectorAll('.dropdown-type').forEach((dropDownContent) => {
        dropDownContent.classList.add('hidden');
      });

      typeSelectBtn.addEventListener('click', () => {
        typeSelectMenu.classList.toggle('hidden');
      });

      document.addEventListener('click', (event) => {
        if (!typeSelect.contains(event.target)) {
          typeSelectMenu.classList.add('hidden');
        }
      });

      const statusSelect = document.createElement('div');
      statusSelect.classList.add('dropdown', 'w-min', 'mx-2');

      const statusSelectBtn = document.createElement('button');
      statusSelectBtn.setAttribute('class', 'drop-btn-status bg-white p-2 rounded-md');
      statusSelectBtn.textContent = 'Statuts';

      statusSelect.appendChild(statusSelectBtn);

      const statusSelectMenu = document.createElement('div');
      statusSelectMenu.setAttribute(
        'class',
        'bg-stone-200 dropdown-status absolute flex flex-col gap-2 transition-all z-10 hidden',
      );

      const statuses = [
        'PROPOSED',
        'ACCEPTED',
        'REFUSED',
        'WORKSHOP',
        'SHOP',
        'ON_SALE',
        'SOLD',
        'REMOVED',
      ];

      statuses.forEach((status) => {
        const statusContainer = document.createElement('article');
        statusContainer.setAttribute('class', 'flex flex-row items-center mx-4 gap-2');

        const statusText = document.createElement('span');
        statusText.textContent = getStatusLabel(status);

        const statusCheckBox = document.createElement('input');
        statusCheckBox.setAttribute('type', 'checkbox');
        statusCheckBox.setAttribute('class', 'status');
        statusCheckBox.setAttribute('id', status.toLocaleLowerCase());

        statusContainer.appendChild(statusCheckBox);
        statusContainer.appendChild(statusText);

        statusSelectMenu.appendChild(statusContainer);
      });

      statusSelect.appendChild(statusSelectMenu);

      document.querySelectorAll('.dropdown-status').forEach((dropDownContent) => {
        dropDownContent.classList.add('hidden');
      });

      statusSelectBtn.addEventListener('click', () => {
        statusSelectMenu.classList.toggle('hidden');
      });

      document.addEventListener('click', (event) => {
        if (!statusSelect.contains(event.target)) {
          statusSelectMenu.classList.add('hidden');
        }
      });

      const dateInput1 = document.createElement('input');
      dateInput1.setAttribute('type', 'date');
      dateInput1.setAttribute('id', 'date-input');
      dateInput1.setAttribute('class', 'bg-white p-2 rounded-md');

      const span1 = document.createElement('span');
      span1.textContent = 'entre';
      span1.classList.add('text-gray-400', 'my-2', 'mx-2');

      const dateInput2 = document.createElement('input');
      dateInput2.setAttribute('type', 'date');
      dateInput2.setAttribute('id', 'date-input');
      dateInput2.setAttribute('placeholder', 'Date');
      dateInput2.setAttribute('class', 'bg-white p-2 rounded-md');

      const searchFilter2 = document.createElement('div');
      searchFilter2.classList.add('flex', 'flex-row', 'justify-center');

      const priceInput1 = document.createElement('input');
      priceInput1.setAttribute('type', 'number');
      priceInput1.setAttribute('step', '0.01');
      priceInput1.setAttribute('id', 'price-input');
      priceInput1.setAttribute('placeholder', 'Prix min');
      priceInput1.setAttribute('class', 'bg-white p-2 rounded-md');
      priceInput1.classList.add('w-20');
      priceInput1.setAttribute('min', '0.00');
      priceInput1.setAttribute('max', '10.00');

      const span2 = document.createElement('span');
      span2.textContent = 'et';
      span2.classList.add('text-gray-400', 'my-2', 'mx-2');

      const priceInput2 = document.createElement('input');
      priceInput2.setAttribute('type', 'number');
      priceInput2.setAttribute('step', '0.01');
      priceInput2.setAttribute('id', 'price-input');
      priceInput2.setAttribute('placeholder', 'Prix max');
      priceInput2.setAttribute('class', 'bg-white p-2 rounded-md');
      priceInput2.classList.add('w-20');
      priceInput2.setAttribute('min', '0.00');
      priceInput2.setAttribute('max', '10.00');

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

      searchFilter1.appendChild(typeSelect);
      searchFilter1.appendChild(statusSelect);
      searchFilter1.appendChild(dateInput1);
      searchFilter1.appendChild(span1);
      searchFilter1.appendChild(dateInput2);
      searchContainer.appendChild(searchFilter1);
      searchFilter2.appendChild(priceInput1);
      searchFilter2.appendChild(span2);
      searchFilter2.appendChild(priceInput2);
      searchFilter2.appendChild(searchBtn);
      searchContainer.appendChild(searchFilter2);

      content.appendChild(searchContainer);
      content.appendChild(listObjects);

      mainDiv.appendChild(content);

      const request = await fetch('/api/objects', {
        method: 'GET',
        headers: {
          Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
        },
      });

      const response = await request.json();
      if (!request.ok) {
        displayErrorMessage(response.message, main);
      } else {
        listObjects.innerHTML = '';
        displayObjects(response, listObjects);
      }

      searchContainer.addEventListener('submit', (e) => {
        e.preventDefault();

        if (e.submitter === typeSelectBtn) {
          return;
        }

        const typesSelected = [];

        document.querySelectorAll('.type').forEach((type) => {
          if (type.checked) {
            typesSelected.push(type.id);
          }
        });

        const statusSelected = [];

        document.querySelectorAll('.status').forEach((status) => {
          if (status.checked) {
            statusSelected.push(status.id);
          }
        });

        let objetsToDisplay = response;

        if (typesSelected.length !== 0) {
          objetsToDisplay = objetsToDisplay.filter((object) =>
            typesSelected.includes(object.fkObjectType.typeName.toLocaleLowerCase()),
          );
        }

        if (statusSelected.length !== 0) {
          objetsToDisplay = objetsToDisplay.filter((object) =>
            statusSelected.includes(object.status.toLocaleLowerCase()),
          );
        }

        const priceMin = priceInput1.value;
        const priceMax = priceInput2.value;

        if (priceMin !== '' && priceMax === '') {
          displayErrorMessage('Veuillez renseigner un prix maximum', main);
        }

        if (priceMin === '' && priceMax !== '') {
          displayErrorMessage('Veuillez renseigner un prix minimum', main);
        }

        if (priceMin !== '' && priceMax !== '') {
          if (priceMin >= 0 && priceMax <= 10) {
            if (priceMin < priceMax) {
              objetsToDisplay = objetsToDisplay.filter(
                (object) =>
                  parseFloat(object.sellingPrice) >= priceMin &&
                  parseFloat(object.sellingPrice) <= priceMax,
              );
            } else {
              displayErrorMessage('Le prix maximum doit être supérieur au prix minimum', main);
            }
          }
        } else if ((priceMin !== '' && priceMax === '') || (priceMin === '' && priceMax !== '')) {
          displayErrorMessage('Veuillez renseigner un prix minimum ET un prix maximum', main);
        }

        const date1 = dateInput1.value;
        const date2 = dateInput2.value;
        if (date1 !== '') {
          const dateMin = new Date(date1);
          let dateMax;
          if (date2 === '') {
            dateMax = new Date();
          } else {
            dateMax = new Date(date2);
          }

          dateMin.setHours(0, 0, 0, 0);
          dateMax.setHours(0, 0, 0, 0);

          if (dateMin < dateMax) {
            objetsToDisplay = objetsToDisplay.filter((object) => {
              const proposalDate = new Date(object.proposalDate);
              const interestConfirmationDate = new Date(object.interestConfirmationDate);
              const storeDepositDate = new Date(object.storeDepositDate);
              const marketWithdrawalDate = new Date(object.marketWithdrawalDate);
              const sellingDate = new Date(object.sellingDate);

              proposalDate.setHours(0, 0, 0, 0);
              interestConfirmationDate.setHours(0, 0, 0, 0);
              storeDepositDate.setHours(0, 0, 0, 0);
              marketWithdrawalDate.setHours(0, 0, 0, 0);
              sellingDate.setHours(0, 0, 0, 0);

              return (
                (proposalDate >= dateMin && proposalDate <= dateMax) ||
                (interestConfirmationDate >= dateMin && interestConfirmationDate <= dateMax) ||
                (storeDepositDate >= dateMin && storeDepositDate <= dateMax) ||
                (marketWithdrawalDate >= dateMin && marketWithdrawalDate <= dateMax) ||
                (sellingDate >= dateMin && sellingDate <= dateMax)
              );
            });
          } else {
            displayErrorMessage('La date maximum doit être supérieur à la date minimum', main);
          }
        }

        listObjects.innerHTML = '';
        displayObjects(objetsToDisplay, listObjects);
      });
    } else {
      Navigate('/');
    }
  } else {
    Navigate('/');
  }
};

export default ObjectsPage;
