import { ModelProxy, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern, StringPattern, ValidatorStrasse } from "@ui";
import type { Erzieherart, ErzieherStammdaten, NationalitaetenKatalogEintrag, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
import { AdressenUtils, Nationalitaeten } from "@core";
import { computed } from "vue";

export class ErzieherStammdatenModelProxy extends ModelProxy<ErzieherStammdaten> {

	private readonly _erzieherartenById: () => Map<number, Erzieherart>;
	private readonly _orteById: () => Map<number, OrtKatalogEintrag>;
	private readonly _ortsteileById: () => Map<number, OrtsteilKatalogEintrag>;
	private readonly _schuljahr: () => number;
	constructor(
		data: () => ErzieherStammdaten,
		erzieherartenById: () => Map<number, Erzieherart>,
		orteById: () => Map<number, OrtKatalogEintrag>,
		ortsteileById: () => Map<number, OrtsteilKatalogEintrag>,
		schuljahr: () => number,
		patch?: (data: Partial<ErzieherStammdaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof ErzieherStammdaten> = [
			'idErzieherArt', 'staatsangehoerigkeitID', 'wohnortID', 'ortsteilID', 'erhaeltAnschreiben'];
		super({	data, patch, listOfAutopatchProps, checkValidBeforePatch: true });
		this._erzieherartenById = erzieherartenById;
		this._orteById = orteById;
		this._ortsteileById = ortsteileById;
		this._schuljahr = schuljahr;
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {

		// Erzieherart
		this.addValidator(new ValidatorInputRequired(() => this.erzieherart.value), "idErzieherArt");

		// Anrede
		this.addValidator(new ValidatorStringLength(() => this.proxy.anrede, null, 20), "anrede");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.anrede, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "anrede");

		// Titel
		this.addValidator(new ValidatorStringLength(() => this.proxy.titel, null, 10), "titel");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.titel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "titel");

		// Name
		this.addValidator(new ValidatorInputRequired(() => this.proxy.nachname), "nachname");
		this.addValidator(new ValidatorStringLength(() => this.proxy.nachname, null, 120), "nachname");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.nachname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "nachname");

		// Rufname
		this.addValidator(new ValidatorInputRequired(() => this.proxy.vorname), "vorname");
		this.addValidator(new ValidatorStringLength(() => this.proxy.vorname, null, 80), "vorname");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.vorname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "vorname");

		// E-Mail
		this.addValidator(new ValidatorStringLength(() => this.proxy.eMail, null, 100), "eMail");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.eMail, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "eMail");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.eMail, StringPattern.IS_EMAIL), "eMail");

		// Adresse
		this.addValidator(new ValidatorStrasse(() => this.adresse.value, 55, 10, 30), "strassenname", "hausnummer", "hausnummerZusatz");

		// Bemerkungen
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkungen, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bemerkungen");

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
		get: () => this._orteById().get(this.proxy.wohnortID ?? -1) ?? null,
		set: (v: OrtKatalogEintrag | null) => {
			this.proxy.wohnortID = v?.id ?? null;
			this.proxy.ortsteilID = null;
		},
	});

	ortsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => this._ortsteileById().get(this.proxy.ortsteilID ?? -1) ?? null,
		set: (v: OrtsteilKatalogEintrag | null) => this.proxy.ortsteilID = v?.id ?? null,
	});

	ortsteileFiltered = computed<OrtsteilKatalogEintrag[]>(
		() => Array.from(this._ortsteileById().values())
			.filter(o => o.ort_id === this.proxy.wohnortID)
	);

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
