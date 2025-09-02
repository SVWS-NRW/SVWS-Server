import { expect, test } from '@playwright/test';
import { useLoginUtils } from "../../utils/LoginUtils";
import { adminFrontendURL } from "../../../../../utils/APIUtils";

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = adminFrontendURL;

test('Smoke-Test - Basic', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	await loginAdmin();

	// prüfen, ob die Anmeldung fehlgeschlagen ist. Zugriff für Admin sollte nicht möglich sein
	const errorNotificationLocator = page.locator('.notification--text');
	await expect(errorNotificationLocator).toContainText("Passwort oder Benutzername falsch.");
});
