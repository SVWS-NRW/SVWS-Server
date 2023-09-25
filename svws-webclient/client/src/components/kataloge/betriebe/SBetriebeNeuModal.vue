<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big" class="hidden">
		<template #modalTitle>Schülerbetrieb hinzufügen</template>
		<template #modalDescription />
		<template #modalContent>
			<svws-ui-content-card title="Basisdaten">
				<div class="input-wrapper">
					<svws-ui-text-input placeholder="Name" v-model="betrieb.name1" type="text" />
					<svws-ui-select title="Beschäftigungsart" v-model="inputBeschaeftigungsarten" :items="mapBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text ?? ''" />
					<svws-ui-text-input placeholder="Namensergänzung" v-model="betrieb.name2" type="text" />
					<svws-ui-text-input placeholder="1. Telefon-Nr." v-model="betrieb.telefon1" type="text" />
					<svws-ui-text-input placeholder="2. Telefon-Nr." v-model="betrieb.telefon2" type="text" />
					<svws-ui-text-input placeholder="Fax-Nr." v-model="betrieb.fax" type="text" />
					<svws-ui-text-input placeholder="E-Mail Adresse" v-model="betrieb.email" type="email" verify-email />
					<svws-ui-text-input placeholder="Branche" v-model="betrieb.branche" title="Branche" type="text" />
				</div>
			</svws-ui-content-card>
			<svws-ui-content-card title="Adresse">
				<div class="input-wrapper">
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Straße / Hausnummer" v-model="betrieb.strassenname" type="text" />
					</div>
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Zusatz" v-model="betrieb.hausnrzusatz" type="text" />
					</div>
					<svws-ui-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-filter="orte_filter"
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
						<svws-ui-textarea-input v-model="betrieb.bemerkungen" placeholder="Bemerkungen" />
					</div>
					<svws-ui-checkbox v-model="betrieb.ausbildungsbetrieb"> Ausbildungsbetrieb </svws-ui-checkbox>
					<svws-ui-checkbox v-model="betrieb.bietetPraktika"> Bietet Praktumsplätze </svws-ui-checkbox>
					<svws-ui-checkbox v-model="betrieb.Massnahmentraeger"> Maßnahmenträger </svws-ui-checkbox>
					<svws-ui-checkbox v-model="betrieb.ErwFuehrungszeugnis"> Erweitertes Führungszeugnis notwendig </svws-ui-checkbox>
					<svws-ui-checkbox v-model="betrieb.BelehrungISG"> Belehrung n. Infektionsschutzgesetz notwendig </svws-ui-checkbox>
				</div>
			</svws-ui-content-card>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="importer()" :disabled="!betrieb.name1"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { BetriebStammdaten, type KatalogEintrag, type OrtKatalogEintrag, type OrtsteilKatalogEintrag } from "@core";
	import { computed, ref, type ComputedRef, type WritableComputedRef } from "vue";
	import { orte_filter, orte_sort } from "~/utils/helfer";

	const props = defineProps<{
		addEintrag: (betrieb: BetriebStammdaten) => Promise<void>;
		deleteEintraege: (betrieb: BetriebStammdaten) => Promise<void>;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const betrieb = ref<BetriebStammdaten>(new BetriebStammdaten());

	const inputBeschaeftigungsarten: ComputedRef<Array<KatalogEintrag>> = computed(()=>
		[...props.mapBeschaeftigungsarten.values()]
	);

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => betrieb.value.ort_id ? props.mapOrte.get(betrieb.value.ort_id) : undefined,
		set: (val) =>	betrieb.value.ort_id = val?.id.valueOf() ? val.id : null
	});

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addEintrag(betrieb.value)
		betrieb.value = new BetriebStammdaten();
		showModal().value = false;
	}

</script>
