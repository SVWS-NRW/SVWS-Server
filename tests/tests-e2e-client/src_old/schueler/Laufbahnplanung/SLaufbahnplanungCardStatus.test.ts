import { expect} from "@playwright/test";

import { test } from "./SchuelerLaufbahnplanung.pages"

import { dataServerConnection } from "../../DataServerConnection"
import { dataSchuelerAuswahl } from "../DataSchuelerAuswahl";
import { dataSchueler_1_pro_JGab9, dataSchueler_IDs_eine_pro_JGab9} from "../DataSchueler";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

const anzahl_schueler : number = 1



test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.waitForURL('**/schueler/**/daten');
	await expect(page.getByRole('heading', { name: 'Schüler' }).locator('span')).toBeVisible();
});


test("Belegprüfungsergenisse testen", async ({ pageSLaufbahnplanungCardStatus, page, errorPage }) => {
	for (const schueler of dataSchueler_1_pro_JGab9) {
		await page.getByRole('row', { name: schueler.name2click}).click();
		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
		await page.waitForURL('**/schueler/**/laufbahnplanung');
		await pageSLaufbahnplanungCardStatus.testeEingabeBelegpruefungsergebnisse();
	}
});




