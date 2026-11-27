<template>
	<div class="flex min-h-0 flex-col gap-2">
		<svws-ui-text-input v-if="filterbar" v-model="suche" type="search" placeholder="Schüler suchen" removable />
		<div v-if="filterbar" class="grid grid-cols-2 gap-2">
			<svws-ui-select v-model="jahrgang" :items="jahrgaenge" :item-text="item => item" title="Jahrgang" removable />
			<svws-ui-select v-model="klasse" :items="klassen" :item-text="item => item" title="Klasse" removable />
		</div>
		<svws-ui-table v-model="ausgewaehlteZeilen" v-model:sort-by-and-order="sortierung" class="svws-no-mx
			[&_.svws-ui-tbody>.svws-ui-tr]:mb-1
			[&_.svws-ui-tbody>.svws-ui-tr]:rounded
			[&_.svws-ui-tbody>.svws-ui-tr]:border
			[&_.svws-ui-tbody>.svws-ui-tr]:border-ui-25
			[&_.svws-ui-tr:has([data-uv-status=direkt])]:bg-ui-selected
			[&_.svws-ui-tr:has([data-uv-status=direkt])]:ring-2
			[&_.svws-ui-tr:has([data-uv-status=direkt])]:ring-ui-brand
			[&_.svws-ui-tr:has([data-uv-status=bezogen])]:bg-ui-selected
			[&_.svws-ui-tr:has([data-uv-status=bezogen])]:border-ui-brand
			[&_.svws-ui-tr:has([data-uv-status=unbeteiligt])]:opacity-70
			[&_.svws-ui-tr:has([data-uv-status=unbeteiligt]):hover]:opacity-100
			[&_.svws-ui-tr:has([data-uv-status=unbeteiligt]):focus-within]:opacity-100"
			:items="zeilen" :columns="spalten" :selectable :lock-selectable="disabled" :row-draggable="zeileZiehbar" :row-dragstart="zeileZiehenStarten"
			:sort-by-multi="externeSortierung" disable-footer>
			<template #cell(nachname)="{ rowData }">
				<span v-if="zeileZiehbar(rowData)" class="icon i-ri-draggable shrink-0 text-ui-secondary" aria-hidden="true" />
				<button v-if="navigationMoeglich && schuelerOeffnen !== undefined"
					type="button"
					class="truncate rounded px-1 text-left font-semibold text-ui-brand hover:underline"
					:data-uv-status="auswahlStatus?.(rowData.idSchueler) ?? 'neutral'"
					@click="props.schuelerOeffnen?.(rowData.idSchueler)">
					{{ rowData.nachname }}
				</button>
				<span v-else class="truncate font-semibold" :data-uv-status="auswahlStatus?.(rowData.idSchueler) ?? 'neutral'">{{ rowData.nachname }}</span>
			</template>
			<template #cell(vorname)="{ rowData }">{{ rowData.vorname }}</template>
			<template #cell(klasse)="{ rowData }">
				<button v-if="rowData.schueler.idKlasse !== null && navigationMoeglich && klasseOeffnen !== undefined"
					type="button"
					class="truncate text-left text-ui-brand hover:underline"
					@click="props.klasseOeffnen?.(rowData.schueler.idKlasse)">
					{{ rowData.klasse }}
				</button>
				<span v-else class="truncate">{{ rowData.klasse }}</span>
			</template>
			<template #cell(jahrgang)="{ rowData }">
				{{ rowData.jahrgang }}
				<span v-if="gruppe !== undefined && !state.uvManager.planungsabschnittSchuelerHatZurGruppePassendenJahrgang(rowData.schueler, gruppe)"
					class="icon icon-ui-warning i-ri-alert-line"
					title="Der Jahrgang ist für diese Gruppe nicht freigegeben." />
			</template>
			<template #cell(aktion)="{ rowData }">
				<s-uv-details-oeffnen v-if="detailsAnzeigen !== undefined && auswahlStatus?.(rowData.idSchueler) === 'direkt'"
					variante="tabelle"
					:oeffnen="() => props.detailsAnzeigen?.(rowData.idSchueler)" />
				<svws-ui-button v-if="hinzufuegen !== undefined"
					type="icon"
					:disabled
					:title="`${rowData.nachname}, ${rowData.vorname} hinzufügen`"
					@click="props.hinzufuegen?.(rowData.idSchueler)">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
				<svws-ui-button v-if="entfernenMoeglich && zeileEntfernbar(rowData)"
					type="icon"
					:disabled
					:title="`${rowData.nachname}, ${rowData.vorname} entfernen`"
					@click="props.entfernen?.(rowData.idSchueler)">
					<span class="icon i-ri-subtract-line" />
				</svws-ui-button>
			</template>
			<template #noData><slot name="no-data">Keine Schüler vorhanden.</slot></template>
		</svws-ui-table>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from 'vue';

	import type { UvPlanungsabschnittSchueler } from '@core/core/data/uv/UvPlanungsabschnittSchueler';
	import type { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
	import { useUvState } from '@ui/states/UvState';
	import type { SortByAndOrder } from '@ui/types';

	import { createUvPresenter } from '../UvPresenter';

	import SUvDetailsOeffnen from './SUvDetailsOeffnen.vue';
	import type { UvAuswahlStatus } from './UvPlanung';
	import type { UvSchuelerTabellenZeile } from './UvSchuelerTabellenDaten';
	import { uvSchuelerAuswahlItems, uvSchuelerAuswahlZeilen, uvSchuelerSortierteZeilen, uvSchuelerTabellenDaten } from './UvSchuelerTabellenDaten';

	const props = defineProps<{
		items: UvPlanungsabschnittSchueler[];
		selected: UvPlanungsabschnittSchueler[];
		selectable: boolean;
		navigationDisabled?: boolean;
		gruppe?: UvSchuelergruppe;
		draggable?: boolean;
		removable?: boolean;
		removableIds?: Set<number>;
		disabled?: boolean;
		filterbar?: boolean;
		auswahlStatus?: (id: number) => UvAuswahlStatus;
		relevantIds?: Set<number>;
		relevanzSortieren?: boolean;
		schuelerOeffnen?: (id: number) => void;
		klasseOeffnen?: (id: number) => void;
		detailsAnzeigen?: (id: number) => void;
		hinzufuegen?: (id: number) => void;
		entfernen?: (id: number) => void;
	}>();

	const emit = defineEmits<{
		'update:selected': [items: UvPlanungsabschnittSchueler[]];
		'drag-start': [event: DragEvent, id: number];
		'visible-count': [anzahl: number];
	}>();

	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));

	const suche = ref('');
	const jahrgang = ref<string | null>(null);
	const klasse = ref<string | null>(null);
	const sortierung = ref<SortByAndOrder>({ key: 'nachname', order: true });

	// Eine übergebene Map unterbindet die interne Sortierung der Standardtabelle.
	// So bleibt die Relevanz vor der vom Benutzer gewählten Spaltensortierung erhalten.
	const externeSortierung = new Map<string, boolean | null>();

	const navigationMoeglich = computed(() => !props.navigationDisabled && !props.draggable);
	const entfernenMoeglich = computed(() => props.removable === true && props.entfernen !== undefined);
	// Tabellenspalten und aufbereitete Schülerdaten

	const spalten = computed(() => [
		{ key: 'nachname', label: 'Nachname', sortable: true },
		{ key: 'vorname', label: 'Vorname', sortable: true },
		{ key: 'klasse', label: 'Klasse', sortable: true },
		{ key: 'jahrgang', label: 'Jahrgang', sortable: true },
		...((props.hinzufuegen !== undefined || entfernenMoeglich.value || props.detailsAnzeigen !== undefined) ? [{ key: 'aktion', label: '', fixedWidth: 2 }] : []),
	]);
	const daten = computed(() => uvSchuelerTabellenDaten(props.items, presenter.value, {
		jahrgang: props.filterbar ? jahrgang.value : null,
		klasse: props.filterbar ? klasse.value : null,
		suche: props.filterbar ? suche.value : '',
	}));
	const jahrgaenge = computed(() => daten.value.jahrgaenge);
	const klassen = computed(() => daten.value.klassen);
	const zeilen = computed(() => uvSchuelerSortierteZeilen(daten.value.zeilen, props.relevanzSortieren === false ? undefined : props.relevantIds, sortierung.value));
	const ausgewaehlteZeilen = computed({
		get: () => uvSchuelerAuswahlZeilen(zeilen.value, props.selected, presenter.value),
		set: (zeilen: UvSchuelerTabellenZeile[]) => {
			emit('update:selected', uvSchuelerAuswahlItems(props.items, zeilen));
		},
	});

	watch(() => zeilen.value.length, anzahl => emit('visible-count', anzahl), { immediate: true });

	watch([jahrgaenge, klassen], ([jahrgangOptionen, klassenOptionen]) => {
		if (jahrgang.value !== null && !jahrgangOptionen.includes(jahrgang.value)) {
			jahrgang.value = null;
		}
		if (klasse.value !== null && !klassenOptionen.includes(klasse.value)) {
			klasse.value = null;
		}
	});

	watch([() => props.gruppe?.id, () => props.filterbar], () => {
		suche.value = '';
		jahrgang.value = null;
		klasse.value = null;
	});

	function zeileEntfernbar(zeile: UvSchuelerTabellenZeile): boolean {
		return props.removableIds === undefined || props.removableIds.has(zeile.idSchueler);
	}

	function zeileZiehbar(zeile: UvSchuelerTabellenZeile): boolean {
		return props.draggable === true && props.disabled !== true && zeileEntfernbar(zeile);
	}

	function zeileZiehenStarten(event: DragEvent, zeile: UvSchuelerTabellenZeile): void {
		emit('drag-start', event, zeile.idSchueler);
	}
</script>
