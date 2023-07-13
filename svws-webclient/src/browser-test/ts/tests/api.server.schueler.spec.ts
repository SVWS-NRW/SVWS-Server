import { test, expect } from '@playwright/test';
import { api } from "../api/Api"
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

test('teste Api', async () => {
	await api.connectTo('localhost');
	await api.login('GymAbi', 'Admin', '');
	const stammdaten = await api.server.getSchuelerStammdaten('GymAbi', 1199)
	expect(stammdaten.nachname).toBe('Addens');
})