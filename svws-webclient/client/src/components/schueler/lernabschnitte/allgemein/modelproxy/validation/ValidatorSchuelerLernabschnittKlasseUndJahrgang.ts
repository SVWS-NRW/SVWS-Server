import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";

export class ValidatorSchuelerLernabschnittKlasseUndJahrgang extends BasicValidator {

	private readonly getKlasse: () => KlassenDaten | null;
	private readonly getJahrgang: () => JahrgangsDaten | null;
	private readonly klassenListe: () => KlassenDaten[];
	private readonly jahrgangsListe: () => JahrgangsDaten[];

	constructor(idKlasse: () => number | null, idJahrgang: () => number | null, klassenListe: () => KlassenDaten[], jahrgangsListe: () => JahrgangsDaten[]) {
		super(ValidatorFehlerart.MUSS);
		this.getKlasse = () => this.klassenListe().find(k => k.id === idKlasse()) ?? null;
		this.getJahrgang = () => this.jahrgangsListe().find(j => j.id === idJahrgang()) ?? null;
		this.klassenListe = klassenListe;
		this.jahrgangsListe = jahrgangsListe;

		this._validatoren.add(new ValidatorInputRequired(() => idKlasse()));
	}

	protected pruefe(): boolean	{
		const klasse = this.getKlasse();
		const jahrgang = this.getJahrgang();

		if ((klasse === null) || (jahrgang === null)) {
			return true;
		}

		// Jahrgangsübergreifende Klasse
		if (this.istKlasseJahrgangsuebergreifend()) {
			return true;
		}

		const klasseJahrgang = this.jahrgangsListe().find(j => j.id === klasse.idJahrgang);
		if (klasseJahrgang?.idJahrgang !== jahrgang.idJahrgang) {
			this.addFehler(1, "Die Kombination aus Klasse und Jahrgang ist nicht gültig.");
		}

		return this.getFehler().isEmpty();
	}

	private istKlasseJahrgangsuebergreifend() {
		return this.getKlasse()?.idJahrgang === null;
	}
}
