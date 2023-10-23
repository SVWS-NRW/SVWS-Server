import { test } from "./SchuelerLaufbahnplanung.pages"

import { dataServerConnection } from "~/DataServerConnection";

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

test.fixme("Teste PDF herunterladen", async({ schuelerlaufbahnplanungpage}) => {
	await schuelerlaufbahnplanungpage.clickPDFherunterladen();
})
