import { test, expect } from '@playwright/test';
import { useLoginUtils } from "../../utils/LoginUtils";
import { getResetButton, getSaveButton } from "../../utils/SchuelerGruppenprozesseUtils";
import { frontendURL } from "../../../../../utils/APIUtils";

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = frontendURL;

test('Smoke Test Gruppenprozesse', async ({ page }) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);

	// locators
	const headlineLocator = page.locator('.svws-headline');
	const subheadlineLocator = page.locator('.svws-subline');

	const saveBtnLocator = await getSaveButton(page);
	const resetBtnLocator = await getResetButton(page);

	const auswahlItem1Checkbox = page.getByRole('row', { name: '09a Ankel Matthias' }).getByRole('checkbox');
	const auswahlItem2Checkbox = page.getByRole('row', { name: '09a Bechtel Kerstin' }).getByRole('checkbox');

	const statusSelectFieldLocator = page.locator('.ui-select').filter({ has: page.getByLabel('Status') })

	// loginAdmin
	await loginAdmin();

	// Prüfe Headline von initialer Page
	await expect(headlineLocator).toContainText('Eleonora Externa');
	await expect(subheadlineLocator).toBeHidden();

	// zwei Schüler selektieren
	await auswahlItem1Checkbox.check();
	await auswahlItem2Checkbox.check();

	// Prüfen, ob Mehrfachauswahl mit Schülern im Titel erscheint
	await expect(headlineLocator).toContainText('Mehrfachauswahl');
	await expect(subheadlineLocator).toContainText('Matthias Ankel, Kerstin Bechtel');

	// Prüfe richtige URL
	await expect(page).toHaveURL(new RegExp('.*/schueler/gruppenprozesse/daten'));

	// Prüfen, ob alle Tabs angezeigt werden und der richtige Tab als Default selektiert wurde
	const tabsLocator = page.locator('.svws-ui-tab-button');
	await expect(tabsLocator).toContainText(['Allgemeines', 'Individualdaten']);
	await expect(tabsLocator.locator(':scope.svws-active')).toContainText('Individualdaten');

	// Prüfen ob alle Individualdaten Content-Cards angezeigt werden
	await expect(page.locator('.content-card--header'))
		.toContainText(['Statusdaten', 'Staatsangehörigkeit und Konfession', 'Migrationshintergrund'])
	// Prüfen, ob Status initial leer
	await expect(statusSelectFieldLocator).toContainText("");

	// Status Änderung vornehmen
	await statusSelectFieldLocator.click();
	await statusSelectFieldLocator.locator('li').filter({ hasText: '2 - Aktiv' }).click();

	// Prüfen, ob Änderung im Feld angezeigt wird und Auswahl nicht mehr möglich ist
	await expect(statusSelectFieldLocator).toContainText('2 - Aktiv')
	await expect(auswahlItem1Checkbox).toBeDisabled();

	// Änderungen zurücksetzen
	await resetBtnLocator.click()

	// Prüfen, ob ohne offene Änderungen Auswahl möglich ist
	await expect(auswahlItem1Checkbox).toBeEnabled();

	// Status Änderung vornehmen und speichern
	await statusSelectFieldLocator.click();
	await statusSelectFieldLocator.locator('li').filter({ hasText: '2 - Aktiv' }).click();
	await saveBtnLocator.click();

	// Prüfe, ob Status danach geleert wurde und Aktions Buttons disabled sind und Auswahl wieder aktiv ist
	await expect(statusSelectFieldLocator).toContainText("");
	await expect(saveBtnLocator).toBeDisabled();
	await expect(resetBtnLocator).toBeDisabled();
	await expect(auswahlItem1Checkbox).toBeEnabled();

	// Selektion der Schüler zurücknehmen
	await auswahlItem1Checkbox.uncheck();
	await auswahlItem2Checkbox.uncheck();

	// prüfen, ob Einzelansicht Individualdaten erscheint
	await expect(headlineLocator).toContainText('Eleonora Externa');
	await expect(subheadlineLocator).toBeHidden();
})
