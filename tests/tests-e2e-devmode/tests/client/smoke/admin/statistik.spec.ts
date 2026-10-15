import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { getAufgeloesteBasis } from '../helpers/navigation';

test.describe('Statistik — Smoke', () => {

	// Statistik hat in GymAbiLite eine eigene Übersichtsseite
	// (/statistik/uebersicht) — anders als Berichte (kein Sidebar-Eintrag,
	// fällt auf Schüler-Individualdaten zurück). Die H1 bleibt dort leer,
	// H2/.svws-headline zeigt den Schulnamen (schulspezifisch, daher hier
	// nicht geprüft). Das genaue Section-Label unterscheidet sich zwischen
	// Umgebungen (TestDB_GY: "Schülerdaten"/"Lehrerdaten"; GymAbi01 in der
	// Pipeline: "Schüler"/"Lehrer") — auf die stabilen, in beiden Fällen
	// enthaltenen Teilstrings "Schüler"/"Lehrer" geprüft (Substring-Match).
	test('Statistik-Übersicht öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/statistik`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);

		const main = page.locator('main');
		await expect(main).toContainText('Schüler', { timeout: 10000 });
		await expect(main).toContainText('Lehrer');

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});
