import type { Expect} from "@playwright/test";
import { test as baseTest } from "@playwright/test"
import SchuelerLaufbahnPage from "./pages/SchuelerLaufbahnPage"
import SchuelerIndividualdatenPage from "./pages/SchuelerIndividualdatenPage"
import { SchuelerLaufbahntabellePage } from "./pages/SchuelerLaufbahntabellePage"
import LoginPage from "../LoginPage"
import ErrorPage from "../ErrorPage"
import { SchuelerAuswahlPage } from "./pages/SchuelerAuswahlPage";


type pages = {
	loginPage: LoginPage,
	schuelerLaufbahnPage: SchuelerLaufbahnPage,
	schuelerLaufbahntabellePage: SchuelerLaufbahntabellePage,
	schuelerIndividualdatenPage: SchuelerIndividualdatenPage,
	schuelerauswahlPage: SchuelerAuswahlPage,
	errorPage: ErrorPage,
	isdd: Expect,
}

const testPages = baseTest.extend<pages>({
	loginPage: async ({ page }, use) => await use(new LoginPage(page)),
	errorPage: async ({ page }, use) => await use(new ErrorPage(page)),
	schuelerLaufbahnPage: async ({ page }, use) => await use(new SchuelerLaufbahnPage(page)),
	schuelerLaufbahntabellePage: async ({ page }, use) => await use(new SchuelerLaufbahntabellePage(page)),
	schuelerIndividualdatenPage: async ({ page }, use) => await use(new SchuelerIndividualdatenPage(page)),
	schuelerauswahlPage: async ({ page }, use) => await use(new SchuelerAuswahlPage(page)),
})

export const test = testPages;
export const expect = testPages.expect;
