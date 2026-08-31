import { computed } from "vue";
import type { BetriebeListeManager } from "@ui";
import { ModelProxy, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { Betrieb, Betriebsart, OrtKatalogEintrag } from "@core";
import { JavaInteger, AdressenUtils } from "@core";
import { ValidatorBetriebName } from "~/components/schule/kataloge/betriebe/modelproxy/validation/ValidatorBetriebName";
import { StringPattern } from "../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";
import { ValidatorStrasse } from "../../../../../../../ui/src/validation/common/ValidatorStrasse";

export class BetriebModelProxy extends ModelProxy<Betrieb> {

	private readonly manager: () => BetriebeListeManager;

	constructor(
		data: () => Betrieb,
		manager: () => BetriebeListeManager,
		patch?: (data: Partial<Betrieb>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Betrieb> = [
			"idBetriebsart", "istAusbildungsbetrieb", "istMassnahmentraeger", "belehrungNachISGErforderlich", "bietetPraktikumsplaetzeAn",
			"erweitertesFuehrungszeugnisErforderlich", "idOrt", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.manager = manager;
		this.addValidatoren(() => manager().liste.list());
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Betrieb>) {
		this.addBlockingValidator(new ValidatorBetriebName(() => this.proxy, liste), "name");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.nameZusatz, null, 50), "nameZusatz");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.nameZusatz, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "nameZusatz");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.branche, null, 50), "branche");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.branche, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "branche");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bemerkungen, null, 255), "bemerkungen");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkungen, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bemerkungen");

		this.addBlockingValidator(new ValidatorStrasse(() => this.adresse.value, 55, 10, 30), "strasse", "hausnummer", "hausnummerZusatz");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefon1, null, 20), "telefon1");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon1, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "telefon1");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon1, StringPattern.IS_PHONE_NUMBER), "telefon1");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefon2, null, 20), "telefon2");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon2, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "telefon2");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon2, StringPattern.IS_PHONE_NUMBER), "telefon2");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.eMail, null, 100), "eMail");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.eMail, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "eMail");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.eMail, StringPattern.IS_EMAIL), "eMail");

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.fax, null, 20), "fax");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.fax, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "fax");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.fax, StringPattern.IS_PHONE_NUMBER), "fax");

		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, JavaInteger.MAX_VALUE), "sortierung");
	}

	betriebsart = computed<Betriebsart | null>({
		get: () => this.manager().betriebsartenById.get(this.proxy.idBetriebsart ?? -1) ?? null,
		set: (v: Betriebsart | null) => this.proxy.idBetriebsart = v?.id ?? null,
	});

	wohnort = computed<OrtKatalogEintrag | null>({
		get: () => this.manager().orteById.get(this.proxy.idOrt ?? -1) ?? null,
		set: (v: OrtKatalogEintrag | null) => this.proxy.idOrt = v?.id ?? null,
	});

	adresse = computed({
		get: () => AdressenUtils.combineStrasse(this.proxy.strasse, this.proxy.hausnummer, this.proxy.hausnummerZusatz),
		set: (adresse: string | null) => {
			const [strasse, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(adresse);
			this.proxy.strasse = strasse;
			this.proxy.hausnummer = hausnummer;
			this.proxy.hausnummerZusatz = hausnummerZusatz;
		},
	});
}
