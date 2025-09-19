import eslint from '@eslint/js';
import tseslint from 'typescript-eslint';
import globals from "globals";
import vueEslintParser from "vue-eslint-parser"
import eslintPluginVue from "eslint-plugin-vue"
import eslintPluginStylistic from "@stylistic/eslint-plugin"
import eslintTypeScriptPlugin from "@typescript-eslint/eslint-plugin"
import typeScriptESlintParser from "@typescript-eslint/parser"

export default tseslint.config(
	// globale Einstellung für extends und ignores
	// betrifft alle vue und ts-Dateien
  {
    files: ["**/*.ts", "**/*.vue", "**/tailwind/*.js"],
		ignores: ["dist/**", "node_modules/**", "build/**"],
		// verwende die Basisregeln, die mitgeliefert werden
		extends: [
			eslint.configs.recommended,
			...eslintPluginVue.configs['flat/recommended'],
			...tseslint.configs.strictTypeChecked,
			...tseslint.configs.stylisticTypeChecked,
		],
    languageOptions: {
			parser: vueEslintParser,
      parserOptions: {
				parser: typeScriptESlintParser,
        tsconfigRootDir: import.meta.dirname,
				extraFileExtensions: [".vue"],
        sourceType: "module",
				programs: false,
        project: true,
      },
      ecmaVersion: "latest",
      sourceType: "module",
			// globals mit eslint bekannt machen, u.a. window, document etc.
			globals: {
				...globals.browser,
			},
    },
    plugins: {
      "vue": eslintPluginVue,
      "@typescript-eslint": eslintTypeScriptPlugin,
			"@stylistic": eslintPluginStylistic,
		},
		rules: {
			//TESTING:
			curly: ["off", "multi"], // if und for als Einzeiler ohne Klammern
			"@stylistic/semi": "off", // verwende Semikolon überall


			// Standard Regeln
			//"@stylistic/semi": "warn", => Probleme mit arrow functions
			"@stylistic/max-len": "off",
			"@stylistic/no-mixed-spaces-and-tabs": "off",

			"@stylistic/arrow-spacing": "warn",
			"@stylistic/comma-dangle": ["warn", {
        "arrays": "always-multiline",
        "imports": "always-multiline",
        "exports": "always-multiline",
        "objects": "always-multiline",
        "functions": "never",
        "importAttributes": "never",
        "dynamicImports": "never"
    	}],
			"@stylistic/no-multi-spaces": "error",
			"@stylistic/no-mixed-operators": "error",
			"@stylistic/no-trailing-spaces": "error",
			"@stylistic/indent": ["error", "tab", { "SwitchCase": 1 }],

			"require-await": "off",
			"no-unused-vars": "off",
			"no-dupe-class-members": "off",

			"eqeqeq": "error",
			"no-extra-boolean-cast": "error",

			// TypeScript-spezifische Regeln
			"@typescript-eslint/no-this-alias": "off",
			"@typescript-eslint/require-await": "off",
			"@typescript-eslint/no-unused-vars": "off",
			"@typescript-eslint/no-explicit-any": "off",
			"@typescript-eslint/no-empty-function": "off",
			"@typescript-eslint/no-inferrable-types": "off",
			"@typescript-eslint/no-non-null-assertion": "off",
			"@typescript-eslint/consistent-type-assertions": "off",
			"@typescript-eslint/no-confusing-void-expression": "off",

			"@typescript-eslint/no-misused-promises": "error",
			"@typescript-eslint/no-floating-promises": "error",
			"@typescript-eslint/consistent-type-imports": "warn",
			// "@typescript-eslint/no-empty-object-type": ["warn", { allowInterfaces: 'with-single-extends' }],
			"@typescript-eslint/no-empty-object-type": "off",
			"@typescript-eslint/restrict-template-expressions": ["error", {'allowNumber': true}],
			"@typescript-eslint/restrict-plus-operands": ["error", {'allowNumberAndString': true}],
			"@typescript-eslint/strict-boolean-expressions": ["error", { allowString: false, allowNumber: false }],
			// "@typescript-eslint/array-type": ["error", {"default": "array-simple", "readonly": "array-simple"}],
			// Deaktivierte Regeln:
			// Temporär defekt:
			"@typescript-eslint/no-unsafe-call": "off",
			"@typescript-eslint/no-unsafe-return": "off",
			"@typescript-eslint/no-unsafe-argument": "off",
			"@typescript-eslint/no-unsafe-assignment": "off",
			"@typescript-eslint/no-unsafe-member-access": "off",
			// Zu häufig, erstmal warn
			"@typescript-eslint/array-type": "off",
			"@typescript-eslint/prefer-for-of": "off",
			"@typescript-eslint/no-invalid-void-type": "off",
			"@typescript-eslint/prefer-optional-chain": "off",
			"@typescript-eslint/prefer-nullish-coalescing": "off",
			"@typescript-eslint/consistent-type-definitions": "off",
			"@typescript-eslint/consistent-generic-constructors": "off",
			"@typescript-eslint/no-unnecessary-boolean-literal-compare": "off",
		},
  },
	{
		files: ["**/*.vue"],
		rules: {
			"vue/max-len": "off",
			"@stylistic/indent": "off",
			"vue/no-mutating-props": "off",
			"vue/singleline-html-element-content-newline": "off",
			"vue/attributes-order": "off",
			// prüfen
			"@typescript-eslint/prefer-function-type": "off",

			"vue/eqeqeq": "error",
			"vue/no-required-prop-with-default": "error",
			"vue/no-setup-props-reactivity-loss": "error",
			"vue/script-indent": ["error", "tab", { "baseIndent": 1, "switchCase": 1 }],
			"vue/max-attributes-per-line": ["error", { "singleline": 10, "multiline": 10, } ],
			"vue/return-in-computed-property": ["error", { "treatUndefinedAsUnspecified": false } ],
			"vue/first-attribute-linebreak": ["error", { "singleline": "ignore", "multiline": "beside" }],
			"vue/html-closing-bracket-newline": ["error", { "singleline": "never", "multiline": "never" }],
			"vue/html-closing-bracket-spacing": ["warn"],
			"vue/html-indent": ["error", "tab", { "baseIndent": 1, "alignAttributesVertically": false, "attribute": 1 }],
		}
	},
	{
		// *.d.ts-Dateien haben 4 Leerzeichen statt Tabs, kann man auch nicht anders einstellen...
		files: ['**/*.d.ts'],
		rules: {
			"@stylistic/indent": "off",
		}
	},
);
