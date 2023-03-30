<template>
	<svws-ui-content-card>
		<template #actions>
			<svws-ui-button type="error">
				<svws-ui-icon><i-ri-delete-bin-2-line /></svws-ui-icon>
				<span class="ml-2"> Löschen </span>
			</svws-ui-button>
			<svws-ui-button type="secondary"> Schüleradresse übernehmen </svws-ui-button>
		</template>
		<div class="input-wrapper">
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2">Basisdaten</h2>
				<div class="entry-content">
					<svws-ui-multi-select title="Erzieherart" v-model="idErzieherArt" :items="mapErzieherarten"
						:item-sort="erzieherArtSort" :item-text="(i: Erzieherart) => i.bezeichnung ?? ''" />
					<svws-ui-checkbox :model-value="erzieher.erhaeltAnschreiben || undefined"
						@update:model-value="doPatch({ erhaeltAnschreiben: Boolean($event) }, erzieher.id)">
						erhält Anschreiben
					</svws-ui-checkbox>
					<svws-ui-text-input placeholder="Name" :model-value="erzieher.nachname || undefined"
						@update:model-value="doPatch({ nachname: String($event) }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="Zusatz zum Nachnamen" :model-value="erzieher.zusatzNachname || undefined"
						@update:model-value="doPatch({ zusatzNachname: String($event) }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="Vorname" :model-value="erzieher.vorname || undefined"
						@update:model-value="doPatch({ vorname: String($event) }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="erzieher.eMail || undefined"
						@update:model-value="doPatch({ eMail: String($event) }, erzieher.id)" type="email" verify-email />
					<svws-ui-multi-select title="1. Staatsangehörigkeit" v-model="staatsangehoerigkeit" :items="Nationalitaeten.values()"
						:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
						:item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
				</div>
			</div>
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2">Adresse</h2>
				<div class="entry-content">
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Straße / Hausnummer" v-model="strassenname" type="text" />
					</div>
					<div class="col-span-2">
						<svws-ui-text-input placeholder="Zusatz" v-model="hausnummerZusatz" type="text" />
					</div>
					<svws-ui-multi-select title="Wohnort" v-model="wohnort" :items="mapOrte" :item-filter="orte_filter"
						:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
					<svws-ui-multi-select title="Ortsteil" v-model="ortsteil" :items="mapOrtsteile"
						:item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" />
				</div>
			</div>
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2">Bemerkungen</h2>
				<div class="entry-content">
					<div class="col-span-2">
						<svws-ui-textarea-input placeholder="Bemerkungen" v-model:value="bemerkungen" />
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { Erzieherart, ErzieherStammdaten, Nationalitaeten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { erzieherArtSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort,
		orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/helfer";

	const props = defineProps<{
		erzieher: ErzieherStammdaten;
		mapErzieherarten: Map<number, Erzieherart>;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<ErzieherStammdaten>, id: number) : void;
	}>()

	function doPatch(data: Partial<ErzieherStammdaten>, id: number) {
		emit('patch', data, id);
	}

	const wohnort: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => props.erzieher.wohnortID === null ? undefined : props.mapOrte.get(props.erzieher.wohnortID),
		set: (value) => void doPatch({ wohnortID: value === undefined ? null : value.id }, props.erzieher.id)
	});

	const ortsteil: WritableComputedRef<OrtsteilKatalogEintrag | undefined> = computed({
		get: () => props.erzieher.ortsteilID === null ? undefined : props.mapOrtsteile.get(props.erzieher.ortsteilID),
		set: (value) => void doPatch({ ortsteilID: value === undefined ? null : value.id }, props.erzieher.id)
	});

	const staatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.erzieher.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => void doPatch({ staatsangehoerigkeitID: value.daten.iso3 }, props.erzieher.id)
	});

	const idErzieherArt: WritableComputedRef<Erzieherart | undefined> = computed({
		get: () => props.erzieher.idErzieherArt === null ? undefined : props.mapErzieherarten.get(props.erzieher.idErzieherArt),
		set: (value) => void doPatch({ idErzieherArt: value === undefined ? null : value.id }, props.erzieher.id)
	});

	const strassenname: WritableComputedRef<string> = computed({
		get: () => props.erzieher.strassenname ?? "",
		set: (value) => void doPatch({ strassenname: value }, props.erzieher.id)
	});

	const hausnummerZusatz: WritableComputedRef<string> = computed({
		get: () => props.erzieher.hausnummerZusatz ?? "",
		set: (value) => void doPatch({ hausnummerZusatz: value }, props.erzieher.id)
	});

	const bemerkungen: WritableComputedRef<string> = computed({
		get: () => props.erzieher.bemerkungen ?? "",
		set: (value) => void doPatch({ bemerkungen: value }, props.erzieher.id)
	});

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
