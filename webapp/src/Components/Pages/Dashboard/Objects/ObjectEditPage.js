import clearPage from '../../../../utils/render';
import displayErrorMessage from '../../../../utils/error';
import DashboardNavbar from '../Navbar/DashboardNavbar';
import { isAuthenticated, isHelper, isManager } from '../../../../utils/auths';
import Navigate from '../../../Router/Navigate';

const ObjectEditPage = async () => {
  clearPage();

  if (isAuthenticated()) {
    if (isHelper() || isManager()) {
      const id = new URLSearchParams(window.location.search).get('id');

      const object = await getObject(id);

      const main = document.querySelector('main');

      const mainDiv = document.createElement('div');
      mainDiv.classList.add('flex', 'flex-col', 'lg:flex-row', 'min-h-[calc(100vh_-_120px)]');
      mainDiv.appendChild(DashboardNavbar());
      main.appendChild(mainDiv);

      const secondDiv = document.createElement('div');
      secondDiv.classList.add('mt-8', 'mx-auto');
      mainDiv.appendChild(secondDiv);

      const title = document.createElement('h2');
      title.classList.add('my-5', 'text-2xl', 'text-swamp-green');
      title.innerText = 'Modification';
      secondDiv.appendChild(title);

      const form = document.createElement('form');
      form.classList.add('flex', 'flex-col');
      secondDiv.appendChild(form);

      const row1 = document.createElement('div');
      row1.classList.add('flex', 'flex-col', 'md:flex-row', 'gap-10');
      form.appendChild(row1);

      const imgLabel = document.createElement('label');
      imgLabel.classList.add(
        'h-72',
        'w-72',
        'sm:h-96',
        'sm:w-96',
        'cursor-pointer',
        'bg-contain',
        'bg-no-repeat',
        'border',
        'text-orange-400',
        'font-bold',
        'relative',
      );
      imgLabel.id = 'label';
      imgLabel.style.backgroundImage = `url(/api/images/${object?.photo})`;
      row1.appendChild(imgLabel);

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
            imgLabel.style.backgroundImage = `url(/api/images/${object?.photo})`;
          }
        };
      });

      imgInput.addEventListener('change', () => {
        const { files } = imgInput;
        if (files.length === 0) {
          // check if no files were selected
          imgLabel.style.backgroundImage = `url(/api/images/${object?.photo})`;
          return;
        }
        const reader = new FileReader();
        reader.onload = (e) => {
          if (imgInput.files[0].size > imgInput.dataset.maxsize) {
            imgLabel.style.backgroundImage = `url(/api/images/${object?.photo})`;
            displayErrorMessage("La taille de l'image ne doit pas dépasser 5Mo", main);
            return;
          }
          imgLabel.style.backgroundImage = `url(${e.target.result})`;
        };
        reader.readAsDataURL(imgInput.files[0]);
      });

      imgLabel.appendChild(imgInput);

      const imgSpan = document.createElement('span');
      imgSpan.classList.add('absolute', 'bottom-0', 'right-0', 'py-1', 'px-2');
      imgSpan.textContent = 'changer la photo';
      imgLabel.appendChild(imgSpan);

      const inputFields = document.createElement('div');
      inputFields.classList.add('flex', 'flex-col', 'gap-2');
      row1.appendChild(inputFields);

      if (object?.unknownUserPhoneNumber) {
        const phoneLabel = document.createElement('p');
        phoneLabel.classList.add('font-bold', 'text-swamp-green');
        phoneLabel.innerText = 'Téléphone proposant';
        const phone = document.createElement('p');
        phone.classList.add('mb-4');
        phone.innerText = `${object.unknownUserPhoneNumber}`;

        inputFields.appendChild(phoneLabel);
        inputFields.appendChild(phone);
      } else {
        const nameSellerLabel = document.createElement('p');
        nameSellerLabel.classList.add('font-bold', 'text-swamp-green');
        nameSellerLabel.innerText = 'Nom proposant';
        const nameSeller = document.createElement('p');
        nameSeller.classList.add('mb-4', 'cursor-pointer');
        nameSeller.innerText = `${object.fkOfferingMember.lastname} ${object.fkOfferingMember.firstname}`;
        nameSeller.addEventListener('click', () =>
          Navigate(`/dashboard/user?id=${object.fkOfferingMember.id}`),
        );

        inputFields.appendChild(nameSellerLabel);
        inputFields.appendChild(nameSeller);
      }

      let priceInput;
      if (object.status === 'SHOP' || object.status === 'ON_SALE' || object.status === 'SOLD') {
        const priceLabel = document.createElement('label');
        priceLabel.classList.add('mb-4', 'flex', 'gap-2', 'items-center');
        priceInput = document.createElement('input');
        priceInput.name = 'price';

        if (object.status === 'SOLD') {
          priceInput.setAttribute('readonly', 'true');
        }

        priceInput.classList.add('border', 'p-2', 'w-11/12');
        priceInput.type = 'number';
        priceInput.placeholder = '--,--';
        priceInput.value = object?.sellingPrice;
        priceInput.step = '0.01';

        const priceText = document.createElement('p');
        priceText.innerText = 'Prix';
        priceText.classList.add('font-bold', 'text-swamp-green');

        priceLabel.appendChild(priceInput);
        priceLabel.appendChild(document.createTextNode('€'));
        inputFields.appendChild(priceText);
        inputFields.appendChild(priceLabel);
      }

      const radioDiv = document.createElement('div');
      radioDiv.classList.add('flex', 'flex-col', 'gap-3');

      if (object.status === 'ACCEPTED' || object.status === 'WORKSHOP') {
        const inWorkshopLabel = document.createElement('label');
        inWorkshopLabel.classList.add('flex', 'gap-2');
        const inWorkshopInput = document.createElement('input');
        inWorkshopInput.type = 'radio';
        inWorkshopInput.name = 'status';
        inWorkshopInput.value = 'WORKSHOP';
        inWorkshopLabel.appendChild(inWorkshopInput);
        inWorkshopLabel.appendChild(document.createTextNode('En Atelier'));
        radioDiv.appendChild(inWorkshopLabel);

        const inShopLabel = document.createElement('label');
        inShopLabel.classList.add('flex', 'gap-2');
        const inShopInput = document.createElement('input');
        inShopInput.type = 'radio';
        inShopInput.name = 'status';
        inShopInput.value = 'SHOP';
        inShopLabel.appendChild(inShopInput);
        inShopLabel.appendChild(document.createTextNode('En Magasin'));
        radioDiv.appendChild(inShopLabel);

        if (object.status === 'WORKSHOP') {
          inWorkshopInput.setAttribute('checked', 'true');
        }
      }

      if (object.status === 'SHOP' && isManager()) {
        const soldLabel = document.createElement('label');
        soldLabel.classList.add('flex', 'gap-2');
        const soldInput = document.createElement('input');
        soldInput.type = 'checkbox';
        soldInput.name = 'status';
        soldInput.value = 'SOLD';
        soldLabel.appendChild(soldInput);
        soldLabel.appendChild(document.createTextNode('Vendu'));
        radioDiv.appendChild(soldLabel);
      }

      if (object.status === 'ON_SALE') {
        const soldLabel = document.createElement('label');
        soldLabel.classList.add('flex', 'gap-2');
        const soldInput = document.createElement('input');
        soldInput.type = 'checkbox';
        soldInput.name = 'status';
        soldInput.value = 'SOLD';
        soldLabel.appendChild(soldInput);
        soldLabel.appendChild(document.createTextNode('Vendu'));
        radioDiv.appendChild(soldLabel);
      }

      inputFields.appendChild(radioDiv);

      const typeLabel = document.createElement('label');
      typeLabel.classList.add('mt-2', 'flex', 'flex-col', 'gap-3');

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

        if (type.typeName === object.fkObjectType.typeName) {
          typeOption.setAttribute('selected', 'true');
        }

        typeOption.text = type.typeName;
        selectElement.add(typeOption);
      });

      typeLabel.appendChild(selectElement);
      inputFields.appendChild(typeLabel);

      const label = document.createElement('label');
      label.classList.add('flex', 'flex-col', 'h-64');

      const span = document.createElement('span');
      span.textContent = 'Description';
      span.classList.add('mt-6', 'mb-2', 'font-bold', 'text-swamp-green');
      label.appendChild(span);

      const textarea = document.createElement('textarea');
      textarea.classList.add('resize-none', 'border', 'p-3', 'h-full', 'w-full');
      textarea.name = 'description';
      textarea.placeholder = 'Description';
      textarea.value = object.description;
      label.appendChild(textarea);
      form.appendChild(label);

      const divJustify = document.createElement('div');
      divJustify.classList.add('flex', 'justify-between');

      if (object.status === 'ON_SALE') {
        const removeBtn = document.createElement('input');
        removeBtn.classList.add(
          'my-2',
          'px-4',
          'py-1',
          'bg-red-700',
          'text-white',
          'cursor-pointer',
        );
        removeBtn.type = 'submit';
        removeBtn.name = 'remove';
        removeBtn.value = 'Retirer';
        divJustify.appendChild(removeBtn);
      }

      const modifyBtn = document.createElement('input');
      modifyBtn.classList.add(
        'my-2',
        'px-4',
        'py-1',
        'bg-swamp-green',
        'text-white',
        'cursor-pointer',
      );
      modifyBtn.type = 'submit';
      modifyBtn.name = 'modify';
      modifyBtn.value = 'Confirmer modifications';
      divJustify.appendChild(modifyBtn);
      form.appendChild(divJustify);

      form.addEventListener('submit', async (e) => {
        e.preventDefault();

        if (e.submitter.name === 'remove') {
          const request = await fetch(`/api/objects/${object.id}/remove`, {
            method: 'PATCH',
            headers: {
              Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
            },
          });

          const result = await request.json();
          if (!request.ok) {
            displayErrorMessage(result.message, main);
          } else {
            await ObjectEditPage();
          }
        } else {
          const formElements = Array.from(form.elements);

          const formData = new FormData();

          formElements.forEach((element) => {
            if (element.type === 'checkbox' && !element.checked) {
              return;
            }
            if (element.type === 'radio' && !element.checked) {
              return;
            }
            if (element.type === 'submit') {
              return;
            }
            let value;

            if (element.type === 'file') {
              const [file] = element.files;
              value = file;
            } else {
              value = element.value;
            }
            formData.append(`${element.name}`, value);
          });

          const request = await fetch(`/api/objects/${object.id}/edit`, {
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
            await ObjectEditPage();
          }
        }
      });
    } else {
      Navigate('/');
    }
  } else {
    Navigate('/');
  }
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

export default ObjectEditPage;
