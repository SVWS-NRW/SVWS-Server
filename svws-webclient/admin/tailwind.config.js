import preset from "../tailwind.preset.js"
export default {
	presets: [ preset ],
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
