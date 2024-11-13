import preset from "./src/tailwind/tailwind.preset.js";

export default {
	presets: [ preset ],
	content: [
		"./src/components/**/*.{vue,js,ts,jsx,tsx}",
	],
}
