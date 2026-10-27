import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";

export class ValidatorSchuelerAlleVornamen extends BasicValidator {

	constructor(data: () => { alleVornamen: string | null }) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => data().alleVornamen, null, 255));
	}

	protected pruefe(): boolean {
		return true;
	}
}
