<template>
	<section class="flex min-h-0 flex-1 flex-col bg-ui">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 p-3">
			<h3 class="font-semibold">Schülergruppe erstellen</h3>
			<svws-ui-button type="secondary" :disabled="loading" @click="props.abbrechen()">Abbrechen</svws-ui-button>
		</div>
		<div class="min-h-0 flex-1 overflow-auto p-3">
			<svws-ui-content-card title="Daten der Schülergruppe">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input v-model="bezeichnung" :disabled="loading" required placeholder="Bezeichnung" class="col-span-full" />
					<svws-ui-multi-select v-model="selectedJahrgaenge"
						:disabled="loading"
						:items="jahrgangItems"
						:item-text="item => item.kuerzel ?? ''"
						class="col-span-full"
						title="Erlaubte Jahrgänge" />
				</svws-ui-input-wrapper>
				<svws-ui-notification v-if="error !== ''" type="error" class="mt-3">{{ error }}</svws-ui-notification>
			</svws-ui-content-card>
		</div>
		<div class="flex shrink-0 justify-end border-t border-ui-10 p-3">
			<svws-ui-button type="primary" :disabled="loading || !isValid" :is-loading="loading" @click="create">Schülergruppe erstellen</svws-ui-button>
		</div>
	</section>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from 'vue';

	import type { JahrgangsDaten } from '@core/core/data/jahrgang/JahrgangsDaten';
	import { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
	import { useUvState } from '@ui/states/UvState';

	const props = defineProps<{
		ziel: { art: 'klassen' | 'kurse'; id: number; bezeichnung: string };
		abbrechen: () => void;
	}>();
	const emit = defineEmits<{
		saved: [];
	}>();
	const state = useUvState();
	const bezeichnung = ref('');
	watch(() => props.ziel, ziel => {
		bezeichnung.value = ziel.bezeichnung;
	}, { immediate: true });
	const selectedJahrgaenge = ref<JahrgangsDaten[]>([]);
	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);
	const loading = ref(false);
	const error = ref('');
	const isValid = computed(() => state.hatKompetenzAendern && state.planungsabschnitt !== null && state.planungsabschnitt.id !== -1 && bezeichnung.value.trim() !== '');

	async function create(): Promise<void> {
		const plan = state.planungsabschnitt;
		if (!isValid.value || loading.value || plan === null) {
			return;
		}
		loading.value = true;
		error.value = '';
		try {
			const gruppe = new UvSchuelergruppe();
			gruppe.bezeichnung = bezeichnung.value.trim();
			for (const jahrgang of selectedJahrgaenge.value) {
				gruppe.idsJahrgaengeErlaubt.add(jahrgang.id);
			}
			await state.planung.createSchuelergruppeFuerZiel(props.ziel.art, props.ziel.id, gruppe);
			emit('saved');
		} catch (e) {
			error.value = e instanceof Error ? e.message : 'Die Schülergruppe konnte nicht erstellt oder zugeordnet werden.';
		} finally {
			loading.value = false;
		}
	}
</script>
