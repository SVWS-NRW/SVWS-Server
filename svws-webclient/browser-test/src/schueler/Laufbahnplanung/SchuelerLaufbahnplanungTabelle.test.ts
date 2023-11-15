import type { Page } from "@playwright/test";

import { test } from "./SchuelerLaufbahnplanung.pages";

import { dataServerConnection } from "../../DataServerConnection"
import { dataSchueler , type Schueler } from "../DataSchueler"

import type { SchuelerLaufbahnPage } from "./PageSchuelerLaufbahn";
import type { SchuelerLaufbahntabellePage } from "./PageSchuelerLaufbahntabelle";


process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';


test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.goto(`${baseURL}schueler/9115/laufbahnplanung`);
	// await page.getByRole('row', { name: schueler.d }).click();
	// await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
	// schuelerLaufbahnPage.ladeConfig(schueler.id);
	// await page.waitForURL('**/laufbahnplanung');
});

const tabclicken = async (page: Page, schueler: Schueler, sp : SchuelerLaufbahnPage | SchuelerLaufbahntabellePage) => {
	await page.getByRole('row', { name: schueler.name }).click();
	await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
	sp.ladeConfig(schueler);
	await page.waitForURL('**/schueler/**/laufbahnplanung');
};


for (const schueler of dataSchueler) {

	test.skip(`Laufbahntabelle Sichtbarkeit Tabellentexte mit ${schueler.name}`, async ({ page, schuelerLaufbahntabellePage }) => {
		await tabclicken(page, schueler,schuelerLaufbahntabellePage);
		await schuelerLaufbahntabellePage.pruefeSichtbarkeit();
	});

	test.skip(`Belegsprüfungserbegnisse editieren mit ${schueler.name}`, async ({ schuelerLaufbahnPage, page}) => {
		await tabclicken(page, schueler,schuelerLaufbahnPage);
		await schuelerLaufbahnPage.testeEingabeBelegpruefungsergebnisse();
	});

	test.skip(`Beratungsdaten editieren mit ${schueler.name}`, async ({ schuelerLaufbahnPage, page }) => {
		await tabclicken(page, schueler,schuelerLaufbahnPage);
		await schuelerLaufbahnPage.testeEingabeBeratung();
	});

	test.skip(`Vergleiche Faecher zwischen API und Website mit ${schueler.name}`, async ({ schuelerLaufbahntabellePage, page }) => {
		await tabclicken(page, schueler,schuelerLaufbahntabellePage);
		await schuelerLaufbahntabellePage.vergleicheFaecher_API_Webseite();
	});

	test.skip(`Testen Clicken der Fächerwahlen mit ${schueler.name}`, async ({ schuelerLaufbahntabellePage, page }) => {
		await tabclicken(page, schueler,schuelerLaufbahntabellePage);
		await schuelerLaufbahntabellePage.ladeFachbelegungeng_von_Webseite();
		await schuelerLaufbahntabellePage.clickZelle_EF_bis_Q2_alle_Faecher();
		await schuelerLaufbahntabellePage.vergleicheFaecher_API_Webseite();
	});

	/**
	 * TODO  fehlende Entscheidung : Sollen die Tests dieser Datei seperat und einem Test ausgeführt werden?
	 */

	// test(`Jeder Test mit ${schueler.name}`, async ({ page, schuelerLaufbahntabellePage, schuelerLaufbahnPage, errorPage }) => {
	// 	await page.getByRole('row', { name: schueler.name }).click();
	// 	await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
	// 	await page.waitForURL('**/laufbahnplanung');
	// 	schuelerLaufbahnPage.ladeConfig(schueler);
	// 	schuelerLaufbahntabellePage.ladeConfig(schueler);
	// 	await schuelerLaufbahntabellePage.pruefeSichtbarkeit();
	// 	await schuelerLaufbahnPage.testeEingabeBelegpruefungsergebnisse();
	// 	await schuelerLaufbahnPage.testeEingabeBeratung();
	// 	await schuelerLaufbahnPage.clickImportieren(errorPage);
	// 	await schuelerLaufbahnPage.clickExportieren(errorPage);
	// 	await schuelerLaufbahntabellePage.vergleicheFaecher_API_Webseite();
	// 	await schuelerLaufbahntabellePage.clickZelle_EF_bis_Q2_alle_Faecher();
	// });

}

