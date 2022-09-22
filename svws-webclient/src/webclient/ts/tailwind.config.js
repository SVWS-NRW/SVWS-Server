module.exports = {
	content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
	theme: {
		extend: {
			screens: {
				"3xl": "1920px",
				"4xl": "2160px"
			}
		}
	},
	variants: {
		extend: {}
	},
	plugins: []
};
