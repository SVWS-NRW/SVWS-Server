import { PendingStateManager } from "@ui";
import type { AuswahlManager, Haltestelle, KatalogEintrag, ReligionEintrag, SchuelerStammdaten, SchulEintrag } from "@core";
import { Nationalitaeten, SchuelerStatus, Verkehrssprache } from "@core";
import { computed } from "vue";

/**
 * Die Klasse `PendingStateManagerSchuelerIndividualdaten` erweitert den `PendingStateManager`
 * und dient der Verwaltung von individuell bezogenen Schülerdaten innerhalb eines PendingState.
 *
 */
export class PendingStateManagerSchuelerIndividualdaten extends PendingStateManager<SchuelerStammdaten> {
	/**
	 * Maps, die Religion-IDs zu entsprechenden Religionseinträgen zuordnet.
	 */
	private readonly _mapReligionen: Map<number, ReligionEintrag>;

	/**
	 * Maps, die Schulnummern zu entsprechenden Schuleinträgen zuordnet.
	 */
	private readonly _mapSchulen: Map<string, SchulEintrag>;

	/**
	 * Maps, die Haltestellen-IDs zu entsprechenden Katalogeinträgen zuordnet.
	 */
	private readonly _mapHaltestellen: Map<number, Haltestelle>;

	/**
	 * Maps, die Fahrschülerarten zu entsprechenden Katalogeinträgen zuordnet.
	 */
	private readonly _mapFahrschuelerarten: Map<number, KatalogEintrag>;

	/**
	 * Konstruktor, der einen neuen PendingState für Schülerstammdaten erstellt.
	 * @param idFieldName Der Name des Attributs, welches Änderungen hält.
	 * @param auswahlManager Funktion, die einen AuswahlManager bereitstellt.
	 * @param mapReligionen Map der Religionen.
	 * @param mapSchulen Map der Schulen.
	 * @param mapHaltestellen Map der Haltestellen.
	 * @param mapFahrschuelerarten Map der Fahrschülerarten.
	 */
	public constructor(idFieldName: any, auswahlManager: () => AuswahlManager<any, any, SchuelerStammdaten>, mapReligionen: Map<number, ReligionEintrag>,
		mapSchulen: Map<string, SchulEintrag>, mapHaltestellen: Map<number, Haltestelle>, mapFahrschuelerarten: Map<number, KatalogEintrag>) {
		super(idFieldName, auswahlManager);
		this._mapReligionen = mapReligionen;
		this._mapSchulen = mapSchulen;
		this._mapHaltestellen = mapHaltestellen;
		this._mapFahrschuelerarten = mapFahrschuelerarten;
		this.initializeAttributeDisplayMappers();
	}

	/**
	 * Initialisiert die Mapper für die Attributanzeige und ordnet
	 * verschiedenen Attributen spezifische Darstellungslogiken zu.
	 */
	private initializeAttributeDisplayMappers() {
		this._attributeDisplayMappers.set('status', (value: any) => SchuelerStatus.data().getWertByKuerzel('' + value)?.daten(this.auswahlManager.getSchuljahr())?.text);
		this._attributeDisplayMappers.set('staatsangehoerigkeitID', (value: any) => Nationalitaeten.getByISO3(value)?.daten(this.auswahlManager.getSchuljahr())?.bezeichnung);
		this._attributeDisplayMappers.set('staatsangehoerigkeit2ID', (value: any) => Nationalitaeten.getByISO3(value)?.daten(this.auswahlManager.getSchuljahr())?.bezeichnung);
		this._attributeDisplayMappers.set('religionID', (value: any) => this._mapReligionen.get(Number(value))?.bezeichnung);
		this._attributeDisplayMappers.set('fahrschuelerArtID', (value: any) => this._mapFahrschuelerarten.get(Number(value))?.text);
		this._attributeDisplayMappers.set('haltestelleID', (value: any) => this._mapHaltestellen.get(Number(value))?.bezeichnung);
		this._attributeDisplayMappers.set('verkehrspracheFamilie', (value: any) => Verkehrssprache.getByIsoKuerzel(value)?.daten(this.auswahlManager.getSchuljahr())?.text);
		this._attributeDisplayMappers.set('geburtsland', (value: any) => Nationalitaeten.data().getWertByKuerzel('' + value)?.daten(this.auswahlManager.getSchuljahr())?.text);
		this._attributeDisplayMappers.set('geburtslandMutter', (value: any) => Nationalitaeten.data().getWertByKuerzel('' + value)?.daten(this.auswahlManager.getSchuljahr())?.text);
		this._attributeDisplayMappers.set('geburtslandVater', (value: any) => Nationalitaeten.data().getWertByKuerzel('' + value)?.daten(this.auswahlManager.getSchuljahr())?.text);
		this._attributeDisplayMappers.set('druckeKonfessionAufZeugnisse', this._defaultBooleanDisplayMapper);
		this._attributeDisplayMappers.set('istVolljaehrig', this._defaultBooleanDisplayMapper);
		this._attributeDisplayMappers.set('keineAuskunftAnDritte', this._defaultBooleanDisplayMapper);
		this._attributeDisplayMappers.set('istSchulpflichtErfuellt', this._defaultBooleanDisplayMapper);
		this._attributeDisplayMappers.set('istBerufsschulpflichtErfuellt', this._defaultBooleanDisplayMapper);
		this._attributeDisplayMappers.set('hatMasernimpfnachweis', this._defaultBooleanDisplayMapper);
		this._attributeDisplayMappers.set('erhaeltSchuelerBAFOEG', this._defaultBooleanDisplayMapper);
		this._attributeDisplayMappers.set('hatMigrationshintergrund', this._defaultBooleanDisplayMapper);
		this._attributeDisplayMappers.set('aufnahmedatum', this._defaultDateDisplayMapper);
		this._attributeDisplayMappers.set('religionabmeldung', this._defaultDateDisplayMapper);
		this._attributeDisplayMappers.set('religionanmeldung', this._defaultDateDisplayMapper);
	}

