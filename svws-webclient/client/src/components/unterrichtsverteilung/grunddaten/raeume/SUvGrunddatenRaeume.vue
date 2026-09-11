<template>
	<div v-if="raum !== undefined" class="page page-grid-cards">
		<svws-ui-content-card title="Raumdaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Kürzel" :model-value="form.kuerzel" @change="kuerzel => patch({ kuerzel: kuerzel ?? undefined })" required />
				<svws-ui-input-number :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Größe" :model-value="form.groesse" @change="groesse => patch({ groesse: groesse ?? undefined })" :min="0" required />
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Beschreibung" :model-value="form.beschreibung" @change="beschreibung => patch({ beschreibung: beschreibung })" span="full" />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Gültig von" :model-value="form.gueltigVon" @change="onGueltigVonChange" type="date" required />
					<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Gültig bis" :model-value="form.gueltigBis" @change="onGueltigBisChange" type="date" />
				</div>
				<div class="col-span-full flex items-center gap-2 min-w-0">
					<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" :key="raum.id" title="Raumgruppe" :empty-text="() => 'Keine Raumgruppe zugeordnet'" :items="raumgruppenMitLeer" :item-text="raumgruppentext"
						:model-value="selectedRaumgruppe" @update:model-value="onRaumgruppeChange" class="flex-1 min-w-0" />
					<svws-ui-button v-if="raum.idRaumgruppe !== null" type="icon" title="Raumgruppe öffnen" @click="gotoRaumgruppe(raum.idRaumgruppe)"><span class="icon i-ri-link" /></svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
	<div v-else class="page text-ui-secondary">Kein Raum ausgewählt</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import { UvRaum } from "@core/core/data/uv/UvRaum";
	import type { UvRaumgruppe } from "@core/core/data/uv/UvRaumgruppe";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { useUvState } from "@ui/states/UvState";

	const props = defineProps<{
		raum: UvRaum | undefined;
		gotoRaumgruppe: (id: number) => Promise<void>;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const form = computed(() => props.raum ?? new UvRaum());
	async function patch(changes: Partial<UvRaum>): Promise<void> {
		if ((props.raum === undefined) || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchRaum(props.raum.id, changes);
		} finally {
			loading.value = false;
		}
	}


	const raumgruppenMitLeer = computed<(UvRaumgruppe | null)[]>(() => {
		return props.raum === undefined ? [] : [null, ...state.uvManager.raumgruppeGetMengeGueltigByRaum(props.raum)];
	});

	const raumgruppentext = (item: UvRaumgruppe | null) => {
		if (item === null) {
			return "— Keine Raumgruppe —";
		}
		return item.bezeichnung;
	};

	const selectedRaumgruppe = computed<UvRaumgruppe | null>(() => {
		const id = form.value.idRaumgruppe;
		return id === null ? null : state.uvManager.raumgruppeGetByIdOrException(id);
	});

	async function onRaumgruppeChange(raumgruppe: UvRaumgruppe | null | undefined) {
		if (!props.raum) {
			return;
		}
		await patch({ idRaumgruppe: raumgruppe?.id ?? null });
	}

	async function onGueltigVonChange(gueltigVon: string | null) {
		if (!props.raum) {
			return;
		}
		if (gueltigVon !== null && DateUtils.isValidDate(gueltigVon)) {
			await patch({ gueltigVon });
		}
	}

	async function onGueltigBisChange(gueltigBis: string | null) {
		if (!props.raum) {
			return;
		}
		if (DateUtils.isValidDate(gueltigBis)) {
			await patch({ gueltigBis });
		} else if (gueltigBis === null || gueltigBis.trim().length === 0) {
			await patch({ gueltigBis: null });
		}
	}

</script>
