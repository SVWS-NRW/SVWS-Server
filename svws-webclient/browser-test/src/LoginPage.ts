import type { Page } from "@playwright/test";

export default class LoginPage{

	constructor(public page: Page) {
		// empty
	}

	async login(servername: string, benutzername: string, passwort: string){
		await this.enterServeradresse(servername);
		await this.enterBenutzername(benutzername);
		await this.enterPasswort(passwort);
		await this.clickLoginButton();
	}

	async enterServeradresse(servername: string) {
		await this.page.getByLabel('Serveraddresse').fill(servername);
	}

	async selectSchema(schema: string){
		//TODO
	}

	async enterBenutzername(benutzername: string) {
		await this.page.getByLabel('Benutzername').fill(benutzername);
	}

	async enterPasswort(passwort: string) {
		await this.page.getByLabel('Passwort').waitFor({ state: "visible" });
		await this.page.getByLabel('Passwort').fill(passwort);
	}

	async clickLoginButton () {
		await this.page.getByRole('button', { name: 'Anmelden' }).click();
	}

}