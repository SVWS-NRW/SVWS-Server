import preset from "./src/tailwind/tailwind.preset";

export default {
	presets: [ preset ],
	content: [
		"./src/components/**/*.{vue,js,ts,jsx,tsx}",
	],
}
