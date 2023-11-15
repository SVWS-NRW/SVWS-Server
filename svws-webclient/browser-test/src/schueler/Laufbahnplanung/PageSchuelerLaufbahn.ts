import { type Page } from "@playwright/test";
import { expect } from "@playwright/test";
import type ErrorPage from "../../PageError";
import type { Schueler } from "../DataSchueler";

export class SchuelerLaufbahnPage {

	public schueler : Schueler | undefined;

	constructor (public page: Page) {
	}

	ladeConfig(schueler : Schueler) {
		this.schueler = schueler
	}

	async testeEingabeBelegpruefungsergebnisse() {
		await this.pruefeLabelBelegPruefungsergebnisse()
		await this.click_rb_ef1();
		await this.click_rb_gesamt();
		await this.click_rb_automatisch();
	}

	private async pruefeLabelBelegPruefungsergebnisse() {
		await this.page.locator("//h3[@title='Belegprüfungsergebnisse']").waitFor({state:"visible"})
		expect((await this.page.locator("//h3[@title='Belegprüfungsergebnisse']").innerText()).valueOf()).toBe("Belegprüfungsergebnisse")
	}
	private async click_rb_ef1() {
		const rb_ef1 =  this.page.locator("(//label[@class='radio--label radio--label--checked-'])[1]");
		await rb_ef1.waitFor({state:"visible"});
		await rb_ef1.click();
	}

	private async click_rb_gesamt() {
		const rb_gesamt = this.page.locator("(//label[@class='radio--label radio--label--checked-'])[2]");
		await rb_gesamt.waitFor({state:"visible"});
		await rb_gesamt.click();
	}

	private async click_rb_automatisch() {
		const rb_automatisch =  this.page.locator("(//label[@class='radio--label radio--label--checked-'])[3]");
		await rb_automatisch.waitFor({state:"visible"});
		await rb_automatisch.click();
	}


	async testeEingabeBeratung() {
		await this.pruefeLabelBelegPruefungsergebnisse();
		await this.clickBerater();
		await this.fillBeratungsdatum();
		await this.fillKommter();
		await this.clickBeratugspeichen();

	}

	private async pruefeLabelBeratung() {
		await this.page.locator("//h3[@title='Beratung']").waitFor({state:"visible"});
		expect((await this.page.locator("//h3[@title='Beratung']").innerText()).valueOf()).toBe("Beratung");
	}

	private async clickBerater() {
		await this.page.getByLabel('Letzte Beratung durchgeführt von').click();
		// TODO Tests mit den Lehren von API
		await this.page.getByText('BERG (Antje Berg)').click();
	}

	private async fillBeratungsdatum() {
		await this.page.getByLabel('Beratungsdatum').waitFor({state:"visible"});
		await this.page.getByLabel('Beratungsdatum').fill('2023-08-10');
	}

	private async fillKommter() {
		await this.page.getByLabel('Kommentar').waitFor({state:"visible"});
		await this.page.getByLabel('Kommentar').fill('Das ist ein Kommentar.');
	}

	private async clickBeratugspeichen() {
		await this.page.getByRole('button', { name: 'Beratungsdaten speichern' }).click();
	}

}