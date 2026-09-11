<template>
	<slot :open-modal />
	<svws-ui-modal :auto-close="!loading" :close-in-title="!loading" v-model:show="show" class="hidden" size="small">
		<template #modalTitle>Stundentafel neu erstellen</template>
		<template #modalContent>
			<svws-ui-notification v-if="actionError" type="error" class="mb-3">{{ actionError }}</svws-ui-notification>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Bezeichnung" v-model="bezeichnung" span="full" required />
				<svws-ui-select :disabled="loading || !hatKompetenzAendern" title="Jahrgang" v-model="jahrgang" :items="jahrgaenge" :item-text="j => ''+j.bezeichnung" required class="col-span-2" />
				<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Gültig von" v-model="gueltigVon" type="date" required />
					<svws-ui-text-input :disabled="loading || !hatKompetenzAendern" placeholder="Gültig bis" v-model="gueltigBis" type="date" />
				</div>
				<svws-ui-textarea-input placeholder="Beschreibung" v-model="beschreibung" span="full" />
				<div class="col-span-full border-t border-ui pt-4 mt-2 font-bold">Import</div>
				<svws-ui-select :disabled="loading || !hatKompetenzAendern" title="Schuljahr" v-model="importSchuljahr" :items="schuljahre" :item-text="schuljahrText" class="col-span-full" />
				<svws-ui-select title="Klasse" v-model="importKlasse" :items="importKlassen" :item-text="k => k.kuerzel ?? ''" class="col-span-full" :disabled="loading || !hatKompetenzAendern || importSchuljahr === null || jahrgang === null" />
				<svws-ui-checkbox :disabled="loading || !hatKompetenzAendern" v-model="fehlendeUvFaecherAnlegen" class="col-span-full">Fehlende UV-Fächer anlegen</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button :disabled="loading || !hatKompetenzAendern" type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button @click="create" :disabled="loading || !hatKompetenzAendern || !isValid">Erstellen</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import type { KlassenListeEintrag } from "@core/asd/data/klassen/KlassenListeEintrag";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";

	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const actionError = ref('');
	watch(() => state.planungsabschnitt?.id, () => {
		show.value = false;
		actionError.value = '';
	});
	const abschnittState = useAbschnittState();

	const show = ref<boolean>(false);
	const bezeichnung = ref("");
	const jahrgang = ref<JahrgangsDaten | null>(null);
	const gueltigVon = ref("");
	const gueltigBis = ref("");
	const beschreibung = ref("");
	const importSchuljahr = ref<number | null>(null);
	const importKlasse = ref<KlassenListeEintrag | null>(null);
	const klassenImImportAbschnitt = ref<KlassenListeEintrag[]>([]);
	const fehlendeUvFaecherAnlegen = ref(true);

	const jahrgaenge = computed<List<JahrgangsDaten>>(() => {
		const result = new ArrayList<JahrgangsDaten>();
		for (const jg of state.uvManager.jahrgangsdatenGetMenge()) {
			if (jg.kuerzel !== "E3") { // Das dritte Jahr der Schuleingangsphase sollte nicht für einen Jahrgang einer Klasse verwendet werden, da es Schüler-spezifisch ist
				result.add(jg);
			}
		}
		return result;
	});

	const isValid = computed(() => bezeichnung.value.trim().length > 0 && jahrgang.value !== null && gueltigVon.value !== "");
	const schuljahre = computed(() => [...new Set([...abschnittState.alle].map(a => a.schuljahr))].sort((a, b) => b - a));
	const importKlassen = computed(() => klassenImImportAbschnitt.value);
	const schuljahrText = (schuljahr: number) => `${schuljahr}/${schuljahr + 1}`;

	watch([importSchuljahr, jahrgang], async ([schuljahr, selectedJahrgang]) => {
		importKlasse.value = null;
		if ((schuljahr === null) || (selectedJahrgang === null)) {
			klassenImImportAbschnitt.value = [];
			return;
		}
		const abschnitt1 = [...abschnittState.alle].find(a => (a.schuljahr === schuljahr) && (a.abschnitt === 1));
		if (abschnitt1 === undefined) {
			klassenImImportAbschnitt.value = [];
			return;
		}
		klassenImImportAbschnitt.value = await state.getImportKlassen(abschnitt1.id, selectedJahrgang.id);
	});

	const openModal = () => {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		actionError.value = '';
		bezeichnung.value = "";
		jahrgang.value = null;
		gueltigVon.value = "";
		gueltigBis.value = "";
		beschreibung.value = "";
		importSchuljahr.value = null;
		importKlasse.value = null;
		klassenImImportAbschnitt.value = [];
		fehlendeUvFaecherAnlegen.value = true;
		show.value = true;
	};

	async function create() {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		actionError.value = '';
		try {
			if (!isValid.value || !jahrgang.value) {
				return;
			}
			const daten = {
				bezeichnung: bezeichnung.value,
				idJahrgang: jahrgang.value.id,
				gueltigVon: gueltigVon.value,
				gueltigBis: gueltigBis.value === "" ? null : gueltigBis.value,
				beschreibung: beschreibung.value === "" ? null : beschreibung.value,
			};
			if ((importSchuljahr.value !== null) && (importKlasse.value !== null)) {
				await state.importStundentafel({ ...daten, schuljahr: importSchuljahr.value, idKlasse: importKlasse.value.id, fehlendeUvFaecherAnlegen: fehlendeUvFaecherAnlegen.value });
			} else {
				await state.createStundentafel(daten);
			}
			show.value = false;
		} catch {
			actionError.value = 'Die Aktion konnte nicht abgeschlossen werden. Die Eingaben bleiben erhalten. Bitte den aktuellen Bestand prüfen und erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
