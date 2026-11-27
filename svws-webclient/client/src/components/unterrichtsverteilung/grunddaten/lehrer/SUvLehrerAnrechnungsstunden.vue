<template>
	<svws-ui-content-card title="Anrechnungsstunden" class="col-span-full pt-5">
		<svws-ui-table :items="listAnrechnungen" :columns="colsAnrechnungsstunden" count scroll>
			<template #cell(anrechnungsgrundKrz)="{ rowData }">
				<div class="whitespace-normal break-words">
					<div class="text-sm text-ui-secondary">{{ anrechnungsart(rowData.anrechnungsgrundKrz) }}</div>
					<div>{{ presenter.anrechnungsgrundText(abschnittState.auswahl.schuljahr, rowData.anrechnungsgrundKrz) }}</div>
				</div>
			</template>
			<template #cell(anzahlStunden)="{ rowData }">
				<svws-ui-input-number headless
					:steps="false"
					:decimal-places="2"
					:min="0"
					:model-value="rowData.anzahlStunden"
					:disabled="loading"
					:readonly="!hatKompetenzAendern"
					placeholder="Anrechnungsstunden"
					@change="value => patchAnrechnungsstunde(rowData.id, value)" />
			</template>
			<template #cell(loeschen)="{ rowData }">
				<svws-ui-button v-if="hatKompetenzAendern"
					type="trash"
					title="Anrechnungsstunden löschen"
					:disabled="loading"
					@click="deleteAnrechnungsstunden(rowData)" />
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
						<span v-if="lehrer.idKLehrer !== null" :title="canImportAnrechnungsstunden ? 'Anrechnungsstunden aus Personalabschnittsdaten importieren' : 'Import aus Personalabschnittsdaten nur möglich, wenn noch keine Anrechnungsstunden vorhanden sind.'">
							<svws-ui-button type="icon"
								@click="handleImportAnrechnungsstundenFromPersonalabschnittsdaten"
								:disabled="loading || !canImportAnrechnungsstunden"
								:title="canImportAnrechnungsstunden ? 'Anrechnungsstunden aus Personalabschnittsdaten importieren' : undefined">
								<span class="icon i-ri-upload-2-line" />
							</svws-ui-button>
						</span>
						<s-uv-grunddaten-lehrer-anrechnungsstunden-neu-modal v-slot="{ openModal }" :lehrer>
							<svws-ui-button-select type="secondary" :dropdown-actions="[
								{ key: 1, text: 'Anrechnung', action: () => openModal(3) },
								{ key: 2, text: 'Mehrleistung', action: () => openModal(1) },
								{ key: 3, text: 'Minderleistung', action: () => openModal(2) },
							] as Iterable<Item>">
								<template #icon><span class="icon i-ri-add-line" /></template>
							</svws-ui-button-select>
						</s-uv-grunddaten-lehrer-anrechnungsstunden-neu-modal>
					</div>
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from "vue";

	import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import type { UvLehrerAnrechnungsstunden } from "@core/core/data/uv/UvLehrerAnrechnungsstunden";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";
	import type { DataTableColumn } from "@ui/types";

	import { createUvPresenter, uvAnrechnungsart as anrechnungsart } from "../../UvPresenter";

	import SUvGrunddatenLehrerAnrechnungsstundenNeuModal from "./SUvGrunddatenLehrerAnrechnungsstundenNeuModal.vue";

	const props = defineProps<{
		lehrer: UvLehrer;
		hatKompetenzAendern: boolean;
		patchGueltigkeit: (item: UvLehrerAnrechnungsstunden, feld: 'gueltigVon' | 'gueltigBis', value: string | null) => Promise<void>;
	}>();

	const state = useUvState();
	const loading = defineModel<boolean>('loading', { required: true });
	const abschnittState = useAbschnittState();
	const presenter = computed(() => createUvPresenter(state.uvManager));

	type Item = {
		text: string;
		action: () => void | Promise<any>;
		default?: boolean;
		separator?: boolean;
	};

	const listAnrechnungen = computed<UvLehrerAnrechnungsstunden[]>(() => {
		return [...state.uvManager.lehrerAnrechnungsstundenGetMengeByLehrer(props.lehrer)];
	});

	const canImportAnrechnungsstunden = computed(() => (listAnrechnungen.value.length === 0));

	const colsAnrechnungsstunden = computed<DataTableColumn[]>(() => [
		{ key: "anrechnungsgrundKrz", label: "Anrechnung / Grund", sortable: true, defaultSort: 'asc' },
		{ key: "anzahlStunden", label: "Stunden", sortable: false, fixedWidth: 8, align: 'right' },
		{ key: "gueltigVon", label: "Gültig von", sortable: true, fixedWidth: 10, align: 'left' },
		{ key: "gueltigBis", label: "Gültig bis", sortable: true, fixedWidth: 10, align: 'left' },
		{ key: "loeschen", label: "", tooltip: "Löschen", fixedWidth: 3 },
	]);

	async function patchAnrechnungsstunde(id: number, anzahlStunden: number | null) {
		if (anzahlStunden === null || loading.value || !props.hatKompetenzAendern) {
			return;
		}
		loading.value = true;
		try {
			await state.grunddaten.patchAnrechnungsstunde(id, { anzahlStunden });
		} finally {
			loading.value = false;
		}
	}

	async function deleteAnrechnungsstunden(item: UvLehrerAnrechnungsstunden) {
		if (loading.value || !props.hatKompetenzAendern) {
			return;
		}
		loading.value = true;
		try {
			await state.grunddaten.delAnrechnungsstunden([item]);
		} finally {
			loading.value = false;
		}
	}

	async function handleImportAnrechnungsstundenFromPersonalabschnittsdaten() {
		const lehrer = props.lehrer;
		if (!canImportAnrechnungsstunden.value) {
			return;
		}
		await state.grunddaten.importAnrechnungsstundenFromPersonalabschnittsdaten(lehrer);
	}
</script>
