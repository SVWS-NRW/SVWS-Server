import { test } from "./SchuelerIndividualdaten.pages"

import { dataServerConnection } from "~/DataServerConnection";

test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	// await page.goto(`${baseURL}schueler/9115/laufbahnplanung`);
	// await page.getByRole('row', { name: schueler.name }).click();
	// await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
	// schuelerLaufbahnPage.ladeConfig(schueler.id);
	await page.waitForURL('**/schueler/**/daten');
});

test("Contentcard editieren", async ({ schuelerIndividualdatenPage }) => {
	await schuelerIndividualdatenPage.fillContentCardAllgemein();
});
