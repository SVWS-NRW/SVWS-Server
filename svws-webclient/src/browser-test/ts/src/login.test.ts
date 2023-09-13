import { test } from "@playwright/test";
import LoginPage from "./LoginPage";
import {config} from "./config/data.connection"

const servername = "localhost"
const benutzername = "Admin"
const passwort = ""

test("Login test", async ({ page, baseURL }) => {
	await page.goto(`${baseURL}login`);
	const login = new LoginPage(page);
	await login.login(config.server.servername, config.server.benutzername, config.server.passwort);
	await page.goto(`${baseURL}schueler`);
	await page.waitForTimeout(2000);
});
