module.exports = {
	env: {
		"vue/setup-compiler-macros": true 
	},
	extends: [
		"./.eslintrc-auto-import.json"
	],
	rules: {
		// in diesem Package ist der REactivity Transform aktiviert, daher ist props destrukturieren erlaubt.
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
					"Table",
					"Headline",
					"Button.story",
					"Checkbox.story",
					"Dropdown.story",
					"Toggle.story",
					"Modal.story",
					"Overlay.story",
					"Menu.story",
					"Avatar.story",
					"Header.story",
					"Icon.story",
					"Badge.story",
					"Popover.story",
					"Tooltip.story",
					"Headline.story",
					"Table.story"
				]
			}
		]
	}
};
