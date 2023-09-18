/** @type {import('tailwindcss').Config} */
module.exports = {
	presets: [ require("../tailwind.preset.cjs") ],
	content: [
		"./src/components/**/*.{vue,js,ts,jsx,tsx}",
	],
}