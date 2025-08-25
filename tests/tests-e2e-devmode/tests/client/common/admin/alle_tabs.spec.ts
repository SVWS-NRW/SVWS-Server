import { test, expect } from '@playwright/test';
import { useLoginUtils } from "../../utils/LoginUtils";

import { frontendURL } from '../../../../../utils/APIUtils'


test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = frontendURL;

test('Admins können entsprechende Bereiche im DEV Mode einsehen', async ({page}) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);
	await loginAdmin();

	await expect(page.getByRole('heading', {name: 'Schüler'})).toBeVisible();
	await page.getByRole('cell', {name: 'Triebel'}).click();
	await expect(page.locator('header')).toContainText('ID: 1010');


	await expect(page.getByRole('button', {name: 'Individualdaten'})).toBeVisible();
	await page.getByRole('button', {name: 'Individualdaten'}).click();
	await expect(page.getByText('Staatsangehörigkeit und Konfession')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Sonstiges'})).toBeVisible();
	await page.getByRole('button', {name: 'Sonstiges'}).click();
	await expect(page.getByRole('button', {name: 'Vermerke'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Einwilligungen'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Lernplattformen'})).toBeVisible();
	await page.getByRole('button', {name: 'Vermerke'}).click();
	await expect(page.getByText('Nur sichtbare Vermerkarten anzeigen')).toBeVisible();
	await page.getByRole('button', {name: 'Einwilligungen'}).click();
	await expect(page.getByText('Nicht abgefragt')).toBeVisible();
	await page.getByRole('button', {name: 'Lernplattformen'}).click();
	await expect(page.getByText('Einwilligung Abgefragt')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Erziehungsberechtigte'})).toBeVisible();
	await page.getByRole('button', {name: 'Erziehungsberechtigte'}).click();
	await expect(page.getByText('Erziehungsberechtigte')).toBeVisible();
	await expect(page.getByText('Daten zu Caroline Triebel')).toBeVisible();

	await expect(page.getByRole('button', {name: 'Ausbildungsbetriebe'})).toBeVisible();
	await page.getByRole('button', {name: 'Ausbildungsbetriebe'}).click();


	await expect(page.getByText('Noch kein Schülerbetrieb vorhanden.')).toBeVisible();


	await expect(page.getByRole('button', {name: 'KAoA'})).toBeVisible();
	await page.getByRole('button', {name: 'KAoA'}).click();
	await expect(page.getByRole('button', {name: 'Neuer Eintrag'})).toBeVisible();


	await expect(page.getByRole('button', {name: 'Schulbesuch'})).toBeVisible();
	await page.getByRole('button', {name: 'Schulbesuch'}).click();
	await expect(page.getByText('Vor der Aufnahme besucht')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Lernabschnitte'})).toBeVisible();
	await page.getByRole('button', {name: 'Lernabschnitte'}).click();
	await expect(page.getByRole('button', {name: 'Allgemein'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Leistungsdaten', exact: true})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Klausuren'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Versetzung/Abschluss'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Konferenz'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Zeugnisdruck'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Nachprüfung'})).toBeVisible();

	await page.getByRole('button', {name: 'Allgemein'}).click();
	await expect(page.getByText('Klassenlehrer')).toBeVisible();

	await page.getByRole('button', {name: 'Leistungsdaten'}).click();
	await expect(page.getByText('Fehlstunden (Summe)')).toBeVisible();

	await page.getByRole('button', {name: 'Klausuren'}).click();
	await expect(page.getByText('Abschnitt', {exact: true})).toBeVisible();

	await page.getByRole('button', {name: 'Versetzung/Abschluss'}).click();
	await expect(page.getByText('TODO: istAbschlussPrognose')).toBeVisible()

	await page.getByRole('button', {name: 'Konferenz'}).click();
	await expect(page.getByText('Abschnitt', {exact: true})).toBeVisible()

	await page.getByRole('button', {name: 'Zeugnisdruck'}).click();
	await expect(page.getByText('TODO: nur Anzeige - abschluss', {exact: true})).toBeVisible()

	await page.getByRole('button', {name: 'Nachprüfung'}).click();
	await expect(page.getByText(' Dieser Bereich ist noch in Entwicklung.')).toBeVisible()


	await expect(page.getByRole('button', {name: 'Sprachen'})).toBeVisible();
	await page.getByRole('button', {name: 'Sprachen'}).click();
	await expect(page.getByText('Sprachenfolge')).toBeVisible();
	await expect(page.getByText('Sprachprüfungen – Herkunftsprachlicher Unterricht')).toBeVisible();
	await expect(page.getByText('Sprachprüfungen – Feststellungsprüfungen')).toBeVisible()


	await expect(page.getByRole('button', {name: 'Laufbahnplanung'})).toBeVisible();
	await page.getByRole('button', {name: 'Laufbahnplanung'}).click();
	await expect(page.getByText('Belegprüfungsergebnisse')).toBeVisible();
	await expect(page.getByText('Wochenstunden')).toBeVisible();
	await expect(page.getByText('Projektkurs Informatik')).toBeVisible();

	await expect(page.getByRole('button', {name: 'Abitur'})).toBeVisible();
	await page.getByRole('button', {name: 'Abitur'}).click();
	await expect(page.getByRole('button', {name: 'Zulassung'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Prüfung'})).toBeVisible();

	await page.getByRole('button', {name: 'Zulassung'}).click();
	await expect(page.getByText('Abitur-Zulassung')).toBeVisible();

	await page.getByRole('button', {name: 'Prüfung'}).click();
	await expect(page.getByText('Triebel, Michael')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Stundenplan'})).toBeVisible();
	await page.getByRole('button', {name: 'Stundenplan'}).click();
	await expect(page.getByText('Stundenplan 2. Halbjahr')).toBeVisible();
});

test('Admins können entsprechende Bereiche bei den Lehrern im DEV Mode sehen', async ({page}) => {
	const { loginAdmin } = useLoginUtils(targetHost, page);
	await loginAdmin();
	await page.getByRole('link', {name: 'Lehrkräfte'}).click();
	await expect(page.locator('header').first()).toContainText('ALBE');

	await expect(page.getByRole('button', {name: 'Individualdaten'})).toBeVisible();
	await page.getByRole('button', {name: 'Individualdaten'}).click();
	await expect(page.getByText('Wohnort und Kontaktdaten')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Personaldaten'})).toBeVisible();
	await page.getByRole('button', {name: 'Personaldaten'}).click();
	await expect(page.getByRole('heading', {name: 'Mehr- und Minderleistung, Anrechnungsstunden'})).toBeVisible();


	await expect(page.getByRole('button', {name: 'Stundenplan'})).toBeVisible();
	await page.getByRole('button', {name: 'Stundenplan'}).click();
	await expect(page.getByText('Stundenplan 2. Halbjahr')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Unterricht'})).toBeVisible();
	await page.getByRole('button', {name: 'Unterricht'}).click();
	await expect(page.getByText('Unterrichtsdaten', {exact: true})).toBeVisible();


	await expect(page.getByRole('button', {name: 'Einwilligungen'})).toBeVisible();
	await page.getByRole('button', {name: 'Einwilligungen'}).click();
	await expect(page.getByRole('heading', {name: 'Nicht abgefragt'})).toBeVisible();


	await expect(page.getByRole('button', {name: 'Lernplattform'})).toBeVisible();
	await page.getByRole('button', {name: 'Lernplattform'}).click();
	await expect(page.getByText('Einwilligung Abgefragt')).toBeVisible();
})

test('Admins können Oberstufe bearbeiten', async ({page}) => {
	test.setTimeout(60_000);
	const { loginAdmin } = useLoginUtils(targetHost, page);
	await loginAdmin();
	await expect(page.getByRole('link', {name: 'Oberstufe'})).toBeVisible();
});

test('Admins können Einstellung vornehmen', async ({page}) => {
	test.setTimeout(60_000);
	const { loginAdmin } = useLoginUtils(targetHost, page);
	await loginAdmin();
	await expect(page.getByRole('link', {name: 'Einstellungen'})).toBeVisible();
});
