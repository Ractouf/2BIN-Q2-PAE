function displayErrorMessage(errorMessage, element, duration = 5000) {
  const errorDiv = document.createElement('div');
  errorDiv.classList.add(
    'fixed',
    'bottom-0',
    'left-0',
    'right-0',
    'bg-red-500',
    'text-white',
    'p-4',
    'shadow-lg',
    'z-50',
    'animate-pulse',
    'animate-fadeInUp',
    'duration-500',
    'ease-out',
  );

  const closeButtonDiv = document.createElement('div');
  closeButtonDiv.classList.add('float-right');

  const closeButton = document.createElement('button');
  closeButton.classList.add('text-white', 'font-bold', 'text-xl');
  closeButton.textContent = '×';

  closeButton.addEventListener('click', () => {
    errorDiv.parentNode.removeChild(errorDiv);
  });

  const errorMessageSpan = document.createElement('span');
  errorMessageSpan.textContent = errorMessage;
  errorMessageSpan.classList.add('font-medium');

  closeButtonDiv.appendChild(closeButton);
  errorDiv.appendChild(errorMessageSpan);
  errorDiv.appendChild(closeButtonDiv);
  element.appendChild(errorDiv);

  setTimeout(() => {
    setTimeout(() => {
      errorDiv.parentNode.removeChild(errorDiv);
    }, 500);
  }, duration);
}

module.exports = displayErrorMessage;
