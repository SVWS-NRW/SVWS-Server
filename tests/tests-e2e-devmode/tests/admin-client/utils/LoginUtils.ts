import { expect, type Page } from "@playwright/test";

export function useLoginUtils(targetHost: string, page: Page) {

	const loginRoot = async () => {
		await page.goto(targetHost);
		await page.waitForURL("**/login?redirect=/**", { timeout: 20_000 });
		await page.getByLabel('Benutzername').click();
		await page.getByLabel('Benutzername').fill('root');
		await page.getByLabel('Passwort').fill('root');
		await page.getByRole('button', { name: 'Anmelden' }).click();
		await page.waitForURL('**/admin#/schema/**/uebersicht', { timeout: 20_000 });
	};

	const logout = async () => {
		await page.getByRole('link', { name: 'Abmelden' }).click();
		await expect(page.getByRole('button', { name: 'Anmelden' })).toContainText('Anmelden');
	};

	return {
		loginRoot,
		logout,
	};
}
