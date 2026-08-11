import { defineConfig } from "vitest/config";
import { resolve } from 'node:path';

export default defineConfig({
	test: {
		globals: true,
		// Nur die Testquellen unter tests/ ausführen. Ohne diese Eingrenzung würde ein Lauf auch
		// kompilierte Kopien einsammeln, die außerhalb des Verzeichnisses entstehen.
		include: ["tests/**/*.test.ts"],
		testTimeout: 20000,
		reporters: ["default", "junit"],
		outputFile: "./build/test-results/api-test-results.xml",
		globalSetup: ["globalSetup.ts"],
		silent: false,
		isolate: false,
		fileParallelism: false,
	},
	resolve: {
		alias: {
			"~": resolve(__dirname, "src"),
		},
	},
});
