<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" type="text" :required="true" :max-len="15" v-model="data.kuerzel"
						:validation="() => modelProxy.getFehler('kuerzel')" skip-default-validation />
					<svws-ui-text-input placeholder="Beschreibung" type="text" :max-len="150" v-model="data.beschreibung"
						:validation="() => modelProxy.getFehler('beschreibung')" skip-default-validation />
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

	import { ref, computed, onMounted, watch, shallowRef } from "vue";
	import type { KlassenNeuProps } from "~/components/klassen/SKlassenNeuProps";
	import type { JahrgangsDaten, List } from '@core';
	import { KlassenDaten, AllgemeinbildendOrganisationsformen, Klassenart, Schulgliederung, ArrayList, BerufskollegOrganisationsformen, WeiterbildungskollegOrganisationsformen } from "@core";
	import { KlassenModelProxy } from "./KlassenModelProxy";

	const props = defineProps<KlassenNeuProps>();

	const schulform = computed(() => props.schulform);
	const schuljahr = computed(() => props.manager().getSchuljahr());

	const dataNotPatched = shallowRef(new KlassenDaten());

	const modelProxy = new KlassenModelProxy(() => dataNotPatched.value, () => props.manager().liste.list());
	const data = modelProxy.proxy;

	onMounted(() => {
		const proxy = modelProxy.proxy;
		initWithDefaults(proxy);
		modelProxy.validate();

		watch(() => modelProxy.pending, () => props.checkpoint.active = true);
	});


	const isValid = computed<boolean>(() => modelProxy.getAlleFehler().isEmpty());

	/**
	 * Initialisiere den Default-State
	 *
	 * @param daten   die zu initialisierenden Daten
	 */
	function initWithDefaults(daten: KlassenDaten) {
		const schulgliederungDefault = Schulgliederung.getDefault(props.schulform);
		const schulgliederung = (schulgliederungDefault === null)
			? Schulgliederung.getBySchuljahrAndSchulform(props.manager().getSchuljahr(), props.schulform).getFirst()
			: schulgliederungDefault;
		const idSchulgliederung = schulgliederung.daten(props.manager().getSchuljahr())?.id ?? -1;
		daten.kuerzel = "";
		daten.beschreibung = "";
		daten.idJahrgang = null;
		daten.parallelitaet = null;
		daten.idSchulgliederung = idSchulgliederung;
		if (props.schulform.istAllgemeinbildend()) {
			daten.idKlassenart = (Klassenart.getDefault(props.schulform).daten(props.manager().getSchuljahr())?.id) ?? (Klassenart.UNDEFINIERT.daten(props.manager().getSchuljahr())?.id ?? -1);
			daten.idAllgemeinbildendOrganisationsform = AllgemeinbildendOrganisationsformen.GANZTAG.daten(props.manager().getSchuljahr())?.id ?? null;
		} else if (props.schulform.istBerufsbildend()) {
			daten.idBerufsbildendOrganisationsform = BerufskollegOrganisationsformen.VOLLZEIT.daten(props.manager().getSchuljahr())?.id ?? null;
		} else if (props.schulform.istWeiterbildung()) {
			daten.idWeiterbildungOrganisationsform = WeiterbildungskollegOrganisationsformen.VOLLZEIT.daten(props.manager().getSchuljahr())?.id ?? null;
		}
	}

	const isLoading = ref<boolean>(false);

	const parallelitaet = computed<string | null>({
		get: () => data.parallelitaet ?? '---',
		set: (value) => data.parallelitaet = value,
	});

	const schulgliederung = computed<Schulgliederung | null>({
		get: () => (data.idSchulgliederung === -1) ? null : Schulgliederung.data().getWertByID(data.idSchulgliederung),
		set: (value) => data.idSchulgliederung = value?.daten(schuljahr.value)?.id ?? -1,
	});
	const schulgliederungen = computed(() => Schulgliederung.getBySchuljahrAndSchulform(schuljahr.value, schulform.value));

	const klassenart = computed<Klassenart | null>({
		get: () => (data.idKlassenart === -1) ? null : Klassenart.data().getWertByID(data.idKlassenart),
		set: (value) => data.idKlassenart = value?.daten(schuljahr.value)?.id ?? -1,
	});
	const klassenarten = computed(() => Klassenart.getBySchuljahrAndSchulform(schuljahr.value, schulform.value));

	const organisationsformAllgemeinbildend = computed<AllgemeinbildendOrganisationsformen | null>({
		get: () => {
			const id = data.idAllgemeinbildendOrganisationsform;
			return (id === null) ? null : AllgemeinbildendOrganisationsformen.data().getWertByID(id);
		},
		set: (value) => data.idAllgemeinbildendOrganisationsform = value?.daten(schuljahr.value)?.id ?? null,
	});
	const organisationsformenAllgemeinbildend = computed(() => AllgemeinbildendOrganisationsformen.values());

	const organisationsformBerufsbildend = computed<BerufskollegOrganisationsformen | null>({
		get: () => {
			const id = data.idBerufsbildendOrganisationsform;
			return (id === null) ? null : BerufskollegOrganisationsformen.data().getWertByID(id);
		},
		set: (value) => data.idBerufsbildendOrganisationsform = value?.daten(schuljahr.value)?.id ?? null,
	});
	const organisationsformenBerufsbildend = computed(() => BerufskollegOrganisationsformen.values());

	const organisationsformWeiterbildend = computed<WeiterbildungskollegOrganisationsformen | null>({
		get: () => {
			const id = data.idWeiterbildungOrganisationsform;
			return (id === null) ? null : WeiterbildungskollegOrganisationsformen.data().getWertByID(id);
		},
		set: (value) => data.idWeiterbildungOrganisationsform = value?.daten(schuljahr.value)?.id ?? null,
	});
	const organisationsformenWeiterbildend = computed(() => WeiterbildungskollegOrganisationsformen.values());

	const jahrgang = computed<JahrgangsDaten | null>({
		get: () => {
			const id = data.idJahrgang;
			return (id === null) ? null : props.manager().jahrgaenge.get(id);
		},
		set: (value) => (data.idJahrgang = value?.id ?? null),
	});
	const jahrgaenge = computed<List<JahrgangsDaten>>(() => {
		const result = new ArrayList<JahrgangsDaten>();
		for (const jg of props.manager().jahrgaenge.list()) {
			if (jg.kuerzel !== "E3") { // Das dritte Jahr der Schuleingangsphase sollte nicht für einen Jahrgang einer Klasse verwendet werden, da es Schüler-spezifisch ist
				result.add(jg);
			}
		}
		return result;
	});

	const vorgaengerklasse = computed<KlassenDaten | null>({
		get: () => {
			const id = data.idVorgaengerklasse;
			return (id === null) ? null : props.mapKlassenVorigerAbschnitt().get(id) ?? null;
		},
		set: (value) => data.idVorgaengerklasse = value?.id ?? null,
	});

	const folgeklasse = computed<KlassenDaten | null>({
		get: () => {
			const id = data.idFolgeklasse;
			return (id === null) ? null : props.mapKlassenFolgenderAbschnitt().get(id) ?? null;
		},
		set: (value) => data.idFolgeklasse = value?.id ?? null,
	});

	const kuerzelVorgaengerklasse = computed<string | null>(() => (data.kuerzelVorgaengerklasse === null) ? '&nbsp;' : data.kuerzelVorgaengerklasse);

	const kuerzelFolgeklasse = computed<string | null>(() => (data.kuerzelFolgeklasse === null) ? '&nbsp;' : data.kuerzelFolgeklasse);

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	async function addKlasse() {
		if (isLoading.value === true) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		await props.add(modelProxy.pending);
		isLoading.value = false;
	}

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

		const idJahrgang = data.idJahrgang;
		if (idJahrgang === null) {
			for (const kl of props.mapKlassenFolgenderAbschnitt().values()) {
				result.add(kl);
			}
			return result;
		}

		const jg = props.manager().jahrgaenge.get(idJahrgang);
		if (jg === null) {
			return result;
		}

		for (const kl of props.mapKlassenFolgenderAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl);
			} else {
				const jgKl = props.manager().jahrgaenge.get(kl.idJahrgang);
				if (jg.idFolgejahrgang === jgKl?.id) {
					result.add(kl);
				}
			}
		}
		return result;
	});

	const listeVorgaengerklassen = computed<List<KlassenDaten>>(() => {
		const result = new ArrayList<KlassenDaten>();

		const idJahrgang = data.idJahrgang;
		if (idJahrgang === null) {
			for (const kl of props.mapKlassenVorigerAbschnitt().values()) {
				result.add(kl);
			}
			return result;
		}

		const jg = props.manager().jahrgaenge.get(idJahrgang);
		if (jg === null) {
			return result;
		}

		for (const kl of props.mapKlassenVorigerAbschnitt().values()) {
			if (kl.idJahrgang === null) {
				result.add(kl);
			} else {
				const jgKl = props.manager().jahrgaenge.get(kl.idJahrgang);
				if (jg.id === jgKl?.idFolgejahrgang) {
					result.add(kl);
				}
			}
		}
		return result;
	});

</script>
