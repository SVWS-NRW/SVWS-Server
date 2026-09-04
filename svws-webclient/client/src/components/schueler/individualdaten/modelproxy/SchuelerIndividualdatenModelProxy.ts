import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { SchuelerStatusKatalogEintrag } from "@core/asd/data/schueler/SchuelerStatusKatalogEintrag";
import type { NationalitaetenKatalogEintrag } from "@core/asd/data/schule/NationalitaetenKatalogEintrag";
import type { VerkehrsspracheKatalogEintrag } from "@core/asd/data/schule/VerkehrsspracheKatalogEintrag";
import { Geschlecht } from "@core/asd/types/Geschlecht";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import { Nationalitaeten } from "@core/asd/types/schule/Nationalitaeten";
import { Verkehrssprache } from "@core/asd/types/schule/Verkehrssprache";
import { ValidatorSsdSchuelerStammdatenGeburtsdatum } from "@core/asd/validate/schueler/ValidatorSsdSchuelerStammdatenGeburtsdatum";
import { ValidatorSsgSchuelerStammdatenGeschlecht } from "@core/asd/validate/schueler/ValidatorSsgSchuelerStammdatenGeschlecht";
import { ValidatorSsnSchuelerStammdatenNachname } from "@core/asd/validate/schueler/ValidatorSsnSchuelerStammdatenNachname";
import { ValidatorSsvSchuelerStammdatenVorname } from "@core/asd/validate/schueler/ValidatorSsvSchuelerStammdatenVorname";
import type { ValidatorKontext } from "@core/asd/validate/ValidatorKontext";
import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
import type { OrtsteilKatalogEintrag } from "@core/core/data/kataloge/OrtsteilKatalogEintrag";
import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import { AdressenUtils } from "@core/core/utils/AdressenUtils";
import { ModelProxy } from "@ui/model/ModelProxy";
import type { OrteState } from "@ui/states/kataloge/OrteState";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorNumberRange } from "@ui/validation/common/ValidatorNumberRange";
import { ValidatorStrasse } from "@ui/validation/common/ValidatorStrasse";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";
import { ValidatorSchuelerGeburtsdatum } from "@ui/validation/ValidatorSchuelerGeburtsdatum";
import { computed } from "vue";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";

export class SchuelerIndividualdatenModel extends ModelProxy<SchuelerStammdaten> {

	private readonly orteState: OrteState = orteStateImpl;

	private readonly schuljahr: () => number;
	private readonly religionenById: () => Map<number, ReligionEintrag>;
	private readonly fahrschuelerartenById: () => Map<number, Fahrschuelerart>;
	private readonly haltestellenById: () => Map<number, Haltestelle>;

