import { ApiEnmServerTest } from "../../utils/ApiEnmServerTest";

export const apiEmpty = new ApiEnmServerTest('', '');
export const apiGehring = new ApiEnmServerTest('M.Gehring@lmail.de', 'uTdNE7EUIb');
export const apiGiesen = new ApiEnmServerTest('T.Giesen@lmail.de', 'UD73Js0Uro');
export const apiBerthold = new ApiEnmServerTest('D.Berthold@lmail.de', 'uXkpaRLY');

let isLoggedIn = false;

async function doLogin(api: ApiEnmServerTest) {
	const result = await api.login();
	if (result.isChangePassword) {
		await api.changePassword();
	}
}

export async function ensureLogin(): Promise<void> {
	if (isLoggedIn) {
		return;
	}
	await doLogin(apiGehring);
	await doLogin(apiGiesen);
	await doLogin(apiBerthold);
	isLoggedIn = true;
}

