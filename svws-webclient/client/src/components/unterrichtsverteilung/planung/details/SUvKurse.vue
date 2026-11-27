<template>
	<div v-if="props.kurs !== undefined" class="flex flex-col gap-3">
		<svws-ui-content-card>
			<template #title>
				<div class="content-card--header gap-2"><h3 class="content-card--headline" title="Kursunterricht">Kursunterricht</h3><s-uv-fach-badge :id-fach="props.kurs.idFach" /></div>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Fach"
					:model-value="selectedFach"
					:items="fachItems"
					:item-text="presenter.fachText"
					@update:model-value="fach => fach !== null && fach !== undefined && patchKurs({ idFach: fach.id })"
					:readonly="!hatKompetenzAendern"
					:disabled="gesperrt"
					autocomplete
					required />
				<svws-ui-select title="Kursart"
					:model-value="formKurs.kursart"
					:items="[...kursarten.keys()]"
					:item-text="art => `${art} (${kursarten.get(art) ?? 'Nicht verfügbar'})`"
					@update:model-value="kursart => kursart !== null && kursart !== undefined && patchKurs({ kursart })"
					:readonly="!hatKompetenzAendern"
					:disabled="gesperrt"
					autocomplete
					required />
				<svws-ui-input-number placeholder="Kursnummer"
					:model-value="formKurs.kursnummer"
					@change="kursnummer => kursnummer !== null && patchKurs({ kursnummer })"
					:valid="UvPlanungsManager.kursnummerGueltig"
					:min="1"
					:step="1"
					:readonly="!hatKompetenzAendern"
					:disabled="gesperrt"
					required />
				<svws-ui-select :model-value="selectedSchuljahresabschnitt"
					@update:model-value="onSchuljahresabschnittChange"
					:items="schuljahresabschnittItems"
					:item-text="presenter.schuljahresabschnittText"
					title="Schuljahresabschnitt" :readonly="!hatKompetenzAendern" :disabled="gesperrt" required />
				<svws-ui-select title="Schülergruppe"
					:model-value="selectedSchuelergruppe"
					@update:model-value="onSchuelergruppeChange"
					:items="schuelergruppeItems"
					:item-text="sg => sg.bezeichnung" :readonly="!hatKompetenzAendern" :disabled="gesperrt" autocomplete required />
			</svws-ui-input-wrapper>
			<svws-ui-button v-if="hatKompetenzAendern"
				type="secondary"
				:disabled="gesperrt || entwurf !== undefined"
				@click="props.schuelergruppeErstellen('kurse', props.kurs.id, presenter.kursBezeichnung(props.kurs))">
				Neue Schülergruppe
			</svws-ui-button>
			<div v-if="error" class="mt-4">
				<svws-ui-notification type="error">{{ error }}</svws-ui-notification>
				<svws-ui-button v-if="entwurf !== undefined && hatKompetenzAendern && validationErrors.length === 0"
					type="secondary"
					:disabled="gesperrt"
					@click="patchKurs({})">
					Erneut versuchen
				</svws-ui-button>
				<svws-ui-button v-if="entwurf !== undefined" type="secondary" :disabled="gesperrt" @click="entwurfVerwerfen">Änderungen verwerfen</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
	<div v-else class="p-3">
		<span class="text-ui-disabled">Kein Kurs ausgewählt</span>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import { UvKurs } from "@core/core/data/uv/UvKurs";
	import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";

	import SUvFachBadge from "../../SUvFachBadge.vue";
	import { createUvPresenter } from "../../UvPresenter";
	import type { UvZuordnungBearbeitung } from "../UvPlanung";
	import { UvPlanungsManager } from "../UvPlanungsManager";
	import { getKursarten } from "~/utils/helfer";

	const props = defineProps<{
		kurs: UvKurs | undefined;
		schuljahr: number;
		bearbeitung: UvZuordnungBearbeitung | null;
		schuelergruppeErstellen: (art: 'kurse', id: number, bezeichnung: string) => void;
	}>();

	const abschnittState = useAbschnittState();
	const state = useUvState();
	const presenter = computed(() => createUvPresenter(state.uvManager));

	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const gesperrt = computed(() => loading.value || props.bearbeitung !== null);
	const error = ref("");
	const entwurf = ref<Partial<UvKurs> | undefined>();
	const formKurs = computed(() => Object.assign(new UvKurs(), props.kurs, entwurf.value));
	// Auswahlwerte und Prüfung des Kursentwurfs

	const fachItems = computed(() => [...state.uvManager.fachGetMengeAsList()]);
	const selectedFach = computed(() => fachItems.value.find(fach => fach.id === formKurs.value.idFach));
	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);

	const selectedSchuljahresabschnitt = computed(() => {
		if (props.kurs === undefined) {
			return undefined;
		}
		return abschnittState.getOrNull(formKurs.value.idSchuljahresabschnitt) ?? undefined;
	});

	const schuelergruppeItems = computed(() => state.planungsabschnitt === null ? []
		: [...state.uvManager.schuelergruppeGetMengeByPlanungsabschnitt(state.planungsabschnitt)]);

	const selectedSchuelergruppe = computed(() => {
		if (props.kurs === undefined) {
			return undefined;
		}
		return schuelergruppeItems.value.find(sg => sg.id === formKurs.value.idSchuelergruppe);
	});

	const kursarten = computed(() => getKursarten(selectedSchuljahresabschnitt.value?.schuljahr ?? props.schuljahr));
	const validationErrors = computed(() => UvPlanungsManager.kursFehler(state.uvManager, formKurs.value, selectedSchuljahresabschnitt.value?.schuljahr ?? props.schuljahr));

	function entwurfVerwerfen(): void {
		error.value = "";
		entwurf.value = undefined;
	}

	watch(() => props.kurs?.id, entwurfVerwerfen);

	/** Sammelt Änderungen im Entwurf und speichert ihn erst nach erfolgreicher Validierung. */
	async function patchKurs(patch: Partial<UvKurs>) {
		const kurs = props.kurs;
		if (kurs === undefined || !hatKompetenzAendern.value || gesperrt.value) {
			return;
		}
		error.value = "";
		const changes = { ...entwurf.value, ...patch };
		entwurf.value = changes;
		if (validationErrors.value.length > 0) {
			error.value = validationErrors.value.join(' ') + ' Die Änderung wurde nicht gespeichert.';
			return;
		}
		loading.value = true;
		try {
			await state.planung.patchKurs(kurs.id, changes);
			if (props.kurs?.id === kurs.id) {
				entwurf.value = undefined;
			}
		} catch {
			if (props.kurs?.id === kurs.id) {
				entwurf.value = changes;
				error.value = "Die Änderung konnte nicht gespeichert werden. Bitte erneut versuchen.";
			}
		} finally {
			loading.value = false;
		}
	}

	// Änderungen aus den Auswahllisten

	function onSchuljahresabschnittChange(abschnitt: Schuljahresabschnitt | null | undefined): void {
		if (abschnitt !== null && abschnitt !== undefined) {
			void patchKurs({ idSchuljahresabschnitt: abschnitt.id });
		}
	}

	function onSchuelergruppeChange(gruppe: UvSchuelergruppe | null | undefined): void {
		if (gruppe !== null && gruppe !== undefined) {
			void patchKurs({ idSchuelergruppe: gruppe.id });
		}
	}

</script>
