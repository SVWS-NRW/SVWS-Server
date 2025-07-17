import { test, expect } from '@playwright/test';

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = process.env.VITE_targetHost ?? "https://localhost"

test.skip('test', async ({ page }) => {
	await page.goto(targetHost + '/#/login?redirect=/');
	await page.getByLabel('Benutzername').click();
	await page.getByLabel('Benutzername').fill('admin');
	await page.getByRole('button', { name: 'Anmelden' }).click();
	await expect(page.getByRole('heading', { name: 'Schüler' })).toBeVisible();
	await page.getByRole('cell', { name: 'Triebel' }).click();
	await expect(page.locator('header')).toContainText('ID: 1010');
	await page.getByRole('button', { name: 'Sonstiges' }).click();
	await page.getByRole('button', { name: 'Einwilligungen' }).click();
	await page.getByRole('button', { name: 'Lernplattformen' }).click();
	await page.getByRole('button', { name: 'Erziehungsberechtigte' }).click();
	await expect(page.getByText('Daten zu Caroline Triebel')).toBeVisible();
	await page.getByRole('button', { name: 'Ausbildungsbetriebe' }).click();
	await expect(page.getByText('Beschäftigungen')).toBeVisible();
	await page.getByRole('button', { name: 'KAoA' }).click();
	await expect(page.getByRole('button', { name: 'Neuer Eintrag' })).toBeVisible();
	await page.getByRole('button', { name: 'Lernabschnitte' }).click();
	await page.getByRole('button', { name: 'Schulbesuch' }).click();
	await expect(page.getByText('Fehlstunden (Summe)')).toBeVisible();
	await page.getByRole('button', { name: 'Allgemein' }).click();
	await expect(page.getByText('Klassenlehrer')).toBeVisible();
	await page.getByRole('button', { name: 'Klausuren' }).click();
	await expect(page.getByText('Planung öffnen')).toBeVisible();
	await page.getByRole('button', { name: 'Versetzung/Abschluss' }).click();
	await expect(page.getByText('istAbschlussPrognose')).toBeVisible();
	await page.getByRole('button', { name: 'Konferenz' }).click();
	await expect(page.getByLabel('Zeugnisbemerkungen')).toBeEmpty();
	await page.getByRole('button', { name: 'Zeugnisdruck' }).click();
	await expect(page.getByText('zeugnisart')).toBeVisible();
	await page.getByRole('button', { name: 'Nachprüfung' }).click();
	await expect(page.getByText('Dieser Bereich ist noch in Entwicklung.')).toBeVisible();
	await page.getByRole('button', { name: 'Sprachen' }).click();
	await expect(page.getByText('Sprachenfolge')).toBeVisible();
	await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
	await expect(page.getByRole('main')).toContainText('Belegprüfungsergebnisse');
	await page.getByRole('button', { name: 'Abitur' }).click();
	await expect(page.getByRole('main')).toContainText('normierte Punktsumme:');
	await page.getByRole('button', { name: 'Prüfung' }).click();
	await expect(page.locator('thead')).toContainText('mündliche Prüfung');
	await page.getByRole('button', { name: 'Stundenplan' }).click();
	await expect(page.locator('h3')).toContainText('Halbjahr');
	await page.getByRole('link', { name: 'Lehrkräfte' }).click();
	await expect(page.locator('header')).toContainText('ALBE');
	await page.getByRole('button', { name: 'Individualdaten' }).click();
	await expect(page.getByText('Wohnort und Kontaktdaten')).toBeVisible();
	await page.getByRole('button', { name: 'Personaldaten' }).click();
	await expect(page.getByText('Lehrämter')).toBeVisible();
	await page.getByRole('button', { name: 'Stundenplan' }).click();
	await expect(page.getByRole('heading', { name: 'Stundenplan 2. Halbjahr' })).toBeVisible();
	await page.getByRole('button', { name: 'Unterricht' }).click();
	await expect(page.getByRole('main')).toContainText('Unterrichtsdaten');
	await page.getByRole('button', { name: 'Einwilligungen' }).click();
	await expect(page.locator('h3')).toContainText('Nicht abgefragt');
	await page.getByRole('button', { name: 'Lernplattformen' }).click();
	await expect(page.getByText('Lernplattform')).toBeVisible();

});