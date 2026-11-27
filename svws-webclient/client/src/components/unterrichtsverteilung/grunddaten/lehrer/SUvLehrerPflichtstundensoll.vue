<template>
	<svws-ui-content-card title="Pflichtstundensoll" class="col-span-full pt-5">
		<svws-ui-table :items="listPflichtstundensoll" :columns="colsPflichtstundensoll" scroll>
			<template #cell(pflichtstdSoll)="{ rowData }">
				<svws-ui-input-number headless
					:steps="false"
					:decimal-places="2"
					:min="0"
					:model-value="rowData.pflichtstdSoll"
					:disabled="loading"
					:readonly="!hatKompetenzAendern"
					placeholder="Pflichtstunden"
					@change="value => patchPflichtstundensoll(rowData.id, value)" />
			</template>
			<template #cell(loeschen)="{ rowData }">
				<svws-ui-button v-if="hatKompetenzAendern"
					type="trash"
					title="Pflichtstundensoll löschen"
					:disabled="loading"
					@click="deletePflichtstundensoll(rowData)" />
			</template>
			<template #cell(gueltigVon)="{ rowData }">
				<svws-ui-text-input headless
					type="date"
					class="my-0 pr-2 [&.text-input--date>.svws-icon]:hidden [&.text-input--date>input::-webkit-calendar-picker-indicator]:hidden"
					placeholder="Gültig von"
					required
					:model-value="rowData.gueltigVon"
					:disabled="loading"
					:readonly="!hatKompetenzAendern"
					@change="value => patchGueltigkeit(rowData, 'gueltigVon', value)" />
			</template>
			<template #cell(gueltigBis)="{ rowData }">
				<div class="w-full" :class="{ '[&:not(:focus-within)_.text-input--headless]:opacity-0': rowData.gueltigBis === null || rowData.gueltigBis === '' }">
					<svws-ui-text-input headless
						type="date"
						class="my-0 pr-2 [&.text-input--date>.svws-icon]:hidden [&.text-input--date>input::-webkit-calendar-picker-indicator]:hidden"
						placeholder="Gültig bis"
						:model-value="rowData.gueltigBis"
						:disabled="loading"
						:readonly="!hatKompetenzAendern"
						@change="value => patchGueltigkeit(rowData, 'gueltigBis', value)" />
				</div>
			</template>
			<template #actions v-if="hatKompetenzAendern">
				<div class="flex w-full items-center">
					<div class="ml-auto flex items-center gap-1">
						<span v-if="lehrer.idKLehrer !== null" :title="canImportPflichtstundensoll ? 'Pflichtstundensoll aus Personalabschnittsdaten importieren' : 'Import aus Personalabschnittsdaten nur möglich, wenn noch keine Pflichtstunden-Einträge vorhanden sind.'">
							<svws-ui-button type="icon"
								@click="handleImportPflichtstundensollFromPersonalabschnittsdaten"
								:disabled="loading || !canImportPflichtstundensoll"
								:title="canImportPflichtstundensoll ? 'Pflichtstundensoll aus Personalabschnittsdaten importieren' : undefined">
								<span class="icon i-ri-upload-2-line" />
							</svws-ui-button>
						</span>
						<s-uv-grunddaten-lehrer-pflichtstundensoll-neu-modal v-slot="{ openModal }" :lehrer>
							<svws-ui-button @click="openModal" type="icon"><span class="icon i-ri-add-line" /></svws-ui-button>
						</s-uv-grunddaten-lehrer-pflichtstundensoll-neu-modal>
					</div>
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from "vue";

	import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import type { UvLehrerPflichtstundensoll } from "@core/core/data/uv/UvLehrerPflichtstundensoll";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";

	import SUvGrunddatenLehrerPflichtstundensollNeuModal from "./SUvGrunddatenLehrerPflichtstundensollNeuModal.vue";

	const props = defineProps<{
		lehrer: UvLehrer;
		hatKompetenzAendern: boolean;
		patchGueltigkeit: (item: UvLehrerPflichtstundensoll, feld: 'gueltigVon' | 'gueltigBis', value: string | null) => Promise<void>;
	}>();

	const state = useUvState();
	const loading = defineModel<boolean>('loading', { required: true });

	const listPflichtstundensoll = computed<UvLehrerPflichtstundensoll[]>(() => {
		return [...state.uvManager.lehrerPflichtstundensollGetMengeByLehrer(props.lehrer)];
	});

	const canImportPflichtstundensoll = computed(() => (listPflichtstundensoll.value.length === 0));

	const colsPflichtstundensoll = computed<DataTableColumn[]>(() => [
		{ key: "pflichtstdSoll", label: "Stunden", sortable: true, defaultSort: 'asc', fixedWidth: 8, align: 'left' },
		{ key: "gueltigVon", label: "Gültig von", sortable: true, minWidth: 10, align: 'left' },
		{ key: "gueltigBis", label: "Gültig bis", sortable: true, minWidth: 10, align: 'left' },
		{ key: "loeschen", label: "", tooltip: "Löschen", fixedWidth: 3 },
	]);

	async function patchPflichtstundensoll(id: number, pflichtstdSoll: number | null) {
		if (pflichtstdSoll === null || loading.value || !props.hatKompetenzAendern) {
			return;
		}
		loading.value = true;
		try {
			await state.grunddaten.patchPflichtstundensoll(id, { pflichtstdSoll });
		} finally {
			loading.value = false;
		}
	}

	async function deletePflichtstundensoll(item: UvLehrerPflichtstundensoll) {
		if (loading.value || !props.hatKompetenzAendern) {
			return;
		}
		loading.value = true;
		try {
			await state.grunddaten.delPflichtstundensoll([item]);
		} finally {
			loading.value = false;
		}
	}

	async function handleImportPflichtstundensollFromPersonalabschnittsdaten() {
		const lehrer = props.lehrer;
		if (!canImportPflichtstundensoll.value) {
			return;
		}
		await state.grunddaten.importPflichtstundensollFromPersonalabschnittsdaten(lehrer);
	}
</script>
