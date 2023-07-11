import { Page, expect, test } from "@playwright/test";

const schueler_ids =[9299,9184,9301] ;
const schueler_id = 9299;

test.beforeEach( async ( { page }) => {
	await page.goto('/', { waitUntil: 'domcontentloaded'});
	await page.getByRole('button', { name: 'Anmelden' }).click();
	//await page.waitForTimeout(2000);
	await expect(page).toHaveTitle('SVWS NRW'); //TODO  ---  Schulname ???

	await page.goto(`/#/schueler/${schueler_id}/daten`)

});

const schueler_tabs = [
	"daten",
	"erziehungsberechtigte",
	"ausbildungsbetriebe",
	"leistungsdaten",
	"laufbahnplanung",
	"stundenplan",
]



test.describe('Schueler', () =>{

	test.describe('Allgemein',() => {

		test ("Überprüfe die Sichtbarkeit der Tabs",async ( { page }) => {
			for (let s = 0; s < 3; s++) {
				for (let i = 0; i < 6; i++) {
					//await page.pause();
					const element = schueler_tabs[i];
					await page.goto(`/#/schueler/${schueler_ids[s]}/${element}`, { waitUntil: 'domcontentloaded'});
					await page.waitForTimeout(3000);
					expect(page.url()).toContain(`/schueler/${schueler_ids[s]}/${element}`)
				}
			}
		})
	})

	test.describe('Tab Laufbahnplanung',() => {

		test('Überprüfe die Sichbarkeit des Tabellenkopfes ',async ( { page }) => {

			await page.goto(`/#/schueler/${schueler_id}/laufbahnplanung`, { waitUntil: 'domcontentloaded'});
			expect(page.url()).toContain(`/schueler/${schueler_id}/`)
			await page.waitForTimeout(1000);
			//Tabellenkopf I
			const headers_1 = [ "Fach", "Sprachen", "EF", "Qualifikationsphase", "Abitur" ];
			const titels = page.locator("(//div[@class='data-table__th-title'])");
			await expect(titels).toHaveCount(5);
			expect(await titels.allInnerTexts()).toEqual(headers_1);
			console.log(await titels.allInnerTexts())

			//Tabellenkopf II
			const headers_2 = [ " Fach ", " Bezeichnung ", " WS ", " Folge ", " ab Jg ", " EF.1 ", " Q1.1 ",
				" Q1.2 ", " Fach "];

			for (let i = 0; i < headers_2.length; i++) {
				const element = page.locator(`//span[text()='${headers_2[i]}']`);
				expect(await element.isVisible());
				expect(await element.textContent()).toContain(headers_2[i]);
			}

		})
	})
})






