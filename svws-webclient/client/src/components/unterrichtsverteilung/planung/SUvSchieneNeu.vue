<template>
	<section class="flex min-h-0 flex-1 flex-col bg-ui">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 p-3">
			<h3 class="font-semibold">Schiene erstellen</h3>
			<svws-ui-button type="secondary" :disabled="loading" @click="props.abbrechen()">Abbrechen</svws-ui-button>
		</div>
		<div class="min-h-0 flex-1 overflow-auto p-3">
			<svws-ui-content-card title="Daten der Schiene">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-input-number v-model="item.nummer" :disabled="loading" required placeholder="Nummer" :min="1" />
					<svws-ui-text-input v-model="item.bezeichnung" :disabled="loading" placeholder="Bezeichnung" />
					<svws-ui-multi-select v-model="selectedJahrgaenge"
						:disabled="loading"
						:items="jahrgangItems"
						:item-text="jahrgang => jahrgang.kuerzel ?? ''"
						class="col-span-full"
						title="Jahrgänge" />
				</svws-ui-input-wrapper>
				<p v-if="!nummerFrei" class="mt-2 text-sm text-ui-danger">Die Nummer wird in diesem Planungsabschnitt bereits verwendet.</p>
				<svws-ui-notification v-if="error" type="error" class="mt-3">{{ error }}</svws-ui-notification>
			</svws-ui-content-card>
		</div>
		<div class="flex shrink-0 justify-end border-t border-ui-10 p-3">
			<svws-ui-button type="primary" :disabled="loading || !isValid" :is-loading="loading" @click="create">Schiene erstellen</svws-ui-button>
		</div>
	</section>
</template>

<script setup lang="ts">
	import { computed, ref } from 'vue';

	import type { JahrgangsDaten } from '@core/core/data/jahrgang/JahrgangsDaten';
	import { UvSchiene } from '@core/core/data/uv/UvSchiene';
	import { useUvState } from '@ui/states/UvState';

	const props = defineProps<{
		abbrechen: () => void;
	}>();
	const emit = defineEmits<{
		created: [id: number];
	}>();
	const state = useUvState();
	const item = ref(new UvSchiene());
	item.value.nummer = 1;
	const selectedJahrgaenge = ref<JahrgangsDaten[]>([]);
	const loading = ref(false);
	const error = ref('');
	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);
	const nummerFrei = computed(() => state.planungsabschnitt !== null && state.uvManager.schieneGetByPlanungsabschnittAndNummer(state.planungsabschnitt, item.value.nummer) === null);
	const isValid = computed(() => state.hatKompetenzAendern && state.planungsabschnitt !== null && state.planungsabschnitt.id !== -1
		&& Number.isInteger(item.value.nummer) && item.value.nummer >= 1 && nummerFrei.value);

	async function create(): Promise<void> {
		const plan = state.planungsabschnitt;
		if (!isValid.value || loading.value || plan === null) {
			return;
		}
		loading.value = true;
		error.value = '';
		item.value.idsJahrgaengeErlaubt.clear();
		for (const jahrgang of selectedJahrgaenge.value) {
			item.value.idsJahrgaengeErlaubt.add(jahrgang.id);
		}
		try {
			const neu = await state.planung.createSchiene(item.value);
			emit('created', neu.id);
		} catch (e) {
			error.value = e instanceof Error ? e.message : 'Die Schiene konnte nicht erstellt werden. Bitte die Eingaben prüfen.';
		} finally {
			loading.value = false;
		}
	}
</script>
