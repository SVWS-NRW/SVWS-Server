import { expect, test } from '@playwright/test';

import { frontendURL } from "../../../../../utils/APIUtils";
import { useLoginUtilsClient } from "../../utils/LoginUtils";

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = frontendURL;

test('Alle Apps — Sidebar-Navigation Komplett @smoke', async ({ page }) => {
	test.setTimeout(120_000);
	const { loginAdmin, logout } = useLoginUtilsClient(targetHost, page);

	await loginAdmin();

	// Default App ist Schüler
	await expect(page.locator('header')).toContainText('Eleonora Externa');

	await page.getByRole('link', { name: 'Schule' }).click();
	await expect(page.locator('header')).toContainText('Albert Zweistein Gymnasium');
	await page.getByRole('link', { name: 'Schüler', exact: true }).click();
	await expect(page.locator('header')).toContainText('Eleonora Externa');
	await page.getByRole('link', { name: 'Lehrkräfte' }).click();
	await expect(page.locator('header')).toContainText('Mike Albers ID: 1');
	// MERGE (2026-08-02, aus svws-qa/svws-tests smoke/lehrkraefte.spec.ts +
	// lehrer-nav.spec.ts): der Header-Beleg oben zeigt die SPEZIFISCH
	// fokussierte Lehrkraft. Er beweist aber nicht, dass die dahinterliegende
	// Liste echte Daten hat — nur, dass der App-Header-String (eine geteilte
	// Layout-Komponente, die theoretisch entkoppelt vom Hauptinhalt
	// aktualisiert werden könnte) den erwarteten Text zeigt. Ergaenzt um
	// einen vom Header UNABHAENGIGEN Beleg: mindestens eine echte Tabellen-
	// zeile im Hauptinhalt sichtbar (Datenpräsenz-Beweis, nicht nur Text-
	// Vorhandensein).
	const lehrkraefteZeilen = page.locator('.svws-ui-table tbody tr, [role="row"]').filter({ hasText: /\S/ });
	await expect(lehrkraefteZeilen.first(), 'Lehrkräfte-Tabelle muss mindestens einen Eintrag zeigen').toBeVisible();
	await page.getByRole('link', { name: 'Klassen' }).click();
	await expect(page.locator('header')).toContainText('Klasse 05a ID: 22');
	// MERGE (2026-08-02, Pilot-Verschmelzung mit svws-qa/svws-tests
	// smoke/klassen.spec.ts): der Header-Beleg oben beweist eine SPEZIFISCHE
	// Entitaet (Klasse 05a ID: 22, Testdaten-abhaengig). Ergaenzt um zwei
	// testdaten-unabhaengige Belege aus Pauls gehaerteter Klassen-Smoke-
	// Vorlage: ein stabiler Modul-Heading-Beweis (bleibt "Klassen" auch wenn
	// die Default-Klasse sich aendert) und ein expliziter Error-Guard.
	await expect(page).not.toHaveURL(/\/error/);
	await expect(page.locator('main h1, h1').filter({ hasText: /^Klassen$/ })).toBeVisible();
	await page.getByRole('link', { name: 'Kurse' }).click();
	await expect(page.locator('header')).toContainText('N-F-1');
	// MERGE (2026-08-02, aus svws-qa/svws-tests smoke/kurse.spec.ts): der
	// Header-Beleg zeigt den spezifischen Kurs. Ergaenzt um einen vom
	// Header unabhaengigen Beweis (Modul-Ueberschrift + URL), damit ein
	// "Header aktualisiert, Hauptinhalt aber nicht geladen"-Fehlerbild
	// (Header ist eine geteilte Layout-Komponente) nicht unbemerkt bliebe.
	await expect(page).not.toHaveURL(/\/error/);
	await expect(page.locator('main h1, h1').filter({ hasText: /^Kurse$/ })).toBeVisible();
	// FIX (2026-08-03): container-robust gemacht. Zwei Probleme behoben:
	// (1) Noten-Klick ist ein echter, reproduzierbarer Timing-Flake (live
	// gegen nightly bestaetigt: die Navigation nach /notenmodul/... kann
	// spuerbar laenger als das Standard-expect()-Timeout dauern, bevor sie
	// durchschlaegt) — ein explizites waitForURL() auf das stabile
	// URL-Segment faengt das robust ab, statt sich auf die implizite
	// Text-Polling-Wartezeit von toContainText() zu verlassen.
	// (2) Der Oberstufe-Header war auf den Datenwert "Abiturjahrgang 2019"
	// gepinnt — Container (GymAbi01) und nightly haben unterschiedliche
	// Abiturjahrgangs-/Notenstaende, der Jahreswert ist zudem grundsaetzlich
	// zeitabhaengig (aendert sich mit dem Schuljahr). Live bestaetigt zeigt
	// nightly aktuell "Allgemeine Vorlagen" (Jahrgang -1), nicht "2019" —
	// die alte Assertion war also bereits vor diesem Fix auf nightly
	// fragil, nicht nur auf dem Container. Geprueft wird jetzt bewusst die
	// Route/das Modul (URL-Segment + musterbasierter, datenneutraler
	// Heading-Beleg), nicht der konkrete Datenwert.
	// FIX (2026-10-15): Seit 2d03962d2 (ENM-Funktionsansichten) laedt das Notenmodul
	// beim Betreten alle ENM-Daten. In CI blieb die Navigation nach dem Klick
	// sporadisch aus (Retry noetig). Klick + Ziel-URL werden daher gemeinsam
	// wiederholt, statt nur laenger zu warten.
	await expect(async () => {
		await page.getByRole('link', { name: 'Noten' }).click();
		await page.waitForURL(/notenmodul/, { timeout: 10000 });
	}, 'Navigation zu Noten blieb aus').toPass({ timeout: 45000 });
	await expect(page.locator('header')).toContainText('Leistungsdaten', { timeout: 15000 });
	await page.getByRole('link', { name: 'Oberstufe' }).click();
	await page.waitForURL(/gost/, { timeout: 15000 });
	await expect(page.locator('header')).toContainText(/Abiturjahrgang \d{4}|Oberstufe|Vorlagen/);
	// MERGE (2026-08-02, aus svws-qa/svws-tests smoke/gost.spec.ts), ergaenzt
	// 2026-08-03: Tabellen-Struktur-Beweis ("Kürzel"-Spalte), den der Kunde
	// nirgends prueft — bleibt unveraendert, ist bereits datenneutral.
	await expect(page.locator('main')).toContainText('Kürzel');
	// FIX (2026-10-15): In CI blieb der Header nach dem Statistik-Klick auf
	// "Allgemeine Vorlagen" (Oberstufe) stehen, der Klick wurde verschluckt.
	await expect(async () => {
		await page.getByRole('link', { name: 'Statistik' }).click();
		await expect(page.locator('header')).toContainText('Albert Zweistein Gymnasium', { timeout: 5000 });
	}, 'Navigation zu Statistik blieb aus').toPass({ timeout: 30000 });
	await page.getByRole('link', { name: 'Stundenplan' }).click();
	await expect(page.locator('header')).toContainText('Stundenplan 2. Halbjahr ID: 1');
	// MERGE (2026-08-02, aus svws-qa/svws-tests smoke/stundenplan.spec.ts):
	// gleiches Muster wie Klassen/Kurse — stabiler Modul-Heading-Beweis
	// unabhaengig vom Header-String.
	await expect(page).not.toHaveURL(/\/error/);
	await expect(page.locator('main h1, h1').filter({ hasText: /^Stundenplan$/ })).toBeVisible();
	await page.getByRole('link', { name: 'Einstellungen' }).click();
	await expect(page.locator('header')).toContainText('E-Mail-Server');

	await logout();
});
