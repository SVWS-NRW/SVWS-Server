import { api } from "~/Api";
import { dataSchueler } from "../DataSchueler";
import { test } from "./SchuelerIndividualdaten.pages"

import { dataServerConnection } from "~/DataServerConnection";
import { expect } from "@playwright/test";

test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.waitForURL('**/schueler/**/daten');
});

// TODO - Beim Zurücksetzen werden bei Eingabefeldern kein Patch angestoßen.
// Gelöst : Schülerwechsel und wieder zurück
test("Contentcard editieren", async ({ schuelerIndividualdatenPage, baseURL, page}) => {
	await page.getByRole('row', { name: dataSchueler[0].name2click }).click();
	//Überprüfung der Testbedingungen : Torsten ist aus Rüthen(849)
	await api.verbinde();
	const api_schueler = await api.server.getSchuelerStammdaten(api.schema,dataSchueler[0].id);
	expect(api_schueler.wohnortID).toBe(849);
	await schuelerIndividualdatenPage.testeIndividualDaten_Allgemein();

});
