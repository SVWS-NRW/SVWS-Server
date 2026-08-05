import { computed } from "vue";
import type { OrteState } from "@ui";
import {
	ModelProxy, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern, StringPattern, ValidatorStrasse,
} from "@ui";
import type { Erzieherart, ErzieherStammdaten, NationalitaetenKatalogEintrag, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
import { AdressenUtils, Nationalitaeten } from "@core";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";

export class ErzieherStammdatenModelProxy extends ModelProxy<ErzieherStammdaten> {

	private readonly orteState: OrteState = orteStateImpl;

	private readonly _erzieherartenById: () => Map<number, Erzieherart>;
	private readonly _schuljahr: () => number;

	constructor(
		data: () => ErzieherStammdaten,
		erzieherartenById: () => Map<number, Erzieherart>,
		schuljahr: () => number,
		patch?: (data: Partial<ErzieherStammdaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof ErzieherStammdaten> = [
			'idErzieherArt', 'staatsangehoerigkeitID', 'wohnortID', 'ortsteilID', 'erhaeltAnschreiben'];
		super({	data, patch, listOfAutopatchProps });

		this._erzieherartenById = erzieherartenById;
		this._schuljahr = schuljahr;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {

		// Erzieherart
		this.addBlockingValidator(new ValidatorInputRequired(() => this.erzieherart.value), "idErzieherArt");

		// Anrede
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.anrede, null, 20), "anrede");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.anrede, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "anrede");

		// Titel
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.titel, null, 10), "titel");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.titel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "titel");

		// Name
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.nachname), "nachname");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.nachname, null, 120), "nachname");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.nachname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "nachname");

		// Rufname
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.vorname), "vorname");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.vorname, null, 80), "vorname");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.vorname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "vorname");

		// E-Mail
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.eMail, null, 100), "eMail");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.eMail, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "eMail");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.eMail, StringPattern.IS_EMAIL), "eMail");

		// Adresse
		this.addBlockingValidator(new ValidatorStrasse(() => this.adresse.value, 55, 10, 30), "strassenname", "hausnummer", "hausnummerZusatz");

		// Bemerkungen
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkungen, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bemerkungen");

	}

	erzieherart = computed<Erzieherart | null>({
		get: () => this._erzieherartenById().get(this.proxy.idErzieherArt ?? -1) ?? null,
		set: (v: Erzieherart | null) => this.proxy.idErzieherArt = v?.id ?? null,
	});

	staatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.getByISO3(this.proxy.staatsangehoerigkeitID)?.daten(this._schuljahr()) ?? null,
		set: (v: NationalitaetenKatalogEintrag | null) => this.proxy.staatsangehoerigkeitID = v?.iso3 ?? null,
	});

	wohnort = computed<OrtKatalogEintrag | null>({
		get: () => this.orteState.orte.byId.get(this.proxy.wohnortID ?? -1) ?? null,
		set: (v: OrtKatalogEintrag | null) => {
			this.proxy.wohnortID = v?.id ?? null;
			this.proxy.ortsteilID = null;
		},
	});

	ortsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => this.orteState.ortsteile.byId.get(this.proxy.ortsteilID ?? -1) ?? null,
		set: (v: OrtsteilKatalogEintrag | null) => this.proxy.ortsteilID = v?.id ?? null,
	});

	adresse = computed({
		get: () => AdressenUtils.combineStrasse(this.proxy.strassenname, this.proxy.hausnummer, this.proxy.hausnummerZusatz),
		set: (adresse: string | null) => {
			const [strassenname, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(adresse);
			this.proxy.strassenname = strassenname;
			this.proxy.hausnummer = hausnummer;
			this.proxy.hausnummerZusatz = hausnummerZusatz;
		},
	});
}
