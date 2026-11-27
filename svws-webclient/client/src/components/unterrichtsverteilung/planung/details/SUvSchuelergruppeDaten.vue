<template>
	<svws-ui-content-card v-if="gruppe !== null" title="Daten der Schülergruppe">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :key="eingabeVersion"
				placeholder="Bezeichnung der Schülergruppe"
				:model-value="bezeichnungsentwurf ?? gruppe.bezeichnung"
				:readonly="!hatKompetenzAendern"
				:disabled="loading || bearbeitung !== null"
				required
				@change="bezeichnung => bezeichnungSpeichern(bezeichnung?.trim() ?? '')" />
		</svws-ui-input-wrapper>
		<s-uv-jahrgang-zuordnung class="mt-3"
			titel="Erlaubte Jahrgänge"
			:jahrgaenge="selectedJahrgaenge"
			art="gruppe-jahrgang"
			:id-ziel="gruppe.id"
			:bearbeitung
			:readonly="!hatKompetenzAendern"
			:loading
			:bearbeiten="props.bearbeiten"
			:entfernen="removeJahrgang" />
		<p v-if="state.uvManager.schuelergruppeHatSchuelerMitFalschemJahrgang(gruppe)" class="mt-2 text-sm text-ui-caution">Die Gruppe enthält Schüler aus einem anderen Jahrgang.</p>
		<p class="mt-2 text-xs text-ui-secondary">Änderungen an dieser Schülergruppe gelten überall, wo sie verwendet wird.</p>
		<svws-ui-notification v-if="bezeichnungsfehler !== ''" type="error" class="mt-3">{{ bezeichnungsfehler }}</svws-ui-notification>
		<div v-if="bezeichnungsentwurf !== null && bezeichnungsfehler !== ''" class="mt-3 flex gap-2">
			<svws-ui-button type="secondary" :disabled="loading || bearbeitung !== null || !hatKompetenzAendern || bezeichnungsentwurf === ''"
				@click="bezeichnungSpeichern(bezeichnungsentwurf)">
				Erneut speichern
			</svws-ui-button>
			<svws-ui-button type="secondary" :disabled="loading" @click="bezeichnungVerwerfen">Änderung verwerfen</svws-ui-button>
		</div>
		<svws-ui-notification v-if="jahrgangsfehler !== ''" type="error" class="mt-3">{{ jahrgangsfehler }}</svws-ui-notification>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from 'vue';

	import type { UvSchuelergruppe } from '@core/core/data/uv/UvSchuelergruppe';
	import { useUvState } from '@ui/states/UvState';

	import SUvJahrgangZuordnung from '../SUvJahrgangZuordnung.vue';
	import type { UvZuordnungArt, UvZuordnungBearbeitung } from '../UvPlanung';

	const props = defineProps<{
		gruppe: UvSchuelergruppe | null;
		bearbeitung: UvZuordnungBearbeitung | null;
		bearbeiten: (art: UvZuordnungArt, idZiel: number) => void;
	}>();

	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const bezeichnungsentwurf = ref<string | null>(null);
	const bezeichnungsfehler = ref('');
	const jahrgangsfehler = ref('');
	const eingabeVersion = ref(0);
	const kontext = ref(0);

	const selectedJahrgaenge = computed(() => {
		const gruppe = props.gruppe;
		if (gruppe === null) {
			return [];
		}
		const ids = new Set(gruppe.idsJahrgaengeErlaubt);
		return [...state.uvManager.jahrgangsdatenGetMenge()].filter(item => ids.has(item.id));
	});

	watch([() => props.gruppe?.id, () => state.uvManager], () => {
		kontext.value++;
		loading.value = false;
		jahrgangsfehler.value = '';
		bezeichnungVerwerfen();
	}, { flush: 'sync' });

	function bezeichnungVerwerfen(): void {
		bezeichnungsentwurf.value = null;
		bezeichnungsfehler.value = '';
		eingabeVersion.value++;
	}

	/** Speichert die Bezeichnung und übernimmt die Rückmeldung nur für die weiterhin geöffnete Gruppe. */
	async function bezeichnungSpeichern(bezeichnung: string): Promise<void> {
		const gruppe = props.gruppe;
		if (gruppe === null || !hatKompetenzAendern.value || loading.value || props.bearbeitung !== null) {
			return;
		}
		bezeichnungsentwurf.value = bezeichnung;
		if (bezeichnung === '') {
			bezeichnungsfehler.value = 'Bitte eine Bezeichnung eingeben.';
			return;
		}
		const aktuellerKontext = kontext.value;
		loading.value = true;
		bezeichnungsfehler.value = '';
		try {
			await state.planung.patchSchuelergruppe(gruppe.id, { bezeichnung });
			if (kontext.value === aktuellerKontext) {
				bezeichnungsentwurf.value = null;
			}
		} catch {
			if (kontext.value === aktuellerKontext) {
				bezeichnungsfehler.value = 'Die Bezeichnung konnte nicht gespeichert werden.';
			}
		} finally {
			if (kontext.value === aktuellerKontext) {
				loading.value = false;
			}
		}
	}

	async function removeJahrgang(id: number): Promise<void> {
		const gruppe = props.gruppe;
		if (gruppe === null || !hatKompetenzAendern.value || loading.value
			|| props.bearbeitung?.art !== 'gruppe-jahrgang' || props.bearbeitung.idZiel !== gruppe.id) {
			return;
		}
		const aktuellerKontext = kontext.value;
		loading.value = true;
		jahrgangsfehler.value = '';
		try {
			await state.planung.aendereSchuelergruppenJahrgang(gruppe.id, id, false);
		} catch {
			if (kontext.value === aktuellerKontext) {
				jahrgangsfehler.value = 'Der Jahrgang konnte nicht entfernt werden.';
			}
		} finally {
			if (kontext.value === aktuellerKontext) {
				loading.value = false;
			}
		}
	}
</script>
