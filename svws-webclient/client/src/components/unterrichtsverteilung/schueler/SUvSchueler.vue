<template>
	<div v-if="schueler !== undefined" class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<h3 class="content-card--headline">{{ schueler.daten.nachname }}, {{ schueler.daten.vorname }}</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="big"> ID: {{ schueler.idSchueler }} </svws-ui-badge>
				<svws-ui-button type="icon" @click="gotoSchueler(schueler.idSchueler)" title="Schild-Schüler öffnen"> <span class="icon i-ri-link" /> </svws-ui-button>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Jahrgang" :items="state.uvManager.jahrgangsdatenGetMenge()" :item-text="i => i.kuerzel ?? '—'"
					:model-value="jahrgang" @update:model-value="value => patch({ idJahrgang: value?.id })"
					autocomplete statistics required />
				<div class="flex items-center gap-2 min-w-0">
					<svws-ui-select class="flex-1 min-w-0" :readonly="!hatKompetenzAendern" :disabled="loading" title="Klasse" :items="klassen" :item-text="i => i.kuerzel + i.parallelitaet"
						:model-value="klasse" @update:model-value="value => patch({ idKlasse: value?.id ?? null })"
						autocomplete statistics required />
					<svws-ui-button v-if="schueler.idKlasse !== null" type="icon" title="UV-Klasse öffnen" @click="gotoKlasse(schueler.idKlasse)"><span class="icon i-ri-link" /></svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
	<div v-else class="page page-flex-row max-w-480">
		<span class="text-ui-disabled">Kein Schüler ausgewählt</span>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from "vue";

	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { UvKlasse } from "@core/core/data/uv/UvKlasse";
	import type { UvPlanungsabschnittSchueler } from "@core/core/data/uv/UvPlanungsabschnittSchueler";
	import { useUvState } from "@ui/states/UvState";

	const props = defineProps<{
		schueler: UvPlanungsabschnittSchueler | undefined;
		gotoSchueler: (id: number) => Promise<void>;
		gotoKlasse: (idKlasse: number) => Promise<void>;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const draft = shallowRef<Partial<UvPlanungsabschnittSchueler>>({});
	const klassen = computed(() => state.planungsabschnitt === null ? [] : [...state.uvManager.klasseGetMengeByPlanungsabschnitt(state.planungsabschnitt)]);
	watch(() => [props.schueler?.idPlanungsabschnitt, props.schueler?.idSchueler], () => {
		draft.value = {};
	});
	async function patch(changes: Partial<UvPlanungsabschnittSchueler>) {
		const schueler = props.schueler;
		if (schueler === undefined || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		draft.value = { ...draft.value, ...changes };
		const data = draft.value;
		const idPlanungsabschnitt = schueler.idPlanungsabschnitt;
		const idSchueler = schueler.idSchueler;
		loading.value = true;
		try {
			await state.patchSchueler(schueler.idPlanungsabschnitt, schueler.idSchueler, data);
			if (props.schueler?.idPlanungsabschnitt === idPlanungsabschnitt && props.schueler.idSchueler === idSchueler) {
				draft.value = {};
			}
		} catch {
			// Der Entwurf bleibt für eine erneute Änderung erhalten.
		} finally {
			loading.value = false;
		}
	}

	const jahrgang = computed<JahrgangsDaten | null>(() => {
		const id = draft.value.idJahrgang ?? props.schueler?.idJahrgang ?? null;
		if (id === null) {
			return null;
		}
		return state.uvManager.jahrgangsdatenGetById(id);
	});

	const klasse = computed<UvKlasse | null>(() => {
		const id = draft.value.idKlasse === undefined ? props.schueler?.idKlasse ?? null : draft.value.idKlasse;
		if (id === null) {
			return null;
		}
		return state.uvManager.klasseGetByIdOrException(id);
	});

</script>
