import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { SchuelerStatusKatalogEintrag } from "@core/asd/data/schueler/SchuelerStatusKatalogEintrag";
import type { NationalitaetenKatalogEintrag } from "@core/asd/data/schule/NationalitaetenKatalogEintrag";
import type { VerkehrsspracheKatalogEintrag } from "@core/asd/data/schule/VerkehrsspracheKatalogEintrag";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import { Nationalitaeten } from "@core/asd/types/schule/Nationalitaeten";
import { Verkehrssprache } from "@core/asd/types/schule/Verkehrssprache";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { AuswahlManager } from "@ui/ui/manager/AuswahlManager";
import { PendingStateManager } from "@ui/ui/wrapper/PendingStateManager";
import { computed } from "vue";
import { routeApp } from "~/router/apps/RouteApp";
import { schuleStateImpl } from "~/states/SchuleStateImpl";

/**
 * Die Klasse `PendingStateManagerSchuelerIndividualdaten` erweitert den `PendingStateManager`
 * und dient der Verwaltung von individuell bezogenen Schülerdaten innerhalb eines PendingState.
 *
 */
export class PendingStateManagerSchuelerIndividualdaten extends PendingStateManager<SchuelerStammdaten> {

	/**
	 * Maps, die Schulnummern zu entsprechenden Schuleinträgen zuordnet.
	 */
	private readonly _mapSchulen: Map<string, SchulEintrag>;

	/**
	 * Konstruktor, der einen neuen PendingState für Schülerstammdaten erstellt.
	 * @param idFieldName Der Name des Attributs, welches Änderungen hält.
	 * @param auswahlManager Funktion, die einen AuswahlManager bereitstellt.
	 * @param mapSchulen Map der Schulen.
	 */
	public constructor(idFieldName: any, auswahlManager: () => AuswahlManager<any, any, SchuelerStammdaten>, mapSchulen: Map<string, SchulEintrag>) {
		super(idFieldName, auswahlManager);
		this._mapSchulen = mapSchulen;
		this.initializeAttributeDisplayMappers();
	}



	/**
	 * Initialisiert die Mapper für die Attributanzeige und ordnet
	 * verschiedenen Attributen spezifische Darstellungslogiken zu.
	 */
	private initializeAttributeDisplayMappers() {
		this._attributeDisplayMappers.set('status', (value: any) => SchuelerStatus.data().getWertByKuerzel('' + value)?.daten(schuleStateImpl.schuljahr)?.text);
		this._attributeDisplayMappers.set('idStaatsangehoerigkeit', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.bezeichnung);
		this._attributeDisplayMappers.set('idStaatsangehoerigkeit2', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.bezeichnung);
		this._attributeDisplayMappers.set('religionID', (value: any) => routeApp.cache.kataloge.religionenById.get(Number(value))?.bezeichnung);
		this._attributeDisplayMappers.set('fahrschuelerArtID', (value: any) => routeApp.cache.kataloge.fahrschuelerartenById.get(Number(value))?.bezeichnung);
		this._attributeDisplayMappers.set('haltestelleID', (value: any) => routeApp.cache.kataloge.haltestellenById.get(Number(value))?.bezeichnung);
		this._attributeDisplayMappers.set('idVerkehrspracheFamilie', (value: any) => Verkehrssprache.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.text);
		this._attributeDisplayMappers.set('idGeburtsland', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.text);
		this._attributeDisplayMappers.set('idGeburtslandMutter', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.text);
		this._attributeDisplayMappers.set('idGeburtslandVater', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.text);
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
	public staatsangehoerigkeitID = this.genComputed<NationalitaetenKatalogEintrag | null>('idStaatsangehoerigkeit', null,
		(value: number | null) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		(value: NationalitaetenKatalogEintrag | null) => value?.id
	);

	/**
	 * Erzeugt das Attribut staatsangehoerigkeit2ID als computed value.
	 */
	public staatsangehoerigkeit2ID = this.genComputed<NationalitaetenKatalogEintrag | null>('idStaatsangehoerigkeit2', null,
		(value: number | null) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		(value: NationalitaetenKatalogEintrag | null) => value?.id
	);

	/**
	 * Erzeugt das Attribut konfession als computed value.
	 */
	public konfession = this.genComputed<ReligionEintrag | null>('religionID', null,
		(id: number) => routeApp.cache.kataloge.religionenById.get(id) ?? null,
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
	public status = this.genComputed<SchuelerStatusKatalogEintrag | null>('status', null,
		(value: number) => SchuelerStatus.data().getEintragByID(value),
		(value: SchuelerStatusKatalogEintrag | null) => value?.id
	);

	/**
	 * Erzeugt das Attribut istDuplikat als computed value.
	 */
	public istDuplikat = this.genComputed<boolean>('istDuplikat', false, null, null);

	/**
	 * Gibt zurück, ob alle Schüler in der Auswahl den Status "Extern" haben.
	 */
	public alleExtern = computed<boolean>(() => [...this.auswahlManager.liste.auswahl()].every(s => s.status === SchuelerStatus.EXTERN.daten(schuleStateImpl.schuljahr)?.id));

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
	public fahrschuelerArtID = this.genComputed<Fahrschuelerart | null>('fahrschuelerArtID', null,
		(value: number | null | undefined) => ((value === null) || (value === undefined)) ? null : routeApp.cache.kataloge.fahrschuelerartenById.get(value) ?? null,
		(value: Fahrschuelerart | null) => value?.id ?? null
	);

	/**
	 * Erzeugt das Attribut haltestelleID als computed value.
	 */
	public haltestelleID = this.genComputed<Haltestelle | null>('haltestelleID', null,
		(value: number | null | undefined) => ((value === null) || (value === undefined)) ? null : routeApp.cache.kataloge.haltestellenById.get(value) ?? null,
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
	public idGeburtsland = this.genComputed<NationalitaetenKatalogEintrag | null>('idGeburtsland', null,
		(value: number | null) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		(value) => value?.id
	);

	/**
	 * Erzeugt das Attribut verkehrssprache als computed value.
	 */
	public verkehrssprache = this.genComputed<VerkehrsspracheKatalogEintrag | null>('idVerkehrspracheFamilie', null,
		(value: number | null | undefined) => (value === null || value === undefined) ? null
			: Verkehrssprache.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		(value) => value?.id
	);

	/**
	 * Erzeugt das Attribut geburtslandMutter als computed value.
	 */
	public geburtslandMutter = this.genComputed<NationalitaetenKatalogEintrag | null>('idGeburtslandMutter', null,
		(value: number | null) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		(value) => value?.id
	);

	/**
	 * Erzeugt das Attribut geburtslandVater als computed value.
	 */
	public geburtslandVater = this.genComputed<NationalitaetenKatalogEintrag | null>('idGeburtslandVater', null,
		(value: number | null) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		(value) => value?.id
	);

}
