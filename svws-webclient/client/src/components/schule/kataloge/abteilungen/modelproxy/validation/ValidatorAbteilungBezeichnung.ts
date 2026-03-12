import { type Abteilung, BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern, ViewType } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";
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
