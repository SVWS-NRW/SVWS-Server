import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { oeffneBereich, pruefeZeilenwechsel } from '../helpers/navigation';

test.describe('Lehrkräfte — Smoke', () => {

	test('Lehrkräfteliste öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/lehrkraefte', /\/lehrkraefte\/\d+\/daten$/);

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);

		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Lehrkräftesuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/lehrkraefte', /\/lehrkraefte\/\d+\/daten$/);
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
	test('Lehrkräftefilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/lehrkraefte', /\/lehrkraefte\/\d+\/daten$/);
		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		const vorher = await filterPanel.locator('[role="combobox"]').count();

		await page.getByRole('button', { name: 'Filter' }).click();
		await page.waitForTimeout(600);

		const nachher = await filterPanel.locator('[role="combobox"]').count();
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Lehrkräfteliste: Zeilenklick wechselt die Lehrkraft @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/lehrkraefte', /\/lehrkraefte\/\d+\/daten$/);

		await pruefeZeilenwechsel(page, 'lehrkraefte', 'Lehrkräfte');
		await page.waitForLoadState('networkidle');

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).toHaveURL(/\/lehrkraefte\/\d+\/daten$/);
		await expect(page.locator('main').first()).not.toBeEmpty();

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Lehrkraft → Tab Stundenplan zeigt echten Inhalt @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/lehrkraefte', /\/lehrkraefte\/\d+\/daten$/);
		await page.waitForLoadState('networkidle');

		// Der Tab ist ein einfacher <button> (kein role="tab") mit dem Text in
		// einem <span>; die Rolle "button" trifft das klickbare Element. Die
		// Navigation dauert ca. 1,6 s — die URL-Assertion unten wartet darauf,
		// ein Wiederholungsklick ist nicht nötig (live gemessen: Erstklick 8/8).
		const stundenplanTab = page.getByRole('button', { name: 'Stundenplan', exact: true });
		await expect(stundenplanTab).toBeVisible({ timeout: 8000 });
		await stundenplanTab.click();

		await expect(page, 'Klick auf "Stundenplan" muss in die Stundenplan-Unterroute navigieren').toHaveURL(/\/stundenplan\//);
		await expect(page).not.toHaveURL(/\/error/);
		// Spezifischer Inhaltsnachweis statt reiner Container-Sichtbarkeit: ein
		// echter Stundenplan zeigt Wochentage.
		await expect(page.getByText('Montag', { exact: true })).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// "Neu" öffnet hier kein Modal, sondern navigiert auf eine eigene
	// /neu-Route mit Formular (Speichern-/Abbrechen-Button) — live bestätigt,
	// kein role="dialog" in dieser App.
	test('Lehrkräfte → Neu-Formular öffnet und lässt sich abbrechen @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/lehrkraefte', /\/lehrkraefte\/\d+\/daten$/);
		// Ein Klick auf "Neu" während der Weiterleitung verpufft (live
		// bestätigt); oeffneBereich wartet deshalb auf die abgeschlossene
		// Weiterleitung.
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
