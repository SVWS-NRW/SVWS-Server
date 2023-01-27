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
					<svws-ui-multi-select title="Erzieherart" v-model="idErzieherArt" :items="katalogErzieherarten"
						:item-sort="erzieherArtSort" :item-text="(i: Erzieherart) => i.bezeichnung?.toString() || ''" />
					<svws-ui-checkbox v-model="erhaeltAnschreiben"> erhält Anschreiben </svws-ui-checkbox>
					<svws-ui-text-input placeholder="Name" v-model="nachname" type="text" />
					<svws-ui-text-input placeholder="Zusatz zum Nachnamen" v-model="zusatzNachname" type="text" />
					<svws-ui-text-input placeholder="Vorname" v-model="vorname" type="text" />
					<svws-ui-text-input placeholder="E-Mail Adresse" v-model="email" type="email" verify-email />
					<svws-ui-multi-select title="1. Staatsangehörigkeit" v-model="staatsangehoerigkeit" :items="Nationalitaeten.values()"
						:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit.toString()" :item-sort="staatsangehoerigkeitKatalogEintragSort"
						:item-filter="staatsangehoerigkeitKatalogEintragFilter" />
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
					<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="inputKatalogOrte" :item-filter="orte_filter"
						:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
					<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="inputKatalogOrtsteil"
						:item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil?.toString() || ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" />
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

	import { Erzieherart, ErzieherStammdaten, Nationalitaeten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { DataKatalogErzieherarten } from "~/apps/schueler/DataKatalogErzieherarten";
	import { DataSchuelerErzieherStammdaten } from "~/apps/schueler/DataSchuelerErzieherStammdaten";
	import { erzieherArtSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort,
		orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/helfer";

	const props = defineProps<{ data: DataSchuelerErzieherStammdaten, erzieher: ErzieherStammdaten, erzieherarten: DataKatalogErzieherarten }>();

	const main: Main = injectMainApp();

	const nachname: WritableComputedRef<string> = computed({
		get: () => props.erzieher.nachname?.toString() || "",
		set: (value) => void props.data.patch({ nachname: value }, props.erzieher)
	});

	const zusatzNachname: WritableComputedRef<string> = computed({
		get: () => props.erzieher.zusatzNachname?.toString() || "",
		set: (value) => void props.data.patch({ zusatzNachname: value }, props.erzieher)
	});

	const vorname: WritableComputedRef<string> = computed({
		get: () => props.erzieher.vorname?.toString() || "",
		set: (value) => void props.data.patch({ vorname: value }, props.erzieher)
	});

	const email: WritableComputedRef<string> = computed({
		get: () => props.erzieher.eMail?.toString() || "",
		set: (value) => void props.data.patch({ eMail: value }, props.erzieher)
	});

	const erhaeltAnschreiben: WritableComputedRef<boolean> = computed({
		get: () => (props.erzieher.erhaeltAnschreiben === null) ? true : props.erzieher.erhaeltAnschreiben,
		set: (value) => void props.data.patch({ erhaeltAnschreiben: value }, props.erzieher)
	});

	// TODO Lese Katalog in der Route aus und nutze keinen globalen Katalog
	const inputKatalogOrte: Ref<OrtKatalogEintrag[]> = ref(main.kataloge.orte.toArray() as OrtKatalogEintrag[]);
	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => inputKatalogOrte.value.find(ort => ort.id == props.erzieher.wohnortID),
		set: (value) => void props.data.patch({ wohnortID: value?.id }, props.erzieher)
	});

	// TODO Lese Katalog in der Route aus und nutze keinen globalen Katalog
	const inputKatalogOrtsteil: Ref<OrtsteilKatalogEintrag[]> = ref(main.kataloge.ortsteile.toArray() as OrtsteilKatalogEintrag[]);
	const inputOrtsteilID: WritableComputedRef<OrtsteilKatalogEintrag | undefined> = computed({
		get: () => inputKatalogOrtsteil.value.find(ortsteil => ortsteil.id == props.erzieher.ortsteilID),
		set: (value) => void props.data.patch({ ortsteilID: value?.id }, props.erzieher)
	});

	const staatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(props.erzieher.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => void props.data.patch({ staatsangehoerigkeitID: value.daten.iso3 }, props.erzieher)
	});

	const katalogErzieherarten: ComputedRef<Erzieherart[]> = computed(() => props.erzieherarten.daten?.toArray() as Erzieherart[] || []);
	const idErzieherArt: WritableComputedRef<Erzieherart | undefined> = computed({
		get: () => katalogErzieherarten.value.find(n => n.id === props.erzieher.idErzieherArt),
		set: (value) => void props.data.patch({ idErzieherArt: value?.id }, props.erzieher)
	});

	const strassenname: WritableComputedRef<string> = computed({
		get: () => props.erzieher.strassenname?.toString() || "",
		set: (value) => void props.data.patch({ strassenname: value }, props.erzieher)
	});

	const hausnummerZusatz: WritableComputedRef<string> = computed({
		get: () => props.erzieher.hausnummerZusatz?.toString() || "",
		set: (value) => void props.data.patch({ hausnummerZusatz: value }, props.erzieher)
	});

	const bemerkungen: WritableComputedRef<string> = computed({
		get: () => props.erzieher.bemerkungen?.toString() || "",
		set: (value) => void props.data.patch({ bemerkungen: value }, props.erzieher)
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