	constructor(
		data: () => SchuelerStammdaten,
		validatorKontext: () => ValidatorKontext,
		schuljahr: () => number,
		religionenById: () => Map<number, ReligionEintrag>,
		fahrschuelerartenById: () => Map<number, Fahrschuelerart>,
		haltestellenById: () => Map<number, Haltestelle>,
		patch?: (data: Partial<SchuelerStammdaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchuelerStammdaten> = ["geschlecht", "status", "fahrschuelerArtID",
			"haltestelleID", "idStaatsangehoerigkeit", "idStaatsangehoerigkeit2", "religionID", "idGeburtsland",
			"idVerkehrspracheFamilie", "idGeburtslandMutter", "idGeburtslandVater", "istDuplikat", "istVolljaehrig",
			"keineAuskunftAnDritte", "istSchulpflichtErfuellt", "istBerufsschulpflichtErfuellt", "hatMasernimpfnachweis",
			"erhaeltSchuelerBAFOEG", "druckeKonfessionAufZeugnisse", "hatMigrationshintergrund", "externeSchulNr",
		];
		super({ data, patch, listOfAutopatchProps });

		this.schuljahr = schuljahr;
		this.religionenById = religionenById;
		this.fahrschuelerartenById = fahrschuelerartenById;
		this.haltestellenById = haltestellenById;
		this.addAsdValidatoren(validatorKontext());
		this.addUiValidatoren();
		this.validate();
	}

	private addAsdValidatoren(vk: ValidatorKontext) {
		this.addValidator(new ValidatorSsnSchuelerStammdatenNachname({ get: () => this.proxy.nachname }, vk), "nachname");
		this.addValidator(new ValidatorSsvSchuelerStammdatenVorname({ get: () => this.proxy.vorname }, vk), "vorname");
		this.addValidator(new ValidatorSsgSchuelerStammdatenGeschlecht({ get: () => this.proxy.geschlecht }, vk), "geschlecht");
		this.addValidator(new ValidatorSsdSchuelerStammdatenGeburtsdatum({ get: () => this.proxy.geburtsdatum }, vk), "geburtsdatum");
	}

	private addUiValidatoren() {
		// Nachname
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.nachname, null, 120), "nachname");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.nachname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "nachname");

		// Rufname (vorname) – Required durch ASD abgedeckt
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.vorname, null, 80), "vorname");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.vorname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "vorname");

		// Alle Vornamen
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.alleVornamen, null, 255), "alleVornamen");

		// Geburtsdatum – Required + Datumsformat durch ASD abgedeckt
		this.addBlockingValidator(new ValidatorSchuelerGeburtsdatum(() => this.proxy.geburtsdatum, () => null, false), "geburtsdatum");

		// Geburtsort
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.geburtsort, null, 100), "geburtsort");

		// Geburtsname
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.geburtsname, null, 120), "geburtsname");

		// Straße (parallel zu #3222)
		this.addBlockingValidator(new ValidatorStrasse(() => this.adresse.value, 55, 10, 30), "strassenname", "hausnummer", "hausnummerZusatz");

		// Telefon
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefon, null, 20), "telefon");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon, StringPattern.IS_PHONE_NUMBER), "telefon");

		// Mobil / Fax
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefonMobil, null, 20), "telefonMobil");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefonMobil, StringPattern.IS_PHONE_NUMBER), "telefonMobil");

		// Private E-Mail
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.emailPrivat, null, 100), "emailPrivat");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.emailPrivat, StringPattern.IS_EMAIL), "emailPrivat");

		// Schulische E-Mail
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.emailSchule, null, 100), "emailSchule");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.emailSchule, StringPattern.IS_EMAIL), "emailSchule");

		// Status – Required
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.status), "status");

		// Schülerausweis-Nummer
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.idSchuelerausweis, null, 30), "idSchuelerausweis");

		// Beruf
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.beruf, null, 100), "beruf");

		// 1. Staatsangehörigkeit – Required
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idStaatsangehoerigkeit), "idStaatsangehoerigkeit");

		// Konfession – Required
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.religionID), "religionID");

		// Zuzugsjahr
		const nextYear = new Date().getFullYear() + 1;
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.zuzugsjahr, nextYear - 100, nextYear), "zuzugsjahr");
	}

	geschlecht = computed<Geschlecht | null>({
		get: () => Geschlecht.fromValue(this.proxy.geschlecht),
		set: (v: Geschlecht | null) => this.proxy.geschlecht = v?.id ?? Geschlecht.X.id,
	});

	status = computed<SchuelerStatusKatalogEintrag | null>({
		get: () => {
			for (const s of SchuelerStatus.values()) {
				const d = s.daten(this.schuljahr());
				if (d?.id === this.proxy.status) {
					return d;
				}
			}
			return null;
		},
		set: (v: SchuelerStatusKatalogEintrag | null) => {
			if (v !== null) {
				this.proxy.status = v.id;
			}
		},
	});

	religionID = computed<ReligionEintrag | null>({
		get: () => this.proxy.religionID === null ? null : this.religionenById().get(this.proxy.religionID) ?? null,
		set: (v: ReligionEintrag | null) => this.proxy.religionID = v?.id ?? null,
	});

	fahrschuelerArtID = computed<Fahrschuelerart | null>({
		get: () => this.proxy.fahrschuelerArtID === null ? null : this.fahrschuelerartenById().get(this.proxy.fahrschuelerArtID) ?? null,
		set: (v: Fahrschuelerart | null) => this.proxy.fahrschuelerArtID = v?.id ?? null,
	});

	haltestelleID = computed<Haltestelle | null>({
		get: () => this.proxy.haltestelleID === null ? null : this.haltestellenById().get(this.proxy.haltestelleID) ?? null,
		set: (v: Haltestelle | null) => this.proxy.haltestelleID = v?.id ?? null,
	});

	staatsangehoerigkeitID = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(this.proxy.idStaatsangehoerigkeit)?.daten(this.schuljahr()) ?? null,
		set: (v: NationalitaetenKatalogEintrag | null) => this.proxy.idStaatsangehoerigkeit = v?.id ?? null,
	});

	staatsangehoerigkeit2ID = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(this.proxy.idStaatsangehoerigkeit2)?.daten(this.schuljahr()) ?? null,
		set: (v: NationalitaetenKatalogEintrag | null) => this.proxy.idStaatsangehoerigkeit2 = v?.id ?? null,
	});

	geburtsland = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(this.proxy.idGeburtsland)?.daten(this.schuljahr()) ?? null,
		set: (v: NationalitaetenKatalogEintrag | null) => this.proxy.idGeburtsland = v?.id ?? null,
	});

	geburtslandMutter = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(this.proxy.idGeburtslandMutter)?.daten(this.schuljahr()) ?? null,
		set: (v: NationalitaetenKatalogEintrag | null) => this.proxy.idGeburtslandMutter = v?.id ?? null,
	});

	geburtslandVater = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(this.proxy.idGeburtslandVater)?.daten(this.schuljahr()) ?? null,
		set: (v: NationalitaetenKatalogEintrag | null) => this.proxy.idGeburtslandVater = v?.id ?? null,
	});

	verkehrspracheFamilie = computed<VerkehrsspracheKatalogEintrag | null>({
		get: () => Verkehrssprache.data().getWertByIDOrNull(this.proxy.idVerkehrspracheFamilie)?.daten(this.schuljahr()) ?? null,
		set: (v: VerkehrsspracheKatalogEintrag | null) => this.proxy.idVerkehrspracheFamilie = v?.id ?? null,
	});

	selectedOrt = computed<OrtKatalogEintrag | null>({
		get: () => this.orteState.orte.byId.get(this.proxy.wohnortID ?? -1) ?? null,
		set: (value: OrtKatalogEintrag | null) => this.setAndPatchOrtAndOrtsteil(value?.id ?? null, null),
	});

	selectedOrtsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => this.orteState.ortsteile.byId.get(this.proxy.ortsteilID ?? -1) ?? null,
		set: (value: OrtsteilKatalogEintrag | null) => {
			const v = value;
			const wohnortId = (v === null) ? this.proxy.wohnortID : (this.orteState.orte.byId.get(v.idOrt ?? -1)?.id ?? null);
			const ortsteilId = (v === null || wohnortId === null) ? null : v.id;
			this.setAndPatchOrtAndOrtsteil(wohnortId, ortsteilId);
		},
	});

	externeSchulNr = computed<string | null>({
		get: () => this.proxy.externeSchulNr ?? null,
		set: (v) => this.proxy.externeSchulNr = v ?? null,
	});

	private setAndPatchOrtAndOrtsteil(wohnortID: number | null, ortsteilID: number | null): void {
		this.pending = { ...this.pending, wohnortID, ortsteilID };
		void this.patch();
	}

	adresse = computed<string | null>({
		get: () => AdressenUtils.combineStrasse(this.proxy.strassenname, this.proxy.hausnummer ?? '', this.proxy.hausnummerZusatz ?? ''),
		set: (adresse) => {
			const [strassenname, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(adresse);
			if (strassenname === '' && hausnummer === '' && hausnummerZusatz === '') {
				this.proxy.strassenname = null;
				this.proxy.hausnummer = null;
				this.proxy.hausnummerZusatz = null;
			} else {
				this.proxy.strassenname = strassenname;
				this.proxy.hausnummer = hausnummer;
				this.proxy.hausnummerZusatz = hausnummerZusatz;
			}
		},
	});
}
