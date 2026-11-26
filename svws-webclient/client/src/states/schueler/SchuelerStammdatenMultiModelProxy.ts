import { computed } from "vue";

import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { SchuelerStatusKatalogEintrag } from "@core/asd/data/schueler/SchuelerStatusKatalogEintrag";
import type { NationalitaetenKatalogEintrag } from "@core/asd/data/schule/NationalitaetenKatalogEintrag";
import type { VerkehrsspracheKatalogEintrag } from "@core/asd/data/schule/VerkehrsspracheKatalogEintrag";
import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
import { Nationalitaeten } from "@core/asd/types/schule/Nationalitaeten";
import { Verkehrssprache } from "@core/asd/types/schule/Verkehrssprache";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { MultiModelProxyConfiguration } from "@ui/model/MultiModelProxy";
import { MultiModelProxy } from "@ui/model/MultiModelProxy";
import { useFahrschuelerartenState } from "@ui/states/kataloge/FahrschuelerartenState";
import { useHaltestellenState } from "@ui/states/kataloge/HaltestellenState";
import { useHerkunftschulenState } from "@ui/states/kataloge/HerkunftschulenState";
import { useReligionenState } from "@ui/states/kataloge/ReligionenState";

import { schuleStateImpl } from "~/states/SchuleStateImpl";

/**
 * Der {@link MultiModelProxy} für die Stammdaten der Schüler der Mehrfachauswahl. Er bereitet die ausstehenden Änderungen
 * für die Anzeige und die Bindung in der UI auf (Anzeige-Mapper und Bindungen für Katalogwerte).
 */
export class SchuelerStammdatenMultiModelProxy extends MultiModelProxy<SchuelerStammdaten> {

