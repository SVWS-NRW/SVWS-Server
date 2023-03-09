<template>
	<svws-ui-content-card>
		<template #actions>
			<svws-ui-button type="error">
				<svws-ui-icon> <i-ri-delete-bin-2-line /> </svws-ui-icon>
				<span class="ml-2">Löschen</span>
			</svws-ui-button>
		</template>
		<div class="input-wrapper">
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2">Basisdaten</h2>
				<div class="entry-content">
					<svws-ui-text-input placeholder="Name" v-model="name" type="text" />
					<svws-ui-text-input placeholder="Namensergänzung" v-model="namezusatz" type="text" />
					<svws-ui-text-input placeholder="1. Telefon-Nr." v-model="telefon1" type="text" />
					<svws-ui-text-input placeholder="2. Telefon-Nr." v-model="telefon2" type="text" />
					<svws-ui-text-input placeholder="Fax-Nr." v-model="fax" type="text" />
					<svws-ui-text-input placeholder="E-Mail Adresse" v-model="email" type="email" verify-email />
					<svws-ui-text-input placeholder="Branche" v-model="branche" title="Branche" type="text" />
					<div class="flex w-full flex-row items-center space-x-4">
						<div class="flex-grow">
							<svws-ui-multi-select v-if="mapAnsprechpartner.size > 0" title="Ansprechpartner" v-model="ansprechpartner"
								:items="mapAnsprechpartner" :item-text="(i: BetriebAnsprechpartner) => i.name ??''" />
							<p v-else>Kein Ansprechpartner</p>
						</div>
						<div class="flex flex-row space-x-4">
							<s-schueler-adresse-modal-ansprechpartner v-if="ansprechpartner !== undefined" :ansprechpartner="ansprechpartner" :patch-ansprechpartner="patchAnsprechpartner" />
							<s-schueler-adresse-modal-ansprechpartner-add :betriebs-stammdaten="betriebsStammdaten" :create-ansprechpartner="createAnsprechpartner" />
						</div>
					</div>
					<svws-ui-multi-select title="betreuende Lehrkraft" v-model="inputBetreuungslehrer" :items="mapLehrer" :item-text="(i:LehrerListeEintrag) => i.nachname" />
				</div>
			</div>
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2">Adresse</h2>
				<div class="entry-content">
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Straße / Hausnummer" v-model="strassenname" type="text" />
					</div>
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Zusatz" v-model="hausnummerzusatz" type="text" />
					</div>
					<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-filter="orte_filter"
						:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
					<!-- TODO In der Datenbank gibt es für die Adresse nur Ortsteil_id
					<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="inputKatalogOrtsteil" :item-filter="ortsteilFilter"
						:item-sort="ortsteilSort" :item-text="i => i.ortsteil" />
					-->
					<svws-ui-checkbox v-model="anschreiben"> erhält Anschreiben </svws-ui-checkbox>
				</div>
			</div>
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2"> Bemerkungen </h2>
				<div class="entry-content">
					<div class="col-span-2">
						<svws-ui-textarea-input v-model="bemerkungen" placeholder="Bemerkungen" />
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { BetriebAnsprechpartner, BetriebStammdaten, LehrerListeEintrag, OrtKatalogEintrag, SchuelerBetriebsdaten } from "@svws-nrw/svws-core";
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

	.input-wrapper {
		@apply grid flex-grow grid-cols-1 gap-4 xl:grid-cols-2;
	}

	.entry-content {
		@apply grid flex-grow grid-cols-1 gap-4 xl:grid-cols-2;
	}

	h2 {
		@apply mb-2 text-sm font-bold;
	}

</style>
