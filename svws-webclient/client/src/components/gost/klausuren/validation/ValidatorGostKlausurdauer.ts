import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { GostKlausurvorgabe } from "@core/core/data/gost/klausuren/GostKlausurvorgabe";
import { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";

export class ValidatorGostKlausurdauer extends BasicValidator {

	public constructor(
		private readonly data: () => GostKlausurvorgabe | null,
		private readonly berechneApoDauer: (vorgabe: GostKlausurvorgabe) => number,
		private readonly istNeuEinsetzendeFremdsprache: (vorgabe: GostKlausurvorgabe) => boolean
	) {
		super(ValidatorFehlerart.HINWEIS);
	}

	protected pruefe(): boolean {
		const vorgabe = this.data();
		if (vorgabe === null) {
			return true;
		}
		const apoDauer = this.berechneApoDauer(vorgabe);
		if (vorgabe.dauer === apoDauer) {
			return true;
		}
		const halbjahr = GostHalbjahr.fromIDorException(vorgabe.halbjahr);
		if (halbjahr.istEinfuehrungsphase() && this.istNeuEinsetzendeFremdsprache(vorgabe)) {
			const minDauer = apoDauer - 45;
			if ((vorgabe.dauer >= minDauer) && (vorgabe.dauer <= apoDauer)) {
				return true;
			}
			this.addFehler(0, `Die Dauer weicht von den Vorgaben in APO-GOSt ab (${minDauer}-${apoDauer} Minuten).`);
			return false;
		}
		this.addFehler(0, `Die Dauer weicht von den Vorgaben in APO-GOSt ab (${apoDauer} Minuten).`);
		return false;
	}

}
