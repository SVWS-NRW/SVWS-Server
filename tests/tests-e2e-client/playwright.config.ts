import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
	// Look for test files in the "tests" directory, relative to this configuration file.
	testDir: './tests',

	// Run all tests in parallel.
	fullyParallel: false,

	// Fail the build on CI if you accidentally left test.only in the source code.
	forbidOnly: process.env.CI !== undefined,

	// Retry on CI only.
	retries: process.env.CI !== undefined ? 2 : 0,

	// Opt out of parallel tests on CI.
	workers: 1,

	// Reporter to use
	outputDir: './build/test-results',

	reporter: [['line'], ['junit', { outputFile: './build/test-results/zusammenfassung/e2e-results.xml' }]],

	// For expect calls
	expect: {
		timeout: 60000,
	},

	use: {
		video: {
			mode: 'retain-on-failure',
			size: { width: 960, height: 720 },
		},
		baseURL: process.env.VITE_targetHost ?? "https://localhost",
	},
	projects: [
		{
			name: 'chromium',
			use: { ...devices['Desktop Chrome'] },
		},
		{
			name: 'firefox',
			use: { ...devices['Desktop Firefox'] },
		},
		// {
		// 	name: 'webkit',
		// 	use: { ...devices['Desktop Safari'] },
		// },
	],
});
