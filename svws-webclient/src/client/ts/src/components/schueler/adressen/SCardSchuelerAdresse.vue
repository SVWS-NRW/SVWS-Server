<template>
	<svws-ui-content-card :title="`Details zu ${betriebsStammdaten.name1}`" class="mt-16 lg:mt-20">
		<template #actions>
			<svws-ui-checkbox class="mr-3" v-model="anschreiben"> Erhält Anschreiben </svws-ui-checkbox>
			<svws-ui-button type="error">
				<i-ri-delete-bin-line />
				<span>Eintrag löschen</span>
			</svws-ui-button>
		</template>
		<svws-ui-input-wrapper :grid="4">
			<svws-ui-text-input placeholder="Name" :model-value="betriebsStammdaten.name1" @change="name1=>patchBetrieb({name1}, props.betriebsStammdaten.id)" type="text" span="2" />
			<svws-ui-text-input placeholder="Namensergänzung" :model-value="betriebsStammdaten.name2" @change="name2=>patchBetrieb({name2}, props.betriebsStammdaten.id)" type="text" />
			<svws-ui-text-input placeholder="Branche" :model-value="betriebsStammdaten.branche" @change="branche=>patchBetrieb({branche}, props.betriebsStammdaten.id)" title="Branche" type="text" />
			<svws-ui-text-input placeholder="Telefon" :model-value="betriebsStammdaten.telefon1" @change="telefon1=>patchBetrieb({telefon1}, props.betriebsStammdaten.id)" type="text" />
			<svws-ui-text-input placeholder="2. Telefon" :model-value="betriebsStammdaten.telefon2" @change="telefon2=>patchBetrieb({telefon2}, props.betriebsStammdaten.id)" type="text" />
			<svws-ui-text-input placeholder="Fax" :model-value="betriebsStammdaten.fax" @change="fax=>patchBetrieb({fax}, props.betriebsStammdaten.id)" type="text" />
			<svws-ui-text-input placeholder="E-Mail" :model-value="betriebsStammdaten.email" @change="email=>patchBetrieb({email}, props.betriebsStammdaten.id)" type="email" verify-email />
			<svws-ui-spacing />
			<svws-ui-multi-select title="Betreuende Lehrkraft" v-model="inputBetreuungslehrer" :items="mapLehrer" :item-text="(i:LehrerListeEintrag) => i.nachname" />
			<div class="flex gap-1 h-min items-center">
				<div class="flex-grow">
					<svws-ui-multi-select v-if="mapAnsprechpartner.size > 0" title="Ansprechpartner" v-model="ansprechpartner"
						:items="mapAnsprechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name ??''" />
					<span v-else class="opacity-50">Kein Ansprechpartner</span>
				</div>
				<s-schueler-adresse-modal-ansprechpartner v-if="ansprechpartner !== undefined" :ansprechpartner="ansprechpartner" :patch-ansprechpartner="patchAnsprechpartner" />
				<s-schueler-adresse-modal-ansprechpartner-add :betriebs-stammdaten="betriebsStammdaten" :create-ansprechpartner="createAnsprechpartner" />
			</div>
			<svws-ui-spacing :size="2" />
			<svws-ui-text-input placeholder="Straße und Hausnummer" :model-value="betriebsStammdaten.strassenname" @change="strassenname=>patchBetrieb({strassenname}, props.betriebsStammdaten.id)" type="text" />
			<svws-ui-text-input placeholder="Zusatz" :model-value="betriebsStammdaten.hausnrzusatz" @change="hausnrzusatz=>patchBetrieb({hausnrzusatz}, props.betriebsStammdaten.id)" type="text" />
			<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-filter="orte_filter"
				:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<!-- TODO In der Datenbank gibt es für die Adresse nur Ortsteil_id
			<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="inputKatalogOrtsteil" :item-filter="ortsteilFilter"
				:item-sort="ortsteilSort" :item-text="i => i.ortsteil" />
			-->
			<!-- <svws-ui-multi-select title="Ortsteil" disabled :items="[]" :item-text="i=>i" /> -->
			<svws-ui-spacing :size="2" />
			<svws-ui-textarea-input :model-value="betriebsStammdaten.bemerkungen" placeholder="Bemerkungen" autoresize span="full"
				@change="bemerkungen => patchBetrieb({ bemerkungen: bemerkungen === null ? '' : bemerkungen }, betriebsStammdaten.id)" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { BetriebAnsprechpartner, BetriebStammdaten, LehrerListeEintrag, OrtKatalogEintrag, SchuelerBetriebsdaten } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { orte_filter, orte_sort } from "~/helfer";

	const props = defineProps<{
		patchBetrieb: (data : Partial<BetriebStammdaten>, id : number) => Promise<void>;
		patchSchuelerBetriebsdaten: (data : Partial<SchuelerBetriebsdaten>, id : number) => Promise<void>;
		patchAnsprechpartner: (data : Partial<BetriebAnsprechpartner>, id : number) => Promise<void>;
		createAnsprechpartner: (data: BetriebAnsprechpartner) => Promise<void>;
		betrieb: SchuelerBetriebsdaten;
		betriebsStammdaten: BetriebStammdaten;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapAnsprechpartner: Map<number, BetriebAnsprechpartner>;
		mapOrte: Map<number, OrtKatalogEintrag>;
	}>();


	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get: () => props.betrieb.ansprechpartner_id === null ? undefined : props.mapAnsprechpartner.get(props.betrieb.ansprechpartner_id),
		set: (value) => void props.patchSchuelerBetriebsdaten({ ansprechpartner_id: value === undefined ? null : value.id }, props.betrieb.id)
	});

	const inputBetreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => props.betrieb.betreuungslehrer_id === null ? undefined : props.mapLehrer.get(props.betrieb.betreuungslehrer_id),
		set: (value) => void props.patchSchuelerBetriebsdaten({ betreuungslehrer_id: value === undefined ? null : value.id }, props.betrieb.id)
	});

	/** Adresse */

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => props.betriebsStammdaten.ort_id === null ? undefined : props.mapOrte.get(props.betriebsStammdaten.ort_id),
		set: (value) => void props.patchBetrieb({ ort_id: value === undefined ? null : value.id }, props.betriebsStammdaten.id)
	});


	const anschreiben: WritableComputedRef<boolean> = computed({
		get: () => props.betrieb.allgadranschreiben === null ? false : props.betrieb.allgadranschreiben,
		set: (value) => void props.patchSchuelerBetriebsdaten({ allgadranschreiben: value }, props.betrieb.id)
	});

</script>

<style scoped>

	.entry-content {
		@apply grid flex-grow grid-cols-1 gap-4 xl:grid-cols-2;
	}

	h2 {
		@apply mb-2 text-sm font-bold;
	}

</style>
