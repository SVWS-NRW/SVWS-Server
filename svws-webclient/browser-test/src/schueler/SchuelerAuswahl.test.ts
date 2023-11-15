import { expect } from "@playwright/test";
import { test } from "./Schueler.pages"

import { dataServerConnection } from "~/DataServerConnection";

test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.waitForURL('**/schueler/**/daten');
	await expect(page.getByRole('heading', { name: 'Schüler' }).locator('span')).toBeVisible();
});



test.fixme("Schüler auswahl testen", async ({ schuelerauswahlPage }) => {
	await schuelerauswahlPage.clickAlleSchueler();
});

test.fixme("Filter entfernen", async ({ schuelerauswahlPage }) => {
	await schuelerauswahlPage.clearFilter();
});

test.fixme("Auswahl editieren", async ({ schuelerauswahlPage }) => {
	await schuelerauswahlPage.pruefeAttributAriaLabel();
});
