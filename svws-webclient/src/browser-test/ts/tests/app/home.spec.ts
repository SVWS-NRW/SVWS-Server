import { Page, expect, test } from "@playwright/test";

test.beforeEach( async ( { page }) => {
	await page.goto('/', { waitUntil: 'domcontentloaded'});
	await page.getByRole('button', { name: 'Anmelden' }).click();
	//await page.waitForTimeout(2000);
	await expect(page).toHaveTitle('SVWS NRW'); //TODO  ---  Schulname ???


});

const menu_body_items = [
	"Schule",
	"Kataloge",
	"Schüler",
	"Lehrkräfte",
	"Klassen",
	"Kurse",
	"Oberstufe",
	//"Statistik",
	//"Stundenplan",
]

test.describe('Sichtbarkeit der Menüleiste', () =>{

	test ("Überprüfe die Sichtbarkeit von Menüheader",async ( { page }) => {
		const menuHeader = page.locator('div.sidebar--menu--header');
		const menuInitials = menuHeader.locator('a.app--menu--initials');
		await expect(menuInitials).toHaveCount(1);
		await expect(menuInitials).toHaveAttribute('href','#');

	})
	test("Überprüfe Sichbarkeit der Menüpunkte",async ( { page }) => {
		const menuBody = page.locator('div.sidebar--menu--body');
		const hyperLinks = menuBody.locator('a.sidebar--menu-item');
		await expect(hyperLinks).toHaveCount(7);
		//const anzahl = await hyperLinks.count();
		// console.log(anzahl);
		// console.log(await hyperLinks.allInnerTexts());

		// for (let index = 0; index < anzahl; index++) {
		//     const element = hyperLinks.nth(index);
		//     expect(await element.isVisible());

		// }

		expect (await hyperLinks.allInnerTexts()).toEqual(menu_body_items);
		// await page.pause();
		// for (let index = 0; index < 9; index++) {
		//     const element = hyperLinks.nth(index);
		//     const text = await element.textContent();
		//     await element.click();
		//     await expect(page).toHaveURL(/text/);
		//     //await page.waitForTimeout(2000);

		// }

	})

	test ("Überprüfe die Sichtbarkeit von Menu-footer",async ( { page }) => {
		const menuFooter = page.locator('div.sidebar--menu--footer');

		const menu_item_abmelden = menuFooter.locator("a[title='Abmelden']");
		await expect(menu_item_abmelden).toHaveText("Abmelden");
		await expect(menu_item_abmelden).toHaveAttribute('href','#');
		await expect(menu_item_abmelden).toHaveAttribute('title','Abmelden');

		const menu_item_ansicht = menuFooter.locator("div.app--appearance-settings");
		await expect(menu_item_ansicht).toHaveText("Ansicht");
		const menu_item_ansicht_link = menu_item_ansicht.locator("a.sidebar--menu-item");
		await expect(menu_item_ansicht_link).toHaveAttribute('href','#');
		await expect(menu_item_ansicht_link).toHaveAttribute('title','Ansicht');

		// TODO : Powered by
		// TODO : client info



	})

})

test.describe('Funktionen der Menüleiste', () =>{

	test("Menüpunkt Schueler öffnen",async ( { page }) => {
		await page.goto("/#/schueler");
		expect(page.url()).toContain('schueler');

	})



})


