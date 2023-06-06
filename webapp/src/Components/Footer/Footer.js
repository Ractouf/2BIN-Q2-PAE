const Footer = () => {
  const footer = document.querySelector('footer');
  footer.classList.add('bg-footer-grey', 'text-center');

  const ul = document.createElement('ul');
  ul.classList.add('py-2', 'text-gray-500');

  const li1 = document.createElement('li');
  const p1 = document.createElement('p');
  p1.textContent = 'Rue du Heuseux 77ter, 4671, Blégny';
  li1.appendChild(p1);

  const li2 = document.createElement('li');
  const p2 = document.createElement('p');
  p2.textContent = 'RessourceRie, Riez ©';
  li2.appendChild(p2);

  ul.appendChild(li1);
  ul.appendChild(li2);

  footer.appendChild(ul);
};

export default Footer;
