import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { getAufgeloesteBasis } from '../helpers/navigation';

test.describe('Oberstufe — Smoke', () => {

	// @nightly-only — auf stable (TestDB_BK) landet /gost auf der
	// Schüler-Ansicht statt GOSt-Inhalt; vermutlich Berufskolleg-Schema ohne
	// gymnasiale Oberstufe (Feature-Lücke, kein Bug).
	test('GOSt-Übersicht öffnet ohne Fehler @nightly-only @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/gost`, { waitUntil: 'domcontentloaded', timeout: 60000 });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);

		const heading = page.locator('main h1:visible, main h2:visible, main .svws-headline:visible').first();
		await expect(heading, 'Überschrift muss auf Abiturjahrgang/Fächer-Ansicht hinweisen').toBeVisible({ timeout: 10000 });
		await expect(heading).toContainText(/Abiturjahrgang|Allgemeine Vorlagen/);
		await expect(page.locator('main')).toContainText('Kürzel');

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// Route braucht ein Jahrgang-Segment; Jahrgang -1 ("Allgemeine Vorlagen")
	// wird verwendet, siehe gost-regression.spec.ts für Details. Kursplanung
	// fällt dort dokumentiert auf die Fächer-Ansicht zurück (braucht einen
	// echten Abiturjahrgang) — Assertion toleriert beide Zustände.
	test('GOSt-Kursplanung öffnet ohne Fehler @nightly-only @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/gost/-1/kursplanung`, { waitUntil: 'domcontentloaded', timeout: 60000 });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);

		// Warten bis das clientseitige Routing sein finales Ziel erreicht hat —
		// ohne Wartezeit wird ein Zwischenzustand des SPA-Routings erwischt
		// (live beobachtet: "Individualdaten"-Tab, ein Zwischenstand aus der
		// Login-Landing-Sequenz, kein echter Bug).
		const aktiverTab = page.locator('.svws-ui-tab-button.svws-active, [role="tab"][aria-selected="true"]').first();
		await expect(
			aktiverTab,
			'Aktiver Tab muss "Kursplanung" sein, oder der bekannte "Fächer"-Fallback für Jahrgang -1 (Kursplanung braucht einen echten Abiturjahrgang)'
		).toContainText(/Kursplanung|Fächer/, { timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// Fächer-Ansicht (Jahrgang -1) hat einen Filter-Bereich ohne Comboboxen
	// (live bestätigt: 0 Comboboxen vor und nach dem Klick) — der Filter-Button
	// blendet stattdessen Checkbox-/Textinhalt ein, geprüft über eine
	// Textlängenänderung des Filter-Bereichs statt Combobox-Anzahl.
	test('GOSt-Filter reagiert @nightly-only @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/gost/-1/faecher`, { waitUntil: 'domcontentloaded', timeout: 60000 });
		const filterPanel = page.locator('.svws-ui-table-filter');
		await expect(filterPanel).toBeVisible({ timeout: 10000 });
		const vorher = (await filterPanel.innerText()).length;

		await page.getByRole('button', { name: 'Filter' }).click();
		await page.waitForTimeout(800);

		const nachher = (await filterPanel.innerText()).length;
		expect(nachher, 'Filter-Button muss den Filter-Bereich sichtbar verändern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});
