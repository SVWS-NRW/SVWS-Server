<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1 class="select-none">Schüler</h1>
			<div><abschnitt-auswahl /></div>
		</div>
		<div class="secondary-menu--header">
			<slot name="header" />
		</div>
		<div class="secondary-menu--content">
			<svws-ui-table :lock-selectable="pendingStateManagerRegistry().pendingStateExists()" :clickable="!manager().liste.auswahlExists()"
				:clicked="clickedEintrag" @update:clicked="schueler => gotoDefaultView(schueler.id)"
				:items="rowsFiltered" :model-value="[...manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns="cols" selectable count :filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-if="abschnittState.istSchuljahresabschnittAktuell()" v-model="filterStatus" title="Status"
						:items="manager().schuelerstatus.list()" :item-text="status => status.daten(abschnittState.auswahl.schuljahr)?.text ?? '—'" class="col-span-full" />
					<div v-else class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="filterNurMitLernabschitt">nur mit Lernabschnitt</svws-ui-checkbox>
					</div>
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="manager().klassen.list()" :item-text="klasse => klasse.kuerzel ?? ''"
						:item-filter="find" />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="manager().jahrgaenge.list()"
						:item-text="jahrgang => jahrgang.kuerzel ?? ''" :item-filter="find" />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="manager().kurse.list()" :item-text="textKurs" :item-filter="findKurs" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="manager().schulgliederungen.list()"
						:item-text="textSchulgliederung" />
				</template>
				<template #cell(idKlasse)="{ rowData, value }">
					{{ value === null ? "–" : (manager().klasseGetOrNull(value)?.kuerzel) ?? "–" }}
					<svws-ui-tooltip v-if="!manager().schuelerIstImSchuljahresabschnitt(rowData.id)" autosize>
						<span v-if="abschnittState.auswahl.id === schuleState.abschnitt.id"
							class="icon icon-ui-danger i-ri-alert-line" />
						<span v-else class="icon icon-ui-brand i-ri-information-line" />
						<template #content>
							Der Schüler befindet sich nicht in dem ausgewählten Schuljahrsabschnitt, sondern in
							{{ manager().schuelerSchuljahresabschnittAsString(rowData.id) }}
						</template>
					</svws-ui-tooltip>
				</template>
				<!-- <template v-if="primarstufe" #cell(epJahre)="{ rowData }"> {{ rowData.jahrgang }} </template> -->
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="showSchnelleingabe">
						<svws-ui-button :disabled="((activeViewType === ViewType.NEU) || (activeViewType === ViewType.HINZUFUEGEN))" type="icon" @click="startQuickCreationMode"
							:has-focus="rowsFiltered.length === 0">
							<span class="icon i-ri-edit-2-line" />
						</svws-ui-button>
						<template #content>
							Zur Schnelleingabeansicht wechseln
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip v-if="serverState.hasDev && hatKompetenzAendern" position="bottom">
						<svws-ui-button :disabled="((activeViewType === ViewType.HINZUFUEGEN) || (activeViewType === ViewType.NEU))" type="icon" @click="startCreationMode"
							:has-focus="rowsFiltered.length === 0">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Schüler anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
	<svws-ui-modal v-model:show="showModalGruppenaktionen" size="medium">
		<template #modalTitle>
			Aktionen für {{ selectedItems.length }} ausgewählte Schüler
		</template>

		<template #modalContent>
			<div class="opacity-50 mb-4">
				{{ [...selectedItems].splice(0, 10).map(schueler => schueler.vorname + ' ' + schueler.nachname).join(', ') }}
				{{ selectedItems.length > 10 ? ' und ' + (selectedItems.length - 10) + ' weitere' : '' }}
			</div>
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-button type="transparent">Button</svws-ui-button>
				<svws-ui-button type="transparent">Button</svws-ui-button>
				<svws-ui-button type="transparent">Button</svws-ui-button>
				<svws-ui-button type="transparent">Button</svws-ui-button>
				<svws-ui-button type="transparent">Button</svws-ui-button>
			</svws-ui-input-wrapper>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef } from "vue";
	import type { SchuelerAuswahlProps } from "./SSchuelerAuswahlProps";
	import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
	import type { KursDaten } from "@core/asd/data/kurse/KursDaten";
	import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
	import type { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { SchuelerListeEintrag } from "@core/core/data/schueler/SchuelerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useServerState } from "@ui/states/ServerState";
	import type { SortByAndOrder } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<SchuelerAuswahlProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	// const primarschulformen = new Set([Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.PS, Schulform.S, Schulform.KS, Schulform.V]);
	// const primarstufe = computed(() => primarschulformen.has(props.schulform));

	// function getEpJahre(ep: number | null) {
	// 	if (!primarstufe.value || (ep === null))
	// 		return null;
	// 	const schuljahr = schuleState.schuljahr;
	// 	return PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertBySchluesselOrException(ep.toString()).daten(schuljahr)?.kuerzel ?? null;
	// }

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const showModalGruppenaktionen = ref<boolean>(false);

	const showSchnelleingabe = computed(() => serverState.hasDev && props.manager().hasDaten()
		&& (props.manager().auswahl().status === SchuelerStatus.NEUAUFNAHME.daten(abschnittState.auswahl.schuljahr)?.id)
		&& hatKompetenzAendern.value);

	const search = ref<string>("");

	async function startCreationMode(): Promise<void> {
		props.manager().schuelerstatus.auswahlClear();
		props.manager().schuelerstatus.auswahlAdd(SchuelerStatus.NEUAUFNAHME);
		await props.setFilter();
		await props.gotoHinzufuegenView(true);
	}

	async function startQuickCreationMode(): Promise<void> {
		await props.gotoSchnelleingabeView(true, props.manager().auswahl().id);
	}

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const { field, ascending } of props.manager().orderGet()) {
			map.set(field === "klassen" ? "idKlasse" : field, ascending);
		}
		return map;
	});

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = props.manager().orderGet();
			if (list.length === 0) {
				return undefined;
			} else {
				const { field: key, ascending: order } = list[0];
				return { key: key === 'klassen' ? 'idKlasse' : key, order };
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null)) {
				return;
			}
			const key = value.key === 'idKlasse' ? 'klassen' : value.key;
			props.manager().orderUpdate(key, value.order);
			void props.setFilter();
		},
	});

	const cols = computed(() => {
		const arr = [{ key: "idKlasse", label: "Klasse", sortable: true, span: 1 },
			{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
			{ key: "vorname", label: "Rufname", sortable: true, span: 2 },
		];
		// if (primarstufe.value)
		// 	arr.push({ key: "epJahre", label: "Jg", sortable: false, span: 1 });
		return arr;
	});

	const rowsFiltered = computed<SchuelerListeEintrag[]>(() => {
		const arr = [];
		const searchValueIsNumber = /^[0-9]+$/.test(search.value.trim());
		const searchValueLowerCase = search.value.toLocaleLowerCase();
		for (const e of props.manager().filtered()) {
			if ((searchValueIsNumber && e.id.toString().includes(search.value))
				|| (e.nachname.toLocaleLowerCase().includes(searchValueLowerCase) || e.vorname.toLocaleLowerCase().includes(searchValueLowerCase))) {
				arr.push(e);
			}
		}
		return arr;
	});

	const filterNurMitLernabschitt = computed<boolean>({
		get: () => props.manager().filterNurMitLernabschitt(),
		set: (value) => {
			props.manager().setFilterNurMitLernabschitt(value);
			void props.setFilter();
		},
	});

	const filterStatus = computed<SchuelerStatus[]>({
		get: () => [...props.manager().schuelerstatus.auswahl()],
		set: (value) => {
			props.manager().schuelerstatus.auswahlClear();
			for (const v of value) {
				props.manager().schuelerstatus.auswahlAdd(v);
			}
			void props.setFilter();
		},
	});

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...props.manager().schulgliederungen.auswahl()],
		set: (value) => {
			props.manager().schulgliederungen.auswahlClear();
			for (const v of value) {
				props.manager().schulgliederungen.auswahlAdd(v);
			}
			void props.setFilter();
		},
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...props.manager().jahrgaenge.auswahl()],
		set: (value) => {
			props.manager().jahrgaenge.auswahlClear();
			for (const v of value) {
				props.manager().jahrgaenge.auswahlAdd(v);
			}
			void props.setFilter();
		},
	});

	const filterKlassen = computed<KlassenDaten[]>({
		get: () => [...props.manager().klassen.auswahl()],
		set: (value) => {
			props.manager().klassen.auswahlClear();
			for (const v of value) {
				props.manager().klassen.auswahlAdd(v);
			}
			void props.setFilter();
		},
	});

	const filterKurse = computed<KursDaten[]>({
		get: () => [...props.manager().kurse.auswahl()],
		set: (value) => {
			props.manager().kurse.auswahlClear();
			for (const v of value) {
				props.manager().kurse.auswahlAdd(v);
			}
			void props.setFilter();
		},
	});

	async function filterReset() {
		props.manager().schulgliederungen.auswahlClear();
		props.manager().schuelerstatus.auswahlClear();
		props.manager().schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		props.manager().schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		props.manager().jahrgaenge.auswahlClear();
		props.manager().klassen.auswahlClear();
		props.manager().kurse.auswahlClear();
		await props.setFilter();
	}

	function filterChanged(): boolean {
		if (props.manager().schulgliederungen.auswahlExists()
			|| props.manager().jahrgaenge.auswahlExists()
			|| props.manager().klassen.auswahlExists()
			|| props.manager().kurse.auswahlExists()) {
			return true;
		}
		return (!(props.manager().schuelerstatus.auswahlSize() === 2
			&& props.manager().schuelerstatus.auswahlHas(SchuelerStatus.AKTIV)
			&& props.manager().schuelerstatus.auswahlHas(SchuelerStatus.EXTERN)));
	}

	function textKurs(kurs: KursDaten): string {
		let jahrgaenge = "";
		let index = 0;
		for (const j of kurs.idJahrgaenge) {
			const jg = props.manager().jahrgaenge.get(j);
			if (jg === null) {
				continue;
			}
			jahrgaenge += jg.kuerzel;
			if (index < kurs.idJahrgaenge.size() - 1) {
				jahrgaenge += ', ';
			}
			index++;
		}
		return `${kurs.kuerzel} (${jahrgaenge})`;
	}

	function find(klassen: Iterable<JahrgangsDaten | KlassenDaten>, search: string) {
		const matchedKlassen = [];
		for (const klasse of klassen) {
			if ((klasse.kuerzel !== null) && klasse.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase())) {
				matchedKlassen.push(klasse);
			}
		}
		return matchedKlassen;
	}

	function findKurs(kurse: Iterable<KursDaten>, search: string) {
		const matchedKurse = [];
		for (const kurs of kurse) {
			if (kurs.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase())) {
				matchedKurse.push(kurs);
			}
		}
		return matchedKurse;
	}

	function textSchulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(abschnittState.auswahl.schuljahr)?.kuerzel ?? '—';
	}

	const selectedItems = shallowRef<SchuelerListeEintrag[]>([]);

	async function setAuswahl(schuelerEintraege: SchuelerListeEintrag[]) {
		props.manager().liste.auswahlClear();
		for (const schueler of schuelerEintraege) {
			if (props.manager().liste.hasValue(schueler)) {
				props.manager().liste.auswahlAdd(schueler);
			}
		}

		if (props.manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
		}
	}

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

</script>
