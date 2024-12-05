import typography from '@tailwindcss/typography';

import { fontSize, fontWeight } from './tailwind.fonts';
import { borderWidth, boxShadow } from './tailwind.borders';
import { screens } from './tailwind.screens';
import { colors, backgroundColor, textColor, borderColor, accentColor, ringColor, opacity } from './tailwind.colors';
import { spacing, maxWidth, zIndex } from './tailwind.dimensions';
import { iconSize, iconColors, iconPath, iconPlugins } from './tailwind.icons';

/**
 * Die Voreinstellungen für die Nutzung von Tailwind - siehe auch: https://tailwindcss.com/docs/theme#configuration-reference
 * Neue Einträge sollen zur Übersichtlichkeit in den oben importierten Dateien ergänzt werde. Bei Bedarf sind ggf. weitere
 * Dateien zu ergänzen. Das direkte Ergänzen von konkreten Werten in dieser Datei sollte vermieden werden.
 */
export default {
	layers: ["components", "utilities"],
	safelist: ["theme-dark"],
	darkMode: 'class',
	theme: {
		fontSize,
		fontWeight,
		borderWidth,
		iconSize,
		iconColors,
		iconPath,
		extend: {
			screens,
			colors,
			backgroundColor,
			textColor,
			borderColor,
			accentColor,
			ringColor,
			opacity,
			zIndex,
			boxShadow,
			spacing,
			maxWidth,
		},
	},
	variants: {
		extend: {},
	},
	plugins: [
		...iconPlugins,
		typography,
	],
};
