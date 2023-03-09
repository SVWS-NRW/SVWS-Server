// Wir verwenden xhr2, damit wir die openapi-ts auch in Node verwenden können.
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore
global.XMLHttpRequest = require("xhr2");
import { describe, test, expect, beforeAll } from "vitest";
import { Kurse } from "~/apps/kurse/Kurse";
import { ListKurse } from "~/apps/kurse/ListKurse";
import { App } from "~/apps/BaseApp";
import { Schule } from "~/apps/schule/Schule";

describe("Kurse", () => {
	let instance: Kurse;
	let schule: Schule;

	beforeAll(async () => {
		App.setup({
			url: "localhost",
			username: "Admin",
			password: "",
			schema: "svwsschema"
		});
		instance = new Kurse();
		schule = new Schule();
		await schule.init();
		await instance.init();
		await instance.auswahl.update_list();
	});

	test("auswahl: liefert ein Objekt ListSchule", async () => {
		expect(instance.auswahl).toBeInstanceOf(ListKurse);
	});
	test("kursdaten: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.kursdaten.daten?.kuerzel).toBeDefined();
	});
});
