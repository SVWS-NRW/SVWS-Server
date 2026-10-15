import { expect, type Page } from '@playwright/test';

/**
 * Liefert die vom Router NACH DEM LOGIN tatsächlich aufgelöste Basis-URL
 * (inkl. echter schemaId), statt der statischen getAppBase().
 *
 * Hintergrund: login() navigiert selbst bereits einmal. Ein zweites
 * page.goto(getAppBase()+pfad) ist bei gesetztem VITE_targetHost
 * (schemaId-los, notwendig damit Login funktioniert) ohne schemaId-Kontext —
 * der Router fällt dann auf die zuletzt bekannte Route zurück. Diese Basis
 * HIER enthält die echte, vom Router bereits aufgelöste schemaId und macht
 * beliebig viele Folge-Navigationen zuverlässig.
 *
 * Ohne gesetztes VITE_targetHost (Dropdown-Login-Weg) funktioniert dieselbe
 * Regex ebenso — die schemaId ist dort genauso Teil der aufgelösten URL.
 */
export function getAufgeloesteBasis(page: Page): string {
	const match = (/^(.*#\/[^/]+\/\d+)\//.exec(page.url()))?.[1];
	if (match === undefined || match === '') {
		// Ein stiller Fallback auf die statische getAppBase() würde genau den
		// Stale-Base-Bug reproduzieren, den diese Funktion beheben soll (siehe
		// Docstring oben) — fail-fast statt einen unbemerkten Rückfall.
		throw new Error(`getAufgeloesteBasis: konnte Basis nicht aus URL parsen: ${page.url()}`);
	}
	return match;
}

/**
 * Öffnet einen Bereich und wartet, bis die client-seitige Weiterleitung auf
 * die Zielroute abgeschlossen ist. Ohne dieses Warten steht nach dem goto noch
 * die Seite der vorherigen Route im DOM (z. B. die Schüler-Auswahl der
 * Login-Landung); Klicks und Zählungen darauf verpuffen oder treffen die
 * falsche Liste. Bleibt die Weiterleitung aus oder passt die URL nicht zum
 * Zielmuster (z. B. /error oder Login), schlägt der Aufruf mit klarer Meldung
 * fehl.
 *
 * Voraussetzung ist eine gefüllte Testdatenbank: Die Weiterleitung auf
 * .../{id}/daten gibt es nur, wenn die Liste mindestens einen Eintrag hat.
 * Eine leere Liste macht den Aufruf bewusst rot (Teil des Smoke-Vertrags).
 *
 * Grenzen: Geprüft wird nur das Zielmuster. Ein passender Bereich mit
 * falschem Datensatz, ein falsches Schema bzw. Schuljahresabschnitt oder ein
 * nur kurz passender URL-Zustand bleiben grün; Schema, Abschnitt und
 * konkrete ID werden nicht verglichen. Die Zielmuster sollten am Ende
 * verankert sein (/daten$): URLs enden auf nightly ohne Query und Suffix
 * auf /daten (für klassen, kurse, lehrkraefte, schueler, einstellungen
 * gemessen).
 *
 * Nur für Bereiche verwenden, die tatsächlich weiterleiten (klassen, kurse,
 * lehrkraefte, schueler, einstellungen, stundenplan). Nicht für Routen ohne
 * Weiterleitung (notenmodul, gost, statistik, uv) und nicht für Tiefenlinks.
 *
 * @param page         die Seite (nach login())
 * @param route        Bereichsroute MIT führendem Slash, z. B. '/klassen'
 * @param zielMuster   erwartete URL nach der Weiterleitung, z. B. /\/klassen\/\d+\/daten$/
 * @returns die danach neu bestimmte Basis-URL (wie getAufgeloesteBasis)
 */
export async function oeffneBereich(page: Page, route: string, zielMuster: RegExp): Promise<string> {
	await page.goto(`${getAufgeloesteBasis(page)}${route}`, { waitUntil: 'networkidle' });
	await expect(page, `Weiterleitung von ${route} auf ${zielMuster} blieb aus`).toHaveURL(zielMuster);
	return getAufgeloesteBasis(page);
}

/** ID des ausgewählten Datensatzes aus .../{bereich}/{id}/daten, sonst undefined. */
function idAusUrl(page: Page, bereich: string): string | undefined {
	return new RegExp(`/${bereich}/(\\d+)/daten`).exec(page.url())?.[1];
}

/**
 * Merkt die ID aus der URL, klickt die erste NICHT ausgewählte Datenzeile der
 * Auswahltabelle und erwartet danach eine andere ID in der URL. Die
 * ausgewählte Zeile trägt die CSS-Klasse svws-clicked (Zeile des aktuell
 * angezeigten Eintrags), Datenzeilen die Klasse group; die Kopfzeile hat
 * beides nicht. Der Test ist damit unabhängig davon, welche Zeile die
 * Weiterleitung vorausgewählt hat.
 *
 * Hinweis Kurse: Die URL-ID direkt nach der Weiterleitung ist dort eine
 * Fremd-ID der Vorroute (gemessen: 9312 nach Schüler, 22 nach Klassen), während
 * die erste Zeile markiert ist und angezeigt wird. Der Vergleich gegen diese
 * URL-ID ist trotzdem sinnvoll, weil jede angeklickte Zeile ihre eigene ID in
 * die URL schreibt; er schlägt nur fehl, wenn die URL-ID zufällig der ID der
 * angeklickten Zeile entspricht.
 *
 * Voraussetzung: mindestens zwei Datenzeilen in der Testdatenbank. Aufruf erst
 * nach oeffneBereich().
 *
 * @param page         die Seite
 * @param bereich      URL-Bereich ohne Slash, z. B. 'klassen'
 * @param bezeichnung  Pluralbezeichnung für die Fehlermeldung, z. B. 'Klassen'
 */
export async function pruefeZeilenwechsel(page: Page, bereich: string, bezeichnung: string): Promise<void> {
	const datenzeilen = page.locator('[role="row"].group');
	await expect(datenzeilen.nth(1), `Smoke braucht ≥ 2 ${bezeichnung} in der Testdatenbank`).toBeVisible({ timeout: 10000 });

	const idVorher = idAusUrl(page, bereich);
	expect(idVorher, `Nach der Weiterleitung muss eine ${bezeichnung}-ID in der URL stehen`).toBeDefined();

	await page.locator('[role="row"].group:not(.svws-clicked)').first().click();
	await expect.poll(() => {
		const idNachher = idAusUrl(page, bereich);
		return idNachher !== undefined && idNachher !== idVorher;
	}, { message: `Klick auf eine nicht ausgewählte Zeile muss zu einem anderen Eintrag (${bezeichnung}) wechseln` }).toBe(true);
}
