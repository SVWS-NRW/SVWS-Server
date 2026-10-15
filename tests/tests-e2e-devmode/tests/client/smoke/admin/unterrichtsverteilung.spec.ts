import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { getAufgeloesteBasis } from '../helpers/navigation';

test.describe('Unterrichtsverteilung — Smoke', () => {

	// /uv landet auf /uv/-1/lehrer — eine einzelne Übersichtsseite ohne
	// bestätigten Neu-Button oder Liste-mit-Detail-Muster (anders als
	// Schüler/Lehrkräfte); die Prüfung bleibt deshalb bewusst auf
	// Laden-ohne-Fehler und sichtbaren Inhalt beschränkt.
	test('Unterrichtsverteilung-Übersicht öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/uv`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);
		await expect(page.locator('main').first()).not.toBeEmpty();

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});
