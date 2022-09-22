// Wir verwenden xhr2, damit wir die openapi-ts auch in Node verwenden können.
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore
global.XMLHttpRequest = require("xhr2");
import { describe, test, expect, beforeAll } from "vitest";
import { Faecher } from "~/apps/faecher/Faecher";
import { ListFaecher } from "~/apps/faecher/ListFaecher";
import { App } from "~/apps/BaseApp";
import { Schule } from "~/apps/schule/Schule";

describe("Faecher", () => {
	let instance: Faecher;
	let schule: Schule;

	beforeAll(async () => {
		App.setup({
			url: "localhost",
			username: "Admin",
			password: "",
			schema: "svwsschema"
		});
		instance = new Faecher();
		schule = new Schule();
		await schule.init();
		await instance.init();
		await instance.auswahl.update_list();
	});

	test("auswahl: liefert ein Objekt ListSchule", async () => {
		expect(instance.auswahl).toBeInstanceOf(ListFaecher);
	});
	test("dataFach: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.dataFach.daten?.kuerzel).toBeDefined();
	});
});
