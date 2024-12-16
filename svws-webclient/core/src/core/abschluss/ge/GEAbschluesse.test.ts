import { readFileSync, readdirSync } from "fs";
import { resolve } from "path";
import { describe, test, expect } from "vitest";
import { AbschlussManager, ServicePrognose, AbschlussErgebnis, GEAbschlussFaecher } from "../../../index";

const test_dir = resolve(__dirname, "../../../../../../svws-core/src/test/resources/de/svws_nrw/abschluesse/ge/test");
const files = readdirSync(test_dir);

describe.each(files)("Testfall %s", (file) => {
	const data_raw = readFileSync(resolve(test_dir, file), "utf8");
	const testfall = new GEAbschlussTestfall(data_raw);
	console.log(`${file}...`);
	test(`Testfall ${file}`, () => {
		const abschlussBerechnung = new ServicePrognose();
		const output = abschlussBerechnung.handle(testfall.input);
		const testAbschluss = AbschlussManager.equalsAbschluesse(output.abschluss, testfall.prognose.abschluss);
		if (testAbschluss === false)
			console.error("" + file + " -> " + JSON.stringify(output) + "\n" + JSON.stringify(testfall.input));
		expect(testAbschluss).toBe(true);
	});
});


class GEAbschlussTestfall {
	/** Die Fachinformationen für die Abschlussberechnung. */
	public input: GEAbschlussFaecher;

	/** Das erwartete Ergebnis bei der Prognoseberechnung. */
	public prognose = new AbschlussErgebnis();

	/** Das erwartete Ergebnis bei der Berechnung des Hauptschulabschlusses nach Klasse 9. */
	public ha9 = new AbschlussErgebnis();

	/** Das erwartete Ergebnis bei der Berechnung des Hauptschulabschlusses nach Klasse 10. */
	public ha10 = new AbschlussErgebnis();

	/** Das erwartete Ergebnis bei der Berechnung des Mittleren Schulabschlusses nach Klasse 10. */
	public msa = new AbschlussErgebnis();

	/** Das erwartete Ergebnis bei der Berechnung der Berechtigung zum Besuch der gymnasialen Oberstufe mit dem Mittleren Schulabschlusses nach Klasse 10. */
	public msa_q = new AbschlussErgebnis();

	constructor(o: string) {
		const testfall = JSON.parse(o);
		this.input = GEAbschlussFaecher.transpilerFromJSON(JSON.stringify(testfall.input));
		if (testfall.Prognose !== null)
			this.prognose = AbschlussErgebnis.transpilerFromJSON(JSON.stringify(testfall.Prognose));
		if (testfall.HA9 !== null)
			this.ha9 = AbschlussErgebnis.transpilerFromJSON(JSON.stringify(testfall.HA9));
		if (testfall.HA10 !== null)
			this.ha10 = AbschlussErgebnis.transpilerFromJSON(JSON.stringify(testfall.HA10));
		if (testfall.MSA !== null)
			this.msa = AbschlussErgebnis.transpilerFromJSON(JSON.stringify(testfall.MSA));
		if (testfall.MSA_Q !== null)
			this.msa_q = AbschlussErgebnis.transpilerFromJSON(JSON.stringify(testfall.MSA_Q));
	}
}