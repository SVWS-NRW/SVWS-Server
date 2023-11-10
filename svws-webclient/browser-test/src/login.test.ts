import { expect } from "@playwright/test";
import { test } from "./App.pages";

import { dataServerConnection } from "./DataServerConnection"

test("Login test", async ({ page, baseURL, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.goto(`${baseURL}schueler`);
	await page.waitForURL('**/schueler/**');
	await expect(page.getByRole('heading', { name: 'Schüler' }).locator('span')).toBeVisible();
});
