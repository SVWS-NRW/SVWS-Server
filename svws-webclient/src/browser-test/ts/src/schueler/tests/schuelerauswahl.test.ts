import { test, expect } from "../schuelerfixtures"

import { config } from "src/config/data.connection";
import { config_schueler } from "src/config/data.schueler";

test.beforeEach(async ({ baseURL, page, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(config.server.servername, config.server.benutzername, config.server.passwort);
	// await page.goto(`${baseURL}schueler/9115/laufbahnplanung`);
	// await page.getByRole('row', { name: schueler.name }).click();
	// await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
	// schuelerLaufbahnPage.ladeConfig(schueler.id);
	await page.waitForURL('**/schueler/**/daten');
});

test("Auswahl editieren", async ({ schuelerauswahlPage }) => {
	await schuelerauswahlPage.pruefeAttributAriaLabel();
});
