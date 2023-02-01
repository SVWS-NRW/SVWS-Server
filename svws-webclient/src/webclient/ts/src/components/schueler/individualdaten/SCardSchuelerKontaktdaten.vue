<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Straße" v-model="inputStrasse" type="text" :valid="eingabeStrasseOk" />
			</div>
			<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="orte" :item-filter="orte_filter"
				:item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="ortsteile" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''"
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

	import { AdressenUtils, OrtKatalogEintrag, OrtsteilKatalogEintrag, SchuelerStammdaten, List } from "@svws-nrw/svws-core-ts";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{
		stammdaten: DataSchuelerStammdaten;
		orte: List<OrtKatalogEintrag>;
		ortsteile: List<OrtsteilKatalogEintrag>;
	}>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());

	const eingabeStrasseOk = ref(true);

	const inputStrasse: WritableComputedRef<string | undefined> = computed({
		get: () => {
			const ret = AdressenUtils.combineStrasse(daten.value.strassenname || "", daten.value.hausnummer || "", daten.value.hausnummerZusatz || "");
			return ret ?? undefined;
		},
		set(value: string | undefined) {
			if (value) {
				const vals = AdressenUtils.splitStrasse(value);
				void props.stammdaten.patch({ strassenname: vals?.[0] || value, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
				eingabeStrasseOk.value = !!vals;
			}
		}
	});
	const inputWohnortID: WritableComputedRef<OrtKatalogEintrag | undefined> = computed({
		get: () => {
			for (const ort of props.orte)
				if (ort.id == daten.value.wohnortID)
					return ort;
			return undefined
		},
		set: (value) => void props.stammdaten.patch({ wohnortID: value?.id })
	});

	const inputOrtsteilID: WritableComputedRef<OrtsteilKatalogEintrag | undefined> = computed({
		get: () => {
			for (const ortsteil of props.ortsteile)
				if (ortsteil.id == daten.value.ortsteilID)
					return ortsteil;
			return undefined
		},
		set: (value) => void props.stammdaten.patch({ ortsteilID: value?.id })
	});

	const inputTelefon: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.telefon ?? undefined,
		set: (value) => void props.stammdaten.patch({ telefon: value })
	});

	const inputTelefonMobil: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.telefonMobil ?? undefined,
		set: (value) => void props.stammdaten.patch({ telefonMobil: value })
	});

	const inputEmailPrivat: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.emailPrivat ?? undefined,
		set: (value) => void props.stammdaten.patch({ emailPrivat: value })
	});

	const inputEmailSchule: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.emailSchule ?? undefined,
		set: (value) => void props.stammdaten.patch({ emailSchule: value })
	});

</script>
