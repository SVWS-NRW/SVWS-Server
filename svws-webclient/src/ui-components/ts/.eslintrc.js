module.exports = {
	root: true,
	env: {
		node: true,
		"vue/setup-compiler-macros": true
	},
	extends: [
		"../../../.eslintrc.js",
		"./.eslintrc-auto-import.json"
	],
	rules: {
		"vue/no-setup-props-destructure": 0,
		"vue/multi-word-component-names": [
			"error",
			{
				ignores: [
					"Button",
					"Checkbox",
					"Dropdown",
					"Toggle",
					"Modal",
					"Overlay",
					"Menu",
					"Avatar",
					"Header",
					"Icon",
					"Badge",
					"Popover",
					"Tooltip",
				]
			}
		]
	}
};
