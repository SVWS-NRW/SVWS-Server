import { beforeEach, describe, expect, test, vi } from "vitest";

import { BenutzerDaten } from "@core/core/data/benutzer/BenutzerDaten";
import { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
import { LehrerCredentialsOhneBenutzerEintrag } from "@core/core/data/benutzer/LehrerCredentialsOhneBenutzerEintrag";
import { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
import { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";
import { ArrayList } from "@core/java/util/ArrayList";

import { RouteDataEinstellungenBenutzer } from "~/router/apps/einstellungen/benutzer/RouteDataEinstellungenBenutzer";

const server = vi.hoisted(() => ({
	getBenutzerliste: vi.fn(),
	getLehrer: vi.fn(),
	getLehrerCredentialsOhneBenutzer: vi.fn(),
	createBenutzerLehrer: vi.fn(),
	removeBenutzerMenge: vi.fn(async () => { /* Der Server wird nicht gebraucht. */ }),
}));

vi.mock("~/router/Api", () => ({ api: { schema: "test", server } }));
vi.mock("~/router/RouteManager", () => ({ RouteManager: { doRoute: vi.fn(async () => { /* Es wird nicht navigiert. */ }) } }));
vi.mock("~/router/RouteNode", () => ({ RouteNode: { getNodeByName: vi.fn() } }));
vi.mock("~/router/apps/einstellungen/benutzer/RouteEinstellungenBenutzer", () => ({
	routeEinstellungenBenutzer: { getRoute: vi.fn(), getRouteSelectedChild: vi.fn() },
}));
vi.mock("~/router/apps/einstellungen/benutzer/RouteEinstellungenBenutzerDaten", () => ({ routeEinstellungenBenutzerDaten: { name: "daten" } }));

function benutzer(id: number, typ: BenutzerTyp, typID: number, name: string): BenutzerListeEintrag {
	return Object.assign(new BenutzerListeEintrag(), { id, typ: typ.id, typID, name, anzeigename: name });
}

function liste<T>(...elemente: T[]): ArrayList<T> {
	const result = new ArrayList<T>();
	for (const e of elemente) {
		result.add(e);
	}
	return result;
}

const lehrer = Object.assign(new LehrerListeEintrag(), { id: 7, kuerzel: "MEI", nachname: "Meier", vorname: "Paul" });
const vorhandeneCredentials = Object.assign(new LehrerCredentialsOhneBenutzerEintrag(), { idLehrer: 7, idCredentials: 70, benutzername: "paul.alt" });

async function ladeDaten(): Promise<RouteDataEinstellungenBenutzer> {
	server.getBenutzerliste.mockResolvedValue(liste(benutzer(1, BenutzerTyp.ALLGEMEIN, 10, "admin"), benutzer(2, BenutzerTyp.LEHRER, 8, "ALB")));
	server.getLehrer.mockResolvedValue(liste(lehrer));
	server.getLehrerCredentialsOhneBenutzer.mockResolvedValue(liste(vorhandeneCredentials));
	const data = new RouteDataEinstellungenBenutzer();
	await data.ladeListe();
	return data;
}

/**
 * Prüft, dass die Benutzerverwaltung nach dem Anlegen und Löschen von Benutzern neue Maps bereitstellt, damit
 * abhängige Komponenten wie das Modal zum Anlegen von Benutzern die Änderung bemerken.
 */
describe("RouteDataEinstellungenBenutzer", () => {

	beforeEach(() => {
		vi.clearAllMocks();
	});

	test("Beim Laden werden die Lehrer und die vorhandenen Lehrer-Credentials bereitgestellt", async () => {
		const data = await ladeDaten();
		expect(data.listLehrer.toArray(new Array<LehrerListeEintrag>())).toEqual([lehrer]);
		expect(data.mapLehrerCredentialsOhneBenutzer.get(7)).toBe(vorhandeneCredentials);
	});

	test("Nach dem Anlegen eines Lehrer-Benutzers enthalten neue Maps den Benutzer und nicht mehr die vorhandenen Credentials", async () => {
		const data = await ladeDaten();
		const mapBenutzerVorher = data.mapBenutzer;
		const mapCredentialsVorher = data.mapLehrerCredentialsOhneBenutzer;
		server.createBenutzerLehrer.mockResolvedValue(Object.assign(new BenutzerDaten(), {
			id: 3, typ: BenutzerTyp.LEHRER.id, typID: 7, name: "paul.alt", anzeigename: "Paul Meier", idCredentials: 70,
		}));

		await data.createBenutzerLehrer(7, "paul.alt", "geheim");

		expect(server.createBenutzerLehrer).toHaveBeenCalledWith(expect.objectContaining({ idLehrer: 7, benutzername: "paul.alt", password: "geheim" }), "test");
		expect(data.mapBenutzer).not.toBe(mapBenutzerVorher);
		expect(data.mapBenutzer.get(3)).toMatchObject({ typ: BenutzerTyp.LEHRER.id, typID: 7, name: "paul.alt" });
		expect(data.mapLehrerCredentialsOhneBenutzer).not.toBe(mapCredentialsVorher);
		expect(data.mapLehrerCredentialsOhneBenutzer.has(7)).toBe(false);
	});

	test("Nach dem Löschen von Benutzern enthält eine neue Map die übrigen Benutzer", async () => {
		const data = await ladeDaten();
		const mapBenutzerVorher = data.mapBenutzer;
		const lehrerBenutzer = data.mapBenutzer.get(2);
		if (lehrerBenutzer === undefined) {
			throw new Error("Lehrer-Benutzer nicht geladen");
		}

		await data.deleteBenutzerMenge([lehrerBenutzer]);

		expect(server.removeBenutzerMenge).toHaveBeenCalled();
		expect(data.mapBenutzer).not.toBe(mapBenutzerVorher);
		expect([...data.mapBenutzer.keys()]).toEqual([1]);
		expect(data.listBenutzer.size()).toBe(1);
	});

});
