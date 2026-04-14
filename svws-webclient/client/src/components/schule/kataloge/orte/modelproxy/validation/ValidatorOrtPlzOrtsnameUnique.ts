import type { OrtKatalogEintrag } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";

export class ValidatorOrtPlzOrtsnameUnique extends BasicValidator {

	constructor(private readonly data: () => OrtKatalogEintrag, private readonly alleOrte: () => Iterable<OrtKatalogEintrag>) {
		super(ValidatorFehlerart.MUSS);
	}

	protected pruefe(): boolean {
		const current = this.data();
		for (const ort of this.alleOrte()) {
			if (ort.id === current.id) {
				continue;
			}
			if (ort.plz === current.plz
					&& ort.ortsname?.toLowerCase() === current.ortsname?.toLowerCase()) {
				this.addFehler(0, "Die Kombination aus PLZ und Ortsname ist bereits vergeben.");
				return false;
			}
		}
		return true;
	}

}
