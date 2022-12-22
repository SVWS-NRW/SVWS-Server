<template>
	<svws-ui-content-card>
		<template #actions>
			<svws-ui-button type="danger">
				<svws-ui-icon><i-ri-delete-bin-2-line /></svws-ui-icon>
				<span class="ml-2"> Löschen </span>
			</svws-ui-button>
			<svws-ui-button type="secondary"> Schüleradresse übernehmen </svws-ui-button>
		</template>
		<div class="input-wrapper">
			<div class="entry-wrapper">
				<h2 class="svws-ui-text-black col-span-2">Basisdaten</h2>
				<div class="entry-content">
					<svws-ui-multi-select title="Erzieherart" v-model="idErzieherArt" :items="inputKatalogErzieherarten"
						:item-sort="erzieherArtSort" :item-text="(i: Erzieherart) => i.bezeichnung" />
					<svws-ui-checkbox v-model="erhaeltAnschreiben"> erhält Anschreiben </svws-ui-checkbox>
					<svws-ui-text-input placeholder="Name" v-model="nachname" type="text" />
					<svws-ui-text-input placeholder="Zusatz zum Nachnamen" v-model="zusatzNachname" type="text" />
					<svws-ui-text-input placeholder="Vorname" v-model="vorname" type="text" />
					<svws-ui-text-input placeholder="E-Mail Adresse" v-model="email" type="email" verify-email />
					<svws-ui-multi-select title="1. Staatsangehörigkeit" v-model="staatsangehoerigkeit" :items="inputKatalogStaatsangehoerigkeit"
						:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
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
						:item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" />
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

	import { Erzieherart, ErzieherStammdaten, List, Nationalitaeten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref, Ref, WritableComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { erzieherArtSort, staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, orte_filter, orte_sort,
		ortsteilFilter, ortsteilSort } from "~/helfer";

	const props = defineProps({
		erzieher: {
			type: Object as () => ErzieherStammdaten,
			default: () => new ErzieherStammdaten()
		}
	});

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const nachname: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return props.erzieher?.nachname !== null ? String(props.erzieher?.nachname) : "";
		},
		set(val: string | undefined) {
			app.erzieher?.patch({ nachname: val }, props.erzieher);
		}
	});

	const zusatzNachname: WritableComputedRef<string | null> = computed({
		get(): string | null {
			if (props.erzieher?.zusatzNachname !== null) {
				return props.erzieher?.zusatzNachname !== null ? String(props.erzieher?.zusatzNachname) : "";
			}
			return null;
		},
		set(val: string | null) {
			app.erzieher?.patch({ zusatzNachname: val }, props.erzieher);
		}
	});

	const vorname: WritableComputedRef<string | null> = computed({
		get(): string | null {
			if (props.erzieher?.vorname !== null) {
				return String(props.erzieher?.vorname);
			}
			return null;
		},
		set(val: string | null) {
			app.erzieher?.patch({ vorname: val }, props.erzieher);
		}
	});

	const email: WritableComputedRef<string | null> = computed({
		get(): string | null {
			if (props.erzieher?.eMail !== null) {
				return String(props.erzieher?.eMail);
			}
			return null;
		},
		set(val: string | null) {
			app.erzieher?.patch({ eMail: val }, props.erzieher);
		}
	});

	const erhaeltAnschreiben: WritableComputedRef<boolean | null> = computed({
		get(): boolean | null {
			if (props.erzieher?.erhaeltAnschreiben !== undefined) {
				return Boolean(props.erzieher?.erhaeltAnschreiben);
			}
			return null;
		},
		set(val: boolean | null) {
			app.erzieher?.patch({ erhaeltAnschreiben: val }, props.erzieher);
		}
	});

	const inputKatalogStaatsangehoerigkeit: ComputedRef<Nationalitaeten[]> = computed(() =>
		Nationalitaeten.values()
	);

	const inputKatalogErzieherarten: Ref<Erzieherart[]> = ref(
		//TODO fix erzieherarten
		[]
		//main.kataloge.erzieherarten || []
	);

	const inputKatalogOrte: Ref<List<OrtKatalogEintrag>> = ref(main.kataloge.orte);

	const inputKatalogOrtsteil: Ref<List<OrtsteilKatalogEintrag>> = ref(
		main.kataloge.ortsteile
	);

	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get(): OrtKatalogEintrag | undefined {
			// TODO:TEST testen
			const id = props.erzieher.wohnortID;
			let o;
			for (const r of inputKatalogOrte.value) { 
				if (r.id === id) { 
					o = r; 
					break;
				}
			}
			return o;
		},
		set(val: OrtKatalogEintrag | undefined) {
			app.erzieher?.patch({ wohnortID: val?.id }, props.erzieher);
		}
	});

	const inputOrtsteilID: WritableComputedRef<OrtsteilKatalogEintrag | undefined> = computed({
		get(): OrtsteilKatalogEintrag | undefined {
			// TODO:TEST testen
			const id = props.erzieher.ortsteilID;
			let o;
			for (const r of inputKatalogOrtsteil.value) { 
				if (r.id === id) {
					o = r;
					break;
				}
			}
			return o;
		},
		set(val: OrtsteilKatalogEintrag | undefined) {
			app.erzieher?.patch({ ortsteilID: val?.id }, props.erzieher);
		}
	});

	const staatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get(): Nationalitaeten {
			return Nationalitaeten.getByISO3(props.erzieher.staatsangehoerigkeitID) || Nationalitaeten.DEU;
		},
		set(val: Nationalitaeten) {
			app.stammdaten.patch({ staatsangehoerigkeitID: val.daten.iso3 });
		}
	});

	const idErzieherArt: WritableComputedRef<Erzieherart | undefined> = computed({
		get(): Erzieherart | undefined {
			const id = props.erzieher.idErzieherArt;
			return inputKatalogErzieherarten.value.find(n => n.id === id);
		},
		set(val: Erzieherart | undefined) {
			app.erzieher?.patch({ idErzieherArt: val?.id }, props.erzieher);
		}
	});

	const strassenname: WritableComputedRef<string | null> = computed({
		get(): string | null {
			if (props.erzieher?.strassenname !== null) {
				return String(props.erzieher?.strassenname);
			}
			return null;
		},
		set(val: string | null) {
			app.erzieher?.patch({ strassenname: val }, props.erzieher);
		}
	});

	const hausnummerZusatz: WritableComputedRef<string | null> = computed({
		get(): string | null {
			if (props.erzieher?.hausnummerZusatz !== null) {
				return String(props.erzieher?.hausnummerZusatz);
			}
			return null;
		},
		set(val: string | null) {
			app.erzieher?.patch({ hausnummerZusatz: val }, props.erzieher);
		}
	});

	const bemerkungen: WritableComputedRef<string | null> = computed({
		get(): string | null {
			if (props.erzieher?.bemerkungen !== null) {
				return String(props.erzieher?.bemerkungen);
			}
			return null;
		},
		set(val: string | null) {
			app.erzieher?.patch({ bemerkungen: val }, props.erzieher);
		}
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
