import { expect, test } from '@playwright/test';
import { useLoginUtils } from "../../utils/LoginUtils";
import { frontendURL } from "../../../../../utils/APIUtils";

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = frontendURL;

test('Smoke-Test - Basic', async ({ page }) => {
	const { loginRoot } = useLoginUtils(targetHost, page);

	await loginRoot();

	// prüfen, ob die Anmeldung fehlgeschlagen ist. Zugriff für root User sollte nicht möglich sein
	const errorNotificationLocator = page.locator('.notification--text');
	await expect(errorNotificationLocator).toContainText("Passwort oder Benutzername falsch.");
});
