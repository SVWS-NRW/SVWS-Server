<template>
	<slot :open-modal />
	<svws-ui-modal :auto-close="!loading" :close-in-title="!loading" v-model:show="show" class="hidden" size="small">
		<template #modalTitle>UV-Lehrer neu erstellen</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<svws-ui-input-wrapper :grid="1">
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="kuerzel" placeholder="Kürzel" required />
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="nachname" placeholder="Nachname" required />
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" v-model="vorname" placeholder="Vorname" required />
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
	const kuerzel = ref<string>("");
	const nachname = ref<string>("");
	const vorname = ref<string>("");

	const isValid = computed(() => {
		return kuerzel.value.trim().length > 0 && nachname.value.trim().length > 0 && vorname.value.trim().length > 0;
	});

	const openModal = () => {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		actionError.value = '';
		kuerzel.value = "";
		nachname.value = "";
		vorname.value = "";
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
			await state.createLehrer({
				kuerzel: kuerzel.value,
				nachname: nachname.value,
				vorname: vorname.value,
			});
			show.value = false;
		} catch {
			actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Die Eingaben bleiben erhalten. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
