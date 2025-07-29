import { test, expect } from '@playwright/test';
import { useLoginUtils } from "../utils/LoginUtils";
import { frontendURL } from "../../../utils/APIUtils";

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = frontendURL;

test('Smoke Test für alte Gruppenprozesse, anhand des Jahrgänge Katalogs', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	// locators
	const headlineLocator = page.locator('.svws-headline');
	const subheadlineLocator = page.locator('.svws-subline');

	const auswahlItem1Checkbox = page.getByRole('row', { name: '5. Jahrgang' }).getByRole('checkbox');
	const auswahlItem2Checkbox = page.getByRole('row', { name: '6. Jahrgang' }).getByRole('checkbox');

	// loginAdmin
	await loginAdmin();

	// Schule -> Jahrgänge Katalog aufrufen
	await page.getByRole('link', { name: 'Schule' }).click();
	await page.getByRole('link', { name: 'Jahrgänge' }).click();

	// prüfen, ob Einzelansicht Individualdaten erscheint
	await expect(headlineLocator).toContainText('5. Jahrgang');
	await expect(subheadlineLocator).toContainText('05');

	// zwei Schüler selektieren
	await auswahlItem1Checkbox.check();
	await auswahlItem2Checkbox.check();

	// Prüfen, ob Mehrfachauswahl mit Schülern im Titel erscheint
	await expect(headlineLocator).toContainText('Gruppenprozesse');
	await expect(subheadlineLocator).toContainText('05, 06');

	// Prüfe richtige URL
	await expect(page).toHaveURL(new RegExp('.*/schule/jahrgaenge/gruppenprozesse'));

	// Selektion der Schüler zurücknehmen
	await auswahlItem1Checkbox.uncheck();
	await auswahlItem2Checkbox.uncheck();

	// prüfen, ob Einzelansicht Individualdaten erscheint
	await expect(headlineLocator).toContainText('5. Jahrgang');
	await expect(subheadlineLocator).toContainText('05');
})
