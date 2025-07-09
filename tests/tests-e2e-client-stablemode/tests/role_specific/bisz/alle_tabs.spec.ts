import { test, expect } from '@playwright/test';
import { useLoginUtils } from "../../utils/LoginUtils";

test.use({
	ignoreHTTPSErrors: true,
});

const targetHost = process.env.VITE_targetHost ?? "https://localhost:3000/#/svws"

test('Nicht privilegierte Nutzer können nur entsprechende Bereiche im STABLE Mode einsehen', async ({page}) => {
	const { loginBISZ } = useLoginUtils(targetHost, page);
	await loginBISZ();
	await expect(page.getByRole('heading', {name: 'Schüler'})).toBeVisible();
	await page.getByRole('cell', {name: 'Triebel'}).click();
	await expect(page.locator('header')).toContainText('ID: 1010');


	await expect(page.getByRole('button', {name: 'Individualdaten'})).toBeVisible();
	await page.getByRole('button', {name: 'Individualdaten'}).click();
	await expect(page.getByText('Staatsangehörigkeit und Konfession')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Lernabschnitte'})).toBeVisible();
	await page.getByRole('button', {name: 'Lernabschnitte'}).click();
	await expect(page.getByRole('button', {name: 'Allgemein'})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Leistungsdaten', exact: true})).toBeVisible();
	await expect(page.getByRole('button', {name: 'Klausuren'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Versetzung/Abschluss'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Konferenz'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Zeugnisdruck'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Nachprüfung'})).toHaveCount(0);

	await page.getByRole('button', {name: 'Allgemein'}).click();
	await expect(page.getByText('Klassenlehrer')).toBeVisible();

	await page.getByRole('button', {name: 'Leistungsdaten'}).click();
	await expect(page.getByText('Fehlstunden (Summe)')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Sprachen'})).toBeVisible();
	await page.getByRole('button', {name: 'Sprachen'}).click();
	await expect(page.getByText('Sprachenfolge')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Stundenplan'})).toBeVisible();
	await page.getByRole('button', {name: 'Stundenplan'}).click();
	await expect(page.getByText('Stundenplan 2. Halbjahr')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Sonstiges'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Erziehungsberechtigte'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Ausbildungsbetriebe'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'KAoA'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Schulbesuch'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Laufbahnplanung'})).toHaveCount(0);
});

test('Nicht privilegierte Nutzer können nur entsprechende Bereiche bei den Lehrern im STABLE Mode sehen', async ({page}) => {
	const { loginBISZ } = useLoginUtils(targetHost, page);
	await loginBISZ();
	await page.getByRole('link', {name: 'Lehrkräfte'}).click();
	await expect(page.locator('header').first()).toContainText('ALBE');


	await expect(page.getByRole('button', {name: 'Individualdaten'})).toBeVisible();
	await page.getByRole('button', {name: 'Individualdaten'}).click();
	await expect(page.getByText('Wohnort und Kontaktdaten')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Stundenplan'})).toBeVisible();
	await page.getByRole('button', {name: 'Stundenplan'}).click();
	await expect(page.getByText('Stundenplan 2. Halbjahr')).toBeVisible();


	await expect(page.getByRole('button', {name: 'Unterricht'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Personaldaten'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Einwilligungen'})).toHaveCount(0);
	await expect(page.getByRole('button', {name: 'Lernplattformen'})).toHaveCount(0);
})

test('Nicht privilegierter User können Oberstufe nicht bearbeiten', async ({page}) => {
	test.setTimeout(60_000);
	const { loginBISZ } = useLoginUtils(targetHost, page);
	await loginBISZ();
	await expect(page.getByRole('link', {name: 'Oberstufe'})).not.toBeVisible();
});

test('Nicht privilegierter User können Einstellung nicht vornehmen', async ({page}) => {
	test.setTimeout(60_000);
	const { loginBISZ } = useLoginUtils(targetHost, page);
	await loginBISZ();
	await expect(page.getByRole('link', {name: 'Einstellung'})).not.toBeVisible();
});
