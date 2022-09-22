function withOpacity(cssVariale) {
	return ({ opacityValue }) => {
		if (opacityValue !== undefined) {
			return `rgba(var(${cssVariale}), ${opacityValue})`;
		}
		return `rgb(var(${cssVariale}))`;
	};
}

module.exports = {
	layers: ["components", "utilities"],
	safelist: ["theme-dark"],
	theme: {
		colors: {
			disabled: {
				light: "#F3F3F3",
				DEFAULT: "#D9D9D9",
				medium: "#C0C0C0",
				dark: "#959595"
			}
		},
		fontFamily: {
			sans: [
				"-apple-system",
				"BlinkMacSystemFont",
				'"Segoe UI"',
				"Roboto",
				"Helvetica",
				"Arial",
				"sans-serif"
			],
			mono: [
				"SFMono-Regular",
				"Consolas",
				'"Liberation Mono"',
				"Menlo",
				"monospace"
			]
		},
		fontSize: {
			caption: ["0.625rem", { lineHeight: "1.5" }],
			button: ["0.8125rem", { lineHeight: "1.125" }],
			body: ["0.875rem", { lineHeight: "1.5" }],
			input: ["1rem", { lineHeight: "1.25" }],
			"headline-5": ["1rem", { lineHeight: "1.25" }],
			"headline-4": ["1.25rem", { lineHeight: "1.25" }],
			"headline-3": ["1.5rem", { lineHeight: "1.25" }],
			"headline-2": ["2.125rem", { lineHeight: "1.2" }],
			"headline-1": ["3rem", { lineHeight: "1.125" }]
		},
		fontWeight: {
			normal: "400",
			bold: "700"
		},
		extend: {
			screens: {
				"3xl": "1920px",
				"4xl": "2160px"
			},
			colors: {
				transparent: "transparent",
				current: "currentColor",

				white: withOpacity("--color-white"),
				black: withOpacity("--color-black"),
				gray: withOpacity("--color-gray"),
				primary: withOpacity("--color-primary"),
				light: withOpacity("--color-light"),
				"dark-20": withOpacity("--color-dark-20"),
				"dark-80": withOpacity("--color-dark-80"),
				dark: withOpacity("--color-dark"),
				error: withOpacity("--color-error"),
				success: withOpacity("--color-success"),
				highlight: withOpacity("--color-highlight"),

				brown: withOpacity("--color-brown"),
				"brown-light": withOpacity("--color-brown-light"),
				"brown-dark": withOpacity("--color-brown-dark"),
				orange: withOpacity("--color-orange"),
				"orange-light": withOpacity("--color-orange-light"),
				"orange-dark": withOpacity("--color-orange-dark"),
				purple: withOpacity("--color-purple"),
				"purple-light": withOpacity("--color-purple-light"),
				"purple-dark": withOpacity("--color-purple-dark"),
				blue: withOpacity("--color-blue"),
				"blue-light": withOpacity("--color-blue-light"),
				"blue-dark": withOpacity("--color-blue-dark"),
				green: withOpacity("--color-green"),
				"green-light": withOpacity("--color-green-light"),
				"green-dark": withOpacity("--color-green-dark")
			},
			opacity: {
				92: "0.92"
			},
			zIndex: {
				"-10": "-10",
				"-20": "-20"
			},
			maxWidth: {
				116: "29rem",
				"modal-sm": "28rem",
				"modal-md": "76rem",
				"modal-lg": "116rem"
			},
			minWidth: {
				64: "16rem",
				84: "21rem"
			},
			boxShadow: {
				"border-t": "0 -2px 0 0 rgba(0, 0, 0, 1)",
				"border-b": "0 2px 0 0 rgba(0, 0, 0, 1)"
			}
		}
	},
	variants: {
		extend: {}
	},
	plugins: [],
};
