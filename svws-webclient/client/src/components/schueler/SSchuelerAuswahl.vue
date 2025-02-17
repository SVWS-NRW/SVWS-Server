<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1 class="select-none">Schüler</h1>
			<div class="input--schule-abschnitte">
				<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
			</div>
		</div>
		<div class="secondary-menu--header">
			<slot name="header" />
		</div>
		<div class="secondary-menu--content">
			<svws-ui-table :clickable="!manager().liste.auswahlExists()" :clicked="clickedEintrag" @update:clicked="schueler => gotoDefaultView(schueler.id)"
				:items="rowsFiltered" :model-value="[...manager().liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns="cols" selectable count :filter-open="true" :filtered="filterChanged()" :filterReset="filterReset" scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi="sortByMulti" allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-if="manager().istSchuljahresabschnittAktuell()" v-model="filterStatus" title="Status"
						:items="manager().schuelerstatus.list()" :item-text="status => status.daten(schuljahr)?.text ?? '—'" class="col-span-full" />
					<div v-else class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="filterNurMitLernabschitt">nur mit Lernabschnitt</svws-ui-checkbox>
					</div>
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="manager().klassen.list()" :item-text="klasse => klasse.kuerzel ?? ''" :item-filter="find" />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="manager().jahrgaenge.list()" :item-text="jahrgang => jahrgang.kuerzel ?? ''" :item-filter="find" />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="manager().kurse.list()" :item-text="textKurs" :item-filter="findKurs" />
					<svws-ui-multi-select v-model="filterSchulgliederung" title="Schulgliederung" :items="manager().schulgliederungen.list()" :item-text="textSchulgliederung" />
					<!--					<svws-ui-button type="transparent" class="justify-center">
						<span class="icon i-ri-filter-line" />
						Erweiterte Filter
					</svws-ui-button>-->
				</template>
				<template #cell(idKlasse)="{ rowData, value }">
					{{ value === null ? "–" : (manager().klasseGetOrNull(value)?.kuerzel) ?? "–" }}
					<svws-ui-tooltip v-if="!manager().schuelerIstImSchuljahresabschnitt(rowData.id)" autosize>
						<span v-if="schuljahresabschnittsauswahl().aktuell === schuljahresabschnittsauswahl().schule" class="icon icon-ui-danger i-ri-alert-line" />
						<span v-else class="icon icon-ui-brand i-ri-information-line" />
						<template #content>
							Der Schüler befindet sich nicht in dem ausgewählten Schuljahrsabschnitt, sondern in {{ manager().schuelerSchuljahresabschnittAsString(rowData.id) }}
						</template>
					</svws-ui-tooltip>
				</template>

				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="ServerMode.DEV.checkServerMode(serverMode) && hatKompetenzAendern">
						<svws-ui-button :disabled="activeViewType === ViewType.HINZUFUEGEN" type="icon" @click="startCreationMode" :has-focus="rowsFiltered.length === 0">
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
				{{ [ ... selectedItems ].splice(0,10).map(schueler => schueler.vorname + ' ' + schueler.nachname).join(', ') }}
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

	import { computed, ref, shallowRef, watch } from "vue";
	import type { SchuelerListeEintrag, JahrgangsDaten, KlassenDaten, Schulgliederung, KursDaten } from "@core";
	import { ServerMode, SchuelerStatus, BenutzerKompetenz } from "@core";
	import type { SortByAndOrder } from "@ui";
	import { ViewType } from "@ui";
	import type { SchuelerAuswahlProps } from "./SSchuelerAuswahlProps";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<SchuelerAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const showModalGruppenaktionen = ref<boolean>(false);

	const schuljahr = computed<number>(() => props.schuljahresabschnittsauswahl().aktuell.schuljahr);

	const search = ref<string>("");

	async function startCreationMode(): Promise<void> {
		await props.gotoHinzufuegenView(true)
	}

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const pair of props.manager().orderGet())
			if (pair.b !== null)
				map.set(pair.a === "klassen" ? "idKlasse" : pair.a, pair.b);
		return map;
	})

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = props.manager().orderGet();
			if (list.isEmpty())
				return undefined;
			else {
				const { a: key, b: order} = list.get(0);
				return {key: key === 'klassen' ? 'idKlasse' : key, order};
			}
		},
		set: (value) => {
			if ((value === undefined) || (value.key === null))
				return;
			const key = value.key === 'idKlasse' ? 'klassen' : value.key;
			props.manager().orderUpdate(key, value.order);
			void props.setFilter();
		},
	})

	const cols = [
		{ key: "idKlasse", label: "Klasse", sortable: true, span: 1 },
		{ key: "nachname", label: "Nachname", sortable: true, span: 2 },
		{ key: "vorname", label: "Vorname", sortable: true, span: 2 },
	]

	watch(() => props.manager().filtered(), async (neu) => {
		if (props.manager().hasDaten() && !neu.contains(props.manager().auswahl()))
			await props.gotoDefaultView(neu.isEmpty() ? null : neu.get(0).id);
	})

	const rowsFiltered = computed<SchuelerListeEintrag[]>(() => {
		const arr = [];
		const searchValueIsNumber = /^[0-9]+$/.test(search.value.trim());
		const searchValueLowerCase = search.value.toLocaleLowerCase();
		for (const e of props.manager().filtered())
			if ((searchValueIsNumber && e.id.toString().includes(search.value))
				|| (e.nachname.toLocaleLowerCase().includes(searchValueLowerCase) || e.vorname.toLocaleLowerCase().includes(searchValueLowerCase)))
				arr.push(e);
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
			for (const v of value)
				props.manager().schuelerstatus.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterSchulgliederung = computed<Schulgliederung[]>({
		get: () => [...props.manager().schulgliederungen.auswahl()],
		set: (value) => {
			props.manager().schulgliederungen.auswahlClear();
			for (const v of value)
				props.manager().schulgliederungen.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => [...props.manager().jahrgaenge.auswahl()],
		set: (value) => {
			props.manager().jahrgaenge.auswahlClear();
			for (const v of value)
				props.manager().jahrgaenge.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterKlassen = computed<KlassenDaten[]>({
		get: () => [...props.manager().klassen.auswahl()],
		set: (value) => {
			props.manager().klassen.auswahlClear();
			for (const v of value)
				props.manager().klassen.auswahlAdd(v);
			void props.setFilter();
		},
	});

	const filterKurse = computed<KursDaten[]>({
		get: () => [...props.manager().kurse.auswahl()],
		set: (value) => {
			props.manager().kurse.auswahlClear();
			for (const v of value)
				props.manager().kurse.auswahlAdd(v);
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
			|| props.manager().kurse.auswahlExists())
			return true;
		return (!(props.manager().schuelerstatus.auswahlSize() === 2
			&& props.manager().schuelerstatus.auswahlHas(SchuelerStatus.AKTIV)
			&& props.manager().schuelerstatus.auswahlHas(SchuelerStatus.EXTERN)))
	}

	function textKurs(kurs: KursDaten): string {
		let jahrgaenge = "";
		let index = 0;
		for (const j of kurs.idJahrgaenge) {
			const jg = props.manager().jahrgaenge.get(j);
			if (jg === null)
				continue;
			jahrgaenge += jg.kuerzel;
			if (index < kurs.idJahrgaenge.size()-1)
				jahrgaenge += ', ';
			index++;
		}
		return `${kurs.kuerzel} (${jahrgaenge})`;
	}

	function find(klassen: Iterable<JahrgangsDaten | KlassenDaten>, search: string) {
		const matchedKlassen = [];
		for (const klasse of klassen)
			if ((klasse.kuerzel !== null) && klasse.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				matchedKlassen.push(klasse);
		return matchedKlassen;
	}

	function findKurs(kurse: Iterable<KursDaten>, search: string) {
		const matchedKurse = [];
		for (const kurs of kurse)
			if (kurs.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				matchedKurse.push(kurs);
		return matchedKurse;
	}

	function textSchulgliederung(schulgliederung: Schulgliederung): string {
		return schulgliederung.daten(schuljahr.value)?.kuerzel ?? '—';
	}

	const selectedItems = shallowRef<SchuelerListeEintrag[]>([]);

	async function setAuswahl(schuelerEintraege : SchuelerListeEintrag[]) {
		props.manager().liste.auswahlClear();
		for (const schueler of schuelerEintraege)
			if (props.manager().liste.hasValue(schueler))
				props.manager().liste.auswahlAdd(schueler);

		if (props.manager().liste.auswahlExists())
			await props.gotoGruppenprozessView(true);
		else
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

	const clickedEintrag = computed(() => {
		if ((props.activeViewType === ViewType.GRUPPENPROZESSE) || (props.activeViewType === ViewType.HINZUFUEGEN))
			return null;
		return props.manager().hasDaten() ? props.manager().auswahl() : null;
	});

</script>
