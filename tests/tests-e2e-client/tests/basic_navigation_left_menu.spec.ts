import {test, expect} from '@playwright/test';

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = process.env.VITE_targetHost ?? "https://localhost"

test('Basic_Navigation_left_menu', async ({page}) => {
	test.setTimeout(60_000);
	await page.goto(targetHost + '/#/login?redirect=/');
	await page.getByLabel('Benutzername').click();
	await page.getByLabel('Benutzername').fill('Admin');
	await page.getByRole('button', {name: 'Anmelden'}).click();
	await expect(page.locator('header')).toContainText('Eleonora Externa');
	await page.getByRole('link', {name: 'Schule'}).click();
	await expect(page.locator('header')).toContainText('Albert Zweistein Gymnasium');
	await page.getByRole('link', {name: 'Lehrkräfte'}).click();
	await expect(page.locator('header')).toContainText('Mike Albers ID: 1');
	await page.getByRole('link', {name: 'Klassen'}).click();
	await expect(page.locator('header')).toContainText('Klasse 05a ID: 22');
	await page.getByRole('link', {name: 'Kurse'}).click();
	await expect(page.locator('header')).toContainText('N-F-1');
	await page.getByRole('link', {name: 'Oberstufe'}).click();
	await expect(page.locator('header')).toContainText('Abiturjahrgang 2019');
	await page.getByRole('link', {name: 'Stundenplan'}).click();
	await expect(page.locator('header')).toContainText('Stundenplan 2. Halbjahr ID: 1');
	await page.getByRole('link', {name: 'Einstellungen'}).click();
	await expect(page.locator('header')).toContainText('Administrator');
	await page.getByRole('link', {name: 'Abmelden'}).click();
	await expect(page.locator('h1')).toContainText('SVWS NRW');
	await expect(page.getByRole('button', {name: 'Anmelden'})).toContainText('Anmelden');
});
