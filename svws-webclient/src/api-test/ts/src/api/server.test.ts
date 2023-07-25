import { describe, test, expect } from "vitest";
import type {
	LehrerListeEintrag,
	List
} from "~/index";
import {
	ApiServer,
	BetriebListeEintrag,
	DBSchemaListeEintrag,
	Erzieherart,
	ErzieherListeEintrag,
	FaecherListeEintrag,
	GostFach
} from "~/index";

const username = "Admin";
const password = "";
const schema = "gymabi"
const url = import.meta.env.VITE_svws_testing_api_host + ":" + import.meta.env.VITE_svws_testing_api_port;

const server = new ApiServer(url, username, password);

describe("Server", () => {
	test("should be able to create a server", async () => {
		expect(server).toBeDefined();
	});
	test("isAlive", async () => {
		try {
			await server.isAlive();
		} catch (e) {
			console.log(e);
		}
		expect(await server.isAlive()).toBeDefined();
	});
	test.skip("should fail",() => {
		expect(1).greaterThan(2);
	});
	test.skip("setClientConfigGlobalKey", async () => {
		//const res = await server.setClientConfigGlobalKey(schema, 1)
		//expect(res).toBeTruthy();
	});
	test.skip("setClientConfigUserKey", async () => {
		//const res = await server.setClientConfigUserKey(schema, 1)
		//expect(res).toBeTruthy();
	});
	test("patchErzieherStammdaten", async () => {
		await expect(server.patchErzieherStammdaten(
			{ hausnummer: "443" },
			schema,
			19942
		)).resolves.not.toThrow();
	});
	test.skip("patchGostAbiturjahrgang", async () => {
		const res = await server.patchGostAbiturjahrgang(
			{ abiturjahr: 2021 },
			schema,
			2021
		);
		expect(res).toBeTruthy();
	});
	test.skip("patchGostAbiturjahrgangFach", async () => {
		const res = await server.patchGostAbiturjahrgangFach(
			{ bezeichnung: "Deutsch" },
			schema,
			2021,
			16
		);
		expect(res).toBeTruthy();
	});
	test.skip("patchGostBlockung", async () => {
		// const res = await server.patchGostBlockung(schema, 1)
		// expect(res).toBeTruthy()
	});
	test.skip("patchGostBlockungKurs", async () => {
		// const res = await server.patchGostBlockungKurs(schema, 1)
		// expect(res).toBeTruthy()
	});
	test.skip("patchGostBlockungRegel", async () => {
		// const res = await server.patchGostBlockungRegel(schema, 1)
		// expect(res).toBeTruthy()
	});
	test.skip("patchGostBlockungSchiene", async () => {
		// const res = await server.patchGostBlockungSchiene(schema, 1)
		// expect(res).toBeTruthy()
	});
	test.skip("patchGostFach", async () => {
		// const res = await server.patchGostFach(schema, 1)
		// expect(res).toBeTruthy()
	});
	test.skip("patchGostSchuelerFachwahl", async () => {
		const res = await server.patchGostSchuelerFachwahl(schema, 1199, 16);
		expect(res).toBeTruthy();
	});
	test.skip("patchBeschaeftigungsart", async () => {
		const res = await server.patchBeschaeftigungsart(schema, 1);
		expect(res).toBeTruthy();
	});
	test.skip("patchBetriebsart", async () => {
		const res = await server.patchBetriebsart(schema, 1);
		expect(res).toBeTruthy();
	});
	test.skip("patchLehrerPersonaldaten", async () => {
		const res = await server.patchLehrerPersonaldaten(schema, 1);
		expect(res).toBeTruthy();
	});
	test.skip("patchLehrerStammdaten", async () => {
		const res = await server.patchLehrerStammdaten(schema, 1);
		expect(res).toBeTruthy();
	});
	test.skip("patchSchuelerBetrieb", async () => {
		const res = await server.patchSchuelerBetrieb(schema, 1);
		expect(res).toBeTruthy();
	});
	test.skip("patchSchuelerSchulbesuch", async () => {
		const res = await server.patchSchuelerSchulbesuch(schema, 1);
		expect(res).toBeTruthy();
	});
	test.skip("patchSchuelerStammdaten", async () => {
		const res = await server.patchSchuelerStammdaten(schema, 1);
		expect(res).toBeTruthy();
	});
	test.skip("patchSchuleStammdaten", async () => {
		const res = await server.patchSchuleStammdaten(schema);
		expect(res).toBeTruthy();
	});
	test.skip("putSchullogo", async () => {
		const res = await server.putSchullogo(schema);
		expect(res).toBeTruthy();
	});
	test.skip("createGostAbiturjahrgangBlockung", async () => {
		const res = await server.createGostAbiturjahrgangBlockung(
			schema,
			2021,
			1
		);
		expect(res).toBeTruthy();
	});
	test.skip("patchBetriebStammdaten", async () => {
		// const res = await server.patchBetriebStammdaten(
		// 	{ hausnr: "73", branche: "Lebensmittel" },
		// 	schema,
		// 	1
		// );
		// expect(res).toBeTruthy();
	});
	test.skip("getConfigCertificate", async () => {
		const res = await server.getConfigCertificate();
		expect(res).matchSnapshot();
	});
	test.skip("getConfigCertificateBase64", async () => {
		const res = await server.getConfigCertificateBase64();
		expect(res).matchSnapshot();
	});
	test.skip("getConfigDBSchemata: hole Liste mit DBs", async () => {
		const res = await server.getConfigDBSchemata();
		expect(res).matchSnapshot();
		const array = res.toArray();
		expect(array[0]).toBeInstanceOf(DBSchemaListeEintrag);
	});
	test.skip("getConfigPublicKeyBase64", async () => {
		const res = await server.getConfigPublicKeyBase64();
		expect(res).matchSnapshot();
	});
	test.skip("getBetriebe", async () => {
		const res = await server.getBetriebe(schema);
		expect(res).matchSnapshot();
		const array = res.toArray();
		expect(array[0]).toBeInstanceOf(BetriebListeEintrag);
	});
	test.skip("getBetriebStammdaten", async () => {
		const res = await server.getBetriebStammdaten(schema, 1);
		expect(res).matchSnapshot();
	});
	test.todo("getClientConfigUserKey", async () => {
		// throw bei fehlendem Parameter, bzw Fehler abfangen
		const res = await server.getClientConfigUserKey(schema, "test", "test");
		expect(res).matchSnapshot();
	});
	test.todo("getLehrerENMDaten", async () => {
		// Fehler unklar
		const res = await server.getLehrerENMDaten(schema, 76);
		expect(res).matchSnapshot();
	});
	test.skip("getErzieher", async () => {
		const res = await server.getErzieher(schema);
		expect(res).matchSnapshot();
		const array = res.toArray();
		expect(array[0]).toBeInstanceOf(ErzieherListeEintrag);
	});
	test.skip("getErzieherStammdaten", async () => {
		const res = await server.getErzieherStammdaten(schema, 19941);
		expect(res).matchSnapshot();
	});
	test.skip("getErzieherArten", async () => {
		const res = await server.getErzieherArten(schema);
		expect(res).matchSnapshot();
		const array = res.toArray();
		expect(array[0]).toBeInstanceOf(Erzieherart);
	});
	test.skip("getFaecher", async () => {
		const res = await server.getFaecher(schema);
		expect(res).matchSnapshot();
		const array = res.toArray();
		expect(array[0]).toBeInstanceOf(FaecherListeEintrag);
	});
	test.skip("getFach", async () => {
		const res = await server.getFach(schema, 16);
		expect(res).matchSnapshot();
	});
	test.todo("getGesamtschuleSchuelerPrognoseLeistungsdaten", async () => {
		const res = await server.getGesamtschuleSchuelerPrognoseLeistungsdaten(
			schema,
			1199
		);
		expect(res).matchSnapshot();
	});
	test.skip("getGesamtschuleSchuelerPrognosLeistungsdatenFuerAbschnitt", async () => {
		const res =
			await server.getGesamtschuleSchuelerPrognosLeistungsdatenFuerAbschnitt(
				schema,
				1199,
				13
			);
		expect(res).matchSnapshot();
	});
	test.skip("getGostAbiturBelegpruefungEF1", async () => {
		const res = await server.getGostAbiturBelegpruefungEF1();
		expect(res).matchSnapshot();
	});
	test.skip("getGostAbiturBelegpruefungGesamt", async () => {
		const res = await server.getGostAbiturBelegpruefungGesamt();
		expect(res).matchSnapshot();
	});
	test.skip("getGostAbiturjahrgaenge", async () => {
		const res = await server.getGostAbiturjahrgaenge(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getGostAbiturjahrgang", async () => {
		const res = await server.getGostAbiturjahrgang(schema, 2021);
		expect(res).matchSnapshot();
	});
	test.skip("getGostAbiturjahrgangBlockungsliste", async () => {
		const res = await server.getGostAbiturjahrgangBlockungsliste(
			schema,
			2021,
			3
		);
		expect(res).matchSnapshot();
	});
	test.skip("getGostAbiturjahrgangFach", async () => {
		const res = await server.getGostAbiturjahrgangFach(schema, 2021, 16);
		expect(res).matchSnapshot();
		expect(res).toBeInstanceOf(GostFach);
	});
	test.skip("getGostAbiturjahrgangFachwahlen", async () => {
		const res = await server.getGostAbiturjahrgangFachwahlen(schema, 2021);
		expect(res).matchSnapshot();
	});
	test.skip("getGostAbiturjahrgangFaecher", async () => {
		const res = await server.getGostAbiturjahrgangFaecher(schema, 2021);
		expect(res).matchSnapshot();
	});
	test.skip("getGostBlockung", async () => {
		const res = await server.getGostBlockung(schema, 1);
		expect(res).matchSnapshot();
	});
	test.skip("getGostFach", async () => {
		const res = await server.getGostFach(schema, 16);
		expect(res).matchSnapshot();
	});
	test.skip("getGostFaecher", async () => {
		const res = await server.getGostFaecher(schema);
		expect(res).matchSnapshot();
	});
	test.todo("getGostSchuelerAbiturdaten", async () => {
		const res = await server.getGostSchuelerAbiturdaten(schema, 1199);
		expect(res).matchSnapshot();
	});
	test.todo("getGostSchuelerAbiturdatenAusLeistungsdaten", async () => {
		const res = await server.getGostSchuelerAbiturdatenAusLeistungsdaten(
			schema,
			1199
		);
		expect(res).matchSnapshot();
	});
	test.skip("getGostSchuelerLaufbahnplanung", async () => {
		const res = await server.getGostSchuelerLaufbahnplanung(schema, 1199);
		expect(res).matchSnapshot();
	});
	test.skip("getGostSchuelerLeistungsdaten", async () => {
		const res = await server.getGostSchuelerLeistungsdaten(schema, 1199);
		expect(res).matchSnapshot();
	});
	test.todo("getGostSchuelerFachwahl", async () => {
		//Parameter ok, mit der alten API getestet
		const res = await server.getGostSchuelerFachwahl(schema, 1199, 16);
		expect(res).matchSnapshot();
	});
	test.skip("getJahrgaenge", async () => {
		const res = await server.getJahrgaenge(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getJahrgang", async () => {
		const res = await server.getJahrgang(schema, 10);
		expect(res).matchSnapshot();
	});
	test.skip("getKatalogBeschaeftigungsart", async () => {
		const res = await server.getKatalogBeschaeftigungsart(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getKatalogBetriebsart", async () => {
		const res = await server.getKatalogBetriebsart(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getKatalogHaltestellen", async () => {
		const res = await server.getKatalogHaltestellen(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getKatalogOrte", async () => {
		const res = await server.getKatalogOrte(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getKatalogOrtsteile", async () => {
		const res = await server.getKatalogOrtsteile(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getKatalogReligionen", async () => {
		const res = await server.getKatalogReligionen(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getKlasse", async () => {
		const res = await server.getKlasse(schema, 10);
		expect(res).matchSnapshot();
	});
	test.skip("getKlassenFuerAbschnitt", async () => {
		const res = await server.getKlassenFuerAbschnitt(schema, 13);
		expect(res).matchSnapshot();
	});
	test.skip("getKurse", async () => {
		const res = await server.getKurse(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getKurs", async () => {
		const res = await server.getKurs(schema, 6178);
		expect(res).matchSnapshot();
	});
	test.skip("getKurseFuerAbschnitt", async () => {
		const res = await server.getKurseFuerAbschnitt(schema, 13);
		expect(res).matchSnapshot();
	});
	test.skip("getLehrerListe", async () => {
		const liste: List<LehrerListeEintrag> = await server.getLehrer(schema);
		expect(liste).matchSnapshot();
		const lehrer = liste.get(0);
		expect(lehrer).matchSnapshot();
	});
	test.skip("getLehrerPersonaldaten", async () => {
		const res = await server.getLehrerPersonaldaten(schema, 76);
		expect(res).matchSnapshot();
	});
	test.skip("getLehrerStammdaten", async () => {
		const res = await server.getLehrerStammdaten(schema, 76);
		expect(res).matchSnapshot();
	});
	test.skip("getLehrerAbgangsgruende", async () => {
		const res = await server.getLehrerAbgangsgruende(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getLehrerBeschaeftigungsarten", async () => {
		const res = await server.getLehrerBeschaeftigungsarten(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getLehrerEinsatzstatus", async () => {
		const res = await server.getLehrerEinsatzstatus(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getLehrerLeitungsfunktionen", async () => {
		const res = await server.getLehrerLeitungsfunktionen(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getLehrerRechtsverhaeltnisse", async () => {
		const res = await server.getLehrerRechtsverhaeltnisse(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getLehrerZugangsgruende", async () => {
		const res = await server.getLehrerZugangsgruende(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getSchuelerErzieher", async () => {
		const res = await server.getSchuelerErzieher(schema, 1199);
		expect(res).matchSnapshot();
	});
	test.skip("getSchuelerSchulbesuch", async () => {
		const res = await server.getSchuelerSchulbesuch(schema, 1199);
		expect(res).matchSnapshot();
	});
	test.skip("getSchuelerStammdaten", async () => {
		const res = await server.getSchuelerStammdaten(schema, 1199);
		expect(res).matchSnapshot();
	});
	test.skip("getSchuelerFuerAbschnitt", async () => {
		const res = await server.getSchuelerFuerAbschnitt(schema, 13);
		expect(res).matchSnapshot();
	});
	test.skip("getSchuelerAktuell", async () => {
		const res = await server.getSchuelerAktuell(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getSchuelerFahrschuelerarten", async () => {
		const res = await server.getSchuelerFahrschuelerarten(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getSchuelerFoerderschwerpunkte", async () => {
		const res = await server.getSchuelerFoerderschwerpunkte(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getSchullogo", async () => {
		const res = await server.getSchullogo(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getSchuleNummer", async () => {
		const res = await server.getSchuleNummer(schema);
		expect(res).matchSnapshot();
	});
	test.skip("getSchuleStammdaten", async () => {
		const res = await server.getSchuleStammdaten(schema);
		expect(res).matchSnapshot();
	});
});
