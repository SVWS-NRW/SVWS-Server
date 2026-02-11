import { defineConfig, devices } from '@playwright/test';
import { frontendURL } from "../utils/APIUtils";

export default defineConfig({
	// Look for test files in the "tests" directory, relative to this configuration file.
	testDir: './tests',

	// Run all tests in parallel.
	fullyParallel: true,

	// Fail the build on CI if you accidentally left test.only in the source code.
	forbidOnly: process.env.CI !== undefined,

	// Retry on CI only.
	retries: process.env.CI === undefined ? 0 : 2,

	// Opt out of parallel tests on CI.
	workers: 3,

	// Reporter to use
	outputDir: './build/test-results',

	reporter: [['line'], ['junit', { outputFile: './build/test-results/e2e-stable-test-results.xml' }]],

	// For expect calls
	expect: {
		timeout: 20000,
	},
	timeout: 90_000,

	use: {
		video: {
			mode: 'retain-on-failure',
			size: { width: 960, height: 720 },
		},
		baseURL: frontendURL,
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
	],
});
