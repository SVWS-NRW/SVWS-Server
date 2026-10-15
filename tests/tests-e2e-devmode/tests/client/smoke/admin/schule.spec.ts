import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { getAufgeloesteBasis } from '../helpers/navigation';

test.describe('Schule — Smoke', () => {

	test('Schule-Stammdaten öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/stammdaten/adressdaten`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);
		// Reines Formular ohne Tabelle — Nicht-Leerheit des Hauptinhalts statt
		// eines Zeilen-Locators.
		await expect(page.locator('main').first()).not.toBeEmpty();

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schule → Fächer-Katalog öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/faecher`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);
		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schule → Jahrgänge-Katalog öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schule/jahrgaenge`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);
		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});
