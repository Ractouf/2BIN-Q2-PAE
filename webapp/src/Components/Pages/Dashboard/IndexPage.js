import Chart from 'chart.js/auto';
import clearPage from '../../../utils/render';
import DashboardNavbar from './Navbar/DashboardNavbar';
import { isAuthenticated, isHelper, isManager } from '../../../utils/auths';
import Navigate from '../../Router/Navigate';
import displayErrorMessage from '../../../utils/error';

let currentYear = new Date().getFullYear();

const IndexPage = async () => {
  clearPage();

  if (isAuthenticated()) {
    if (isHelper() || isManager()) {
      const main = document.querySelector('main');

      const maindiv = document.createElement('div');
      maindiv.classList.add('flex', 'flex-col', 'lg:flex-row', 'min-h-[calc(100vh_-_120px)]');
      maindiv.appendChild(DashboardNavbar());

      main.appendChild(maindiv);

      const request = await fetch('/api/dashboard', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `${localStorage.getItem('token') || sessionStorage.getItem('token')}`,
        },
      });

      const response = await request.json();
      if (!request.ok) {
        displayErrorMessage(response.message, maindiv);
      } else {
        const mainSection = document.createElement('section');
        mainSection.classList.add('flex-grow', 'flex', 'flex-col', 'my-4');
        maindiv.appendChild(mainSection);

        const title = document.createElement('h1');
        title.classList.add(
          'text-2xl',
          'font-bold',
          'mb-4',
          'text-center',
          'mt-4',
          'text-swamp-green',
        );
        title.textContent = 'Tableau de bord';
        mainSection.appendChild(title);

        const topSection = document.createElement('section');
        topSection.classList.add(
          'flex',
          'flex-col',
          'items-center',
          'justify-center',
          'px-8',
          'shadow-lg',
          'rounded-lg',
          'bg-white',
          'mx-8',
          'my-4',
        );
        mainSection.appendChild(topSection);

        const chartObjectsProposedByMonthCanvas = document.createElement('canvas');
        chartObjectsProposedByMonthCanvas.setAttribute('id', 'chartObjectsProposedByMonth');
        topSection.appendChild(chartObjectsProposedByMonthCanvas);

        const chartObjectsProposedByMonthChart = new Chart(chartObjectsProposedByMonthCanvas, {
          type: 'bar',
          data: {
            labels: [
              'Janvier',
              'Février',
              'Mars',
              'Avril',
              'Mai',
              'Juin',
              'Juillet',
              'Août',
              'Septembre',
              'Octobre',
              'Novembre',
              'Décembre',
            ],
            datasets: [
              {
                label: 'Objets proposés',
                data: Object.values(response.nbrObjectsProposedByMonth[currentYear]),
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1,
              },
            ],
          },
          options: {
            aspectRatio: 5,
            responsive: true,
            plugins: {
              legend: {
                display: false,
              },
              title: {
                display: true,
                text: `Objets proposés en ${currentYear}`,
                font: {
                  size: 14,
                },
              },
              decimation: {
                enabled: false,
              },
            },
            scales: {
              y: {
                min: 0,
                max:
                  Object.values(response.nbrObjectsProposedByMonth[currentYear]).reduce((a, b) =>
                    Math.max(a, b),
                  ) < 10
                    ? 10
                    : 'auto',
              },
            },
          },
        });

        chartObjectsProposedByMonthChart.update();

        const sectionButtons = document.createElement('section');
        sectionButtons.classList.add('flex', 'justify-center', 'mt-4', 'items-center');
        topSection.appendChild(sectionButtons);

        const arrowRight = document.createElement('button');

        const arrowLeft = document.createElement('button');

        const svgLeft = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
        svgLeft.setAttribute('width', '16');
        svgLeft.setAttribute('height', '16');

        const pathLeft = document.createElementNS('http://www.w3.org/2000/svg', 'path');
        pathLeft.setAttribute('d', 'M13 8 H3 m5 -5 L3 8 l5 5');
        pathLeft.setAttribute('stroke', 'gray');
        pathLeft.setAttribute('stroke-width', '2');
        pathLeft.setAttribute('fill', 'none');

        svgLeft.appendChild(pathLeft);
        arrowLeft.appendChild(svgLeft);
        sectionButtons.appendChild(arrowLeft);

        const text = document.createElement('h4');
        text.classList.add('text-center', 'text-xl', 'mx-1', 'text-gray-500', 'font-bold', 'py-4');
        text.textContent = currentYear;
        sectionButtons.appendChild(text);

        arrowLeft.addEventListener('click', () => {
          currentYear -= 1;
          if (response.nbrObjectsProposedByMonth[currentYear] === undefined) {
            currentYear += 1;
            return;
          }
          chartObjectsProposedByMonthChart.data.datasets[0].data = Object.values(
            response.nbrObjectsProposedByMonth[currentYear],
          );
          text.textContent = currentYear;
          chartObjectsProposedByMonthChart.config.options.plugins.title.text = `Objets proposés en ${currentYear}`;
          chartObjectsProposedByMonthChart.update();
        });

        arrowRight.addEventListener('click', () => {
          currentYear += 1;
          if (response.nbrObjectsProposedByMonth[currentYear] === undefined) {
            currentYear -= 1;
            return;
          }
          text.textContent = currentYear;
          chartObjectsProposedByMonthChart.data.datasets[0].data = Object.values(
            response.nbrObjectsProposedByMonth[currentYear],
          );
          chartObjectsProposedByMonthChart.config.options.plugins.title.text = `Objets proposés en ${currentYear}`;
          chartObjectsProposedByMonthChart.update();
        });

        const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
        svg.setAttribute('width', '16');
        svg.setAttribute('height', '16');

        const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
        path.setAttribute('d', 'M5 8 h10 m-5 -5 l5 5 -5 5');
        path.setAttribute('stroke', 'gray');
        path.setAttribute('stroke-width', '2');
        path.setAttribute('fill', 'none');

        svg.appendChild(path);
        arrowRight.appendChild(svg);
        sectionButtons.appendChild(arrowRight);

        const bottomSection = document.createElement('section');
        bottomSection.classList.add('flex', 'flex-col', 'lg:flex-row', 'justify-evenly', 'px-16');
        mainSection.appendChild(bottomSection);

        const chartStatus = document.createElement('article');
        chartStatus.classList.add('shadow-md', 'rounded-lg', 'bg-white', 'm-2', 'p-4');
        bottomSection.appendChild(chartStatus);

        const chartStatusCanvas = document.createElement('canvas');
        chartStatusCanvas.setAttribute('id', 'chartStatus');
        chartStatus.appendChild(chartStatusCanvas);

        const chartStatusChart = new Chart(chartStatusCanvas, {
          type: 'doughnut',
          data: {
            labels: Object.keys(response.status),
            datasets: [
              {
                label: 'Nombre',
                data: Object.values(response.status),
                backgroundColor: [
                  'rgba(255, 99, 132, 0.2)',
                  'rgba(54, 162, 235, 0.2)',
                  'rgba(255, 206, 86, 0.2)',
                ],
                borderColor: [
                  'rgba(255, 99, 132, 1)',
                  'rgba(54, 162, 235, 1)',
                  'rgba(255, 206, 86, 1)',
                ],
                borderWidth: 1,
              },
            ],
          },
          options: {
            responsive: true,
            plugins: {
              title: {
                display: true,
                text: 'Objets par statut',
                font: {
                  size: 14,
                },
              },
            },
          },
        });

        chartStatusChart.update();

        const chartUsers = document.createElement('article');
        chartUsers.classList.add('shadow-md', 'rounded-lg', 'bg-white', 'm-2', 'p-4');
        bottomSection.appendChild(chartUsers);

        const chartUsersCanvas = document.createElement('canvas');
        chartUsersCanvas.setAttribute('id', 'chartUsers');
        chartUsers.appendChild(chartUsersCanvas);

        const chartUsersChart = new Chart(chartUsersCanvas, {
          type: 'doughnut',
          data: {
            labels: ['Utilisateurs', 'Aidants'],
            datasets: [
              {
                label: 'Nombre',
                data: [response.nbrUsers, response.nbrHelpers],
                backgroundColor: ['rgba(255, 99, 132, 0.2)', 'rgba(54, 162, 235, 0.2)'],
                borderColor: ['rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)'],
                borderWidth: 1,
              },
            ],
          },
          options: {
            responsive: true,
            plugins: {
              title: {
                display: true,
                text: 'Utilisateurs',
                font: {
                  size: 14,
                },
              },
            },
          },
        });

        chartUsersChart.update();
      }
    } else {
      Navigate('/');
    }
  } else {
    Navigate('/');
  }
};

export default IndexPage;
