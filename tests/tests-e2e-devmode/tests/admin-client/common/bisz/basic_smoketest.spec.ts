import { expect, test } from '@playwright/test';
import { adminFrontendURL } from "../../../../../utils/APIUtils";

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = adminFrontendURL;

test('Smoke-Test - Basic', async ({ page }) => {
	await page.goto(targetHost);
	await page.waitForURL("**/login?redirect=/**", { timeout: 20_000 });
	await page.getByLabel('Benutzername').click();
	await page.getByLabel('Benutzername').fill('BISZ');
	await page.getByLabel('Passwort').click();
	await page.getByLabel('Passwort').fill('BISZ');
	await page.getByRole('button', { name: 'Anmelden' }).click();

	// prüfen, ob die Anmeldung fehlgeschlagen ist. Zugriff für Admin sollte nicht möglich sein
	const errorNotificationLocator = page.locator('.notification--text');
	await expect(errorNotificationLocator).toContainText("Passwort oder Benutzername falsch.");
});
