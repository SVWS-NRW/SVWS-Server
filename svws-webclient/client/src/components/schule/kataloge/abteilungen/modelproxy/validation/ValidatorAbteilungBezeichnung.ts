import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Abteilung } from "@core/core/data/schule/Abteilung";
import { ViewType } from "@ui/ui/nav/ViewType";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";
import { ValidatorAbteilungBezeichnungUniqueInFolgeAbschnitt } from "~/components/schule/kataloge/abteilungen/modelproxy/validation/ValidatorAbteilungBezeichnungUnique";

export class ValidatorAbteilungBezeichnung extends BasicValidator {

	constructor(data: () => Abteilung, abteilungenAktAbschnitt: () => Iterable<Abteilung>, abteilungenFolgeAbschnitt: () => Iterable<Abteilung>, viewType: ViewType) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorInputRequired(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, 1, 50));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringIsUniqueInList(data, (data: Abteilung) => data.id, (data: Abteilung) => data.bezeichnung,
			abteilungenAktAbschnitt, false));
		this._validatoren.add(new ValidatorAbteilungBezeichnungUniqueInFolgeAbschnitt(() => data().bezeichnung, abteilungenFolgeAbschnitt, viewType === ViewType.HINZUFUEGEN));
	}

	protected pruefe(): boolean {
		return true;
	}
}
