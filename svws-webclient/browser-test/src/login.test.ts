import { expect } from "@playwright/test";
import { test } from "./App.pages";

import { dataServerConnection } from "./DataServerConnection"

test("Login test", async ({ page, baseURL, loginPage }) => {
	test.setTimeout(120000);
	await page.goto(`${baseURL}login`);
	await loginPage.login(dataServerConnection.server.servername, dataServerConnection.server.benutzername, dataServerConnection.server.passwort);
});
