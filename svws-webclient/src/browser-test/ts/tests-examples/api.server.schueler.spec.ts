import { test, expect } from '@playwright/test';
import { api } from "../api/Api"
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

test('teste Api', async () => {
	const connect = await api.connectTo('localhost');
	console.log(connect);
	//await api.connectTo('https://nightly.svws-nrw.de');
	const login = await api.login('gymabi', 'Admin', '');
	console.log(login);
	const stammdaten = await api.server.getSchuelerStammdaten('gymabi', 1199);
	console.log(stammdaten);
	expect(stammdaten.nachname).toBe('Addens');
});
