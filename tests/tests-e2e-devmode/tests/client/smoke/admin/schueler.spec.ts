import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { getAufgeloesteBasis, oeffneBereich } from '../helpers/navigation';
import { SCHUELER_ID_MIT_DATEN } from '../kataloge.config';

test.describe('Schüler — Smoke', () => {

	test('Schülerliste öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/schueler', /\/schueler\/\d+\/daten$/);

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);

		const zeilen = page.locator('main .svws-ui-tr[role="row"], main table tbody tr');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schülersuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/schueler', /\/schueler\/\d+\/daten$/);
		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('TestLisa');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// Filter-Bereich (.svws-ui-table-filter) ist standardmäßig eingeblendet;
	// der "Filter"-Button klappt ihn zu/auf — geprüft wird nur, dass der Klick
	// den Filter-Bereich real verändert, nicht welcher Wert gewählt wird.
	test('Schülerfilter reagiert @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/schueler', /\/schueler\/\d+\/daten$/);
		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 8000 });
		const vorher = await filterPanel.locator('[role="combobox"]').count();

		await page.getByRole('button', { name: 'Filter' }).click();
		await page.waitForTimeout(600);

		const nachher = await filterPanel.locator('[role="combobox"]').count();
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// SCHUELER_ID_MIT_DATEN (nicht SCHUELER_ID): der Standard-Testschüler ist
	// ein bewusster Leer-Fall ohne Klassenzuordnung — Formularfelder und Tabs
	// wären dort leer und der Test würde nur "Seite lädt" statt echten Inhalt
	// beweisen.
	test('Schüler-Detailansicht zeigt Individualdaten-Formular @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schueler/${SCHUELER_ID_MIT_DATEN}/daten`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		const nachname = page.getByLabel('Nachname');
		await expect(nachname).toBeVisible({ timeout: 10000 });
		await expect(nachname).not.toHaveValue('');

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schüler → Tab Schulbesuch öffnet @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schueler/${SCHUELER_ID_MIT_DATEN}/schulbesuch`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).toHaveURL(/schulbesuch/);
		// Card-Überschrift statt reiner Container-Sichtbarkeit — würde
		// fehlschlagen, wenn die Route still auf eine andere Ansicht zurückfällt.
		await expect(
			page.locator('main .content-card--headline, main h3').filter({ hasText: 'Schulbesuchsjahre' })
		).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schüler → Tab Vermerke öffnet @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schueler/${SCHUELER_ID_MIT_DATEN}/sonstiges/vermerke`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page.getByRole('button', { name: 'Neuen Vermerk hinzufügen' })).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// KAoA ist schulform-/klassenabhängig — der Standard-Testschüler hat
	// keine Klasse und deshalb keinen KAoA-Tab. Mit einem Schüler, der einer
	// Klasse zugeordnet ist (SCHUELER_ID_MIT_DATEN), ist der Tab real
	// vorhanden (live bestätigt).
	test('Schüler → Tab KAoA öffnet @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schueler/${SCHUELER_ID_MIT_DATEN}/daten`, { waitUntil: 'networkidle' });
		await page.waitForTimeout(1500);

		const kaoaTab = page.locator('button.svws-ui-tab-button').filter({ hasText: 'KAoA' });
		await expect(kaoaTab).toBeVisible({ timeout: 8000 });
		await kaoaTab.click();
		await page.waitForLoadState('networkidle');

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).toHaveURL(/kaoa/);
		await expect(page.locator('main').first()).not.toBeEmpty();

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schüler → Tab Erziehungsberechtigte öffnet @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schueler/${SCHUELER_ID_MIT_DATEN}/daten`, { waitUntil: 'networkidle' });
		await page.waitForTimeout(1500);

		const tab = page.locator('button.svws-ui-tab-button').filter({ hasText: 'Erziehungsberechtigte' });
		await expect(tab).toBeVisible({ timeout: 8000 });
		await tab.click();
		await page.waitForLoadState('networkidle');

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).toHaveURL(/erziehungsberechtigte/);
		await expect(
			page.locator('main .content-card--headline, main h3').filter({ hasText: 'Erziehungsberechtigte' })
		).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schüler → Tab Betriebe öffnet @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schueler/${SCHUELER_ID_MIT_DATEN}/daten`, { waitUntil: 'networkidle' });
		await page.waitForTimeout(1500);

		const tab = page.locator('button.svws-ui-tab-button').filter({ hasText: 'Betriebe' });
		await expect(tab).toBeVisible({ timeout: 8000 });
		await tab.click();
		await page.waitForLoadState('networkidle');

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).toHaveURL(/betriebe/);
		await expect(
			page.locator('main .content-card--headline, main h3').filter({ hasText: 'Betriebe' })
		).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schüler → Tab Lernabschnitte öffnet @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schueler/${SCHUELER_ID_MIT_DATEN}/daten`, { waitUntil: 'networkidle' });
		await page.waitForTimeout(1500);

		const tab = page.locator('button.svws-ui-tab-button').filter({ hasText: 'Lernabschnitte' });
		await expect(tab).toBeVisible({ timeout: 8000 });
		await tab.click();
		await page.waitForLoadState('networkidle');

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).toHaveURL(/lernabschnitt/);
		await expect(page.locator('main').first()).not.toBeEmpty();

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schüler → Tab Sprachen öffnet @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schueler/${SCHUELER_ID_MIT_DATEN}/daten`, { waitUntil: 'networkidle' });
		await page.waitForTimeout(1500);

		const tab = page.locator('button.svws-ui-tab-button').filter({ hasText: 'Sprachen' });
		await expect(tab).toBeVisible({ timeout: 8000 });
		await tab.click();
		await page.waitForLoadState('networkidle');

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).toHaveURL(/sprachen/);
		await expect(
			page.locator('main .content-card--headline, main h3').filter({ hasText: 'Sprachenfolge' })
		).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Schüler → Tab Stundenplan zeigt echten Inhalt @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/schueler/${SCHUELER_ID_MIT_DATEN}/daten`, { waitUntil: 'networkidle' });
		await page.waitForTimeout(1500);

		const tab = page.locator('button.svws-ui-tab-button').filter({ hasText: 'Stundenplan' });
		await expect(tab).toBeVisible({ timeout: 8000 });
		await tab.click();
		await page.waitForLoadState('networkidle');

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).toHaveURL(/stundenplan/);
		// Spezifischer Inhaltsnachweis statt reiner Container-Sichtbarkeit: ein
		// echter Stundenplan zeigt Wochentage.
		await expect(page.getByText('Montag', { exact: true })).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// "Neu" öffnet hier kein Modal, sondern navigiert auf eine eigene
	// /neu-Route mit Formular — live bestätigt, kein role="dialog" in dieser
	// App. Der Submit-Button heißt hier "Anlegen" (nicht "Speichern" wie bei
	// Klassen/Kurse/Lehrkräfte).
	test('Schüler → Neu-Formular öffnet und lässt sich abbrechen @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/schueler', /\/schueler\/\d+\/daten$/);
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

		const anlegenBtn = page.getByRole('button', { name: 'Anlegen' });
		await expect(anlegenBtn).toBeVisible({ timeout: 8000 });

		// Nicht auf "Anlegen"-Button-Sichtbarkeit prüfen: das Label wird auf der
		// Zielseite von Abbrechen (eine beliebige Schüler-Detailansicht) für
		// eine andere, nicht verwandte Aktion wiederverwendet (live bestätigt) —
		// URL-Check statt Label-Check ist hier der robuste Beweis.
		await page.getByRole('button', { name: 'Abbrechen' }).click();
		await expect(page).not.toHaveURL(/\/neu$/, { timeout: 5000 });
		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});
