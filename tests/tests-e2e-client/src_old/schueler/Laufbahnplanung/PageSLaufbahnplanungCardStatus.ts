import { type Page } from "@playwright/test";
import { expect } from "@playwright/test";

export class PageSLaufbahnplanungCardStatus {

	constructor (public page: Page) { }

	async testeEingabeBelegpruefungsergebnisse() {
		await this.pruefeLabelBelegPruefungsergebnisse()
		await this.click_rb_ef1();
		await this.click_rb_gesamt();
		await this.click_rb_automatisch();
	}

	private async pruefeLabelBelegPruefungsergebnisse() {
		await this.page.getByRole('heading', { name: 'Belegprüfungsergebnisse' }).waitFor({state:"visible"})
		expect((await this.page.getByRole('heading', { name: 'Belegprüfungsergebnisse' }).innerText()).valueOf()).toBe("Belegprüfungsergebnisse")
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
}