import clearPage from '../../../utils/render';
import displayErrorMessage from '../../../utils/error';
import { getAuthenticatedUser, isAuthenticated } from '../../../utils/auths';
import Navigate from '../../Router/Navigate';

const ProposePage = async () => {
  clearPage();

  const main = document.querySelector('main');
  const mainDiv = document.createElement('div');
  mainDiv.classList.add('min-h-[calc(100vh_-_120px)]', 'bg-gradient-green-large');

  const secondaryDiv = document.createElement('div');
  secondaryDiv.classList.add(
    'bg-noise-base',
    'mx-4',
    'md:mx-24',
    'flex',
    'justify-center',
    'gap-10',
    'p-10',
  );

  const content = document.createElement('div');
  content.classList.add('flex', 'flex-col', 'items-center', 'flex-grow');

  const title = document.createElement('h1');
  title.textContent = 'Proposer un objet';
  title.classList.add('text-4xl', 'font-bold', 'text-swamp-green', 'mb-5');
  content.appendChild(title);

  const form = document.createElement('form');
  form.classList.add('flex', 'flex-col');

  const leftDiv = document.createElement('div');

  const imgDiv = document.createElement('div');
  imgDiv.classList.add('flex', 'flex-grow', 'justify-center');

  const imgLabel = document.createElement('label');
  imgLabel.classList.add(
    'h-72',
    'w-72',
    'md:h-96',
    'md:w-96',
    'cursor-pointer',
    'bg-contain',
    'bg-no-repeat',
    'text-orange-400',
    'font-bold',
    'relative',
  );
  imgLabel.style.backgroundImage = `url(/api/images/default-object.png)`;

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
        imgLabel.style.backgroundImage = `url(/api/images/default-object.png)`;
      }
    };
  });

  imgInput.addEventListener('change', () => {
    const { files } = imgInput;
    if (files.length === 0) {
      // check if no files were selected
      imgLabel.style.backgroundImage = `url(/api/images/default-object.png)`;
      return;
    }
    const reader = new FileReader();
    reader.onload = (e) => {
      if (imgInput.files[0].size > imgInput.dataset.maxsize) {
        imgLabel.style.backgroundImage = `url(/api/images/default-object.png)`;
        displayErrorMessage("La taille de l'image ne doit pas dépasser 5Mo", main);
        return;
      }
      imgLabel.style.backgroundImage = `url(${e.target.result})`;
    };
    reader.readAsDataURL(imgInput.files[0]);
  });

  imgLabel.appendChild(imgInput);

  const imgSpan = document.createElement('span');
  imgSpan.classList.add('absolute', 'bottom-0', 'right-0', 'text-4xl', 'px-2');
  imgSpan.textContent = '+';
  imgLabel.appendChild(imgSpan);
  imgDiv.appendChild(imgLabel);
  leftDiv.appendChild(imgDiv);

  const typeLabel = document.createElement('label');
  typeLabel.classList.add('flex', 'flex-col');

  const spanType = document.createElement('span');
  spanType.textContent = 'Type';
  spanType.classList.add('mt-6', 'mb-2', 'font-bold', 'text-swamp-green');
  typeLabel.appendChild(spanType);

  const selectElement = document.createElement('select');
  selectElement.name = 'type';
  selectElement.classList.add('border', 'p-2', 'bg-white');

  const types = await getTypes();
  types.forEach((type) => {
    const typeOption = document.createElement('option');

    typeOption.text = type.typeName;
    selectElement.add(typeOption);
  });

  typeLabel.appendChild(selectElement);
  leftDiv.appendChild(typeLabel);

  const descriptionLabel = document.createElement('label');
  descriptionLabel.classList.add('flex', 'flex-col');

  const descriptionSpan = document.createElement('span');
  descriptionSpan.textContent = 'Description';
  descriptionSpan.classList.add('my-2', 'font-bold', 'text-swamp-green');
  descriptionLabel.appendChild(descriptionSpan);

  const descriptionTextarea = document.createElement('textarea');
  descriptionTextarea.classList.add('resize-none', 'border', 'p-3');
  descriptionTextarea.name = 'description';
  descriptionTextarea.placeholder = 'Description...';
  descriptionTextarea.required = true;
  descriptionLabel.appendChild(descriptionTextarea);

  const responsiveForm = document.createElement('div');
  responsiveForm.classList.add('flex', 'flex-col', 'lg:flex-row', 'gap-4', 'lg:gap-10');
  responsiveForm.appendChild(leftDiv);

  let phoneInput;
  if (!isAuthenticated()) {
    const phoneLabel = document.createElement('label');
    phoneLabel.classList.add('my-2', 'flex', 'flex-col');

    phoneInput = document.createElement('input');
    phoneInput.classList.add('border', 'p-2');
    phoneInput.type = 'tel';
    phoneInput.placeholder = '0400/00.00.00';
    phoneInput.pattern = '[0-9]{4}/[0-9]{2}.[0-9]{2}.[0-9]{2}';
    phoneInput.required = true;

    const phoneText = document.createElement('p');
    phoneText.innerText = 'Numéro de téléphone';
    phoneText.classList.add('font-bold', 'text-swamp-green');

    const phoneSmall = document.createElement('small');
    phoneSmall.classList.add('mb-2');
    phoneSmall.textContent = 'Format: 0400/00.00.00';

    phoneLabel.appendChild(phoneText);
    phoneLabel.appendChild(phoneSmall);
    phoneLabel.appendChild(phoneInput);
    leftDiv.appendChild(phoneLabel);
  }

  leftDiv.appendChild(descriptionLabel);

  const availabilities = await getAvailabilities();

  const groupedObjects = {};
  availabilities.forEach((av) => {
    const dateKey = new Date(av.availabilityDate);
    const year = dateKey.getFullYear();
    const month = String(dateKey.getMonth() + 1).padStart(2, '0');
    const day = String(dateKey.getDate()).padStart(2, '0');
    const dateString = `${year}-${month}-${day}`;

    if (!groupedObjects[dateString]) {
      groupedObjects[dateString] = {
        morning: [],
        afternoon: [],
      };
    }
    if (av.startingHour < '12:00:00') {
      groupedObjects[dateString].morning.push(av.startingHour);
      groupedObjects[dateString].morning.push(av.endingHour);
    } else {
      groupedObjects[dateString].afternoon.push(av.startingHour);
      groupedObjects[dateString].afternoon.push(av.endingHour);
    }
  });

  const availabilitiesDiv = document.createElement('div');
  availabilitiesDiv.classList.add('grid', 'text-center', 'mt-10', 'sm:mt-0', 'lg:w-64');
  availabilitiesDiv.style.height = '717px';
  availabilitiesDiv.style.overflow = 'auto';

  const keys = Object.keys(groupedObjects);
  keys.forEach((key, index) => {
    const paddedIndex = String(index + 1).padStart(3, '0');
    const date = document.createElement('div');
    date.classList.add(
      'grid-item',
      'date',
      `date${paddedIndex}`,
      'flex',
      'items-center',
      'text-lg',
      'font-bold',
      'text-swamp-green',
      'px-2',
      'cursor-pointer',
    );
    date.style.gridRow = `${2 * index + 1} / span 2`;
    date.textContent = `${key}`;

    const hour1 = document.createElement('div');
    hour1.setAttribute(
      'class',
      `grid-item col-start-2 hour hourdate${paddedIndex} text-lg font-bold text-swamp-green p-1 cursor-pointer`,
    );

    const hour2 = hour1.cloneNode();

    const [morningHour1, morningHour2] = groupedObjects[key].morning;
    const [afternoonHour1, afternoonHour2] = groupedObjects[key].afternoon;

    availabilitiesDiv.appendChild(date);

    if (morningHour1) {
      hour1.textContent = `${morningHour1.substring(
        0,
        morningHour1.length - 3,
      )} - ${morningHour2.substring(0, morningHour2.length - 3)}`;
    }

    if (afternoonHour1) {
      hour2.textContent = `${afternoonHour1.substring(
        0,
        afternoonHour1.length - 3,
      )} - ${afternoonHour2.substring(0, afternoonHour2.length - 3)}`;
    }

    availabilitiesDiv.appendChild(hour1);
    availabilitiesDiv.appendChild(hour2);
  });

  let activeDate;
  let activeHour;

  availabilitiesDiv.addEventListener('click', (event) => {
    const clickedElement = event.target;

    if (clickedElement.classList.contains('date')) {
      resetClass('date');

      clickedElement.classList.add('bg-gray-200');
      activeDate = undefined;

      if (activeHour) {
        if (clickedElement.classList[2] !== activeHour.classList[2].substring(4, 11)) {
          resetClass('hour');
          activeHour = undefined;
        }
      }

      activeDate = clickedElement;
    } else if (clickedElement.classList.contains('hour')) {
      resetClass('hour');

      clickedElement.classList.add('bg-gray-200');
      activeHour = undefined;

      activeDate = document.querySelector(`.${clickedElement.classList[3].substring(4, 11)}`);
      resetClass('date');
      activeDate.classList.add('bg-gray-200');

      activeHour = clickedElement;
    }
  });

  function resetClass(className) {
    const elements = document.querySelectorAll(`.${className}`);
    elements.forEach((element) => element.classList.remove('bg-gray-200'));
  }

  responsiveForm.appendChild(availabilitiesDiv);
  form.appendChild(responsiveForm);

  const submitBtn = document.createElement('input');
  submitBtn.classList.add('my-2', 'px-4', 'py-1', 'bg-swamp-green', 'text-white', 'cursor-pointer');
  submitBtn.type = 'submit';
  submitBtn.value = 'Proposer Objet';

  form.appendChild(submitBtn);

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    if (activeDate === undefined || activeHour === undefined) {
      displayErrorMessage('Veuillez choisir une date et heure', main);
      return;
    }

    const image = imgInput.files[0];
    const type = selectElement.value;
    const description = descriptionTextarea.value;
    const availability = `${activeDate.textContent}-${activeHour.textContent.substring(
      0,
      activeHour.textContent.length - 8,
    )}`;

    const formData = new FormData();
    formData.append('type', type);
    formData.append('description', description);
    formData.append('availability', availability);
    if (isAuthenticated()) {
      formData.append('offering_member', getAuthenticatedUser().id);
    } else {
      formData.append('unknown_user_phone_number', phoneInput.value);
    }
    formData.append('file', image);

    const request = await fetch(`/api/objects`, {
      method: 'POST',
      body: formData,
    });

    const result = await request.json();
    if (!request.ok) {
      displayErrorMessage(result.message, main);
    } else if (isAuthenticated()) {
      Navigate('/profile');
    } else {
      Navigate('/');
    }
  });

  content.appendChild(form);

  secondaryDiv.appendChild(content);
  mainDiv.appendChild(secondaryDiv);
  main.appendChild(mainDiv);
};

async function getTypes() {
  const main = document.querySelector('main');

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

async function getAvailabilities() {
  const main = document.querySelector('main');

  const request = await fetch(`/api/availabilities`, {
    method: 'GET',
  });

  const result = await request.json();
  if (!request.ok) {
    displayErrorMessage(result.message, main);
    return null;
  }
  return result;
}

export default ProposePage;
