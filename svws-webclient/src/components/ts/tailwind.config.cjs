/** @type {import('tailwindcss').Config} */
module.exports = {
	presets: [ require("../../../tailwind.config-base") ],
	content: [
		"./src/components/**/*.{vue,js,ts,jsx,tsx}",
	],
}