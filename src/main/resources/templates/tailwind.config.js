/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/**/*.{js,ts,jsx,tsx,html}",
        "./public/index.html", // optional if using plain HTML
    ],
    theme: {
        extend: {
            colors: {
                asterPurple: '#8e44ad',
            },
            fontFamily: {
                fancy: ['Poppins', 'sans-serif'],
            },
        },
    },
    darkMode: 'class', // Enables class-based dark mode
    plugins: [
        require('@tailwindcss/forms'),
        require('@tailwindcss/typography'),
    ],
}
