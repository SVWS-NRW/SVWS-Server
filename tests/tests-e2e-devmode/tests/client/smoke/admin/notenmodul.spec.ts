import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { getAufgeloesteBasis } from '../helpers/navigation';

test.describe('Notenmodul — Smoke', () => {

	test('Notenmodul-Seite öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/notenmodul`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);
		await expect(page).toHaveURL(/notenmodul/);
		const heading = page.locator('main h1, h1').first();
		await expect(heading).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// FIXME (2026-10-15): Seit 2d03962d2 (ENM-Ansichten) zeigt der Admin im CI-Container weder
	// "Zugangsdaten" noch die Auswahl "Ansicht". Offen: App-Fehler (Menü nach ladeDaten nicht
	// aktualisiert) oder fehlendes Recht NOTENMODUL_ADMINISTRATION in der Test-DB. Siehe MR !3311.
	test.fixme('Notenmodul → Zugangsdaten öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/notenmodul`, { waitUntil: 'networkidle' });

		// Seit 2d03962d2 (ENM-Funktionsansichten) sind Administration und Zugangsdaten
		// nur in der Ansicht "Notenmoduladministrator" sichtbar. Ist der Admin zugleich
		// Lehrkraft (CI-Container), startet die App in "Fachlehrer/-in" und zeigt die
		// Auswahl "Ansicht"; sonst ist die Admin-Ansicht direkt aktiv.
		const zugangsdatenLink = page.getByRole('link', { name: 'Zugangsdaten' });
		const ansicht = page.getByRole('combobox', { name: 'Ansicht' });
		await expect(zugangsdatenLink.or(ansicht).first(), 'Weder Zugangsdaten noch Auswahl "Ansicht" sichtbar').toBeVisible({ timeout: 15000 });
		if (!await zugangsdatenLink.isVisible()) {
			await ansicht.click();
			await page.getByRole('option', { name: 'Notenmoduladministrator', exact: true }).click();
		}
		await expect(zugangsdatenLink, 'Zugangsdaten fehlen trotz Ansicht Notenmoduladministrator').toBeVisible({ timeout: 15000 });
		await zugangsdatenLink.click();
		await page.waitForLoadState('networkidle');

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page.locator('main').first()).not.toBeEmpty();

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});
