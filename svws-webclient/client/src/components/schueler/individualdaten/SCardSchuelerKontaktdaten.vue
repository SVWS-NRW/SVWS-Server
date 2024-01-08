<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Straße" :model-value="strasse" @change="patchStrasse" type="text" span="full" />
			<svws-ui-select title="Wohnort" v-model="wohnortID" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete statistics />
			<svws-ui-select title="Ortsteil" v-model="ortsteilID" :items="ortsteile" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" removable />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Telefon" :model-value="data.telefon" @change="telefon => patch({ telefon })" type="tel" />
			<svws-ui-text-input placeholder="Mobil oder Fax" :model-value="data.telefonMobil" @change="telefonMobil => patch({ telefonMobil })" type="tel" />
			<svws-ui-text-input placeholder="Private E-Mail-Adresse" :model-value="data.emailPrivat" @change="emailPrivat => patch({ emailPrivat })" type="email" verify-email />
			<svws-ui-text-input placeholder="Schulische E-Mail-Adresse" :model-value="data.emailSchule" @change="emailSchule => patch({ emailSchule })" type="email" verify-email />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { OrtKatalogEintrag, OrtsteilKatalogEintrag, SchuelerListeManager, SchuelerStammdaten } from "@core";
	import { orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/utils/helfer";
	import { AdressenUtils } from "@core";
	import { computed } from "vue";


	const props = defineProps<{
		schuelerListeManager: () => SchuelerListeManager;
		patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	const data = computed<SchuelerStammdaten>(() => props.schuelerListeManager().daten());

	const strasse = computed(() => AdressenUtils.combineStrasse(data.value.strassenname || "", data.value.hausnummer || "", data.value.hausnummerZusatz || ""))

	const patchStrasse = (value: string ) => {
		if (value) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals?.[0] || value, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
		}
	}

	const wohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.wohnortID;
			return id === null ? undefined : props.mapOrte.get(id)
		},
		set: (value) => void props.patch({ wohnortID: value === undefined ? null : value.id })
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if ((ortsteil.ort_id === null) || (ortsteil.ort_id === data.value.wohnortID))
				result.push(ortsteil);
		return result;
	});

	const ortsteilID = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.ortsteilID;
			return id === null ? undefined : props.mapOrtsteile.get(id)
		},
		set: (value) => void props.patch({ ortsteilID: value === undefined ? null : value.id })
	});

</script>
