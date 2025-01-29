import {defineConfig, devices} from '@playwright/test';

export default defineConfig({
	// Look for test files in the "tests" directory, relative to this configuration file.
	testDir: './tests',

	// Run all tests in parallel.
	fullyParallel: true,

	// Fail the build on CI if you accidentally left test.only in the source code.
	forbidOnly: !!process.env.CI,

	// Retry on CI only.
	retries: process.env.CI ? 2 : 0,

	// Opt out of parallel tests on CI.
	workers: process.env.CI ? 1 : undefined,

	// Reporter to use

	outputDir: './build/test-results',

	reporter: [['line'], ['junit', {outputFile: './build/test-results/zusammenfassung/e2e-results.xml'}]],

	// For expect calls
	expect: {
		timeout: 60000,
	},

	use: {
		video: {
			mode: 'retain-on-failure',
			size: {width: 640, height: 480}
		},
		baseURL: process.env.VITE_targetHost ?? "https://localhost",
	},
	projects: [
		{
			name: 'chromium',
			use: {...devices['Desktop Chrome']},
		},
	],
	// Run your local dev server before starting the tests.
	// webServer: {
	// 	command: 'npm run start',
	// 	url: 'http://127.0.0.1:3000',
	// 	reuseExistingServer: !process.env.CI,
	// },
});
