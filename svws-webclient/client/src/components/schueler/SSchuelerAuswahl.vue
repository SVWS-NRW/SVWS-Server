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
			<svws-ui-table :lock-selectable="pendingStateManagerRegistry().pendingStateExists()" :clickable="!schuelerAuswahlState.liste.auswahlExists()"
				:clicked="clickedEintrag" @update:clicked="schueler => schuelerAuswahlState.navigator.select(schueler.id)"
				:items="schuelerAuswahlState.filter.visible" :model-value="[...schuelerAuswahlState.liste.auswahl()]" @update:model-value="items => setAuswahl(items)"
				:columns="cols" selectable count :filter-open="true" :filtered="schuelerAuswahlState.filter.isActive" :filterReset="schuelerAuswahlState.filter.setDefault" scroll-into-view scroll
				v-model:sort-by-and-order="sortByAndOrder" :sort-by-multi allow-arrow-key-selection :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="schuelerAuswahlState.filter.search" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-multi-select v-if="abschnittState.istSchuljahresabschnittAktuell()" v-model="schuelerAuswahlState.filter.schuelerstatusModel" title="Status"
						:items="schuelerAuswahlState.filter.options.schuelerstatus" :item-text="status => status.daten(abschnittState.auswahl.schuljahr)?.text ?? '—'" class="col-span-full" />
					<div v-else class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" v-model="schuelerAuswahlState.filter.nurMitLernabschnittModel">nur mit Lernabschnitt</svws-ui-checkbox>
					</div>
					<svws-ui-multi-select v-model="schuelerAuswahlState.filter.klassenModel" title="Klasse" :items="schuelerAuswahlState.filter.options.klassen" :item-text="klasse => klasse.kuerzel ?? ''"
						:item-filter="find" />
					<svws-ui-multi-select v-model="schuelerAuswahlState.filter.jahrgaengeModel" title="Jahrgang" :items="schuelerAuswahlState.filter.options.jahrgaenge"
						:item-text="jahrgang => jahrgang.kuerzel ?? ''" :item-filter="find" />
					<svws-ui-multi-select v-model="schuelerAuswahlState.filter.kurseModel" title="Kurs" :items="schuelerAuswahlState.filter.options.kurse" :item-text="textKurs" :item-filter="findKurs" />
					<svws-ui-multi-select v-model="schuelerAuswahlState.filter.schulgliederungenModel" title="Schulgliederung" :items="schuelerAuswahlState.filter.options.schulgliederungen"
						:item-text="textSchulgliederung" />
				</template>
				<template #cell(idKlasse)="{ rowData, value }">
					{{ value === null ? "–" : (schuelerAuswahlState.klasseGetOrNull(value)?.kuerzel) ?? "–" }}
					<svws-ui-tooltip v-if="!schuelerAuswahlState.schuelerIstImSchuljahresabschnitt(rowData.id)" autosize>
						<span v-if="abschnittState.auswahl.id === schuleState.abschnitt.id"
							class="icon icon-ui-danger i-ri-alert-line" />
						<span v-else class="icon icon-ui-brand i-ri-information-line" />
						<template #content>
							Der Schüler befindet sich nicht in dem ausgewählten Schuljahrsabschnitt, sondern in
							{{ schuelerAuswahlState.schuelerSchuljahresabschnittAsString(rowData.id) }}
						</template>
					</svws-ui-tooltip>
				</template>
				<!-- <template v-if="primarstufe" #cell(epJahre)="{ rowData }"> {{ rowData.jahrgang }} </template> -->
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="showSchnelleingabe">
						<svws-ui-button :disabled="((schuelerAuswahlState.navigator.activeViewType === ViewType.NEU) || (schuelerAuswahlState.navigator.activeViewType === ViewType.HINZUFUEGEN))" type="icon" @click="startQuickCreationMode"
							:has-focus="schuelerAuswahlState.filter.visible.isEmpty()">
							<span class="icon i-ri-edit-2-line" />
						</svws-ui-button>
						<template #content>
							Zur Schnelleingabeansicht wechseln
						</template>
					</svws-ui-tooltip>
					<svws-ui-tooltip v-if="serverState.hasDev && hatKompetenzAendern" position="bottom">
						<svws-ui-button :disabled="((schuelerAuswahlState.navigator.activeViewType === ViewType.HINZUFUEGEN) || (schuelerAuswahlState.navigator.activeViewType === ViewType.NEU))" type="icon" @click="startCreationMode"
							:has-focus="schuelerAuswahlState.filter.visible.isEmpty()">
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

	const showSchnelleingabe = computed(() => serverState.hasDev && schuelerAuswahlState.stammdaten.hasDaten
		&& (schuelerAuswahlState.selected.status === SchuelerStatus.NEUAUFNAHME.daten(abschnittState.auswahl.schuljahr)?.id)
		&& hatKompetenzAendern.value);

	async function startCreationMode(): Promise<void> {
		schuelerAuswahlState.filter.setSchuelerstatusSilent([SchuelerStatus.NEUAUFNAHME]);
		await schuelerAuswahlState.setFilter();
		await schuelerAuswahlState.navigator.gotoHinzufuegen(true);
	}

	async function startQuickCreationMode(): Promise<void> {
		await schuelerAuswahlState.navigator.gotoSchnelleingabe(true, schuelerAuswahlState.selected.id);
	}

	const sortByMulti = computed<Map<string, boolean>>(() => {
		const map = new Map<string, boolean>();
		for (const { field, ascending } of schuelerAuswahlState.filter.orderGet()) {
			map.set(field === "klassen" ? "idKlasse" : field, ascending);
		}
		return map;
	});

	const sortByAndOrder = computed<SortByAndOrder | undefined>({
		get: () => {
			const list = schuelerAuswahlState.filter.orderGet();
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
			schuelerAuswahlState.filter.orderUpdate(key, value.order);
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

	function textKurs(kurs: KursDaten): string {
		let jahrgaenge = "";
		let index = 0;
		for (const j of kurs.idJahrgaenge) {
			const jg = schuelerAuswahlState.jahrgaenge.get(j);
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
		await schuelerAuswahlState.setMultiSelection(schuelerEintraege);

		if (schuelerAuswahlState.liste.auswahlExists()) {
			await schuelerAuswahlState.navigator.gotoGruppenprozesse(true);
		} else {
			await schuelerAuswahlState.navigator.select(schuelerAuswahlState.stammdaten.vorherige?.id);
		}
	}

	const clickedEintrag = computed(() => {
		if ((schuelerAuswahlState.navigator.activeViewType === ViewType.GRUPPENPROZESSE) || (schuelerAuswahlState.navigator.activeViewType === ViewType.HINZUFUEGEN)) {
			return null;
		}
		return schuelerAuswahlState.stammdaten.hasDaten ? schuelerAuswahlState.selected : null;
	});

</script>
