import { login } from '../helpers/auth';
import { collectErrors } from '../helpers/errors';
import { expect, test } from '../helpers/fixtures';
import { getAufgeloesteBasis, oeffneBereich } from '../helpers/navigation';

test.describe('Einstellungen — Smoke', () => {

	test('Einstellungen-Seite öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/einstellungen', /\/einstellungen\/benutzer\/\d+\/daten$/);

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);
		// Landet auf /einstellungen/benutzer/1/daten — dort stehen zwei H1
		// ("Einstellungen" und "Benutzer"); der Name wählt die Bereichs-H1.
		const heading = page.getByRole('heading', { level: 1, name: 'Einstellungen', exact: true });
		await expect(heading).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	test('Benutzersuche filtert die Liste @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/einstellungen/benutzer', /\/einstellungen\/benutzer\/\d+\/daten$/);
		const zeilen = page.getByRole('row');
		await expect(zeilen.first()).toBeVisible({ timeout: 10000 });
		const vorher = await zeilen.count();

		const suchfeld = page.getByPlaceholder('Suchen');
		await expect(suchfeld).toBeVisible({ timeout: 8000 });
		await suchfeld.fill('Admin');
		await page.waitForTimeout(500);

		const nachher = await zeilen.count();
		expect(nachher, 'Suche muss die Liste filtern').not.toBe(vorher);

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

	// Anders als bei Klassen/Kurse/Lehrkräfte/Schüler navigiert "Neu" hier nicht
	// auf eine /neu-Route, sondern öffnet das Modal "Benutzer hinzufügen"
	// (SModalBenutzerNeu.vue; URL bleibt unverändert, role="dialog" vorhanden —
	// auf nightly gemessen, das denselben Commit wie der Checkout fährt). Der
	// Bestätigen-Button heißt hier "Weiter".
	test('Einstellungen → Neu-Formular öffnet und lässt sich abbrechen @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await oeffneBereich(page, '/einstellungen/benutzer', /\/einstellungen\/benutzer\/\d+\/daten$/);
		// Ein Klick auf "Neu" während der Weiterleitung verpufft (live
		// bestätigt); oeffneBereich wartet deshalb auf die abgeschlossene
		// Weiterleitung.
		await page.waitForLoadState('networkidle');

		// Der "Neu"-Button ist ein reiner Icon-Button ohne zugänglichen Namen
		// (kein aria-label/title/Text); das Plus-Icon ist deshalb der einzige
		// inhaltliche Anker. Ohne .first(): Mehrdeutigkeit soll auffallen.
		const neuBtn = page.locator('.secondary-menu--content button:has(.i-ri-add-line)');
		await expect(neuBtn).toBeVisible({ timeout: 8000 });
		const urlVorher = page.url();
		await neuBtn.click();

		const dialog = page.getByRole('dialog');
		await expect(dialog, 'Klick auf "Neu" muss das Modal "Benutzer hinzufügen" öffnen').toBeVisible({ timeout: 8000 });
		await expect(page, 'Das Modal darf die Route nicht ändern').toHaveURL(urlVorher);

		const weiterBtn = page.getByRole('button', { name: 'Weiter' });
		await expect(weiterBtn).toBeVisible({ timeout: 8000 });

		await page.getByRole('button', { name: 'Abbrechen' }).click();
		await expect(weiterBtn).not.toBeVisible({ timeout: 5000 });
		await expect(dialog, 'Abbrechen muss das Modal schließen').not.toBeVisible({ timeout: 5000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});

test.describe('Benutzerprofil — Smoke', () => {

	// /benutzerprofil ist strukturell KEIN Unterpunkt von Einstellungen —
	// erreichbar nur über das Avatar-Menü, nicht über die
	// Einstellungen-Sidebar/das Secondary-Menu. Hier trotzdem mitgeführt,
	// weil beide Bereiche aus Nutzersicht "Einstellungen" sind.
	test('Benutzerprofil-Seite öffnet ohne Fehler @smoke', async ({ page }) => {
		await login(page);
		const errors = collectErrors(page);

		await page.goto(`${getAufgeloesteBasis(page)}/benutzerprofil`, { waitUntil: 'networkidle' });

		await expect(page).not.toHaveURL(/\/error/);
		await expect(page).not.toHaveURL(/\/login/);
		// H1 ist generisch "Benutzerprofil" (live bestätigt); die spezifischere
		// Unterreiter-Überschrift "Nutzereinstellungen" sitzt in H2/.svws-headline.
		const heading = page.getByRole('heading', { level: 1, name: 'Benutzerprofil', exact: true });
		await expect(heading).toBeVisible({ timeout: 10000 });

		expect(errors, `JS-Fehler: ${errors.join(' | ')}`).toHaveLength(0);
	});

});
