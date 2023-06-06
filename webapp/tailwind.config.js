/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    fontFamily: {
      riez: ['"Blackadder\ ITC"'],
      sans: ['Candara', 'sans-serif']
    },
    extend: {
      backgroundImage: () => ({
        'gradient-gris': `linear-gradient(to right, rgb(205, 205, 205, 0.9), rgb(235, 235, 235, 0.9), rgb(255, 255, 255, 0.9)), url(/src/img/noise.png)`,
        'noise-base': `linear-gradient(to right, rgb(255, 255, 255, 0.9), rgb(255, 255, 255, 0.9)), url(/src/img/noise.png)`,
        'gradient-vert-small': `linear-gradient(to right, rgb(81, 112, 2, 0.9), rgb(81, 112, 9, 0.9)), url(/src/img/noise.png)`,
        'gradient-green-large': `linear-gradient(to right, rgb(81, 112, 2, 0.9), rgb(51, 76, 47, 0.9)), url(/src/img/noise.png)`,
        'footer-grey': `linear-gradient(to right, rgb(40, 40, 40, 0.9), rgb(40, 40, 40, 0.9)), url(/src/img/noise.png)`,
        'logo': `url(/src/img/Titre_R.png)`,
      }),
      colors: {
        'swamp-green': 'rgb(81, 112, 2, 0.9)',
      }
    },
  },
  plugins: [],
}
