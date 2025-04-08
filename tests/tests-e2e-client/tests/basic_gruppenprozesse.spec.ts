import {test, expect} from '@playwright/test';

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = process.env.VITE_targetHost ?? "https://localhost"

test('Mehrfachauswahl von Schuelern erzeugt View für Gruppenprozesse', async ({page}) => {
	test.setTimeout(60_000);
	await page.goto(targetHost + '/#/login?redirect=/');
	await page.getByLabel('Benutzername').click();
	await page.getByLabel('Benutzername').fill('Admin');
	await page.getByRole('button', {name: 'Anmelden'}).click();
	await expect(page.locator('header')).toContainText('Eleonora Externa');
	await page.getByRole('row', {name: '09a Ankel Matthias'}).getByRole('checkbox').check();
	await page.getByRole('row', {name: '09a Bechtel Kerstin'}).getByRole('checkbox').check();
	await expect(page.locator('header')).toContainText('Gruppenprozesse');
	await expect(page.locator('header')).toContainText('Matthias Ankel, Kerstin Bechtel');
	await page.waitForTimeout(2000);
	await page.getByRole('row', {name: '09a Ankel Matthias'}).getByRole('checkbox').uncheck();
	await page.getByRole('row', {name: '09a Bechtel Kerstin'}).getByRole('checkbox').uncheck();
	await page.getByRole('cell', {name: 'Externa'}).click();
	await page.waitForTimeout(2000);
	await expect(page.locator('header')).toContainText('Eleonora Externa');
})
