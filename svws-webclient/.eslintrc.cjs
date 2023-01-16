module.exports = {
	root: true,
	env: {
		node: true,
	},
	extends: [
		"plugin:vue/vue3-strongly-recommended",
		"eslint:recommended",
		"@vue/typescript/recommended",
	],
	parserOptions: {
		ecmaVersion: "latest"
	},
	rules: {
		// Standard Regeln
		"indent": ["error", "tab"],
		"no-console": process.env.NODE_ENV === "production" ? "error" : "warn",
		"no-debugger": process.env.NODE_ENV === "production" ? "error" : "warn",

		// Vue-spezifische Regeln
		"vue/no-setup-props-destructure": "error", // schützt davor, props unreactiv zu destrukurieren
		"vue/script-indent": ["error", "tab", { "baseIndent": 1, "switchCase": 1 }],
		"vue/html-indent": ["error", "tab", { "baseIndent": 1, "alignAttributesVertically": false, "attribute": 1 }],
		"vue/max-attributes-per-line": ["error", 
			{
				// maximal 10 attribute bei single line element
				"singleline": 10,
				// wenn mehr, wird umgebrochen mit jeweils max. 10 attributen in einer zeile.
				"multiline": 10,
			}
		],
		"vue/html-closing-bracket-newline": ["error", {
			"singleline": "never",
			"multiline": "never"
		}],
		"vue/first-attribute-linebreak": ["error", {
			"singleline": "ignore",
			"multiline": "beside"
		}],
		"vue/return-in-computed-property": ["error", 
			{
				"treatUndefinedAsUnspecified": false
			}
		],
		"vue/no-required-prop-with-default": "error",

		// TypeScript-spezifische Regeln
		// "@typescript-eslint/no-unused-vars": ["warn", { varsIgnorePattern: "^_" }],
		"@typescript-eslint/no-inferrable-types": [
			1,
			{ ignoreParameters: true }
		],
		"@typescript-eslint/ban-types": [
			"error",
			{
				types: {
					// un-ban a type that's banned by default
					"Number": false,
					"String": false,
					"Boolean": false
				},
				extendDefaults: true
			}
		],
		
		"no-mixed-spaces-and-tabs": "off",
		"no-trailing-spaces": "error",
		"max-len": "off", // keine maximale zeilenlänge
		"vue/max-len": "off", // ebensowenig in .vue files
		"vue/singleline-html-element-content-newline": "off",
		
		// BEGIN Für's erste deaktiviert, zu diskutieren
		// TODO: klären, ggf. reaktivieren, lint-fehler fixen
		"vue/no-mutating-props": "off", // deaktiviert, da wir recht häufig Objekte, die wir per props bekommen haben, verändern.
		"@typescript-eslint/no-unused-vars": "off",
		"@typescript-eslint/no-explicit-any": "off",
		"@typescript-eslint/no-empty-function": "off",
		"@typescript-eslint/no-this-alias": "off",
		// END

	},
	overrides: [
		{
			files: [
				"src/ui-components/ts/src/**/*.{ts,js,cjs,mjs}",
				"src/web-client/ts/src/**/*.{ts,js,cjs,mjs}",
			],
			env: {
				// diese Packages haben keinen zugriff auf Node APIs da er im Browser läuft.
				node: false
			},
		},
		{
			files: ["**/*.vue"],
			rules: {
				// für SFCs gibt es eigene regeln bzgl. Indentation
				"indent": "off",
			}
		}
	]
};
