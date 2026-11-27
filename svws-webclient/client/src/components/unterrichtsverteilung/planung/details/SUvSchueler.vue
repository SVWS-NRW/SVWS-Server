<template>
	<div v-if="schueler !== undefined" class="flex flex-col gap-3 p-3">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<h3 class="content-card--headline">{{ schueler.daten.nachname }}, {{ schueler.daten.vorname }}</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="big"> ID: {{ schueler.idSchueler }} </svws-ui-badge>
				<svws-ui-button type="secondary" @click="gotoSchueler(schueler.idSchueler)">Schülerverwaltung öffnen</svws-ui-button>
			</template>
			<svws-ui-notification v-if="fehler" type="error">
				{{ fehler }}
				<svws-ui-button type="secondary" :disabled="loading" @click="patch({})">Erneut versuchen</svws-ui-button>
			</svws-ui-notification>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select :readonly="!hatKompetenzAendern"
					:disabled="loading"
					title="Jahrgang"
					:items="state.uvManager.jahrgangsdatenGetMenge()"
					:item-text="i => i.kuerzel ?? '—'"
					:model-value="jahrgang"
					@update:model-value="value => patch({ idJahrgang: value?.id })"
					autocomplete
					statistics
					required />
				<div class="min-w-0">
					<svws-ui-select class="min-w-0"
						:readonly="!hatKompetenzAendern"
						:disabled="loading"
						title="Klasse"
						:items="klassen"
						:item-text="i => i.kuerzel + i.parallelitaet"
						:model-value="klasse"
						@update:model-value="value => patch({ idKlasse: value?.id ?? null })"
						autocomplete
						statistics
						removable />
				</div>
			</svws-ui-input-wrapper>
			<svws-ui-button v-if="klasse !== null" type="secondary" @click="gotoKlasse(klasse.id)">Klasse öffnen</svws-ui-button>
		</svws-ui-content-card>
	</div>
	<div v-else class="p-3">
		<span class="text-ui-disabled">Kein Schüler ausgewählt</span>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { UvKlasse } from "@core/core/data/uv/UvKlasse";
	import type { UvPlanungsabschnittSchueler } from "@core/core/data/uv/UvPlanungsabschnittSchueler";
	import { useUvState } from "@ui/states/UvState";

	import type { UvSchuelerAenderungsentwurf } from '../UvPlanung';

	const props = defineProps<{
		schueler: UvPlanungsabschnittSchueler | undefined;
		entwuerfe: Map<string, UvSchuelerAenderungsentwurf>;
		gotoSchueler: (id: number) => Promise<void>;
		gotoKlasse: (idKlasse: number) => void;
	}>();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const entwurf = computed(() => {
		const schueler = props.schueler;
		return schueler === undefined ? undefined : props.entwuerfe.get(`${schueler.idPlanungsabschnitt}:${schueler.idSchueler}`);
	});
	const loading = computed(() => entwurf.value?.speichert ?? false);
	const fehler = computed(() => entwurf.value?.fehler ?? '');
	const draft = computed(() => entwurf.value?.daten ?? {});
	const klassen = computed(() => state.planungsabschnitt === null ? [] : [...state.uvManager.klasseGetMengeByPlanungsabschnitt(state.planungsabschnitt)]);

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
		return klassen.value.find(item => item.id === id) ?? null;
	});
	/** Speichert den Entwurf je Schüler und Abschnitt und erhält ihn bei einem fehlgeschlagenen Request. */
	async function patch(changes: Partial<UvPlanungsabschnittSchueler>): Promise<void> {
		const schueler = props.schueler;
		if (schueler === undefined || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		const entwuerfe = props.entwuerfe;
		const { idPlanungsabschnitt, idSchueler } = schueler;
		const schluessel = `${idPlanungsabschnitt}:${idSchueler}`;
		const daten = { ...draft.value, ...changes };
		entwuerfe.set(schluessel, { daten, speichert: true, fehler: '' });
		try {
			await state.planung.patchSchueler(idPlanungsabschnitt, idSchueler, daten);
			entwuerfe.delete(schluessel);
		} catch {
			entwuerfe.set(schluessel, {
				daten,
				speichert: false,
				fehler: 'Die Änderung wurde nicht gespeichert. Die Eingaben bleiben für einen erneuten Versuch erhalten.',
			});
		}
	}


</script>