	/**
	 * Erzeugt das Attribut staatsangehoerigkeitID als computed value.
	 */
	public staatsangehoerigkeitID = this.genComputed<Nationalitaeten | null>('staatsangehoerigkeitID', null,
		(value: string | null) => Nationalitaeten.getByISO3(value),
		(value: Nationalitaeten | null) => value?.historie().getLast().iso3
	);

	/**
	 * Erzeugt das Attribut staatsangehoerigkeit2ID als computed value.
	 */
	public staatsangehoerigkeit2ID = this.genComputed<Nationalitaeten | null>('staatsangehoerigkeit2ID', null,
		(value: string | null) => Nationalitaeten.getByISO3(value),
		(value: Nationalitaeten | null) => value?.historie().getLast().iso3
	);

	/**
	 * Erzeugt das Attribut konfession als computed value.
	 */
	public konfession = this.genComputed<ReligionEintrag | null>('religionID', null,
		(id: number) => this._mapReligionen.get(id) ?? null,
		(value: ReligionEintrag | null) => value?.id
	);

	/**
	 * Erzeugt das Attribut druckeKonfessionAufZeugnisse als computed value.
	 */
	public druckeKonfessionAufZeugnisse = this.genComputed<boolean>('druckeKonfessionAufZeugnisse', false, null, null);

	/**
	 * Erzeugt das Attribut religionanmeldung als computed value.
	 */
	public religionanmeldung = this.genComputed<string | null>('religionanmeldung', null, null, null);

	/**
	 * Erzeugt das Attribut religionabmeldung als computed value.
	 */
	public religionabmeldung = this.genComputed<string | null>('religionabmeldung', null, null, null);

	/**
	 * Erzeugt das Attribut status als computed value.
	 */
	public status = this.genComputed<SchuelerStatus | null>('status', null,
		(value: number) => SchuelerStatus.data().getWertByKuerzel('' + value),
		(value: SchuelerStatus | null) => value?.daten(this.auswahlManager.getSchuljahr())?.id
	);

	/**
	 * Erzeugt das Attribut istDuplikat als computed value.
	 */
	public istDuplikat = this.genComputed<boolean>('istDuplikat', false, null, null);

	/**
	 * Gibt zurück, ob alle Schüler in der Auswahl den Status "Extern" haben.
	 */
	public alleExtern = computed<boolean>(() => [...this.auswahlManager.liste.auswahl()].every(s => s.status === SchuelerStatus.EXTERN.daten(this.auswahlManager.getSchuljahr())?.id));

	/**
	 * Erzeugt das Attribut stammschuleExtern als computed value.
	 */
	public stammschuleExtern = computed<SchulEintrag | null>({
		get: () => {
			const id = this.pendingValues.externeSchulNr;
			if (id === null || id === undefined) {
				return null;
			}
			return this._mapSchulen.get(id) ?? null;
		},
		set: (value: SchulEintrag | null) => {
			this.setPendingState(
				"externeSchulNr",
				value?.schulnummerStatistik ?? null,
				this.auswahlManager.liste.auswahlKeyList()
			);
		},
	});

