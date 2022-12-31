<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Straße" v-model="inputStrasse" type="text" :valid="eingabeStrasseOk" />
			</div>
			<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="inputKatalogOrte" :item-filter="orte_filter"
				:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="inputKatalogOrtsteil" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil"
				:item-sort="ortsteilSort" :item-filter="ortsteilFilter" />
			<svws-ui-text-input placeholder="Telefon" v-model="inputTelefon" type="tel" />
			<svws-ui-text-input placeholder="Mobil oder Fax" v-model="inputTelefonMobil" type="tel" />
			<svws-ui-text-input placeholder="private E-Mail-Adresse" v-model="inputEmailPrivat" type="email" verify-email />
			<svws-ui-text-input placeholder="schulische E-Mail-Adresse" v-model="inputEmailSchule" type="email" verify-email />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ref, WritableComputedRef } from "vue";
	import { orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/helfer";

	import { AdressenUtils, OrtKatalogEintrag, OrtsteilKatalogEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());

	const main: Main = injectMainApp();

	const eingabeStrasseOk = ref(true);

	const inputStrasse: WritableComputedRef<string | undefined> = computed({
		get: () => {
			const ret = AdressenUtils.combineStrasse(daten.value.strassenname || "", daten.value.hausnummer || "", daten.value.hausnummerZusatz || "");
			return ret ? ret.toString() : undefined;
		},
		set(value: string | undefined) {
			if (value) {
				const vals = AdressenUtils.splitStrasse(value);
				props.stammdaten.patch({ strassenname: vals?.[0] || value, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
				eingabeStrasseOk.value = !!vals;
			}
		}
	});

	const inputKatalogOrte: ComputedRef<OrtKatalogEintrag[]> = computed(() => main.kataloge.orte.toArray() as OrtKatalogEintrag[]);
	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => inputKatalogOrte.value.find(ort => ort.id == daten.value.wohnortID),
		set: (value) => props.stammdaten.patch({ wohnortID: value?.id })
	});

	const inputKatalogOrtsteil: ComputedRef<OrtsteilKatalogEintrag[]> = computed(() => main.kataloge.ortsteile.toArray() as OrtsteilKatalogEintrag[]);
	const inputOrtsteilID: WritableComputedRef<OrtsteilKatalogEintrag | undefined> = computed({
		get: () => inputKatalogOrtsteil.value.find(ortsteil => ortsteil.id == daten.value.ortsteilID),
		set: (value) => props.stammdaten.patch({ ortsteilID: value?.id })
	});

	const inputTelefon: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.telefon?.toString(),
		set: (value) => props.stammdaten.patch({ telefon: value })
	});

	const inputTelefonMobil: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.telefonMobil?.toString(),
		set: (value) => props.stammdaten.patch({ telefonMobil: value })
	});

	const inputEmailPrivat: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.emailPrivat?.toString(),
		set: (value) => props.stammdaten.patch({ emailPrivat: value })
	});

	const inputEmailSchule: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.emailSchule?.toString(),
		set: (value) => props.stammdaten.patch({ emailSchule: value })
	});

</script>
