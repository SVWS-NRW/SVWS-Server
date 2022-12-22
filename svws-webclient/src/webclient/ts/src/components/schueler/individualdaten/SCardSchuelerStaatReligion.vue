<template>
	<svws-ui-content-card title="Staatsangehörigkeit und Konfession">
		<div class="input-wrapper">
			<svws-ui-multi-select v-model="inputStaatsangehoerigkeit" title="1. Staatsangehörigkeit"
				:items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter"
				required />
			<svws-ui-multi-select v-model="inputStaatsangehoerigkeit2" title="2. Staatsangehörigkeit"
				:items="Nationalitaeten.values()" :item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit"
				:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" />
			<svws-ui-multi-select v-model="inputReligionID" title="Konfession" :items="inputKatalogReligionen" required />
			<svws-ui-checkbox v-model="inputDruckeKonfessionAufZeugnisse">Konfession aufs Zeugnis</svws-ui-checkbox>
			<svws-ui-text-input v-model="inputReligionabmeldung" type="date"
				placeholder="Abmeldung vom Religionsunterricht" />
			<svws-ui-text-input v-model="inputReligionanmeldung" type="date" placeholder="Wiederanmeldung" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	import { List, Nationalitaeten, ReligionEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "../../../helfer";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());


	const main: Main = injectMainApp();

	const inputKatalogReligionen: ComputedRef<List<ReligionEintrag>> = computed(() => {
		return main.kataloge.religionen;
	});

	const inputStaatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get(): Nationalitaeten {
			return Nationalitaeten.getByISO3(daten.value.staatsangehoerigkeitID) || Nationalitaeten.DEU;
		},
		set(val: Nationalitaeten) {
			props.stammdaten.patch({ staatsangehoerigkeitID: val.daten.iso3 });
		}
	});

	const inputStaatsangehoerigkeit2: WritableComputedRef<Nationalitaeten> = computed({
		get(): Nationalitaeten {
			return Nationalitaeten.getByISO3(daten.value.staatsangehoerigkeit2ID) || Nationalitaeten.DEU;
		},
		set(val: Nationalitaeten) {
			props.stammdaten.patch({ staatsangehoerigkeit2ID: val.daten.iso3 });
		}
	});

	const inputReligionID: WritableComputedRef<ReligionEintrag | undefined> = computed({
		get(): ReligionEintrag | undefined {
			const id = daten.value.religionID;
			let o;
			for (const r of inputKatalogReligionen.value) { 
				if (r.id === id) { 
					o = r;
					break;
				}
			}
			return o;
		},
		set(val) {
			props.stammdaten.patch({ religionID: val?.id });
		}
	});

	const inputReligionabmeldung: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.religionabmeldung?.toString();
		},
		set(val) {
			props.stammdaten.patch({ religionabmeldung: val });
		}
	});

	const inputReligionanmeldung: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.religionanmeldung?.toString();
		},
		set(val) {
			props.stammdaten.patch({ religionanmeldung: val });
		}
	});

	const inputDruckeKonfessionAufZeugnisse: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return daten.value.druckeKonfessionAufZeugnisse;
		},
		set(val) {
			props.stammdaten.patch({ druckeKonfessionAufZeugnisse: val });
		}
	});

</script>
