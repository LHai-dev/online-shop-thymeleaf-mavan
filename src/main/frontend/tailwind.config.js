/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
      "./src/main/resources/templates/**/*.html",
      "../resources/templates/**/*.{html,js}",
      "./src/main/resources/templates/**/*.html",
      "./src/main/resources/templates/**.html",
      "./src/main/resources/templates/**/*.{html,js}",
      './src/main/resources/templates/**/*.html', // Path to Thymeleaf templates
      './src/main/resources/static/**/*.js', // Path to any JavaScript files
      './src/**/*.java', // If you use inline styles in your Java code
      './src/main/resources/static/**/*.html' // If you have static HTML files
  ],
  theme: {
    extend: {},
  },
  plugins: [
      require('@tailwindcss/forms'),
  ],
}