	/** Die Mapper für Attribute, die eine individuelle Aufbereitung in der UI benötigen. Die Werte werden erst beim Aufruf gelesen, daher kann die Map geteilt werden. */
	private static readonly DISPLAY_MAPPERS = new Map<keyof SchuelerStammdaten, (value: any) => any>([
		['status', (value: any) => SchuelerStatus.data().getWertByKuerzel('' + value)?.daten(schuleStateImpl.schuljahr)?.text],
		['fahrschuelerArtID', (value: any) => useFahrschuelerartenState().fahrschuelerarten.byId.get(value)?.bezeichnung],
		['idStaatsangehoerigkeit', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.bezeichnung],
		['idStaatsangehoerigkeit2', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.bezeichnung],
		['religionID', (value: any) => useReligionenState().religionen.byId.get(Number(value))?.bezeichnung],
		['haltestelleID', (value: any) => useHaltestellenState().haltestellen.byId.get(Number(value))?.bezeichnung],
		['idVerkehrspracheFamilie', (value: any) => Verkehrssprache.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.text],
		['idGeburtsland', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.text],
		['idGeburtslandMutter', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.text],
		['idGeburtslandVater', (value: any) => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr)?.text],
		['druckeKonfessionAufZeugnisse', MultiModelProxy.booleanDisplayMapper],
		['istVolljaehrig', MultiModelProxy.booleanDisplayMapper],
		['keineAuskunftAnDritte', MultiModelProxy.booleanDisplayMapper],
		['istSchulpflichtErfuellt', MultiModelProxy.booleanDisplayMapper],
		['istBerufsschulpflichtErfuellt', MultiModelProxy.booleanDisplayMapper],
		['hatMasernimpfnachweis', MultiModelProxy.booleanDisplayMapper],
		['erhaeltSchuelerBAFOEG', MultiModelProxy.booleanDisplayMapper],
		['hatMigrationshintergrund', MultiModelProxy.booleanDisplayMapper],
		['aufnahmedatum', MultiModelProxy.dateDisplayMapper],
		['religionabmeldung', MultiModelProxy.dateDisplayMapper],
		['religionanmeldung', MultiModelProxy.dateDisplayMapper],
	]);

	/**
	 * Erzeugt einen neuen Proxy.
	 *
	 * @param config               die Konfiguration des Proxies
	 * @param auswahlEintraege     liefert die Einträge der Auswahlliste, die in der Mehrfachauswahl gewählt sind
	 */
	public constructor(config: MultiModelProxyConfiguration<SchuelerStammdaten>, private readonly auswahlEintraege: () => Iterable<SchuelerListeEintrag>) {
		super({ ...config, displayMappers: SchuelerStammdatenMultiModelProxy.DISPLAY_MAPPERS });
	}

	/** Die Staatsangehörigkeit als Katalogeintrag */
	public staatsangehoerigkeitID = this.attribute({
		attr: "idStaatsangehoerigkeit", defaultValue: null,
		fromPending: (value: number | null): NationalitaetenKatalogEintrag | null => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		toPending: value => value?.id,
	});

	/** Die zweite Staatsangehörigkeit als Katalogeintrag */
	public staatsangehoerigkeit2ID = this.attribute({
		attr: "idStaatsangehoerigkeit2", defaultValue: null,
		fromPending: (value: number | null): NationalitaetenKatalogEintrag | null => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		toPending: value => value?.id,
	});

	/** Die Konfession als Katalogeintrag */
	public konfession = this.attribute({
		attr: "religionID", defaultValue: null,
		fromPending: (id: number | null): ReligionEintrag | null => (id === null) ? null : useReligionenState().religionen.byId.get(id) ?? null,
		toPending: value => value?.id,
	});

	public druckeKonfessionAufZeugnisse = this.attribute({ attr: "druckeKonfessionAufZeugnisse", defaultValue: false });

	public religionanmeldung = this.attribute({ attr: "religionanmeldung", defaultValue: null });

	public religionabmeldung = this.attribute({ attr: "religionabmeldung", defaultValue: null });

	/** Der Status als Katalogeintrag */
	public status = this.attribute({
		attr: "status", defaultValue: null,
		fromPending: (value: number): SchuelerStatusKatalogEintrag | null => SchuelerStatus.data().getEintragByID(value),
		toPending: value => value?.id,
	});

	public istDuplikat = this.attribute({ attr: "istDuplikat", defaultValue: false });

	/** Gibt zurück, ob alle Schüler in der Auswahl den Status "Extern" haben */
	public alleExtern = computed<boolean>(() => {
		for (const s of this.auswahlEintraege()) {
			if (s.status !== SchuelerStatus.EXTERN.daten(schuleStateImpl.schuljahr)?.id) {
				return false;
			}
		}
		return true;
	});

	/** Die Stammschule bei externen Schülern */
	public stammschuleExtern = computed<SchulEintrag | null>({
		get: () => {
			const id = this.pending.externeSchulNr;
			if (id === null || id === undefined) {
				return null;
			}
			return useHerkunftschulenState().herkunftschulen.bySchulnummerStatistikFilteredByEigeneSchulform.get(id) ?? null;
		},
		set: (value: SchulEintrag | null) => this.setPending("externeSchulNr", value?.schulnummerStatistik ?? null),
	});

	/** Die Fahrschülerart als Katalogeintrag */
	public fahrschuelerArtID = this.attribute({
		attr: "fahrschuelerArtID", defaultValue: null,
		fromPending: (value: number | null | undefined): Fahrschuelerart | null => ((value === null) || (value === undefined)) ? null : useFahrschuelerartenState().fahrschuelerarten.byId.get(value) ?? null,
		toPending: value => value?.id ?? null,
	});

	/** Die Haltestelle als Katalogeintrag */
	public haltestelleID = this.attribute({
		attr: "haltestelleID", defaultValue: null,
		fromPending: (value: number | null | undefined): Haltestelle | null => ((value === null) || (value === undefined)) ? null : useHaltestellenState().haltestellen.byId.get(value) ?? null,
		toPending: value => value?.id ?? null,
	});

	public aufnahmedatum = this.attribute({ attr: "aufnahmedatum", defaultValue: null });

	public istVolljaehrig = this.attribute({ attr: "istVolljaehrig", defaultValue: false });

	public keineAuskunftAnDritte = this.attribute({ attr: "keineAuskunftAnDritte", defaultValue: false });

	public istSchulpflichtErfuellt = this.attribute({ attr: "istSchulpflichtErfuellt", defaultValue: false });

	public istBerufsschulpflichtErfuellt = this.attribute({ attr: "istBerufsschulpflichtErfuellt", defaultValue: false });

	public hatMasernimpfnachweis = this.attribute({ attr: "hatMasernimpfnachweis", defaultValue: false });

	public erhaeltSchuelerBAFOEG = this.attribute({ attr: "erhaeltSchuelerBAFOEG", defaultValue: false });

	public hatMigrationshintergrund = this.attribute({ attr: "hatMigrationshintergrund", defaultValue: false });

	public zuzugsjahr = this.attribute({ attr: "zuzugsjahr", defaultValue: null });

	/** Das Geburtsland als Katalogeintrag */
	public idGeburtsland = this.attribute({
		attr: "idGeburtsland", defaultValue: null,
		fromPending: (value: number | null): NationalitaetenKatalogEintrag | null => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		toPending: value => value?.id,
	});

	/** Die Verkehrssprache in der Familie als Katalogeintrag */
	public verkehrssprache = this.attribute({
		attr: "idVerkehrspracheFamilie", defaultValue: null,
		fromPending: (value: number | null | undefined): VerkehrsspracheKatalogEintrag | null => (value === null || value === undefined) ? null
			: Verkehrssprache.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		toPending: value => value?.id,
	});

	/** Das Geburtsland der Mutter als Katalogeintrag */
	public geburtslandMutter = this.attribute({
		attr: "idGeburtslandMutter", defaultValue: null,
		fromPending: (value: number | null): NationalitaetenKatalogEintrag | null => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		toPending: value => value?.id,
	});

	/** Das Geburtsland des Vaters als Katalogeintrag */
	public geburtslandVater = this.attribute({
		attr: "idGeburtslandVater", defaultValue: null,
		fromPending: (value: number | null): NationalitaetenKatalogEintrag | null => Nationalitaeten.data().getWertByIDOrNull(value)?.daten(schuleStateImpl.schuljahr) ?? null,
		toPending: value => value?.id,
	});

}
