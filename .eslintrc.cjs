module.exports = {
	root: true,
	env: { node: true },
	parser: "vue-eslint-parser",
	extends: [
		"plugin:vue/vue3-strongly-recommended",
		"plugin:@typescript-eslint/recommended",
		"eslint:recommended",
	],
	parserOptions: {
		parser: '@typescript-eslint/parser',
		extraFileExtensions: [".vue"],
		ecmaVersion: "latest",
		sourceType: "module",
		tsconfigRootDir: __dirname,
		project: [
			"./tsconfig.base.json",
			"./svws-transpile/tsconfig.json",
			"./svws-transpile/src/test/ts/tsconfig.json",
			"./svws-webclient/core/tsconfig.json",
			"./svws-webclient/ui/tsconfig.json",
			"./svws-webclient/components/tsconfig.json",
			"./svws-webclient/client/tsconfig.json",
			"./svws-webclient/admin/tsconfig.json",
			"./svws-webclient/laufbahnplanung/tsconfig.json",
			"./svws-webclient/core-test/tsconfig.json",
			"./svws-webclient/api-test/tsconfig.json",
			"./svws-webclient/browser-test/tsconfig.json",
		]
	},
	rules: {
		// Standard Regeln
		"indent": ["error", "tab", { "SwitchCase": 1 }],
		//console.log-Warnung. Können wir bei Bedarf wieder aktivieren
		//"no-console": process.env.NODE_ENV === "production" ? "error" : "warn",
		"no-debugger": process.env.NODE_ENV === "production" ? "error" : "warn",
		"no-mixed-spaces-and-tabs": "off",
		"no-trailing-spaces": "error",
		"max-len": "off", // keine maximale zeilenlänge
		"vue/max-len": "off", // ebensowenig in .vue files
		"no-unused-vars": "off",
		"no-dupe-class-members": "off",
		"eqeqeq": "error",

		// Vue-spezifische Regeln
		"vue/no-setup-props-destructure": "error", // schützt davor, props unreactiv zu destrukurieren
		"vue/script-indent": ["error", "tab", { "baseIndent": 1, "switchCase": 1 }],
		"vue/html-indent": ["error", "tab", { "baseIndent": 1, "alignAttributesVertically": false, "attribute": 1 }],
		"vue/max-attributes-per-line": ["error", { "singleline": 10, "multiline": 10, } ],
		"vue/html-closing-bracket-newline": ["error", { "singleline": "never", "multiline": "never" }],
		"vue/first-attribute-linebreak": ["error", { "singleline": "ignore", "multiline": "beside" }],
		"vue/return-in-computed-property": ["error", { "treatUndefinedAsUnspecified": false } ],
		"vue/no-required-prop-with-default": "error",
		"vue/singleline-html-element-content-newline": "off",

		// TypeScript-spezifische Regeln
		"@typescript-eslint/no-inferrable-types": "off", //[ 1, { ignoreParameters: true } ],
		"@typescript-eslint/ban-types": "error",
		// BEGIN Für's erste deaktiviert, zu diskutieren
		// TODO: klären, ggf. reaktivieren, lint-fehler fixen
		"vue/no-mutating-props": "off", // deaktiviert, da wir recht häufig Objekte, die wir per props bekommen haben, verändern.
		"@typescript-eslint/no-unused-vars": "off",
		"@typescript-eslint/no-explicit-any": "off",
		"@typescript-eslint/no-empty-function": "off",
		"@typescript-eslint/no-this-alias": "off",
		"@typescript-eslint/no-floating-promises": "error",
		"@typescript-eslint/no-misused-promises": "error",
		// Bis wir das gefixt haben, vorerst aus
		"@typescript-eslint/no-non-null-assertion": "off",
		"@typescript-eslint/consistent-type-imports": ["warn"]
		// END
	},
	overrides: [
		{
			// *.d.ts-Dateien haben 4 Leerzeichen statt Tabs, kann man auch nicht anders einstellen...
			files: ['*.d.ts'],
			rules: {
				"indent": ["off", "tab", { "SwitchCase": 1 }],
			}
		},
		{
			files: [
				"svws-webclient/components/src/**/*.{ts,js,cjs,mjs}",
				"svws-webclient/ui/src/**/*.{ts,js,cjs,mjs}",
				"svws-webclient/client/src/**/*.{ts,js,cjs,mjs}",
				"svws-webclient/laufbahnplanung/src/**/*.{ts,js,cjs,mjs}",
			],
			env: {
				// diese Packages haben keinen Zugriff auf Node APIs da es im Browser läuft.
				node: false
			},
		},
		{
			files: ["svws-webclient/**/*.vue"],
			rules: {
				// für SFCs gibt es eigene Regeln bzgl. Indentation
				"indent": "off",
			}
		}
	]
};
