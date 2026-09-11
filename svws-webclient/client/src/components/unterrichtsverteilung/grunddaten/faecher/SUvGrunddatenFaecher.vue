<template>
	<div v-if="fach !== undefined" class="page page-grid-cards">
		<svws-ui-content-card title="Fachdaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Fach" :model-value="state.uvManager.fachdatenGetByFach(fach).bezeichnung" readonly />
				<svws-ui-spacing />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input placeholder="Gültig von" :model-value="form.gueltigVon" @change="onGueltigVonChange" type="date" :readonly="!hatKompetenzAendern" :disabled="loading" :valid="validGueltig" required />
					<svws-ui-text-input placeholder="Gültig bis" :model-value="form.gueltigBis" @change="onGueltigBisChange" type="date" :readonly="!hatKompetenzAendern" :disabled="loading" :valid="validGueltig" />
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card class="mt-5" title="Lehrkräfte, die dieses Fach unterrichten">
			<svws-ui-table :items="lehrerBefaehigt" :columns="columnsLehrer" count>
				<template #filter />
				<template #cell(kuerzel)="{ rowData }">
					{{ state.uvManager.lehrerGetByLehrerUnterrichtsfachOrException(rowData).kuerzel }} <svws-ui-button type="icon" title="UV-Lehrkraft öffnen" @click="gotoLehrer(state.uvManager.lehrerGetByLehrerUnterrichtsfachOrException(rowData).id)"><span class="icon i-ri-link" /></svws-ui-button>
				</template>
				<template #cell(nachname)="{ rowData }">
					{{ state.uvManager.lehrerGetByLehrerUnterrichtsfachOrException(rowData).nachname }}
				</template>
				<template #cell(vorname)="{ rowData }">
					{{ state.uvManager.lehrerGetByLehrerUnterrichtsfachOrException(rowData).vorname }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
	<div v-else class="page text-ui-secondary">Kein Fach ausgewählt</div>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed, ref } from "vue";

	import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
	import { UvFach } from "@core/core/data/uv/UvFach";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";

	const props = defineProps<{
		fach: UvFach | undefined;
		konfliktMitFach: WritableComputedRef<UvFach | null>;
		gotoLehrer: (id: number) => Promise<void>;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const form = computed(() => props.fach ?? new UvFach());
	async function patch(changes: Partial<UvFach>): Promise<void> {
		if ((props.fach === undefined) || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchFach(props.fach.id, changes);
		} finally {
			loading.value = false;
		}
	}

	const lehrerBefaehigt = computed(() => {
		return (props.fach === undefined)
			? new ArrayList<LehrerUnterrichtsfach>()
			: state.uvManager.lehrerUnterrichtsfachGetMengeByFach(props.fach);
	});

	const columnsLehrer = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", span: 0.3 },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
	];

	function validGueltig(gueltigVon: string | null): boolean {
		return props.konfliktMitFach.value === null;
	}

	async function onGueltigVonChange(gueltigVon: string | null) {
		if (!props.fach || gueltigVon === null || !DateUtils.isValidDate(gueltigVon)) {
			return;
		}
		props.konfliktMitFach.value = state.uvManager.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(props.fach, gueltigVon, props.fach.gueltigBis);
		if (props.konfliktMitFach.value !== null) {
			return;
		}
		await patch({ gueltigVon });
	}

	async function onGueltigBisChange(gueltigBis: string | null) {
		if (!props.fach || (gueltigBis !== null && !DateUtils.isValidDate(gueltigBis))) {
			return;
		}
		props.konfliktMitFach.value = state.uvManager.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(props.fach, props.fach.gueltigVon, gueltigBis);
		if (props.konfliktMitFach.value !== null) {
			return;
		}
		await patch({ gueltigBis });
	}


</script>
