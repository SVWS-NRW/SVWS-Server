import { test, expect } from '@playwright/test';

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = process.env.VITE_targetHost ?? "http://localhost:3000"
test.setTimeout(60_000); // globalen Playwright Timeout auf 60 Sekunden setzen

test('Selektion von zwei Schülern öffnet die Individualdaten View für Schüler Gruppenprozesse', async ({page}) => {
	// login
	await page.goto(targetHost);
	await page.getByLabel('Benutzername').click();
	await page.getByLabel('Benutzername').fill('Admin');
	await page.getByRole('button', {name: 'Anmelden'}).click();

	// Initialen State prüfen
	await expect(page.locator('.svws-headline')).toContainText('Eleonora Externa');

	// zwei Schüler selektieren
	await page.getByRole('row', {name: '09a Ankel Matthias'}).getByRole('checkbox').check();
	await page.getByRole('row', {name: '09a Bechtel Kerstin'}).getByRole('checkbox').check();

	// prüfen ob Mehrfachauswahl mit Schülern im Titel erscheint
	await expect(page.locator('.svws-headline')).toContainText('Mehrfachauswahl');
	await expect(page.locator('.svws-subline')).toContainText('Matthias Ankel, Kerstin Bechtel');

	// Selektion der Schüler zurücknehmen
	await page.getByRole('row', {name: '09a Ankel Matthias'}).getByRole('checkbox').uncheck();
	await page.getByRole('row', {name: '09a Bechtel Kerstin'}).getByRole('checkbox').uncheck();

	// prüfen ob wieder die initiale Individualdaten Ansicht erscheint
	await expect(page.locator('.svws-headline')).toContainText('Eleonora Externa');
})
