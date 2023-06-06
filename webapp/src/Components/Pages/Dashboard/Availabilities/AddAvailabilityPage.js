import clearPage from '../../../../utils/render';
import DashboardNavbar from '../Navbar/DashboardNavbar';
import { isAuthenticated, isHelper, isManager } from '../../../../utils/auths';
import Navigate from '../../../Router/Navigate';
import displayErrorMessage from '../../../../utils/error';

const AddAvailabilityPage = async () => {
  clearPage();

  if (isAuthenticated()) {
    if (isManager() || isHelper()) {
      const main = document.querySelector('main');

      const mainDiv = document.createElement('div');
      mainDiv.classList.add('flex', 'flex-col', 'lg:flex-row', 'min-h-[calc(100vh_-_120px)]');
      mainDiv.appendChild(DashboardNavbar());
      main.appendChild(mainDiv);

      const content = document.createElement('section');
      content.classList.add('flex', 'justify-center', 'items-center', 'w-full', 'lg:w-4/5');
      mainDiv.appendChild(content);

      const formAvailability = document.createElement('form');
      formAvailability.setAttribute(
        'class',
        'flex flex-col items-center my-20 l:my-0 gap-4 bg-white p-4 rounded-md shadow-lg w-3/4 h-1/2 2xl:w-1/2 md:w-2/3 justify-center transition-all duration-700',
      );

      const title = document.createElement('h1');
      title.setAttribute('class', 'text-2xl text-swamp-green font-bold text-center');
      title.textContent = 'Ajouter une disponibilité';

      formAvailability.appendChild(title);

      const selectDate = document.createElement('select');
      selectDate.setAttribute(
        'class',
        'w-64 md:w-72 border-2 border-gray-300 rounded-md p-2 text-center',
      );
      selectDate.setAttribute('required', 'true');

      const currentDate = new Date();
      currentDate.setHours(0, 0, 0, 0);

      const startDate = new Date(currentDate);
      startDate.setDate(startDate.getDate() + ((6 - startDate.getDay()) % 7));

      const endDate = new Date(startDate.getFullYear(), 11, 31);
      const date = new Date(startDate);
      while (date <= endDate) {
        const option = document.createElement('option');
        option.text = date.toLocaleDateString('en-GB');
        option.value = date.toDateString('en-GB');
        selectDate.add(option);
        date.setDate(date.getDate() + 7);
      }

      const selectHour = document.createElement('select');
      selectHour.setAttribute('multiple', 'true');
      selectHour.setAttribute(
        'class',
        'w-64 md:w-72 border-2 border-gray-300 rounded-md p-2 text-center h-20',
      );
      selectHour.setAttribute('required', 'true');

      const optionMorning = document.createElement('option');
      optionMorning.setAttribute('value', 'morning');
      optionMorning.textContent = 'Matin (11h-13h)';

      const optionAfternoon = document.createElement('option');
      optionAfternoon.setAttribute('value', 'afternoon');
      optionAfternoon.textContent = 'Après-midi (14h-16h)';

      selectHour.appendChild(optionMorning);
      selectHour.appendChild(optionAfternoon);

      const submit = document.createElement('button');
      submit.setAttribute('type', 'submit');
      submit.setAttribute('class', 'bg-swamp-green text-white rounded-md p-2 w-fit px-4');
      submit.textContent = 'Ajouter';

      formAvailability.appendChild(selectDate);
      formAvailability.appendChild(selectHour);
      formAvailability.appendChild(submit);

      formAvailability.addEventListener('submit', (e) => {
        e.preventDefault();

        const dateSelected = selectDate.value;
        const hoursSelected = Array.from(selectHour.options)

        hoursSelected.forEach(async (hour)  => {
          const hourSelected = hour.selected ? hour.value : null;

          if (hourSelected === null) {
            return;
          }

          let startingHour;
          let endingHour;
          if (hourSelected === 'morning') {
            startingHour = '11:00:00';
            endingHour = '13:00:00';
          } else if (hourSelected === 'afternoon') {
            startingHour = '14:00:00';
            endingHour = '16:00:00';
          }

          const year = new Date(dateSelected).getFullYear();
          const month =
              new Date(dateSelected).getMonth() + 1 < 10
                  ? `0${new Date(dateSelected).getMonth() + 1}`
                  : new Date(dateSelected).getMonth() + 1;
          const day =
              new Date(dateSelected).getDate() < 10
                  ? `0${new Date(dateSelected).getDate()}`
                  : new Date(dateSelected).getDate();

          const theDate = `${year}-${month}-${day}`;

          const request = await fetch('/api/availabilities', {
            method: 'POST',
            headers: {
              Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({
              availabilityDate: theDate,
              startingHour,
              endingHour,
            }),
          });

          const response = await request.json();
          if (!request.ok) {
            displayErrorMessage(response.message, main);
          }
        });

        Navigate('/dashboard/availabilities');
      });

      content.appendChild(formAvailability);
    } else {
      Navigate('/');
    }
  } else {
    Navigate('/');
  }
};

export default AddAvailabilityPage;
