import { expect} from "@playwright/test";

import { test } from "./SchuelerLaufbahnplanung.pages"
import { dataServerConnection } from "../../DataServerConnection"
import { dataSchueler_1_pro_JGab9} from "../DataSchueler";
import { api } from "~/Api";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.waitForURL('**/schueler/**/daten');
	await expect(page.getByRole('heading', { name: 'Schüler' }).locator('span')).toBeVisible();
});


test("Beratung editieren", async ({ pageSLaufbahnplanungCardBeratung, page, errorPage }) => {
	for (const schueler of dataSchueler_1_pro_JGab9) {
	 	await page.getByRole('row', { name: schueler.name2click}).click();
		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
		await page.waitForURL('**/schueler/**/laufbahnplanung');
		await pageSLaufbahnplanungCardBeratung.testeEingabeBeratung();
		await pageSLaufbahnplanungCardBeratung.testeAPI_SpeicherungBeratung(schueler.id);

	}
});




