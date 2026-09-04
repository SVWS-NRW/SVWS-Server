<template>
	<svws-ui-content-card class="col-span-full">
		<svws-ui-table class="contentFocusField"
			v-model="selectedBetriebe"
			:clicked="selectedBetrieb" @update:clicked="value => emit('update:selectedBetrieb', value)"
			:items="betriebe"
			:columns
			:disable-footer="!hatKompetenzBearbeiten" clickable :selectable="hatKompetenzBearbeiten"
			focus-first-element>
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
				{{ formatToLocalDate(value, "—") }}
			</template>
			<template #cell(vertragsende)="{ value }">
				{{ formatToLocalDate(value, "—") }}
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

	import type { SchuelerBetrieb } from '@core/asd/data/schueler/SchuelerBetrieb';
	import { Schulform } from '@core/asd/types/schule/Schulform';
	import { BenutzerKompetenz } from '@core/core/types/benutzer/BenutzerKompetenz';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { useBenutzerState } from '@ui/states/BenutzerState';
	import { useSchuleState } from '@ui/states/SchuleState';
	import type { DataTableColumn } from '@ui/types';
	import type { SchuelerBetriebeManager } from '@ui/ui/manager/schueler/SchuelerBetriebeManager';
	import { ref, computed } from 'vue';
	import { formatToLocalDate } from "~/utils/date";

	const props = defineProps<{
		manager: () => SchuelerBetriebeManager,
		selectedBetrieb: SchuelerBetrieb | null,
		deleteBetriebe: (idsSchuelerBetriebe: List<number>) => Promise<boolean>,
	}>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const emit = defineEmits<{
		(e: 'update:selectedBetrieb', value: SchuelerBetrieb | null): void;
		(e: 'create'): void;
	}>();

	const hatKompetenzBearbeiten = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const istBK = computed(() => {
		const erlaubteSchulformen = [Schulform.BK, Schulform.SB, Schulform.WB];
		return erlaubteSchulformen.includes(schuleState.schulform);
	});
	const betriebe = computed(() => props.manager().schuelerBetriebeById.values());
	const selectedBetriebe = ref<SchuelerBetrieb[]>([]);
	const hasSelectedEntries = computed(() => selectedBetriebe.value.length > 0);

	async function deleteSchuelerBetriebe() {
		const idsToDelete = new ArrayList<number>();
		for (const entry of selectedBetriebe.value) {
			idsToDelete.add(entry.id);
		}
		await props.deleteBetriebe(idsToDelete);
		selectedBetriebe.value = [];
		closeModal();
	}

	const columns = computed<DataTableColumn[]>(() => {
		const cols: DataTableColumn[] = [
			{ key: "idBetrieb", label: "Betrieb" },
			{ key: "vertragsbeginn", label: "Vertragsbeginn", span: 0.5, statistic: true },
			{ key: "vertragsende", label: "Vertragsende", span: 0.5, statistic: true },
		];
		if (istBK.value) {
			cols.push({ key: "idBeschaeftigungsart", label: "Beschäftigungsart" });
		}
		cols.push(
			{ key: "idBetreuungslehrer", label: "Betreuende Lehrkraft" },
			{ key: "idAnsprechpartner", label: "Ansprechpartner im Betrieb" },
			{ key: "nameAusbilder", label: "Betreuer/Ausbilder" },
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
		return (lehrer === undefined) ? '' : `${lehrer.nachname}, ${lehrer.vorname}`;
	}

	function ansprechpartnerName(id: number) {
		const ansprechpartner = props.manager().ansprechpartnerById.get(id);
		return (ansprechpartner === undefined) ? '' : `${ansprechpartner.name}, ${ansprechpartner.rufname}`;
	}

</script>