	/**
	 * Erzeugt das Attribut fahrschuelerArtID als computed value.
	 */
	public fahrschuelerArtID = this.genComputed<KatalogEintrag | null>('fahrschuelerArtID', null,
		(value: number | null | undefined) => ((value === null) || (value === undefined)) ? null : this._mapFahrschuelerarten.get(value) ?? null,
		(value: KatalogEintrag | null) => value?.id ?? null
	);

	/**
	 * Erzeugt das Attribut haltestelleID als computed value.
	 */
	public haltestelleID = this.genComputed<Haltestelle | null>('haltestelleID', null,
		(value: number | null | undefined) => ((value === null) || (value === undefined)) ? null : this._mapHaltestellen.get(value) ?? null,
		(value: Haltestelle | null) => value?.id ?? null
	);

	/**
	 * Erzeugt das Attribut Aufnahmedatum als computed value.
	 */
	public aufnahmedatum = this.genComputed<string | null>('aufnahmedatum', null, null, null);

	/**
	 * Erzeugt das Attribut istVolljaehrig als computed value.
	 */
	public istVolljaehrig = this.genComputed<boolean>('istVolljaehrig', false, null, null);

	/**
	 * Erzeugt das Attribut keineAuskunftAnDritte als computed value.
	 */
	public keineAuskunftAnDritte = this.genComputed<boolean>('keineAuskunftAnDritte', false, null, null);

	/**
	 * Erzeugt das Attribut istSchulpflichtErfuellt als computed value.
	 */
	public istSchulpflichtErfuellt = this.genComputed<boolean>('istSchulpflichtErfuellt', false, null, null);

	/**
	 * Erzeugt das Attribut istBerufsschulpflichtErfuellt als computed value.
	 */
	public istBerufsschulpflichtErfuellt = this.genComputed<boolean>('istBerufsschulpflichtErfuellt', false, null, null);

	/**
	 * Erzeugt das Attribut hatMasernimpfnachweis als computed value.
	 */
	public hatMasernimpfnachweis = this.genComputed<boolean>('hatMasernimpfnachweis', false, null, null);

	/**
	 * Erzeugt das Attribut erhaeltSchuelerBAFOEG als computed value.
	 */
	public erhaeltSchuelerBAFOEG = this.genComputed<boolean>('erhaeltSchuelerBAFOEG', false, null, null);

	/**
	 * Erzeugt das Attribut hatMigrationshintergrund als computed value.
	 */
	public hatMigrationshintergrund = this.genComputed<boolean>('hatMigrationshintergrund', false, null, null);

	/**
	 * Erzeugt das Attribut zuzugsjahr als computed value.
	 */
	public zuzugsjahr = this.genComputed<number | null>('zuzugsjahr', null, null, null);

	/**
	 * Erzeugt das Attribut geburtsland als computed value.
	 */
	public geburtsland = this.genComputed<Nationalitaeten | null>('geburtsland', null,
		(value: string | null) => Nationalitaeten.getByISO3(value),
		(value: Nationalitaeten | null) => value?.historie().getLast().iso3
	);

	/**
	 * Erzeugt das Attribut verkehrssprache als computed value.
	 */
	public verkehrssprache = this.genComputed<Verkehrssprache | null>('verkehrspracheFamilie', null,
		(value: string | null | undefined) => (value === null || value === undefined) ? null : Verkehrssprache.getByIsoKuerzel(value),
		(value: Verkehrssprache | null) => value?.historie().getLast().kuerzel
	);

	/**
	 * Erzeugt das Attribut geburtslandMutter als computed value.
	 */
	public geburtslandMutter = this.genComputed<Nationalitaeten | null>('geburtslandMutter', null,
		(value: string | null) => Nationalitaeten.getByISO3(value),
		(value: Nationalitaeten | null) => value?.historie().getLast().iso3
	);

	/**
	 * Erzeugt das Attribut geburtslandVater als computed value.
	 */
	public geburtslandVater = this.genComputed<Nationalitaeten | null>('geburtslandVater', null,
		(value: string | null) => Nationalitaeten.getByISO3(value),
		(value: Nationalitaeten | null) => value?.historie().getLast().iso3
	);

}
