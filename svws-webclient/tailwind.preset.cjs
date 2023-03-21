function withOpacity(cssVariale) {
	return ({ opacityValue }) => {
		if (opacityValue !== undefined) {
			return `rgba(var(${cssVariale}), ${opacityValue})`;
		}
		return `rgb(var(${cssVariale}))`;
	};
}

/** @type {import('tailwindcss').Config} */
module.exports = {
	layers: ["components", "utilities"],
	safelist: ["theme-dark"],
	theme: {
		fontFamily: {
			sans: [
				"ui-sans-serif",
				"system-ui",
				"-apple-system",
				"BlinkMacSystemFont",
				"Segoe UI",
				"Roboto",
				"Helvetica Neue",
				"Arial",
				"Noto Sans",
				"sans-serif",
				"Apple Color Emoji",
				"Segoe UI Emoji",
				"Segoe UI Symbol",
				"Noto Color Emoji"
			],
			mono: [
				"ui-monospace",
				"SFMono-Regular",
				"Menlo",
				"Monaco",
				"Consolas",
				"Liberation Mono",
				"Courier New",
				"monospace"
			]
		},
		fontSize: {
			'base': ['1rem', {
				lineHeight: '1.33',
				fontWeight: '500',
			}],
			'headline-sm': ['1rem', {
				lineHeight: '1.33',
				fontWeight: '700',
			}],
			'headline-md': ['1.2rem', {
				lineHeight: '1.33',
				fontWeight: '700',
			}],
			'headline': ['1.618rem', {
				lineHeight: '1.1',
				fontWeight: '700',
			}],
			'headline-xl': ['2.618rem', {
				lineHeight: '1.1',
				fontWeight: '700',
			}],
			'sm': ['0.833rem', {
				lineHeight: '1.1',
				fontWeight: '400',
			}],
			'sm-bold': ['0.833rem', {
				lineHeight: '1',
				fontWeight: '700',
			}],
			button: ['0.916rem', {
				lineHeight: '1',
				fontWeight: '700',
			}],
			caption: ["0.625rem", { lineHeight: "1.5" }], // Deprecated
			body: ["0.875rem", { lineHeight: "1.5" }], // Deprecated
			input: ["1rem", { lineHeight: "1.25" }], // Deprecated
			"headline-5": ["1rem", { lineHeight: "1.25" }], // Deprecated
			"headline-4": ["1.25rem", { lineHeight: "1.25" }], // Deprecated
			"headline-3": ["1.5rem", { lineHeight: "1.25" }], // Deprecated
			"headline-2": ["2.125rem", { lineHeight: "1.2" }], // Deprecated
			"headline-1": ["3rem", { lineHeight: "1.125" }] // Deprecated
		},
		fontWeight: {
			normal: "400",
			medium: "500",
			bold: "700"
		},
		borderWidth: {
			0: '0px',
			DEFAULT: 'thin',
			2: '2px',
		},
		extend: {
			screens: {
				"3xl": "1920px",
				"4xl": "2160px"
			},
			colors: {
				disabled: {
					light: "#F3F3F3",
					DEFAULT: "#D9D9D9",
					medium: "#C0C0C0",
					dark: "#959595"
				},
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
			},
		}
	},
	variants: {
		extend: {}
	},
	plugins: [],
};
