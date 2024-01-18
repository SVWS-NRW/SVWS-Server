import { api } from "~/Api";
import { dataSchueler } from "../DataSchueler";
import { test } from "./SchuelerIndividualdaten.pages"

import { dataServerConnection } from "~/DataServerConnection";
import { expect } from "@playwright/test";

test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	// await page.goto(`${baseURL}schueler/9115/laufbahnplanung`);
	// await page.getByRole('row', { name: schueler.name }).click();
	// await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
	// schuelerLaufbahnPage.ladeConfig(schueler.id);
	await page.waitForURL('**/schueler/**/daten');
	await page.getByRole('row', { name: dataSchueler[0].name2click }).click();

	//Überprüfung der Testbedingungen : Torsten ist aus Rüthen(849)
	await api.verbinde();
	const api_schueler = await api.server.getSchuelerStammdaten(api.schema,dataSchueler[0].id);
	expect(api_schueler.wohnortID).toBe(849);
});


// TODO Warte auf #1443
test.fixme("Contentcard editieren", async ({ schuelerIndividualdatenPage, page}) => {
	await schuelerIndividualdatenPage.testeIndividualDaten_Allgemein();
});
