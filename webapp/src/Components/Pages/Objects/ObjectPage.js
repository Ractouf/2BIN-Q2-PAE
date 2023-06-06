import clearPage from '../../../utils/render';
import displayErrorMessage from '../../../utils/error';

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

const ObjectPage = async () => {
  clearPage();

  const main = document.querySelector('main');

  const id = new URLSearchParams(window.location.search).get('id');
  const object = await getObject(id);

  const mainDiv = document.createElement('div');
  mainDiv.classList.add(
    'min-h-[calc(100vh_-_120px)]',
    'bg-gradient-green-large',
    'flex',
    'justify-center',
  );
  main.appendChild(mainDiv);

  const secondDiv = document.createElement('div');
  secondDiv.classList.add(
    'bg-noise-base',
    'flex',
    'flex-col',
    'pt-10',
    'px-10',
    'md:px-24',
    'lg:px-52',
  );
  mainDiv.appendChild(secondDiv);

  const row1 = document.createElement('div');
  row1.classList.add('flex', 'flex-col', 'md:flex-row', 'gap-10', 'md:gap-28');
  secondDiv.appendChild(row1);

  const img = document.createElement('img');
  img.setAttribute('src', `/api/images/${object.photo}`);
  img.classList.add('border');
  img.style.maxWidth = '24rem';
  img.style.maxHeight = '24rem';
  row1.appendChild(img);

  const fields = document.createElement('div');
  fields.classList.add('flex', 'flex-col', 'gap-2');
  row1.appendChild(fields);

  const type = document.createElement('p');
  type.classList.add('text-swamp-green', 'text-2xl', 'font-bold');
  type.textContent = `${object.fkObjectType.typeName}`;
  fields.appendChild(type);

  if (object.status === 'ON_SALE' || object.status === 'SOLD') {
    const price = document.createElement('p');
    price.classList.add('text-orange-400', 'text-6xl', 'font-bold');
    price.textContent = `${object.sellingPrice} €`;

    fields.appendChild(price);
  }

  const objectStatus = document.createElement('p');
  objectStatus.setAttribute('class', 'font-bold text-swamp-green text-xl');
  objectStatus.textContent = `${getStatusLabel(object.status)}`;

  fields.appendChild(objectStatus);

  const description = document.createElement('div');
  description.classList.add('mt-10', 'mb-6');

  const descriptionTitle = document.createElement('h2');
  descriptionTitle.classList.add('mb-4');
  descriptionTitle.classList.add('text-swamp-green', 'text-2xl', 'font-bold');
  descriptionTitle.textContent = 'Description';
  description.appendChild(descriptionTitle);

  const descriptionTextArea = document.createElement('textarea');
  descriptionTextArea.setAttribute('readonly', 'true');
  descriptionTextArea.classList.add('resize-none', 'border', 'p-3', 'w-full');
  descriptionTextArea.value = object.description;
  description.appendChild(descriptionTextArea);

  secondDiv.appendChild(description);
};

async function getObject(id) {
  const main = document.querySelector('main');

  const request = await fetch(`/api/objects/${id}`, {
    method: 'GET',
  });

  const result = await request.json();
  if (!request.ok) {
    displayErrorMessage(result.message, main);
    return null;
  }
  return result;
}

export default ObjectPage;
