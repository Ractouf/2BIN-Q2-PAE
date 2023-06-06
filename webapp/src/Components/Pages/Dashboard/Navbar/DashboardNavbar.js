import Navigate from '../../../Router/Navigate';
import { isManager } from '../../../../utils/auths';

const DashboardNavbar = () => {
  const nav = document.createElement('nav');
  nav.classList.add('bg-gradient-vert-small');
  nav.setAttribute('id', 'dashboard-navbar');

  const links = document.createElement('div');
  links.classList.add('sticky', 'top-16', 'flex', 'flex-col');

  const link1 = document.createElement('button');
  link1.textContent = 'Tableau de bord';
  link1.classList.add(
    'py-3',
    'px-auto',
    'text-white',
    'hover:bg-noise-base',
    'hover:text-swamp-green',
    'transition-all',
  );
  links.appendChild(link1);

  link1.addEventListener('click', () => {
    Navigate('/dashboard');
  });

  const link2 = document.createElement('button');
  link2.textContent = 'Objets';
  link2.classList.add(
    'py-3',
    'px-28',
    'xl:px-32',
    'text-white',
    'hover:bg-noise-base',
    'hover:text-swamp-green',
    'transition-all',
  );
  links.appendChild(link2);

  link2.addEventListener('click', () => {
    Navigate('/dashboard/objects');
  });

  if (isManager()) {
    const link3 = document.createElement('button');
    link3.textContent = 'Objets proposés';
    link3.classList.add(
      'py-3',
      'px-auto',
      'text-white',
      'hover:bg-noise-base',
      'hover:text-swamp-green',
      'transition-all',
    );
    links.appendChild(link3);

    link3.addEventListener('click', () => {
      Navigate('/dashboard/objects/proposed');
    });

    const link4 = document.createElement('button');
    link4.textContent = 'Utilisateurs';
    link4.classList.add(
      'py-3',
      'px-16',
      'text-white',
      'hover:bg-noise-base',
      'hover:text-swamp-green',
      'transition-all',
    );
    links.appendChild(link4);

    link4.addEventListener('click', () => {
      Navigate('/dashboard/users');
    });

    const link5 = document.createElement('button');
    link5.textContent = 'Ajouter une disponibilité';
    link5.classList.add(
      'py-3',
      'px-auto',
      'text-white',
      'hover:bg-noise-base',
      'hover:text-swamp-green',
      'transition-all',
    );
    links.appendChild(link5);

    link5.addEventListener('click', () => {
      Navigate('/dashboard/availabilities');
    });
  }

  nav.appendChild(links);

  return nav;
};

export default DashboardNavbar;
