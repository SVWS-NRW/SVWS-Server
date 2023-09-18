import { AbschlussErgebnis, GEAbschlussFaecher } from "@core";

export class GEAbschlussTestfall {
	/** Die Fachinformationen für die Abschlussberechnung. */
	public input: GEAbschlussFaecher;

	/** Das erwartete Ergebnis bei der Prognoseberechnung. */
	public prognose: AbschlussErgebnis | null;

	/** Das erwartete Ergebnis bei der Berechnung des Hauptschulabschlusses nach Klasse 9. */
	public ha9: AbschlussErgebnis | null;

	/** Das erwartete Ergebnis bei der Berechnung des Hauptschulabschlusses nach Klasse 10. */
	public ha10: AbschlussErgebnis | null;

	/** Das erwartete Ergebnis bei der Berechnung des Mittleren Schulabschlusses nach Klasse 10. */
	public msa: AbschlussErgebnis | null;

	/** Das erwartete Ergebnis bei der Berechnung der Berechtigung zum Besuch der gymnasialen Oberstufe mit dem Mittleren Schulabschlusses nach Klasse 10. */
	public msa_q: AbschlussErgebnis | null;

	constructor(o : any) {
		this.input = GEAbschlussFaecher.transpilerFromJSON(JSON.stringify(o.input));
		this.prognose = !o.Prognose ? null : AbschlussErgebnis.transpilerFromJSON(JSON.stringify(o.Prognose));
		this.ha9 = !o.HA9 ? null : AbschlussErgebnis.transpilerFromJSON(JSON.stringify(o.HA9));
		this.ha10 = !o.HA10 ? null : AbschlussErgebnis.transpilerFromJSON(JSON.stringify(o.HA10));
		this.msa = !o.msa ? null : AbschlussErgebnis.transpilerFromJSON(JSON.stringify(o.MSA));
		this.msa_q = !o.msa_q ? null : AbschlussErgebnis.transpilerFromJSON(JSON.stringify(o.MSA_Q));
	}
}
