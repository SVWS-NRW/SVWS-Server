const config = require("../../../tailwind.config-base.cjs");

config.content = [
	// UI-Framework
	"../../ui/ts/src/**/*.{vue,js,ts,jsx,tsx}",
	// Komponenten wie z.B. die Laufbahnplanung
	"../../components/ts/src/**/*.{vue,js,ts,jsx,tsx}",
	// Lokale Komponenten des Clients selbst
	"./index.html",
	"./src/components/**/*.{vue,js,ts,jsx,tsx}",
]
module.exports = config;
