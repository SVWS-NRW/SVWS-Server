import { Page, expect, test } from "@playwright/test";

const schueler_id = 9299;

test.beforeEach( async ( { page }) => {
	await page.goto('/', { waitUntil: 'domcontentloaded'});
	await page.getByRole('button', { name: 'Anmelden' }).click();
	//await page.waitForTimeout(2000);
	await expect(page).toHaveTitle('SVWS NRW'); //TODO  ---  Schulname ???

	await page.goto(`/#/schueler/${schueler_id}/daten`)

});

test.describe('Schüler -> Tab Laufbahnplanung',() => {
	test('Überprüfe die Sichtbarkeit der Menüleiste',async ( { page }) => {
		await page.goto(`/#/schueler/${schueler_id}/laufbahnplanung`, { waitUntil: 'domcontentloaded'});
		expect(page.url()).toContain(`/schueler/${schueler_id}/`)
		await page.waitForTimeout(1000);
		const menueleiste = page.locator("div.router-tab-bar--subnav");
		const buttons = menueleiste.getByRole('button');
		await expect(buttons).toHaveCount(7);
	})
});
