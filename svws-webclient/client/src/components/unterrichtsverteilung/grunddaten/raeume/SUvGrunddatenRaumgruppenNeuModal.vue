<template>
	<slot :open-modal />
	<svws-ui-modal :auto-close="!loading" :close-in-title="!loading" v-model:show="show" class="hidden" size="small">
		<template #modalTitle>UV-Raumgruppe neu erstellen</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="bezeichnung" placeholder="Bezeichnung" required />
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="beschreibung" placeholder="Beschreibung" span="2" />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="gueltigVon" placeholder="Gültig von" type="date" required />
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="gueltigBis" placeholder="Gültig bis" type="date" />
				</div>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="create()" :disabled="loading || !hatKompetenzAendern || !isValid"> Erstellen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import { DateUtils } from "@core/core/utils/DateUtils";
	import { useUvState } from "@ui/states/UvState";

	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const actionError = ref('');
	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		actionError.value = '';
	});

	const show = ref<boolean>(false);
	const bezeichnung = ref<string>("");
	const beschreibung = ref<string | null>(null);
	const gueltigVon = ref<string>(new Date().toISOString().slice(0, 10));
	const gueltigBis = ref<string | null>(null);

	const isValid = computed(() => {
		return bezeichnung.value.trim().length > 0 && gueltigVon.value.trim().length > 0;
	});

	const openModal = () => {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		actionError.value = '';
		bezeichnung.value = "";
		beschreibung.value = null;
		gueltigVon.value = new Date().toISOString().slice(0, 10);
		gueltigBis.value = null;
		show.value = true;
	};

	async function create() {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		actionError.value = '';
		try {
			if (!isValid.value) {
				return;
			}
			await state.createRaumgruppe({
				bezeichnung: bezeichnung.value,
				beschreibung: beschreibung.value !== null && beschreibung.value.trim().length > 0 ? beschreibung.value : null,
				gueltigVon: gueltigVon.value,
				gueltigBis: DateUtils.isValidDate(gueltigBis.value) ? gueltigBis.value : null,
			});
			show.value = false;
		} catch {
			actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Die Eingaben bleiben erhalten. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
