<template>
	<svws-ui-content-card :title="`Details zu ${name}`" class="mt-16 lg:mt-20">
		<template #actions>
			<svws-ui-checkbox class="mr-3" v-model="anschreiben"> Erhält Anschreiben </svws-ui-checkbox>
			<svws-ui-button type="error">
				<i-ri-delete-bin-line />
				<span>Eintrag löschen</span>
			</svws-ui-button>
		</template>
		<svws-ui-input-wrapper :grid="4">
			<svws-ui-text-input placeholder="Name" v-model="name" type="text" span="2" />
			<svws-ui-text-input placeholder="Namensergänzung" v-model="namezusatz" type="text" />
			<svws-ui-text-input placeholder="Branche" v-model="branche" title="Branche" type="text" />
			<svws-ui-text-input placeholder="Telefon" v-model="telefon1" type="text" />
			<svws-ui-text-input placeholder="2. Telefon" v-model="telefon2" type="text" />
			<svws-ui-text-input placeholder="Fax" v-model="fax" type="text" />
			<svws-ui-text-input placeholder="E-Mail" v-model="email" type="email" verify-email />
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
			<svws-ui-text-input placeholder="Straße und Hausnummer" v-model="strassenname" type="text" />
			<svws-ui-text-input placeholder="Zusatz" v-model="hausnummerzusatz" type="text" />
			<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-filter="orte_filter"
				:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<!-- TODO In der Datenbank gibt es für die Adresse nur Ortsteil_id
			<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="inputKatalogOrtsteil" :item-filter="ortsteilFilter"
				:item-sort="ortsteilSort" :item-text="i => i.ortsteil" />
			-->
			<svws-ui-multi-select title="Ortsteil" disabled :items="[]" />
			<svws-ui-spacing :size="2" />
			<svws-ui-textarea-input v-model="bemerkungen" placeholder="Bemerkungen" autoresize span="full" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { BetriebAnsprechpartner, BetriebStammdaten, LehrerListeEintrag, OrtKatalogEintrag, SchuelerBetriebsdaten } from "@svws-nrw/svws-core";
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

	const name : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.name1 === null ? undefined : props.betriebsStammdaten.name1,
		set: (value) => void props.patchBetrieb({ name1: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

	const namezusatz : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.name2 === null ? undefined : props.betriebsStammdaten.name2,
		set: (value) => void props.patchBetrieb({ name2: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

	const telefon1 : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.telefon1 === null ? undefined : props.betriebsStammdaten.telefon1,
		set: (value) => void props.patchBetrieb({ telefon1: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

	const telefon2 : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.telefon2 === null ? undefined : props.betriebsStammdaten.telefon2,
		set: (value) => void props.patchBetrieb({ telefon2: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

	const fax : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.fax === null ? undefined : props.betriebsStammdaten.fax,
		set: (value) => void props.patchBetrieb({ fax: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

	const email : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.email === null ? undefined : props.betriebsStammdaten.email,
		set: (value) => void props.patchBetrieb({ email: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

	const branche : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.branche === null ? undefined : props.betriebsStammdaten.branche,
		set: (value) => void props.patchBetrieb({ branche: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

	const ansprechpartner: WritableComputedRef<BetriebAnsprechpartner | undefined> = computed({
		get: () => props.betrieb.ansprechpartner_id === null ? undefined : props.mapAnsprechpartner.get(props.betrieb.ansprechpartner_id),
		set: (value) => void props.patchSchuelerBetriebsdaten({ ansprechpartner_id: value === undefined ? null : value.id }, props.betrieb.id)
	});

	const inputBetreuungslehrer: WritableComputedRef<LehrerListeEintrag | undefined> = computed({
		get: () => props.betrieb.betreuungslehrer_id === null ? undefined : props.mapLehrer.get(props.betrieb.betreuungslehrer_id),
		set: (value) => void props.patchSchuelerBetriebsdaten({ betreuungslehrer_id: value === undefined ? null : value.id }, props.betrieb.id)
	});


	/** Adresse */

	const strassenname : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.strassenname === null ? undefined : props.betriebsStammdaten.strassenname,
		set: (value) => void props.patchBetrieb({ strassenname: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

	const hausnummerzusatz : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.hausnrzusatz === null ? undefined : props.betriebsStammdaten.hausnrzusatz,
		set: (value) => void props.patchBetrieb({ hausnrzusatz: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => props.betriebsStammdaten.ort_id === null ? undefined : props.mapOrte.get(props.betriebsStammdaten.ort_id),
		set: (value) => void props.patchBetrieb({ ort_id: value === undefined ? null : value.id }, props.betriebsStammdaten.id)
	});


	const anschreiben: WritableComputedRef<boolean> = computed({
		get: () => props.betrieb.allgadranschreiben === null ? false : props.betrieb.allgadranschreiben,
		set: (value) => void props.patchSchuelerBetriebsdaten({ allgadranschreiben: value }, props.betrieb.id)
	});

	const bemerkungen : WritableComputedRef<string | undefined> = computed({
		get: () => props.betriebsStammdaten.bemerkungen === null ? undefined : props.betriebsStammdaten.bemerkungen,
		set: (value) => void props.patchBetrieb({ bemerkungen: value === undefined ? null : value }, props.betriebsStammdaten.id)
	})

</script>

<style scoped>

	.entry-content {
		@apply grid flex-grow grid-cols-1 gap-4 xl:grid-cols-2;
	}

	h2 {
		@apply mb-2 text-sm font-bold;
	}

</style>
