<template>
	<div v-if="stundentafel !== undefined" class="page page-grid-cards">
		<svws-ui-content-card title="Allgemeine Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Bezeichnung" :model-value="form.bezeichnung" @change="bezeichnung => patch({ bezeichnung: bezeichnung ?? '' })" span="full" />
				<svws-ui-text-input :disabled="loading" placeholder="Jahrgang" :model-value="state.uvManager.jahrgangsdatenGetByStundentafel(stundentafel).bezeichnung ?? state.uvManager.jahrgangsdatenGetByStundentafel(stundentafel).kuerzel" readonly />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Gültig von" :model-value="form.gueltigVon" @change="gueltigVon => patch({ gueltigVon: gueltigVon ?? '' })" type="date" />
					<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Gültig bis" :model-value="form.gueltigBis" @change="gueltigBis => patch({ gueltigBis: gueltigBis ?? undefined })" type="date" />
				</div>
				<svws-ui-textarea-input :disabled="loading" :readonly="!hatKompetenzAendern" placeholder="Beschreibung" :model-value="form.beschreibung" @change="beschreibung => patch({ beschreibung: beschreibung ?? undefined })" span="full" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="1. Halbjahr" class="col-span-full">
			<svws-ui-input-wrapper>
				<div class="flex flex-col gap-2">
					<svws-ui-table :items="faecherAbschnitt1" :columns="colsFaecher" :selectable="hatKompetenzAendern && !loading" v-model="selectedFaecher1" count>
						<template #cell(fach)="{ rowData }">
							{{ state.uvManager.fachdatenGetByStundentafelFach(rowData).bezeichnung }} <svws-ui-button type="icon" title="UV-Fach öffnen" @click="gotoFach(rowData.idFach)"><span class="icon i-ri-link" /></svws-ui-button>
						</template>
						<template #cell(wochenstunden)="{ rowData }">
							<svws-ui-input-number headless :readonly="!hatKompetenzAendern" :disabled="loading" :steps="false" :decimal-places="2"
								placeholder="Wochenstunden" :model-value="rowData.wochenstunden"
								@change="value => value !== null && patchStundentafelFach(rowData.id, { wochenstunden: value })" />
						</template>
						<template #cell(davonErgaenzungsstunden)="{ rowData }">
							<svws-ui-input-number headless :readonly="!hatKompetenzAendern" :disabled="loading" :steps="false" :decimal-places="2"
								placeholder="Ergänzungsstunden" :max="rowData.wochenstunden" :model-value="rowData.davonErgaenzungsstunden"
								@change="value => value !== null && patchStundentafelFach(rowData.id, { davonErgaenzungsstunden: value })" />
						</template>
						<template #actions v-if="hatKompetenzAendern">
							<svws-ui-button @click="deleteStundentafelFaecher(selectedFaecher1)" type="trash" :disabled="loading || !selectedFaecher1.length" />
							<s-uv-grunddaten-stundentafeln-fach-neu-modal v-slot="{ openModal }" :stundentafel :abschnitt="1">
								<svws-ui-button @click="openModal" type="icon" title="Fach hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
							</s-uv-grunddaten-stundentafeln-fach-neu-modal>
						</template>
					</svws-ui-table>
					<span>Summe: {{ summeWochenstunden1 }} Wochenstunden, davon {{ summeErgaenzung1 }} Ergänzungsstunden</span>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="2. Halbjahr" class="col-span-full">
			<svws-ui-input-wrapper>
				<div class="flex flex-col gap-2">
					<svws-ui-table :items="faecherAbschnitt2" :columns="colsFaecher" :selectable="hatKompetenzAendern && !loading" v-model="selectedFaecher2" count>
						<template #cell(fach)="{ rowData }">
							{{ state.uvManager.fachdatenGetByStundentafelFach(rowData).bezeichnung }} <svws-ui-button type="icon" title="UV-Fach öffnen" @click="gotoFach(rowData.idFach)"><span class="icon i-ri-link" /></svws-ui-button>
						</template>
						<template #cell(wochenstunden)="{ rowData }">
							<svws-ui-input-number headless :readonly="!hatKompetenzAendern" :disabled="loading" :steps="false" :decimal-places="2"
								placeholder="Wochenstunden" :model-value="rowData.wochenstunden"
								@change="value => value !== null && patchStundentafelFach(rowData.id, { wochenstunden: value })" />
						</template>
						<template #cell(davonErgaenzungsstunden)="{ rowData }">
							<svws-ui-input-number headless :readonly="!hatKompetenzAendern" :disabled="loading" :steps="false" :decimal-places="2"
								placeholder="Ergänzungsstunden" :max="rowData.wochenstunden" :model-value="rowData.davonErgaenzungsstunden"
								@change="value => value !== null && patchStundentafelFach(rowData.id, { davonErgaenzungsstunden: value })" />
						</template>
						<template #actions v-if="hatKompetenzAendern">
							<svws-ui-button @click="deleteStundentafelFaecher(selectedFaecher2)" type="trash" :disabled="loading || !selectedFaecher2.length" />
							<s-uv-grunddaten-stundentafeln-fach-neu-modal v-slot="{ openModal }" :stundentafel :abschnitt="2">
								<svws-ui-button @click="openModal" type="icon" title="Fach hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
							</s-uv-grunddaten-stundentafeln-fach-neu-modal>
						</template>
					</svws-ui-table>
					<span>Summe: {{ summeWochenstunden2 }} Wochenstunden, davon {{ summeErgaenzung2 }} Ergänzungsstunden</span>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
	<div v-else class="page text-ui-secondary">Keine Stundentafel ausgewählt</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
	import type { UvStundentafelFach } from "@core/core/data/uv/UvStundentafelFach";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";

	import SUvGrunddatenStundentafelnFachNeuModal from "./SUvGrunddatenStundentafelnFachNeuModal.vue";

	const props = defineProps<{
		stundentafel: UvStundentafel | undefined;
		gotoFach: (id: number) => Promise<void>;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const form = computed(() => props.stundentafel ?? new UvStundentafel());
	async function patch(changes: Partial<UvStundentafel>): Promise<void> {
		if ((props.stundentafel === undefined) || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchStundentafel(props.stundentafel.id, changes);
		} finally {
			loading.value = false;
		}
	}

	async function patchStundentafelFach(id: number, changes: Partial<UvStundentafelFach>): Promise<void> {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchStundentafelFach(id, changes);
		} finally {
			loading.value = false;
		}
	}

	async function deleteStundentafelFaecher(faecher: UvStundentafelFach[]): Promise<void> {
		if (loading.value || !hatKompetenzAendern.value || faecher.length === 0) {
			return;
		}
		loading.value = true;
		try {
			await state.delStundentafelFach(faecher);
		} finally {
			loading.value = false;
		}
	}


	const selectedFaecher1 = ref<UvStundentafelFach[]>([]);
	const selectedFaecher2 = ref<UvStundentafelFach[]>([]);

	watch(() => props.stundentafel, () => {
		selectedFaecher1.value = [];
		selectedFaecher2.value = [];
	});

	const faecherAbschnitt1 = computed<UvStundentafelFach[]>(() => {
		if (!props.stundentafel) {
			return [];
		}
		return [...state.uvManager.stundentafelFachGetMengeByStundentafelAndAbschnitt(props.stundentafel, 1)];
	});

	const faecherAbschnitt2 = computed<UvStundentafelFach[]>(() => {
		if (!props.stundentafel) {
			return [];
		}
		return [...state.uvManager.stundentafelFachGetMengeByStundentafelAndAbschnitt(props.stundentafel, 2)];
	});

	const summeWochenstunden1 = computed(() => faecherAbschnitt1.value.reduce((sum: number, f: UvStundentafelFach) => sum + f.wochenstunden, 0));
	const summeErgaenzung1 = computed(() => faecherAbschnitt1.value.reduce((sum: number, f: UvStundentafelFach) => sum + f.davonErgaenzungsstunden, 0));

	const summeWochenstunden2 = computed(() => faecherAbschnitt2.value.reduce((sum: number, f: UvStundentafelFach) => sum + f.wochenstunden, 0));
	const summeErgaenzung2 = computed(() => faecherAbschnitt2.value.reduce((sum: number, f: UvStundentafelFach) => sum + f.davonErgaenzungsstunden, 0));

	const colsFaecher: DataTableColumn[] = [
		{ key: "fach", label: "Fach", sortable: true },
		{ key: "wochenstunden", label: "Wstd.", tooltip: "Wochenstunden", sortable: true, align: 'right', fixedWidth: 10 },
		{ key: "davonErgaenzungsstunden", label: "Ergänz.", tooltip: "Davon Ergänzungsstunden", sortable: true, align: 'right', fixedWidth: 10 },
	];

</script>
