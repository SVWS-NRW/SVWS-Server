import { expect, test } from '@playwright/test';
import { useLoginUtils } from "../../utils/LoginUtils";
import { adminFrontendURL } from "../../../../../utils/APIUtils";

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = adminFrontendURL;

test('Smoke-Test - Basic', async ({ page }) => {
	const { loginRoot, logout } = useLoginUtils(targetHost, page);

	await loginRoot();

	///////////////////////////////////
	// ***** App Schema (Default) *****
	///////////////////////////////////
	const menuHeadlineLocator = page.locator('.secondary-menu--headline');
	// prüfen, ob Auswahlliste eingeblendet ist
	await expect(menuHeadlineLocator).toContainText("Schema")

	const headlineLocator = page.locator('.svws-headline-wrapper');
	// prüfen, ob App Headline eingeblendet ist
	await expect(headlineLocator).toBeVisible();
	expect((await headlineLocator.textContent())?.trim().length).toBeGreaterThan(0);

	const schemaUiCardsLocator = page.locator('.ui-card');
	// prüfen, ob App Ansicht mit allen UI-Card Funktionen eingeblendet sind
	await expect(schemaUiCardsLocator).toHaveCount(3);


	///////////////////////////////////
	// ***** App Schema Neu *****
	///////////////////////////////////
	const addBtnLocator = page.locator('.i-ri-add-line').locator('..');
	await addBtnLocator.click();
	const subHeadlineLocator = page.locator('.svws-subline');
	// prüfen, ob Headline der Schema Neu Route angezeigt wird
	await expect(subHeadlineLocator).toContainText("Neues Schema anlegen");
	const schemaNeuUiCardsLocator = page.locator('.ui-card');
	// prüfen, ob Schema Neu App Ansicht mit allen UI-Card Funktionen eingeblendet ist
	await expect(schemaNeuUiCardsLocator).toHaveCount(4);


	///////////////////////////////////
	// ***** App Konfiguration *****
	///////////////////////////////////
	await page.getByRole('link', { name: 'Konfiguration' }).click();
	await expect(page.locator('.svws-headline')).toContainText('Konfiguration des SVWS-Servers');
	// prüfen ob Zertifikat exportieren Button angezeigt wird
	const certificateBtnLocator = page.locator('.i-ri-upload-2-line').locator('..');
	await expect(certificateBtnLocator).toContainText("Zertifikat exportieren");

	// Logout prüfen
	await logout()
});
