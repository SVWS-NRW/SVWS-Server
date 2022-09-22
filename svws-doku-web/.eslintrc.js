module.exports = {
	root: true,
	env: {
		node: true
	},
	extends: [
		"plugin:vue/vue3-recommended",
		"eslint:recommended",
		"@vue/typescript/recommended",
		"prettier"
	],
	parserOptions: {
		ecmaVersion: "latest",
	},
	rules: {
		"no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
		"no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off"
	}
};
