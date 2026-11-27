<template>
	<div v-if="fach !== undefined" class="page page-grid-cards">
		<svws-ui-content-card title="Fachdaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="2">
				<div class="flex items-center gap-2"><s-uv-fach-badge :id-fach="fach.id" /><svws-ui-text-input class="min-w-0 flex-1" placeholder="Fach" :model-value="state.uvManager.fachdatenGetByFach(fach).bezeichnung" readonly /></div>
				<svws-ui-spacing />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input placeholder="Gültig von"
						v-model="gueltigVon"
						@change="speichereGueltigkeit"
						type="date"
						:readonly="!hatKompetenzAendern"
						:disabled="loading"
						:validation="() => gueltigkeitFehler"
						required />
					<svws-ui-text-input placeholder="Gültig bis"
						:model-value="gueltigBis"
						@update:model-value="value => gueltigBis = value === '' ? null : value"
						@change="speichereGueltigkeit"
						type="date"
						:readonly="!hatKompetenzAendern"
						:disabled="loading"
						:validation="() => gueltigkeitFehler" />
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
	import { computed, ref, watch } from "vue";

	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import type { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
	import type { UvFach } from "@core/core/data/uv/UvFach";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";

	import SUvFachBadge from "../../SUvFachBadge.vue";
	import { uvFachGueltigkeitValidator } from "../../UvGueltigkeitValidator";

	const props = defineProps<{
		fach: UvFach | undefined;
		konfliktMitFach: WritableComputedRef<UvFach | null>;
		gotoLehrer: (id: number) => Promise<void>;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const gueltigVon = ref<string | null>(null);
	const gueltigBis = ref<string | null>(null);
	watch(() => props.fach, fach => {
		gueltigVon.value = fach?.gueltigVon ?? null;
		gueltigBis.value = fach?.gueltigBis ?? null;
	}, { immediate: true });

	const gueltigkeitFehler = computed(() => {
		if (props.fach === undefined) {
			return new ArrayList<ValidatorFehler>();
		}
		const validator = uvFachGueltigkeitValidator(state.uvManager, props.fach, () => gueltigVon.value, () => gueltigBis.value);
		validator.run();
		return validator.getFehler();
	});

	watch([gueltigVon, gueltigBis, () => props.fach], ([von, bis, fach]) => {
		props.konfliktMitFach.value = fach === undefined || von === null || !DateUtils.isValidDate(von)
			|| (bis !== null && !DateUtils.isValidDate(bis)) ? null
			: state.uvManager.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(fach, von, bis);
	});

	async function patch(changes: Partial<UvFach>): Promise<void> {
		if ((props.fach === undefined) || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.grunddaten.patchFach(props.fach.id, changes);
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
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: "asc", span: 0.4 },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
	];

	async function speichereGueltigkeit(): Promise<void> {
		if (gueltigVon.value === null || !gueltigkeitFehler.value.isEmpty()) {
			return;
		}
		await patch({ gueltigVon: gueltigVon.value, gueltigBis: gueltigBis.value });
	}

</script>
