import { test as baseTest } from "@playwright/test"
import LoginPage from "./PageLogin";
import ErrorPage from "./PageError"

/** Erweitert die Test-Definition von Playwright mit den Pages */
export const test = baseTest.extend<{
    loginPage: LoginPage,
	errorPage: ErrorPage,
}>({
	loginPage: async ({ page }, use) => await use(new LoginPage(page)),
	errorPage: async ({ page }, use) => await use(new ErrorPage(page)),
})
