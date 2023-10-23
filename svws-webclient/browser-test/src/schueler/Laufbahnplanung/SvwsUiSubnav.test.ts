import { expect} from "@playwright/test";

import { test } from "./SchuelerLaufbahnplanung.pages"

import { dataServerConnection } from "../../DataServerConnection"
import { dataSchuelerAuswahl } from "../DataSchuelerAuswahl";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

const anzahl_schueler : number = 1



test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.waitForURL('**/schueler/**/daten');
	await expect(page.getByRole('heading', { name: 'Schüler' }).locator('span')).toBeVisible();
});

test.describe("Export und Import",() =>{
	test("Wahlbogen exportieren ", async ({ pageSvwsUiSubnav, page, errorPage }) => {
		for (const schueler of await dataSchuelerAuswahl.getSchuelermitJahrgang_N([5,10,11],anzahl_schueler)) {
			await page.getByRole('row', { name: schueler.name2click}).click();
	        await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
			await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
	        await page.waitForURL('**/schueler/**/laufbahnplanung');
			await pageSvwsUiSubnav.ladeConfig(schueler);
			await pageSvwsUiSubnav.clickExportieren(errorPage);
		}
	});

	test("Wahlbogen importieren ", async ({ pageSvwsUiSubnav, page, errorPage }) => {
		for (const schueler of await dataSchuelerAuswahl.getSchuelermitJahrgang_N([5],anzahl_schueler)) {
			await page.getByRole('row', { name: schueler.name2click}).click();
			await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
			await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
	        await page.waitForURL('**/schueler/**/laufbahnplanung');
			await pageSvwsUiSubnav.ladeConfig(schueler);
			await pageSvwsUiSubnav.clickImportieren(errorPage);
		}
	});

} );


test("Click Planung merken und wiederherstellen ", async ({ pageSvwsUiSubnav, page, errorPage }) => {
	for (const schueler of await dataSchuelerAuswahl.getSchuelermitJahrgang_N([5],anzahl_schueler)) {
		await page.getByRole('row', { name: schueler.name2click}).click();
	    await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
		await pageSvwsUiSubnav.ladeConfig(schueler);
	    await page.waitForURL('**/schueler/**/laufbahnplanung');
		await pageSvwsUiSubnav.clickPanungmerken();
		await pageSvwsUiSubnav.clickPlanungWiederherstellen();
	}
});

test("Click  ->Normaler Modus -> Hochschreibmodus ->  Manueller Modus-> JG 9", async ({ pageSvwsUiSubnav, page, errorPage }) => {
	for (const schueler of await dataSchuelerAuswahl.getSchuelermitJahrgang_N([5], anzahl_schueler)) {
		await page.getByRole('row', { name: schueler.name2click}).click();
		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
		await page.waitForURL('**/schueler/**/laufbahnplanung');
		await pageSvwsUiSubnav.ladeConfig(schueler);

		// Aufgrund der Modi 'Normaler Modus', 'Hochschriebemodus und 'Manueller Modus' drei Aufrufe
		await pageSvwsUiSubnav.clickModusJG9();
		await pageSvwsUiSubnav.clickModusJG9();
		await pageSvwsUiSubnav.clickModusJG9();
	}
});

test("Click ->Normaler Modus -> Manueller Modus ->  ab JG 10", async ({ pageSvwsUiSubnav, page, errorPage }) => {
	for (const schueler of await dataSchuelerAuswahl.getSchuelermitJahrgang_N([10], anzahl_schueler)) {
		await page.getByRole('row', { name: schueler.name2click}).click();
		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
		await page.waitForURL('**/schueler/**/laufbahnplanung');
		await pageSvwsUiSubnav.ladeConfig(schueler);

		// Aufgrund der Modi 'Normaler Modus', 'Hochschriebemodus und 'Manueller Modus' drei Aufrufe
		await pageSvwsUiSubnav.clickModus_ab_10();
		await pageSvwsUiSubnav.clickModus_ab_10();
	}
});




