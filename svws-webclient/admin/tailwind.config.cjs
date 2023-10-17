// Das mit dem Preset funktioniert nicht...
//
/** @type {import('tailwindcss').Config} */
module.exports = {
	presets: [ require("../tailwind.preset.cjs") ],
	content: [
		// UI-Framework
		"../ui/src/**/*.{vue,js,ts,jsx,tsx}",
		// Komponenten wie z.B. die Laufbahnplanung
		"../components/src/**/*.{vue,js,ts,jsx,tsx}",
		// Lokale Komponenten des Clients selbst
		"./index.html",
		"./src/components/**/*.{vue,js,ts,jsx,tsx}",
	],
}
