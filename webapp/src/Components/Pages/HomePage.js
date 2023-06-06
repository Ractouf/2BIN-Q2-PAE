import clearPage from '../../utils/render';
import displayErrorMessage from '../../utils/error';

const getStatusLabel = (status) => {
  switch (status) {
    case 'SHOP':
      return 'En magasin';
    case 'ON_SALE':
      return 'En vente';
    case 'SOLD':
      return 'Vendu';
    default:
      return 'Inconnu';
  }
};

const HomePage = async () => {
  clearPage();

  window.addEventListener('resize', getAmountItemsByScreenWidth);
  const main = document.querySelector('main');

  const listObjects = [];

  await getHomeObjects();
  let filteredList = [...listObjects];

  const carousel = document.createElement('div');
  carousel.classList.add(
    'flex',
    'justify-center',
    'items-center',
    'gap-2',
    'bg-gradient-green-large',
    'py-10',
  );

  main.appendChild(carousel);

  const propose = document.createElement('div');
  propose.classList.add(
    'flex',
    'flex-col',
    'gap-2',
    'sm:flex-row',
    'sm:gap-0',
    'justify-between',
    'py-4',
    'px-10',
    'bg-gradient-gris',
  );

  const filters = document.createElement('div');
  filters.classList.add('flex', 'gap-2');

  const filterStatusForm = document.createElement('form');

  const filterStatusSelect = document.createElement('select');
  filterStatusSelect.classList.add('font-bold', 'text-lg', 'text-orange-500', 'p-2');

  const filterStatusOptionDefault = document.createElement('option');
  filterStatusOptionDefault.setAttribute('value', 'filtrer');
  filterStatusOptionDefault.setAttribute('selected', true);
  filterStatusOptionDefault.textContent = 'État';
  filterStatusSelect.appendChild(filterStatusOptionDefault);

  const filterStatusOptionShop = document.createElement('option');
  filterStatusOptionShop.setAttribute('value', 'SHOP');
  filterStatusOptionShop.textContent = 'En magasin';
  filterStatusSelect.appendChild(filterStatusOptionShop);

  const filterStatusOptionOnSale = document.createElement('option');
  filterStatusOptionOnSale.setAttribute('value', 'ON_SALE');
  filterStatusOptionOnSale.textContent = 'En vente';
  filterStatusSelect.appendChild(filterStatusOptionOnSale);

  const filterStatusOptionSold = document.createElement('option');
  filterStatusOptionSold.setAttribute('value', 'SOLD');
  filterStatusOptionSold.textContent = 'Vendu';
  filterStatusSelect.appendChild(filterStatusOptionSold);

  filterStatusForm.appendChild(filterStatusSelect);
  filters.appendChild(filterStatusForm);

  const filterTypeForm = document.createElement('form');

  const filterTypeSelect = document.createElement('select');
  filterTypeSelect.classList.add('font-bold', 'text-lg', 'text-orange-500', 'p-2');

  const filterTypeOptionDefault = document.createElement('option');
  filterTypeOptionDefault.setAttribute('value', 'filtrer');
  filterTypeOptionDefault.setAttribute('selected', true);
  filterTypeOptionDefault.textContent = 'Type';
  filterTypeSelect.appendChild(filterTypeOptionDefault);

  const types = await getTypes();

  types.forEach((type) => {
    const filterTypeOption = document.createElement('option');
    filterTypeOption.setAttribute('value', `${type.typeName}`);
    filterTypeOption.textContent = `${type.typeName}`;
    filterTypeSelect.appendChild(filterTypeOption);
  });

  filterTypeForm.appendChild(filterTypeSelect);
  filters.appendChild(filterTypeForm);

  filterStatusSelect.addEventListener('change', () => {
    const selectedStatus = filterStatusSelect.value;
    const selectedType = filterTypeSelect.value;

    if (selectedStatus === 'filtrer' && selectedType === 'filtrer') {
      filteredList = listObjects;
    } else if (selectedStatus !== 'filtrer' && selectedType === 'filtrer') {
      filteredList = listObjects.filter((obj) => obj.status === selectedStatus);
    } else if (selectedStatus === 'filtrer' && selectedType !== 'filtrer') {
      filteredList = listObjects.filter((obj) => obj.fkObjectType.typeName === selectedType);
    } else {
      filteredList = listObjects.filter(
        (obj) => obj.status === selectedStatus && obj.fkObjectType.typeName === selectedType,
      );
    }

    displayImages();
  });

  filterTypeSelect.addEventListener('change', () => {
    const selectedStatus = filterStatusSelect.value;
    const selectedType = filterTypeSelect.value;

    if (selectedStatus === 'filtrer' && selectedType === 'filtrer') {
      filteredList = listObjects;
    } else if (selectedStatus === 'filtrer' && selectedType !== 'filtrer') {
      filteredList = listObjects.filter((obj) => obj.fkObjectType.typeName === selectedType);
    } else if (selectedStatus !== 'filtrer' && selectedType === 'filtrer') {
      filteredList = listObjects.filter((obj) => obj.status === selectedStatus);
    } else {
      filteredList = listObjects.filter(
        (obj) => obj.status === selectedStatus && obj.fkObjectType.typeName === selectedType,
      );
    }

    displayImages();
  });

  propose.appendChild(filters);

  const titlePropose = document.createElement('a');
  titlePropose.setAttribute('href', '/object/propose');
  titlePropose.classList.add('font-bold', 'text-2xl', 'text-orange-500');
  titlePropose.textContent = '+ Proposer un objet';
  propose.appendChild(titlePropose);

  main.appendChild(propose);

  const location = document.createElement('div');
  location.classList.add(
    'flex',
    'flex-col',
    'md:flex-row',
    'justify-center',
    'items-center',
    'gap-16',
    'lg:gap-72',
    'py-10',
  );

  const locationInfo = document.createElement('div');

  const titleLocationInfo = document.createElement('h2');
  titleLocationInfo.classList.add('font-bold', 'text-2xl', 'text-gray-500', 'mb-2');
  titleLocationInfo.textContent = 'Adresse -';

  const ressourceRie = document.createElement('span');
  ressourceRie.classList.add('font-riez');
  ressourceRie.textContent = ' RessourceRie';

  const subtitleLocationInfo = document.createElement('h4');
  subtitleLocationInfo.classList.add('font-bold', 'text-lg', 'text-orange-500');
  subtitleLocationInfo.textContent = 'Magasin';

  const locationAdress = document.createElement('p');
  locationAdress.classList.add('font-bold', 'text-lg', 'text-swamp-green', 'mb-6');
  locationAdress.textContent = 'Rue du Heuseux 77ter, 4671, Blégny';

  const infoLocation = document.createElement('p');
  infoLocation.classList.add('font-bold', 'text-md', 'text-orange-500');
  infoLocation.textContent = 'remise des objets au parc à conteneur';

  titleLocationInfo.appendChild(ressourceRie);
  locationInfo.appendChild(titleLocationInfo);
  locationInfo.appendChild(subtitleLocationInfo);
  locationInfo.appendChild(locationAdress);
  locationInfo.appendChild(infoLocation);

  location.appendChild(locationInfo);

  const mapDiv = document.createElement('img');
  mapDiv.setAttribute(
    'src',
    'https://cdn.discordapp.com/attachments/814147584054657026/1099687377783500892/map.png',
  );
  mapDiv.style.maxWidth = '24rem';
  mapDiv.style.maxHeight = '24rem';

  location.appendChild(mapDiv);

  main.appendChild(location);

  let firstImage = 0;
  let items = 4;
  getAmountItemsByScreenWidth();
  displayImages();
  function displayImages() {
    carousel.innerHTML = '';

    if (filteredList.length === 0) {
      const noObjects = document.createElement('h2');
      noObjects.classList.add('text-2xl', 'text-orange-500', 'font-bold');
      noObjects.textContent = 'Aucun objet disponible';

      carousel.appendChild(noObjects);
    } else {
      const arrowBack = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
      arrowBack.setAttribute('width', '50');
      arrowBack.setAttribute('height', '50');

      const pathBack = document.createElementNS('http://www.w3.org/2000/svg', 'path');
      pathBack.setAttribute('d', 'M30 15 L10 25 L30 35');
      pathBack.setAttribute('stroke', '#000');
      pathBack.setAttribute('stroke-width', '4');
      pathBack.setAttribute('fill', 'none');

      arrowBack.appendChild(pathBack);

      arrowBack.addEventListener('click', () => {
        if (filteredList[firstImage - 1] === undefined) {
          return;
        }

        firstImage -= 1;

        displayImages();
      });

      carousel.appendChild(arrowBack);

      const currentObjects = [];
      for (let i = 0; i < items; i += 1) {
        const item = filteredList[firstImage + i];
        if (item) {
          currentObjects.push(item);
        }
      }
      currentObjects.filter((obj) => obj !== undefined);

      currentObjects.forEach((item, index) => {
        const itemNode = document.createElement('a');
        itemNode.classList.add('flex', 'flex-col', 'bg-noise-base');

        const img = document.createElement('img');
        img.classList.add('aspect-square');
        img.style.maxWidth = '18rem';
        img.style.maxHeight = '18rem';
        itemNode.appendChild(img);

        const itemInfo = document.createElement('div');
        itemInfo.classList.add('flex', 'flex-row', 'justify-between', 'p-2');

        const type = document.createElement('p');
        type.classList.add('font-bold', 'text-orange-500');

        const status = document.createElement('p');
        status.classList.add('font-bold', 'text-orange-500');

        const price = document.createElement('p');
        price.classList.add('font-bold', 'text-orange-500');

        itemInfo.appendChild(type);
        itemInfo.appendChild(status);
        itemInfo.appendChild(price);
        itemNode.appendChild(itemInfo);

        carousel.appendChild(itemNode);

        carousel.childNodes[index + 1].childNodes[0].setAttribute(
          'src',
          `/api/images/${currentObjects[index].photo}`,
        );

        carousel.childNodes[
          index + 1
        ].childNodes[1].childNodes[0].textContent = `${currentObjects[index].fkObjectType.typeName}`;

        let sellingPrice = `${currentObjects[index].sellingPrice} €`;

        if (sellingPrice === '0 €') {
          sellingPrice = '';
        }

        carousel.childNodes[index + 1].childNodes[1].childNodes[1].textContent = `${getStatusLabel(
          currentObjects[index].status,
        )}`;

        carousel.childNodes[index + 1].childNodes[1].childNodes[2].textContent = `${sellingPrice}`;

        carousel.childNodes[index + 1].setAttribute(
          'href',
          `/object?id=${currentObjects[index].id}`,
        );
      });

      const arrowGo = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
      arrowGo.setAttribute('width', '50');
      arrowGo.setAttribute('height', '50');

      const pathGo = document.createElementNS('http://www.w3.org/2000/svg', 'path');
      pathGo.setAttribute('d', 'M10 15 L30 25 L10 35');
      pathGo.setAttribute('stroke', '#000');
      pathGo.setAttribute('stroke-width', '4');
      pathGo.setAttribute('fill', 'none');

      arrowGo.appendChild(pathGo);

      arrowGo.addEventListener('click', () => {
        if (filteredList[firstImage + items] === undefined) {
          return;
        }

        firstImage += 1;

        displayImages();
      });

      carousel.appendChild(arrowGo);
    }
  }

  function getAmountItemsByScreenWidth() {
    const w = window.outerWidth;

    if (w > 1400) {
      items = 4;
    } else if (w > 1100) {
      items = 3;
    } else if (w > 750) {
      items = 2;
    } else {
      items = 1;
    }
    displayImages();
  }

  async function getHomeObjects() {
    const request = await fetch('/api/objects/home', {
      method: 'GET',
    });
    const response = await request.json();
    if (!request.ok) {
      displayErrorMessage(response.message, main);
    } else {
      response.forEach((object) => {
        listObjects.push(object);
      });
    }
  }

  async function getTypes() {
    const request = await fetch(`/api/objectTypes`, {
      method: 'GET',
    });

    const result = await request.json();
    if (!request.ok) {
      displayErrorMessage(result.message, main);
      return null;
    }
    return result;
  }
};

export default HomePage;
