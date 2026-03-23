<template>
	<svws-ui-content-card class="col-span-full">
		<svws-ui-table class="contentFocusField"
			v-model="selectedEntries"
			:clicked="clickedEntry"
			@update:clicked="(v) => emit('update:selectedBetrieb', v)"
			:items="entries"
			:columns
			clickable selectable focus-first-element>
			<template #header(erhaeltAnschreiben)>
				<svws-ui-tooltip>
					<span class="icon i-ri-mail-send-line" />
					<template #content>
						Erhält Anschreiben
					</template>
				</svws-ui-tooltip>
			</template>
			<template #cell(idBetrieb)="{ value }">
				{{ manager().betriebeById.get(value)?.name }}
			</template>
			<template #cell(idBeschaeftigungsart)="{ value }" v-if="istBK">
				{{ manager().beschaeftigungsartenById.get(value)?.bezeichnung }}
			</template>
			<template #cell(vertragsbeginn)="{ value }">
				{{ formatDate(value) }}
			</template>
			<template #cell(vertragsende)="{ value }">
				{{ formatDate(value) }}
			</template>
			<template #cell(idBetreuungslehrer)="{ value }">
				{{ lehrerName(value) }}
			</template>
			<template #cell(idAnsprechpartner)="{ value }">
				{{ ansprechpartnerName(value) }}
			</template>
			<template #cell(istPraktikum)="{ value }">
				{{ value ? '&check;' : '&times;' }}
			</template>
			<template #cell(erhaeltAnschreiben)="{ value }">
				{{ value ? '&check;' : '&times;' }}
			</template>
			<template #actions>
				<svws-ui-button type="trash" @click="openModal()" :disabled="!hasSelectedEntries" />
				<svws-ui-button type="icon" @click="$emit('create')">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
			</template>
		</svws-ui-table>

		<svws-ui-modal :show="modalIsOpen" :close-in-title="false">
			<template #modalTitle>
				Betriebe löschen
			</template>
			<template #modalContent>
				<span>Ausgewählte Betriebe löschen?</span><br>
				<span>Diese Aktion kann nicht rückgängig gemacht werden.</span>
			</template>
			<template #modalActions>
				<div class="mt-6 flex gap-4 justify-end">
					<svws-ui-button type="secondary" @click="closeModal">Abbrechen</svws-ui-button>
					<svws-ui-button type="danger" @click="deleteSchuelerBetriebe">Löschen</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { ref, computed } from 'vue';
	import type { DataTableColumn, SchuelerBetriebeManager } from "@ui";
	import type { List, SchuelerBetrieb } from "@core";
	import { Schulform, ArrayList } from "@core";

	const props = defineProps<{
		manager: () => SchuelerBetriebeManager;
		deleteEntries: (idsSchuelerBetriebe: List<number>) => Promise<boolean>;
		schulform: Schulform
	}>();

	const emit = defineEmits<{
		(e: 'create'): void;
		(e: 'update:selectedBetrieb', v: SchuelerBetrieb | null): void;
	}>();

	const istBK = computed(() => {
		const erlaubteSchulformen = [Schulform.BK, Schulform.SB, Schulform.WB];
		return erlaubteSchulformen.includes(props.schulform);
	});
	const entries = computed(() => props.manager().schuelerBetriebeById.values());
	const selectedEntries = ref<SchuelerBetrieb[]>([]);
	const hasSelectedEntries = computed(() => selectedEntries.value.length > 0);
	const clickedEntry = ref<SchuelerBetrieb | null>(null);

	async function deleteSchuelerBetriebe() {
		const idsToDelete = new ArrayList<number>();
		for (const entry of selectedEntries.value) {
			idsToDelete.add(entry.id);
		}
		await props.deleteEntries(idsToDelete);
		selectedEntries.value = [];
		closeModal();
	}

	const columns = computed<DataTableColumn[]>(() => {
		const cols: DataTableColumn[] = [
			{ key: "idBetrieb", label: "Betrieb" },
			{ key: "vertragsbeginn", label: "Beginn", span: 0.3, statistic: true },
			{ key: "vertragsende", label: "Ende", span: 0.3, statistic: true },
		];
		if (istBK.value) {
			cols.push({ key: "idBeschaeftigungsart", label: "Beschäftigungsart" });
		}
		cols.push(
			{ key: "idBetreuungslehrer", label: "Betreuende Lehrkraft" },
			{ key: "idAnsprechpartner", label: "Ansprechpartner" },
			{ key: "nameAusbilder", label: "Sonstiger Betreuer" },
			{ key: "istPraktikum", label: "Praktikum", span: 0.25, tooltip: 'Praktikum', align: 'center' },
			{ key: "erhaeltAnschreiben", label: "Anschreiben", tooltip: 'Betrieb erhält Anschreiben', span: 0.3, align: 'center' }
		);

		return cols;
	});

	// --- modal ---
	const modalIsOpen = ref(false);

	function openModal() {
		modalIsOpen.value = true;
	}

	function closeModal() {
		modalIsOpen.value = false;
	}

	// --- util ---

	function lehrerName(id: number) {
		const lehrer = props.manager().lehrerById.get(id);
		return (lehrer === undefined) ? '' : `${lehrer.vorname} ${lehrer.nachname}`;
	}

	function ansprechpartnerName(id: number) {
		const ansprechpartner = props.manager().ansprechpartnerById.get(id);
		return (ansprechpartner === undefined) ? '' : `${ansprechpartner.rufname} ${ansprechpartner.name}`;
	}

	/** Formatiert ein Datum in das Format 01.01.2020 */
	function formatDate(dateString: string | null): string {
		if (dateString === null) {
			return "—";
		}
		const date = new Date(dateString);
		if (Number.isNaN(date.getTime())) {
			return "—";
		}
		return date.toLocaleDateString("de-DE");
	}

</script>
