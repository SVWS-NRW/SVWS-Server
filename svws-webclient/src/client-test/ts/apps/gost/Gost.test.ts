// Wir verwenden xhr2, damit wir die openapi-ts auch in Node verwenden können.
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore
global.XMLHttpRequest = require("xhr2");
import { describe, test, expect, beforeAll } from "vitest";
import { Gost } from "~/apps/gost/Gost";
import { ListGost } from "~/apps/gost/ListGost";
import { App } from "~/apps/BaseApp";
import { Schule } from "~/apps/schule/Schule";

describe("Gost", () => {
	let instance: Gost;
	let schule: Schule;

	beforeAll(async () => {
		App.setup({
			url: "localhost",
			username: "Admin",
			password: "",
			schema: "svwsschema"
		});
		instance = new Gost();
		schule = new Schule();
		await schule.init();
		await instance.init();
		await instance.auswahl.update_list();
	});

	test("auswahl: liefert ein Objekt ListSchule", async () => {
		expect(instance.auswahl).toBeInstanceOf(ListGost);
	});
	test("jahrgang: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[1];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.dataJahrgang.daten?.jahrgang).toBeDefined();
	});
	test("jahrgang: ist bei Allgemein nicht definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.dataJahrgang.daten?.jahrgang).not.toBeDefined();
	});
	test("dataFaecher: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.dataFaecher.daten?.[0].kuerzel).toBeDefined();
	});
	test("dataFachwahlen: ist bei Allgemein nicht definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.dataFachwahlen.daten?.[0].kuerzel).not.toBeDefined();
	});
	test("dataFachwahlen: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[1];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.dataFachwahlen.daten?.[0].kuerzel).toBeDefined();
	});
});
