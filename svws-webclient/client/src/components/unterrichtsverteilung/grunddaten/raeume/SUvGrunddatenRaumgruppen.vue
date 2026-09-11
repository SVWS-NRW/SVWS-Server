<template>
	<div v-if="raumgruppe !== undefined" class="page page-grid-cards">
		<svws-ui-content-card title="Raumgruppendaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Bezeichnung" :model-value="form.bezeichnung" @change="bezeichnung => patch({ bezeichnung: bezeichnung ?? undefined })" required />
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Beschreibung" :model-value="form.beschreibung" @change="beschreibung => patch({ beschreibung: beschreibung })" span="full" />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Gültig von" :model-value="form.gueltigVon" @change="onGueltigVonChange" type="date" required />
					<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Gültig bis" :model-value="form.gueltigBis" @change="onGueltigBisChange" type="date" />
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
	<div v-else class="page text-ui-secondary">Keine Raumgruppe ausgewählt</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import { UvRaumgruppe } from "@core/core/data/uv/UvRaumgruppe";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { useUvState } from "@ui/states/UvState";

	const props = defineProps<{
		raumgruppe: UvRaumgruppe | undefined;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const form = computed(() => props.raumgruppe ?? new UvRaumgruppe());
	async function patch(changes: Partial<UvRaumgruppe>): Promise<void> {
		if ((props.raumgruppe === undefined) || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchRaumgruppe(props.raumgruppe.id, changes);
		} finally {
			loading.value = false;
		}
	}


	async function onGueltigVonChange(gueltigVon: string | null) {
		if (gueltigVon !== null && DateUtils.isValidDate(gueltigVon)) {
			await patch({ gueltigVon });
		}
	}

	async function onGueltigBisChange(gueltigBis: string | null) {
		if (DateUtils.isValidDate(gueltigBis)) {
			await patch({ gueltigBis });
		} else if (gueltigBis === null || gueltigBis.trim().length === 0) {
			await patch({ gueltigBis: null });
		}
	}

</script>
