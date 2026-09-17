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
			<svws-ui-table :lock-selectable="pendingStateManagerRegistry().pendingStateExists()" :clickable="!schuelerAuswahlState.manager.liste.auswahlExists()"
				:clicked="clickedEintrag" @update:clicked="schueler => schuelerAuswahlState.gotoDefaultView(schueler.id)"
				:items="rowsFiltered" :model-value="[...schuelerAuswahlState.manager.liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns="cols" selectable count :filter-open="true" :filtered="filterChanged()" :filterReset scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-if="abschnittState.istSchuljahresabschnittAktuell()" v-model="filterStatus" title="Status"
						:items="schuelerAuswahlState.manager.schuelerstatus.list()" :item-text="status => status.daten(abschnittState.auswahl.schuljahr)?.text ?? '—'" class="col-span-full" />
					<div v-else class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="filterNurMitLernabschitt">nur mit Lernabschnitt</svws-ui-checkbox>
					</div>
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="schuelerAuswahlState.manager.klassen.list()" :item-text="klasse => klasse.kuerzel ?? ''"
						:item-filter="find" />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="schuelerAuswahlState.manager.jahrgaenge.list()"
						:item-text="jahrgang => jahrgang.kuerzel ?? ''" :item-filter="find" />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="schuelerAuswahlState.manager.kurse.list()" :item-text="textKurs" :item-filter="findKurs" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="schuelerAuswahlState.manager.schulgliederungen.list()"
						:item-text="textSchulgliederung" />
				</template>
				<template #cell(idKlasse)="{ rowData, value }">
					{{ value === null ? "–" : (schuelerAuswahlState.manager.klasseGetOrNull(value)?.kuerzel) ?? "–" }}
					<svws-ui-tooltip v-if="!schuelerAuswahlState.manager.schuelerIstImSchuljahresabschnitt(rowData.id)" autosize>
						<span v-if="abschnittState.auswahl.id === schuleState.abschnitt.id"
							class="icon icon-ui-danger i-ri-alert-line" />
						<span v-else class="icon icon-ui-brand i-ri-information-line" />
						<template #content>
							Der Schüler befindet sich nicht in dem ausgewählten Schuljahrsabschnitt, sondern in
							{{ schuelerAuswahlState.manager.schuelerSchuljahresabschnittAsString(rowData.id) }}
						</template>
					</svws-ui-tooltip>
				</template>
				<!-- <template v-if="primarstufe" #cell(epJahre)="{ rowData }"> {{ rowData.jahrgang }} </template> -->
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="showSchnelleingabe">
						<svws-ui-button :disabled="((schuelerAuswahlState.activeViewType === ViewType.NEU) || (schuelerAuswahlState.activeViewType === ViewType.HINZUFUEGEN))" type="icon" @click="startQuickCreationMode"
							:has-focus="rowsFiltered.length === 0">
							<span class="icon i-ri-edit-2-line" />
						</svws-ui-button>
						<template #content>
							Zur Schnelleingabeansicht wechseln
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip v-if="serverState.hasDev && hatKompetenzAendern" position="bottom">
						<svws-ui-button :disabled="((schuelerAuswahlState.activeViewType === ViewType.HINZUFUEGEN) || (schuelerAuswahlState.activeViewType === ViewType.NEU))" type="icon" @click="startCreationMode"
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

	import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";

	import type { SchuelerAuswahlProps } from "./SSchuelerAuswahlProps";

	const props = defineProps<SchuelerAuswahlProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();
	const schuelerAuswahlState = useSchuelerAuswahlState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const showModalGruppenaktionen = ref<boolean>(false);

	const showSchnelleingabe = computed(() => serverState.hasDev && schuelerAuswahlState.manager.hasDaten()
		&& (schuelerAuswahlState.manager.auswahl().status === SchuelerStatus.NEUAUFNAHME.daten(abschnittState.auswahl.schuljahr)?.id)
		&& hatKompetenzAendern.value);

	const search = ref<string>("");

	async function startCreationMode(): Promise<void> {
		schuelerAuswahlState.manager.schuelerstatus.auswahlClear();
		schuelerAuswahlState.manager.schuelerstatus.auswahlAdd(SchuelerStatus.NEUAUFNAHME);
		await schuelerAuswahlState.setFilter();
		await schuelerAuswahlState.gotoHinzufuegenView(true);
	}

	async function startQuickCreationMode(): Promise<void> {
		await schuelerAuswahlState.gotoSchnelleingabeView(true, schuelerAuswahlState.manager.auswahl().id);
	}

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const { field, ascending } of schuelerAuswahlState.manager.orderGet()) {
			map.set(field === "klassen" ? "idKlasse" : field, ascending);
		}
		return map;
	});

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = schuelerAuswahlState.manager.orderGet();
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
			schuelerAuswahlState.manager.orderUpdate(key, value.order);
			void schuelerAuswahlState.setFilter();
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
		const searchValueIsNumber = /^\d+$/.test(search.value.trim());
		const searchValueLowerCase = search.value.toLocaleLowerCase();
		for (const e of schuelerAuswahlState.manager.filtered()) {
			if ((searchValueIsNumber && e.id.toString().includes(search.value))
				|| (e.nachname.toLocaleLowerCase().includes(searchValueLowerCase) || e.vorname.toLocaleLowerCase().includes(searchValueLowerCase))) {
				arr.push(e);
			}
		}
		return arr;
	});

	const filterNurMitLernabschitt = computed<boolean>({
		get: () => schuelerAuswahlState.manager.filterNurMitLernabschitt(),
		set: (value) => {
			schuelerAuswahlState.manager.setFilterNurMitLernabschitt(value);
			void schuelerAuswahlState.setFilter();
		},
	});

	const filterStatus = computed<SchuelerStatus[]>({
		get: () => [...schuelerAuswahlState.manager.schuelerstatus.auswahl()],
		set: (value) => {
			schuelerAuswahlState.manager.schuelerstatus.auswahlClear();
			for (const v of value) {
				schuelerAuswahlState.manager.schuelerstatus.auswahlAdd(v);
			}
			void schuelerAuswahlState.setFilter();
		},
	});

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...schuelerAuswahlState.manager.schulgliederungen.auswahl()],
		set: (value) => {
			schuelerAuswahlState.manager.schulgliederungen.auswahlClear();
			for (const v of value) {
				schuelerAuswahlState.manager.schulgliederungen.auswahlAdd(v);
			}
			void schuelerAuswahlState.setFilter();
		},
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...schuelerAuswahlState.manager.jahrgaenge.auswahl()],
		set: (value) => {
			schuelerAuswahlState.manager.jahrgaenge.auswahlClear();
			for (const v of value) {
				schuelerAuswahlState.manager.jahrgaenge.auswahlAdd(v);
			}
			void schuelerAuswahlState.setFilter();
		},
	});

	const filterKlassen = computed<KlassenDaten[]>({
		get: () => [...schuelerAuswahlState.manager.klassen.auswahl()],
		set: (value) => {
			schuelerAuswahlState.manager.klassen.auswahlClear();
			for (const v of value) {
				schuelerAuswahlState.manager.klassen.auswahlAdd(v);
			}
			void schuelerAuswahlState.setFilter();
		},
	});

	const filterKurse = computed<KursDaten[]>({
		get: () => [...schuelerAuswahlState.manager.kurse.auswahl()],
		set: (value) => {
			schuelerAuswahlState.manager.kurse.auswahlClear();
			for (const v of value) {
				schuelerAuswahlState.manager.kurse.auswahlAdd(v);
			}
			void schuelerAuswahlState.setFilter();
		},
	});

	async function filterReset() {
		schuelerAuswahlState.manager.schulgliederungen.auswahlClear();
		schuelerAuswahlState.manager.schuelerstatus.auswahlClear();
		schuelerAuswahlState.manager.schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		schuelerAuswahlState.manager.schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		schuelerAuswahlState.manager.jahrgaenge.auswahlClear();
		schuelerAuswahlState.manager.klassen.auswahlClear();
		schuelerAuswahlState.manager.kurse.auswahlClear();
		await schuelerAuswahlState.setFilter();
	}

	function filterChanged(): boolean {
		if (schuelerAuswahlState.manager.schulgliederungen.auswahlExists()
			|| schuelerAuswahlState.manager.jahrgaenge.auswahlExists()
			|| schuelerAuswahlState.manager.klassen.auswahlExists()
			|| schuelerAuswahlState.manager.kurse.auswahlExists()) {
			return true;
		}
		return (!(schuelerAuswahlState.manager.schuelerstatus.auswahlSize() === 2
			&& schuelerAuswahlState.manager.schuelerstatus.auswahlHas(SchuelerStatus.AKTIV)
			&& schuelerAuswahlState.manager.schuelerstatus.auswahlHas(SchuelerStatus.EXTERN)));
	}

	function textKurs(kurs: KursDaten): string {
		let jahrgaenge = "";
		let index = 0;
		for (const j of kurs.idJahrgaenge) {
			const jg = schuelerAuswahlState.manager.jahrgaenge.get(j);
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
		schuelerAuswahlState.manager.liste.auswahlClear();
		for (const schueler of schuelerEintraege) {
			if (schuelerAuswahlState.manager.liste.hasValue(schueler)) {
				schuelerAuswahlState.manager.liste.auswahlAdd(schueler);
			}
		}

		if (schuelerAuswahlState.manager.liste.auswahlExists()) {
			await schuelerAuswahlState.gotoGruppenprozessView(true);
		} else {
			await schuelerAuswahlState.gotoDefaultView(schuelerAuswahlState.manager.getVorherigeAuswahl()?.id);
		}
	}

	const clickedEintrag = computed(() => {
		if ((schuelerAuswahlState.activeViewType === ViewType.GRUPPENPROZESSE) || (schuelerAuswahlState.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return schuelerAuswahlState.manager.hasDaten() ? schuelerAuswahlState.manager.auswahl() : null;
	});

</script>
