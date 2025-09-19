<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="big" class="hidden" :no-scroll="false">
		<template #modalTitle>Schülerbetrieb hinzufügen</template>
		<template #modalDescription />
		<template #modalContent>
			<svws-ui-content-card title="Basisdaten">
				<div class="input-wrapper">
					<svws-ui-text-input placeholder="Name" v-model="betrieb.name1" :disabled />
					<svws-ui-select title="Beschäftigungsart" v-model="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="i => i.text ?? ''" :disabled />
					<svws-ui-text-input placeholder="Namensergänzung" v-model="betrieb.name2" :disabled />
					<svws-ui-text-input placeholder="1. Telefon-Nr." v-model="betrieb.telefon1" :disabled :max-len="20" />
					<svws-ui-text-input placeholder="2. Telefon-Nr." v-model="betrieb.telefon2" :disabled :max-len="20" />
					<svws-ui-text-input placeholder="Fax-Nr." v-model="betrieb.fax" :disabled :max-len="20" />
					<svws-ui-text-input placeholder="E-Mail Adresse" v-model="betrieb.email" type="email" verify-email :disabled />
					<svws-ui-text-input placeholder="Branche" v-model="betrieb.branche" title="Branche" :disabled />
				</div>
			</svws-ui-content-card>
			<svws-ui-content-card title="Adresse">
				<div class="input-wrapper">
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Straße / Hausnummer" v-model="betrieb.strassenname" :disabled />
					</div>
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Zusatz" v-model="betrieb.hausnrzusatz" :disabled />
					</div>
					<svws-ui-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-filter="orte_filter" :disabled
						:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
					<!-- TODO In der Datenbank gibt es für die Adresse nur Ortsteil_id
					<svws-ui-select title="Ortsteil" v-model="inputOrtsteilID" :items="inputKatalogOrtsteil" :item-filter="ortsteilFilter"
						:item-sort="ortsteilSort" :item-text="i => i.ortsteil" />
					-->
					<!-- <svws-ui-checkbox v-model="anschreiben"> erhält Anschreiben </svws-ui-checkbox> -->
				</div>
			</svws-ui-content-card>
			<svws-ui-content-card title="Bemerkungen">
				<div class="entry-content">
					<div class="col-span-2">
						<svws-ui-textarea-input v-model="betrieb.bemerkungen" placeholder="Bemerkungen" :disabled />
					</div>
					<svws-ui-spacing />
					<svws-ui-input-wrapper :grid="2">
						<svws-ui-checkbox v-model="betrieb.ausbildungsbetrieb" :disabled> Ausbildungsbetrieb </svws-ui-checkbox>
						<svws-ui-checkbox v-model="betrieb.bietetPraktika" :disabled> Bietet Praktumsplätze </svws-ui-checkbox>
						<svws-ui-checkbox v-model="betrieb.Massnahmentraeger" :disabled> Maßnahmenträger </svws-ui-checkbox>
						<svws-ui-checkbox v-model="betrieb.ErwFuehrungszeugnis" :disabled> Erweitertes Führungszeugnis notwendig </svws-ui-checkbox>
						<svws-ui-checkbox v-model="betrieb.BelehrungISG" :disabled> Belehrung nach Infektionsschutzgesetz notwendig </svws-ui-checkbox>
					</svws-ui-input-wrapper>
				</div>
			</svws-ui-content-card>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="importer()" :disabled="!betrieb.name1 || !hatKompetenzUpdate"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { KatalogEintrag, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { BetriebStammdaten, BenutzerKompetenz } from "@core";
	import { orte_filter, orte_sort } from "~/utils/helfer";

	const props = defineProps<{
		addEintrag: (betrieb: BetriebStammdaten) => Promise<void>;
		deleteEintraege: (betrieb: BetriebStammdaten) => Promise<void>;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
		benutzerKompetenzen: Set<BenutzerKompetenz>;
	}>();

	const show = ref<boolean>(false);
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const disabled = computed(() => !hatKompetenzUpdate.value);

	const betrieb = ref<BetriebStammdaten>(new BetriebStammdaten());
	const beschaeftigungsart = ref<KatalogEintrag>();

	const inputWohnortID = computed<OrtKatalogEintrag | null>({
		get: () => (betrieb.value.ort_id !== null) ? props.mapOrte.get(betrieb.value.ort_id) ?? null : null,
		set: (val) =>	betrieb.value.ort_id = (val !== null) ? val.id : null,
	});

	const openModal = () => {
		show.value = true;
	}

	async function importer() {
		await props.addEintrag(betrieb.value)
		betrieb.value = new BetriebStammdaten();
		show.value = false;
	}

</script>
