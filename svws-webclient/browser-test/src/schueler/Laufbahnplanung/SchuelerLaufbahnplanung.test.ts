import { expect } from "@playwright/test";
import { test } from "./SchuelerLaufbahnplanung.pages"

import { dataServerConnection } from "~/DataServerConnection";
import { dataSchuelerAuswahl } from "../DataSchuelerAuswahl";
import type { Schueler} from "../DataSchueler";
import { dataSchueler_1_pro_JGab9, dataSchueler_IDs_eine_pro_JGab9 } from "../DataSchueler";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';



test.beforeEach(async ({ baseURL, page, loginPage }) => {

	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.waitForURL('**/schueler/**/daten');
	await expect(page.getByRole('heading', { name: 'Schüler' }).locator('span')).toBeVisible();
});

// TODO : Entscheidung ?  Einzeln : Zusammen
// Meine Auswahl : Zusammen, da es deutlich schneller geht.


// test("Teste Laufwahlbogen herunterladen in der Auswahlliste", async({ schuelerlaufbahnplanungpage,page}) => {
// 	for (const schueler of dataSchueler_1_pro_JGab9) {
// 		await page.getByRole('row', { name: schueler.name2click}).click();
// 		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
// 		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
// 		await page.waitForURL('**/schueler/**/laufbahnplanung');
// 		await schuelerlaufbahnplanungpage.clickLPWahlbogenHerunterladen();
// 	}
// });

// test("Teste Laufwahlbogen mit nur Belegung herunterladen in der Auswahlliste", async({ schuelerlaufbahnplanungpage,page}) => {
// 	for (const schueler of dataSchueler_1_pro_JGab9) {
// 		await page.getByRole('row', { name: schueler.name2click}).click();
// 		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
// 		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
// 		await page.waitForURL('**/schueler/**/laufbahnplanung');
// 		await schuelerlaufbahnplanungpage.clickLPWahlbogenNurBelegungHerunterladen();
// 	}
// });

// test("Teste Anzeigeinfos des Schülers", async({ schuelerlaufbahnplanungpage, page}) => {
// 	for (const schueler of dataSchueler_1_pro_JGab9) {
// 		await page.getByRole('row', { name: schueler.name2click}).click();
// 		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
// 		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
// 		await page.waitForURL('**/schueler/**/laufbahnplanung');
// 		await schuelerlaufbahnplanungpage.testeSchuelerInformation(schueler);
// 	}
// })

test("Teste Anzeige der Hilfe", async({ schuelerlaufbahnplanungpage, page}) => {
	for (const schueler of dataSchueler_1_pro_JGab9) {
		await page.getByRole('row', { name: schueler.name2click}).click();
		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
		await page.waitForURL('**/schueler/**/laufbahnplanung');
		await schuelerlaufbahnplanungpage.clickLPWahlbogenHerunterladen();
		await schuelerlaufbahnplanungpage.clickLPWahlbogenNurBelegungHerunterladen();
		await schuelerlaufbahnplanungpage.testeSchuelerInformation(schueler);
		await schuelerlaufbahnplanungpage.clickHilfe();
	}
})