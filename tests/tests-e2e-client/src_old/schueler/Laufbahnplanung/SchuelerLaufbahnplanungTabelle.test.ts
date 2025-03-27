import { expect, type Page } from "@playwright/test";

import { test } from "./SchuelerLaufbahnplanung.pages";

import { dataServerConnection } from "../../DataServerConnection"
import { dataSchueler , dataSchueler_1_pro_JGab9, type Schueler } from "../DataSchueler"

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';


test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.waitForURL('**/schueler/**/daten');
	await expect(page.getByRole('heading', { name: 'Schüler' }).locator('span')).toBeVisible();
});


test("Laufbahntabelle Sichtbarkeit Tabellentext", async ({ page, schuelerLaufbahntabellePage }) => {
	for (const schueler of dataSchueler_1_pro_JGab9) {
		await page.getByRole('row', { name: schueler.name2click}).click();
	    await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
	    await page.waitForURL('**/schueler/**/laufbahnplanung');
		schuelerLaufbahntabellePage.ladeConfig(schueler);
		//console.log(await page.locator('div.svws-ui-tfoot--data').allInnerTexts());
		//await schuelerLaufbahntabellePage.ladeFachbelegungeng_aus_Tabelle();
		await schuelerLaufbahntabellePage.pruefeSichtbarkeit();
	}

});

test("Vergleiche Faecher zwischen API und Website ", async ({ schuelerLaufbahntabellePage, page }) => {
	for (const schueler of dataSchueler_1_pro_JGab9) {
		await page.getByRole('row', { name: schueler.name2click}).click();
	    await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
	    await page.waitForURL('**/schueler/**/laufbahnplanung');
		schuelerLaufbahntabellePage.ladeConfig(schueler);
		await schuelerLaufbahntabellePage.vergleicheFaecher_API_mit_Tabelle();
	}
});

test("Testen Clicken der Fächerwahlen ", async ({ schuelerLaufbahntabellePage, page }) => {
	test.setTimeout(120000);
	for (const schueler of dataSchueler_1_pro_JGab9) {
		await page.getByRole('row', { name: schueler.name2click}).click();
	    await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await expect(page.getByRole('button', { name: 'Exportieren' })).toBeVisible();
	    await page.waitForURL('**/schueler/**/laufbahnplanung');
		schuelerLaufbahntabellePage.ladeConfig(schueler);
		await schuelerLaufbahntabellePage.vergleicheFaecher_API_mit_Tabelle();
		await schuelerLaufbahntabellePage.clickZelle_EF_bis_Q2_alle_Faecher();
		//2 Sekungen Pause bis zur Kontrolle
		await page.waitForTimeout(2000);
		await schuelerLaufbahntabellePage.vergleicheFaecher_API_mit_Tabelle();
	}

});

