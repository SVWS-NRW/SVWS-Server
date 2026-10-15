import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { getAufgeloesteBasis } from '../helpers/navigation';

test.describe('Stundenplan — Smoke', () => {

	// /stundenplan leitet client-seitig auf /stundenplan/{id}/daten weiter —
	// die URL-Prüfung bestätigt, dass diese Weiterleitung tatsächlich
	// abgeschlossen ist, statt nur einen Zwischenzustand zu erwischen.
	test('Stundenplan-Übersicht öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/stundenplan`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);
		await expect(page).toHaveURL(/\/stundenplan\/\d+\/daten/);
		await expect(page.locator('main').first()).not.toBeEmpty();

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// Stundenplan-Übersicht hat kein Suchfeld, nur den Schalter "Nur Aktive".
	// Direkt nach dem Login steht noch die Schüler-Auswahl im DOM (mit eigenem
	// Filter-Button und Comboboxen); erst die Weiterleitung auf
	// /stundenplan/{id}/daten ersetzt sie. Deshalb zuerst auf die
	// Weiterleitung warten, dann den echten Filter umschalten und den
	// Ausgangszustand wiederherstellen.
	test('Stundenplanfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/stundenplan`, { waitUntil: 'networkidle' });
		await expect(page).toHaveURL(/\/stundenplan\/\d+\/daten/);

		const nurAktive = page.locator('.svws-ui-table-filter').getByRole('checkbox', { name: 'Nur Aktive', exact: true });
		await expect(nurAktive).toBeVisible({ timeout: 8000 });
		const vorher = await nurAktive.isChecked();

		await nurAktive.click();
		await expect(nurAktive, 'Filter "Nur Aktive" muss umschaltbar sein').toBeChecked({ checked: !vorher });

		await nurAktive.click();
		await expect(nurAktive, 'Filter "Nur Aktive" muss auf den Ausgangswert zurückgestellt sein').toBeChecked({ checked: vorher });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});
