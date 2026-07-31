import type { FachklasseKatalogEintrag } from "@core";
import { BasicValidator, Fachklasse, ValidatorFehlerart } from "@core";

export class ValidatorFachklasseFeldDifferentFromCoreType extends BasicValidator {

	private readonly data: () => string | null;
	private readonly idFachklasse: () => number | null;
	private readonly feldAccessor: (eintrag: FachklasseKatalogEintrag) => string | null;

	constructor(
		data: () => string | null,
		idFachklasse: () => number | null | undefined,
		feldAccessor: (eintrag: FachklasseKatalogEintrag) => string | null
	) {
		super(ValidatorFehlerart.HINWEIS);
		this.data = () => data() ?? null;
		this.idFachklasse = () => idFachklasse() ?? null;
		this.feldAccessor = feldAccessor;
	}

	protected pruefe(): boolean {
		const data = this.data();
		if (this.idFachklasse() === null) {
			return true;
		}
		const fachklasse = Fachklasse.data().getEintragByID(this.idFachklasse());
		if (fachklasse === null) {
			return true;
		}
		let originalwert = this.feldAccessor(fachklasse);
		if (originalwert !== data) {
			if (((originalwert === null) || (originalwert === "")) && (data === null || data === "")) {
				// Beim Leeren des Feldes patched der Input auf leeren String statt zurück auf null -> in dem Fall soll dem User kein Hinweis angezeigt werden
				return true;
			}
			originalwert ??= "Keine Angabe";
			this.addFehler(0, `Das Wert wurde vom Originalwert "${originalwert}" abgeändert.`);
			return false;
		}
		return true;
	}

}
