import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { oeffneBereich, pruefeZeilenwechsel } from '../helpers/navigation';

test.describe('Klassen — Smoke', () => {

	test('Klassenliste öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/klassen', /\/klassen\/\d+\/daten$/);

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Klassensuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/klassen', /\/klassen\/\d+\/daten$/);
		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('Test');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// Filter-Bereich (.svws-ui-table-filter) ist standardmäßig eingeblendet;
	// der "Filter"-Button klappt ihn zu/auf — geprüft wird nur, dass der Klick
	// den Filter-Bereich real verändert, nicht welcher Wert gewählt wird.
	test('Klassenfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/klassen', /\/klassen\/\d+\/daten$/);
		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		const vorher = await filterPanel.locator('[role="combobox"]').count();

		await page.getByRole('button', { name: 'Filter' }).click();
		await page.waitForTimeout(600);

		const nachher = await filterPanel.locator('[role="combobox"]').count();
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Klassenliste: Zeilenklick wechselt die Klasse @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/klassen', /\/klassen\/\d+\/daten$/);

		await pruefeZeilenwechsel(page, 'klassen', 'Klassen');
		await page.waitForLoadState('networkidle');

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).toHaveURL(/\/klassen\/\d+\/daten$/);
		await expect(page.locator('main').first()).not.toBeEmpty();

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// "Neu" öffnet hier kein Modal, sondern navigiert auf eine eigene
	// /neu-Route mit Formular (Speichern-/Abbrechen-Button) — live bestätigt,
	// kein role="dialog" in dieser App.
	test('Klassen → Neu-Formular öffnet und lässt sich abbrechen @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/klassen', /\/klassen\/\d+\/daten$/);
		// Der Neu-Button existiert schon vor der Weiterleitung im DOM, ein Klick
		// währenddessen verpufft aber (live bestätigt); oeffneBereich wartet
		// deshalb auf die abgeschlossene Weiterleitung.
		await page.waitForLoadState('networkidle');

		// Der "Neu"-Button ist ein reiner Icon-Button ohne zugänglichen Namen
		// (kein aria-label/title/Text); das Plus-Icon ist deshalb der einzige
		// inhaltliche Anker. Ohne .first(): Mehrdeutigkeit soll auffallen.
		const neuBtn = page.locator('.secondary-menu--content button:has(.i-ri-add-line)');
		await expect(neuBtn).toBeVisible({ timeout: 8000 });
		await neuBtn.click();

		const speichernBtn = page.getByRole('button', { name: 'Speichern' });
		await expect(speichernBtn).toBeVisible({ timeout: 8000 });

		await page.getByRole('button', { name: 'Abbrechen' }).click();
		await expect(speichernBtn).not.toBeVisible({ timeout: 5000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});
