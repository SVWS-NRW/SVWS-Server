<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" :required="true" :max-len="15" :valid="validateKuerzel" v-model="data.kuerzel" type="text" />
					<svws-ui-text-input placeholder="Beschreibung" :max-len="150" :valid="validateBeschreibung" v-model="data.beschreibung" type="text" />
					<svws-ui-spacing />

					<svws-ui-select title="Klassen-Jahrgang" v-model="jahrgang" :items="jahrgaenge" :item-text="getSelectTextJahrgang"
						:empty-text="() => 'JU - Jahrgangsübergreifend'" removable />
					<svws-ui-select title="Parallelität" v-model="parallelitaet" :item-text="p => p"
						:items="['---','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']" />
					<svws-ui-spacing />

					<svws-ui-select v-if="!listeVorgaengerklassen.isEmpty()" title="Vorgängerklasse" v-model="vorgaengerklasse"
						:items="listeVorgaengerklassen" :item-text="f => f.kuerzel ?? '---'" removable />
					<svws-ui-text-input v-else placeholder="Vorgängerklasse" v-model="kuerzelVorgaengerklasse" type="text" disabled />
					<svws-ui-select v-if="!listeFolgeklassen.isEmpty()" title="Folgeklasse" v-model="folgeklasse" :items="listeFolgeklassen"
						:item-text="f => f.kuerzel ?? '---'" removable />
					<svws-ui-text-input v-else placeholder="Folgeklasse" v-model="kuerzelFolgeklasse" type="text" disabled />
					<svws-ui-spacing />

					<svws-ui-select title="Schulgliederung" v-model="schulgliederung" :items="schulgliederungen" :item-text="getSelectText" />
					<svws-ui-text-input placeholder="Prüfungsordnung" v-model="data.pruefungsordnung" type="text" disabled />
					<svws-ui-select v-if="schulform.istAllgemeinbildend()" title="Klassenart" v-model="klassenart" :items="klassenarten" :item-text="getSelectText" />
					<svws-ui-select v-if="schulform.istAllgemeinbildend()" title="Organisationsform" v-model="organisationsformAllgemeinbildend" :items="organisationsformenAllgemeinbildend" :item-text="getSelectText" />
					<svws-ui-select v-if="schulform.istBerufsbildend()" title="Organisationsform" v-model="organisationsformBerufsbildend" :items="organisationsformenBerufsbildend" :item-text="getSelectText" />
					<svws-ui-select v-if="schulform.istWeiterbildung()" title="Organisationsform" v-model="organisationsformWeiterbildend" :items="organisationsformenWeiterbildend" :item-text="getSelectText" />
				</svws-ui-input-wrapper>

				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel" :disabled="isLoading">Abbrechen</svws-ui-button>
					<svws-ui-button @click="addKlasse()" :disabled="!isValid || isLoading">
						Speichern <svws-ui-spinner :spinning="isLoading" />
					</svws-ui-button>
				</div>
			</svws-ui-content-card>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { ref, computed, onMounted, watch } from "vue";
	import type { KlassenNeuProps } from "~/components/klassen/SKlassenNeuProps";
	import type { KlassenDaten, JahrgangsDaten, List } from '@core';
	import { AllgemeinbildendOrganisationsformen, Klassenart, Schulgliederung, ArrayList, Schulform, BerufskollegOrganisationsformen, WeiterbildungskollegOrganisationsformen } from "@core";

	const props = defineProps<KlassenNeuProps>();

	const schulform = computed(() => props.schulform);
	const schuljahr = computed(() => props.manager().getSchuljahr());

	const isLoading = ref<boolean>(false);
	const isValid = ref<boolean>(false);
	const data = ref<Partial<KlassenDaten>>({});

	onMounted(() => {
		const schulgliederungDefault = Schulgliederung.getDefault(props.schulform);
		const schulgliederung = (schulgliederungDefault !== null) ? schulgliederungDefault
			: Schulgliederung.getBySchuljahrAndSchulform(props.manager().getSchuljahr(), props.schulform).getFirst();
		const idSchulgliederung = schulgliederung.daten(props.manager().getSchuljahr())?.id ?? -1;
		const idKlassenart = Klassenart.getDefault(props.schulform).daten(props.manager().getSchuljahr())?.id ?? Klassenart.UNDEFINIERT.daten(props.manager().getSchuljahr())?.id;
		if (props.schulform.istAllgemeinbildend()) {
			data.value = {
				kuerzel: "",
				beschreibung: "",
				idJahrgang: null,
				parallelitaet: null,
				idSchulgliederung,
				idKlassenart,
				idAllgemeinbildendOrganisationsform: AllgemeinbildendOrganisationsformen.GANZTAG.daten(props.manager().getSchuljahr())?.id ?? null,
			}
		} else if (props.schulform.istBerufsbildend()) {
			data.value = {
				kuerzel: "",
				beschreibung: "",
				idJahrgang: null,
				parallelitaet: null,
				idSchulgliederung,
				idBerufsbildendOrganisationsform: BerufskollegOrganisationsformen.VOLLZEIT.daten(props.manager().getSchuljahr())?.id ?? null,
			}
		} else if (props.schulform.istWeiterbildung()) {
			data.value = {
				kuerzel: "",
				beschreibung: "",
				idJahrgang: null,
				parallelitaet: null,
				idSchulgliederung,
				idWeiterbildungOrganisationsform: WeiterbildungskollegOrganisationsformen.VOLLZEIT.daten(props.manager().getSchuljahr())?.id ?? null,
			}
		}

		watch(() => data.value, async () => {
			if (isLoading.value)
				return;

			props.checkpoint.active = true;
			validateAll();
		}, { immediate: false, deep: true });
	})

	const parallelitaet = computed<string | null>({
		get: () => data.value.parallelitaet ?? '---',
		set: (value) => data.value.parallelitaet = value,
	});

	const schulgliederung = computed<Schulgliederung | null>({
		get: () => (data.value.idSchulgliederung === undefined) ? null : Schulgliederung.data().getWertByID(data.value.idSchulgliederung),
		set: (value) => data.value.idSchulgliederung = value?.daten(schuljahr.value)?.id,
	});

	const schulgliederungen = computed(() => Schulgliederung.getBySchuljahrAndSchulform(schuljahr.value, schulform.value));

	const klassenart = computed<Klassenart | null>({
		get: () => (data.value.idKlassenart === undefined) ? null : Klassenart.data().getWertByID(data.value.idKlassenart),
		set: (value) => data.value.idKlassenart = value?.daten(schuljahr.value)?.id,
	});
	const klassenarten = computed(() => Klassenart.getBySchuljahrAndSchulform(schuljahr.value, schulform.value));

	const organisationsformAllgemeinbildend = computed<AllgemeinbildendOrganisationsformen | null>({
		get: () => {
			const id = data.value.idAllgemeinbildendOrganisationsform;
			if ((id === null) || (id === undefined))
				return null;
			return AllgemeinbildendOrganisationsformen.data().getWertByID(id);
		},
		set: (value) => data.value.idAllgemeinbildendOrganisationsform = value?.daten(schuljahr.value)?.id,
	});
	const organisationsformenAllgemeinbildend = computed(() => AllgemeinbildendOrganisationsformen.values());

	const organisationsformBerufsbildend = computed<BerufskollegOrganisationsformen | null>({
		get: () => {
			const id = data.value.idBerufsbildendOrganisationsform;
			if ((id === null) || (id === undefined))
				return null;
			return BerufskollegOrganisationsformen.data().getWertByID(id);
		},
		set: (value) => data.value.idBerufsbildendOrganisationsform = value?.daten(schuljahr.value)?.id,
	});
	const organisationsformenBerufsbildend = computed(() => BerufskollegOrganisationsformen.values());

	const organisationsformWeiterbildend = computed<WeiterbildungskollegOrganisationsformen | null>({
		get: () => {
			const id = data.value.idWeiterbildungOrganisationsform;
			if ((id === null) || (id === undefined))
				return null;
			return WeiterbildungskollegOrganisationsformen.data().getWertByID(id);
		},
		set: (value) => data.value.idWeiterbildungOrganisationsform = value?.daten(schuljahr.value)?.id,
	});
	const organisationsformenWeiterbildend = computed(() => WeiterbildungskollegOrganisationsformen.values());

	const jahrgang = computed<JahrgangsDaten | null>({
		get: () => {
			const id = data.value.idJahrgang;
			if ((id === null) || (id === undefined))
				return null;
			return props.manager().jahrgaenge.get(id);
		},
		set: (value) => (data.value.idJahrgang = value?.id ?? null),
	});
	const jahrgaenge = computed<List<JahrgangsDaten>>(() => {
		const result = new ArrayList<JahrgangsDaten>();
		for (const jg of props.manager().jahrgaenge.list())
			if (jg.kuerzel !== "E3") // Das dritte Jahr der Schuleingangsphase sollte nicht für einen Jahrgang einer Klasse verwendet werden, da es Schüler-spezifisch ist
				result.add(jg);
		return result;
	});

	const vorgaengerklasse = computed<KlassenDaten | null>({
		get: () => {
			const id = data.value.idVorgaengerklasse;
			if ((id === null) || (id === undefined))
				return null;
			return props.mapKlassenVorigerAbschnitt().get(id) ?? null;
		},
		set: (value) => data.value.idVorgaengerklasse = value?.id ?? null,
	});

	const folgeklasse = computed<KlassenDaten | null>({
		get: () => {
			const id = data.value.idFolgeklasse;
			if ((id === null) || (id === undefined))
				return null;
			return props.mapKlassenFolgenderAbschnitt().get(id) ?? null;
		},
		set: (value) => data.value.idFolgeklasse = value?.id ?? null,
	});

	const kuerzelVorgaengerklasse = computed<string | null>(() => (data.value.kuerzelVorgaengerklasse === null) ? '&nbsp;' : data.value.kuerzelVorgaengerklasse ?? null);

	const kuerzelFolgeklasse = computed<string | null>(() => (data.value.kuerzelFolgeklasse === null) ? '&nbsp;' : data.value.kuerzelFolgeklasse ?? null);

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addKlasse() {
		if (isLoading.value === true)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		await props.add(data.value);
		isLoading.value = false;
	}

	const validateKuerzel = (kuerzel: string | null): boolean => props.manager().validateKuerzel(kuerzel);
	const validateBeschreibung = (beschreibung: string | null): boolean => props.manager().validateBeschreibung(beschreibung);

	const validateAll = () => isValid.value = (data.value.kuerzel !== undefined) && validateKuerzel(data.value.kuerzel) && (data.value.beschreibung !== undefined) && validateBeschreibung(data.value.beschreibung);

	function getSelectText(value: Klassenart | Schulgliederung | AllgemeinbildendOrganisationsformen | BerufskollegOrganisationsformen | WeiterbildungskollegOrganisationsformen) {
		return value.daten(schuljahr.value)?.kuerzel + ' - ' + value.daten(schuljahr.value)?.text;
	}

	function getSelectTextJahrgang(jg: JahrgangsDaten): string {
		switch (jg.kuerzel) {
			case null:
				return 'JU - Jahrgangsübergreifend';
			case 'E1':
				return '1E' + ' - ' + jg.bezeichnung;
			case 'E2':
				return '2E' + ' - ' + jg.bezeichnung;
			default:
				return jg.kuerzel + ' - ' + jg.bezeichnung;
		}
	}

	const listeFolgeklassen = computed<List<KlassenDaten>>(() => {
		const result = new ArrayList<KlassenDaten>();
		if (data.value.idJahrgang === null) {
			for (const kl of props.mapKlassenFolgenderAbschnitt().values())
				result.add(kl);
			return result;
		}

		const jg = data.value.idJahrgang === undefined ? null : props.manager().jahrgaenge.get(data.value.idJahrgang);
		if (jg === null)
			return result;

		for (const kl of props.mapKlassenFolgenderAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl);
			} else {
				const jgKl = props.manager().jahrgaenge.get(kl.idJahrgang);
				if (jg.idFolgejahrgang === jgKl?.id)
					result.add(kl);
			}
		}
		return result;
	});

	const listeVorgaengerklassen = computed<List<KlassenDaten>>(() => {
		const result = new ArrayList<KlassenDaten>();
		if (data.value.idJahrgang === null) {
			for (const kl of props.mapKlassenVorigerAbschnitt().values())
				result.add(kl);
			return result;
		}

		const jg = data.value.idJahrgang === undefined ? null : props.manager().jahrgaenge.get(data.value.idJahrgang);
		if (jg === null)
			return result;

		for (const kl of props.mapKlassenVorigerAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl);
			} else {
				const jgKl = props.manager().jahrgaenge.get(kl.idJahrgang);
				if (jg.id === jgKl?.idFolgejahrgang)
					result.add(kl);
			}
		}
		return result;
	});

</script>
