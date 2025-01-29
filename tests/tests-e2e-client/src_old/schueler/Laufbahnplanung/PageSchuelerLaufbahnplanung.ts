import type { Page } from "@playwright/test";
import type { Schueler } from "../DataSchueler";

export class SSchuelerLaufbahnplanungPage{

	constructor(public page: Page) {

	}

	async clickLPWahlbogenHerunterladen() {
		await this.page.locator('header').getByRole('button').nth(1).click();
		const downloadPromise = this.page.waitForEvent('download');
		const buttons = this.page.getByRole('button', { name: 'Laufbahnwahlbogen', exact:true });
		await this.page.getByRole('listbox').getByRole('button', { name: 'Laufbahnwahlbogen', exact: true }).click();
		const download = await downloadPromise;
	}

	async clickLPWahlbogenNurBelegungHerunterladen() {
		await this.page.locator('header').getByRole('button').nth(1).click();
		const downloadPromise = this.page.waitForEvent('download');
		await this.page.getByRole('listbox').getByRole('button', { name: 'Laufbahnwahlbogen (nur Belegung)', exact: true }).click();
		const download = await downloadPromise;
	}

	async clickHilfe() {
		await this.page.getByRole('button', { name: 'Hilfe' }).click();
		await this.page.getByLabel('Hilfe').getByText('Hilfe', { exact: true }).click();
  		await this.page.getByRole('heading', { name: 'Hilfe', exact: true }).getByRole('img').click();
 		await this.page.getByRole('button').click();
	}

	async testeSchuelerInformation(schueler : Schueler | undefined) {
		// TODO Klassenkürzel testen
		await this.page.getByText(schueler?.name+'').click();
  		await this.page.getByText('ID: '+schueler?.id).click();
	}

}