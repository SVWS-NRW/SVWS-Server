import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import type { SchuelerLernabschnittManager } from "~/components/schueler/lernabschnitte/SchuelerLernabschnittManager";

export class ValidatorSchuelerLernabschnittKlasseUndJahrgang extends BasicValidator {

	private readonly getManager: () => SchuelerLernabschnittManager;
	private readonly getKlasse: () => KlassenDaten | null;
	private readonly getJahrgang: () => JahrgangsDaten | null;

	constructor(idKlasse: () => number | null, idJahrgang: () => number | null, manager: () => SchuelerLernabschnittManager) {
		super(ValidatorFehlerart.MUSS);
		this.getManager = manager;
		this.getKlasse = () => this.getManager().klasseGetByIdOrNull(idKlasse() ?? -1);
		this.getJahrgang = () => this.getManager().jahrgangGetByIdOrNull(idJahrgang() ?? -1);

		this._validatoren.add(new ValidatorInputRequired(() => idKlasse()));
	}

	protected pruefe(): boolean	{
		const manager = this.getManager();
		const klasse = this.getKlasse();
		const jahrgang = this.getJahrgang();

		if ((klasse === null) || (jahrgang === null)) {
			return true;
		}

		// Jahrgangsübergreifende Klasse
		if (this.istKlasseJahrgangsuebergreifend()) {
			return true;
		}

		const klasseJahrgang = manager.jahrgangGetByIdOrNull(klasse.idJahrgang ?? -1);
		if (klasseJahrgang?.idJahrgang !== jahrgang.idJahrgang) {
			this.addFehler(1, "Die Kombination aus Klasse und Jahrgang ist nicht gültig.");
		}

		return this.getFehler().isEmpty();
	}

	private istKlasseJahrgangsuebergreifend() {
		return this.getKlasse()?.idJahrgang === null;
	}
}
