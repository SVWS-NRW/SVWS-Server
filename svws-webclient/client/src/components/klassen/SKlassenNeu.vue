<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" :required="true" :max-len="15" v-model="modelProxy.proxy.kuerzel"
						:validation="() => modelProxy.getFehler('kuerzel')" skip-default-validation />
					<svws-ui-text-input placeholder="Beschreibung" :max-len="150" v-model="modelProxy.proxy.beschreibung"
						:validation="() => modelProxy.getFehler('beschreibung')" skip-default-validation />
					<svws-ui-spacing />

					<svws-ui-select title="Klassen-Jahrgang" v-model="modelProxy.jahrgang.value" :items="modelProxy.jahrgaenge.value" :item-text="getSelectTextJahrgang"
						:empty-text="() => 'JU - Jahrgangsübergreifend'" removable />
					<svws-ui-select title="Parallelität" v-model="modelProxy.parallelitaet.value" :item-text="p => p"
						:items="['---','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']" />
					<svws-ui-spacing />

					<svws-ui-select v-if="!modelProxy.listeVorgaengerklassen.value.isEmpty()" title="Vorgängerklasse" v-model="modelProxy.vorgaengerklasse.value"
						:items="modelProxy.listeVorgaengerklassen.value" :item-text="f => f.kuerzel ?? '---'" removable />
					<svws-ui-text-input v-else placeholder="Vorgängerklasse" v-model="modelProxy.kuerzelVorgaengerklasse.value" disabled />
					<svws-ui-select v-if="!modelProxy.listeFolgeklassen.value.isEmpty()" title="Folgeklasse" v-model="modelProxy.folgeklasse.value" :items="modelProxy.listeFolgeklassen.value"
						:item-text="f => f.kuerzel ?? '---'" removable />
					<svws-ui-text-input v-else placeholder="Folgeklasse" v-model="modelProxy.kuerzelFolgeklasse.value" disabled />
					<svws-ui-spacing />

					<svws-ui-select title="Schulgliederung" v-model="modelProxy.schulgliederung.value" :items="modelProxy.schulgliederungen.value" :item-text="getSelectText" />
					<svws-ui-text-input placeholder="Prüfungsordnung" v-model="modelProxy.proxy.pruefungsordnung" disabled />
					<svws-ui-select v-if="schulform.istAllgemeinbildend()" title="Klassenart" v-model="modelProxy.klassenart.value" :items="modelProxy.klassenarten.value" :item-text="getSelectText" />
					<svws-ui-select v-if="schulform.istAllgemeinbildend()" title="Organisationsform" v-model="modelProxy.organisationsformAllgemeinbildend.value" :items="modelProxy.organisationsformenAllgemeinbildend.value" :item-text="getSelectText" />
					<svws-ui-select v-if="schulform.istBerufsbildend()" title="Organisationsform" v-model="modelProxy.organisationsformBerufsbildend.value" :items="modelProxy.organisationsformenBerufsbildend.value" :item-text="getSelectText" />
					<svws-ui-select v-if="schulform.istWeiterbildung()" title="Organisationsform" v-model="modelProxy.organisationsformWeiterbildend.value" :items="modelProxy.organisationsformenWeiterbildend.value" :item-text="getSelectText" />
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
	import { KlassenDaten, AllgemeinbildendOrganisationsformen, Klassenart, Schulgliederung, BerufskollegOrganisationsformen, WeiterbildungskollegOrganisationsformen, type JahrgangsDaten } from "@core";
	import { KlassenDatenModelProxy } from "./KlassenDatenModelProxy";

	const props = defineProps<KlassenNeuProps>();

	const dataNotPatched = shallowRef(new KlassenDaten());

	const modelProxy = new KlassenDatenModelProxy(() => dataNotPatched.value, () => props.manager().liste.list(), props.manager, props.mapKlassenVorigerAbschnitt, props.mapKlassenFolgenderAbschnitt);

	onMounted(() => {
		const proxy = modelProxy.proxy;
		initWithDefaults(proxy);
		modelProxy.validate();

		watch(() => modelProxy.pending, () => props.checkpoint.active = true);
	});

	function getSelectText(value: Klassenart | Schulgliederung | AllgemeinbildendOrganisationsformen | BerufskollegOrganisationsformen | WeiterbildungskollegOrganisationsformen) {
		return value.daten(props.manager().getSchuljahr())?.kuerzel + ' - ' + value.daten(props.manager().getSchuljahr())?.text;
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
		// Erstelle einen create-patch, der die erforderten Attribute zusätzlich zum Pending-State beinhaltet
		const result: Partial<KlassenDaten> = {
			idSchuljahresabschnitt: modelProxy.proxy.idSchuljahresabschnitt,
			kuerzel: modelProxy.proxy.kuerzel,
			idJahrgang: modelProxy.proxy.idJahrgang,
			...modelProxy.pending,
		};
		await props.add(result);
		isLoading.value = false;
	}

</script>
