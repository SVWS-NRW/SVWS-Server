import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Abteilung } from "@core/core/data/schule/Abteilung";

export class ValidatorAbteilungBezeichnungUniqueInFolgeAbschnitt extends BasicValidator {

	private readonly data: () => string;
	private readonly abteilungenFolgeAbschnitt: () => Iterable<Abteilung>;
	private readonly active: boolean;

	constructor(data: () => string, abteilungenFolgeAbschnitt: () => Iterable<Abteilung>, active: boolean) {
		super(ValidatorFehlerart.HINWEIS);
		this.data = data;
		this.abteilungenFolgeAbschnitt = abteilungenFolgeAbschnitt;
		this.active = active;
	}

	protected isActive(): boolean {
		return this.active;
	}

	protected pruefe(): boolean {
		for (const abteilung of this.abteilungenFolgeAbschnitt()) {
			if (abteilung.bezeichnung.toLowerCase() === this.data().toLowerCase()) {
				this.addFehler(0, "Es existiert eine Abteilung im nachfolgenden Schuljahresabschnitt mit der gleichen Bezeichnung.");
				return false;
			}
		}
		return true;
	}
}
