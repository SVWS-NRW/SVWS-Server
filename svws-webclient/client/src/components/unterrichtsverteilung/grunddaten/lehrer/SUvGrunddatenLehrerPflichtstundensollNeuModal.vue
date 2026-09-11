<template>
	<slot :open-modal />
	<svws-ui-modal :auto-close="!loading" :close-in-title="!loading" v-model:show="show" class="hidden">
		<template #modalTitle>Pflichtstundensoll hinzufügen</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number :disabled="loading || !hatKompetenzAendern" v-model="item.pflichtstdSoll" required placeholder="Stundenanzahl" :decimal-places="2" :steps="0.5" :min="0" />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Gültig ab" v-model="item.gueltigVon" type="date" required />
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Gültig bis" v-model="item.gueltigBis" type="date" />
				</div>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="loading || !hatKompetenzAendern || false"> Pflichtstundensoll hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";

	import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import { UvLehrerPflichtstundensoll } from "@core/core/data/uv/UvLehrerPflichtstundensoll";
	import { useUvState } from "@ui/states/UvState";

	const props = defineProps<{
		lehrer: UvLehrer;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const actionError = ref('');
	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		actionError.value = '';
	});

	const show = ref<boolean>(false);

	const item = ref<UvLehrerPflichtstundensoll>(new UvLehrerPflichtstundensoll());

	const openModal = () => {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		actionError.value = '';
		show.value = true;
	};

	async function importer() {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		actionError.value = '';
		try {
			item.value.idLehrer = props.lehrer.id;
			await state.addPflichtstundensoll(item.value);
			item.value = new UvLehrerPflichtstundensoll();
			show.value = false;
		} catch {
			actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Die Eingaben bleiben erhalten. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
