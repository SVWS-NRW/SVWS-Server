import typography from '@tailwindcss/typography'

/**
 * @param {string} cssVar
 * @returns {({opacityValue}: {opacityValue: string}) => string}
 */
function withOpacity(cssVar) {
	return ({opacityValue}) => {
		if (opacityValue !== undefined) {
			return `rgba(var(${cssVar}), ${opacityValue})`;
		}
		return `rgb(var(${cssVar}))`;
	};
}

export default {
	layers: ["components", "utilities"],
	safelist: ["theme-dark"],
	darkMode: 'class',
	theme: {
		fontSize: {
			'base': ['1rem', {
				lineHeight: '1.33',
				fontWeight: '500',
			}],
			'headline-sm': ['1rem', {
				lineHeight: '1.33',
				fontWeight: '700',
			}],
			'headline-md': ['1.125rem', {
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
				transparent: "transparent",
				current: "currentColor",
				'svws': {
					'50': '#f1f7fe',
					'100': '#e3eefb',
					'200': '#c0dcf7',
					'300': '#88c0f1',
					'400': '#4ba1e7',
					'500': '#2285d5',
					DEFAULT: '#2285d5',
					'600': '#1467b5',
					'700': '#115393',
					'800': '#12477a',
					'900': '#153d65',
					'950': '#0e2743',
					'smoke': '#F6F5F4',
					'ash': '#B8CCC4',
					'olive': '#2B3B36',
					'night': '#141414',
				},
				white: withOpacity("--color-white"),
				black: withOpacity("--color-black"),
				gray: withOpacity("--color-gray"),
				primary: withOpacity("--color-primary"),
				light: withOpacity("--color-light"),
				"dark-20": withOpacity("--color-dark-20"),
				dark: withOpacity("--color-dark"),
				error: withOpacity("--color-error"),
				success: withOpacity("--color-success"),
				highlight: withOpacity("--color-highlight"),
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
				"modal-md": "58rem",
				"modal-lg": "90rem"
			},
			minWidth: {
				64: "16rem",
				84: "21rem"
			},
			boxShadow: {
				"border-t": "0 -2px 0 0 rgba(0, 0, 0, 1)",
				"border-b": "0 2px 0 0 rgba(0, 0, 0, 1)",
				"lg-up": "0 -10px 15px -3px rgb(0 0 0 / 0.1), 0 -4px 6px -4px rgb(0 0 0 / 0.1)"
			},
			spacing: {
				'68': '17rem',
				'72': '18rem',
				'76': '19rem',
				'80': '20rem',
				'84': '21rem',
				'88': '22rem',
				'92': '23rem',
				'96': '24rem',
				'100': '25rem',
				'104': '26rem',
				'108': '27rem',
				'112': '28rem',
				'116': '29rem',
				'120': '30rem',
				'124': '31rem',
				'128': '32rem',
				'132': '33rem',
				'136': '34rem',
				'140': '35rem',
				'144': '36rem',
			},
		}
	},
	variants: {
		extend: {}
	},
	plugins: [
		typography,
	],
};
