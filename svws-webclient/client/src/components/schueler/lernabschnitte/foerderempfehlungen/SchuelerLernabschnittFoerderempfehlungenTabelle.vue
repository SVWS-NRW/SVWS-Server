<template>
	<svws-ui-table class="contentFocusField"
		v-model:clicked="selected"
		v-model="selectedFoerderempfehlungen"
		:items="foerderempfehlungen" :columns
		:selectable="true" focus-first-element count clickable :no-data="foerderempfehlungen.isEmpty()">
		<template #cell(datumAngelegt)="{ value } ">
			{{ value ? DateUtils.gibDatumGermanFormat(value) : "-" }}
		</template>
		<template #cell(faecher)="{ value }">
			<div class="break-all line-clamp-3">
				{{ value || '-' }}
			</div>
		</template>
		<template #cell(datumUeberpruefung)="{ value }">
			{{ value ? DateUtils.gibDatumGermanFormat(value) : "-" }}
		</template>
		<template #cell(abgeschlossen)="{ value }">
			{{ value ? '&check;' : '&times;' }}
		</template>
		<template #actions>
			<svws-ui-button @click="deleteAuswahl()" type="trash" :disabled="!hasSelectedFoerderempfehlungen" />
			<svws-ui-button @click="$emit('open-modal')" type="icon">
				<span class="icon i-ri-add-line" />
			</svws-ui-button>
		</template>
	</svws-ui-table>
	<svws-ui-modal :show="zeigeBestaetigung" @update:show="zeigeBestaetigung = $event">
		<template #modalTitle>
			Förderempfehlungen löschen
		</template>
		<template #modalContent>
			<p>Möchten Sie wirklich {{ anzahlZuLoeschendeDatensaetze }} Förderempfehlung{{ anzahlZuLoeschendeDatensaetze !== 1 ? 'en' : '' }} löschen?</p>
			<p class="mt-4 text-error">Diese Aktion kann nicht rückgängig gemacht werden.</p>
			<div class="mt-6 flex gap-4 justify-end">
				<svws-ui-button type="secondary" @click="zeigeBestaetigung = false">Abbrechen</svws-ui-button>
				<svws-ui-button type="danger" @click="confirmedDeleteAuswahl">Löschen</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { List, SchuelerFoerderempfehlung } from '@core';
	import { ArrayList, DateUtils } from '@core';
	import type { DataTableColumn } from '@ui';
	import { computed, ref, watch } from 'vue';

	const selectedFoerderempfehlungen = ref<SchuelerFoerderempfehlung[]>([]);
	const hasSelectedFoerderempfehlungen = computed(() => selectedFoerderempfehlungen.value.length > 0);
	const foerderempfehlungen = computed<List<SchuelerFoerderempfehlung>>(() => props.foerderempfehlungen);

	const zeigeBestaetigung = ref(false);
	const anzahlZuLoeschendeDatensaetze = ref(0);
	const guIDsZuLoeschen = ref<List<string>>(new ArrayList<string>());

	const props = defineProps<{
		foerderempfehlungen: List<SchuelerFoerderempfehlung>;
		selectedFoerderempfehlung: SchuelerFoerderempfehlung | undefined;
		delete: (guiIDs: List<string>) => Promise<void>;
	}>();

	const emit = defineEmits<{
		(e: 'open-modal'): void;
		(e: 'update:selectedFoerderempfehlung', v: SchuelerFoerderempfehlung | undefined): void;
	}>();

	const selected = computed<SchuelerFoerderempfehlung | undefined>({
		get: () => props.selectedFoerderempfehlung,
		set: (v: SchuelerFoerderempfehlung | undefined) => emit('update:selectedFoerderempfehlung', v),
	});

	const columns: DataTableColumn[] = [
		{ key: "datumAngelegt", label: "Angelegt am", fixedWidth: 8, align: "center" },
		{ key: "faecher", label: "Fächer", fixedWidth: 30 },
		{ key: "datumUeberpruefung", label: "Überprüfung bis", fixedWidth: 10, align: "center" },
		{ key: "abgeschlossen", label: "Empfehlung abgeschlossen", fixedWidth: 13, align: "center" },
	];

	async function deleteAuswahl() {
		if (selectedFoerderempfehlungen.value.length === 0) {
			return;
		}
		const guIDs = selectedFoerderempfehlungen.value
			.map(f => f.guid);
		// Bestätigungsdialog anzeigen
		anzahlZuLoeschendeDatensaetze.value = guIDs.length;
		const list = new ArrayList<string>();
		for (const guid of guIDs) {
			list.add(guid);
		}
		guIDsZuLoeschen.value = list;
		zeigeBestaetigung.value = true;
		selected.value = undefined;
	}

	async function confirmedDeleteAuswahl() {
		if (guIDsZuLoeschen.value.isEmpty()) {
			return;
		}
		await props.delete(guIDsZuLoeschen.value);

		clearSelection();
		zeigeBestaetigung.value = false;
	}

	function clearSelection() {
		selectedFoerderempfehlungen.value = [];
	}

	watch(foerderempfehlungen, () => {
		clearSelection();
	});

</script>
