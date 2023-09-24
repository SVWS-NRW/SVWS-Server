import { test } from "./App.pages";

import { dataServerConnection } from "./DataServerConnection"

test("Login test", async ({ page, baseURL, loginPage }) => {
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
	await page.goto(`${baseURL}schueler`);
	await page.waitForTimeout(2000);
});
