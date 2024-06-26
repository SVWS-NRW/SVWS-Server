<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Straße" :model-value="strasse" @change="patchStrasse" type="text" span="full" />
			<svws-ui-select v-model="wohnortID" title="Wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-select v-model="ortsteilID" title="Ortsteil" :items="ortsteile" :item-sort="ortsteilSort" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" removable />
			<svws-ui-spacing />
			<svws-ui-text-input :model-value="data.telefon" @change="telefon => patch({telefon})" type="tel" placeholder="Telefon" />
			<svws-ui-text-input :model-value="data.telefonMobil" @change="telefonMobil => patch({telefonMobil})" type="tel" placeholder="Mobil oder Fax" />
			<svws-ui-text-input :model-value="data.emailPrivat" @change="emailPrivat => patch({emailPrivat})" type="email" placeholder="Private E-Mail-Adresse" verify-email />
			<svws-ui-text-input :model-value="data.emailDienstlich" @change="emailDienstlich => patch({emailDienstlich})" type="email" placeholder="Schulische E-Mail-Adresse" verify-email />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerListeManager, LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { orte_filter, orte_sort, ortsteilSort } from "~/utils/helfer";
	import { AdressenUtils } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
		patch: (data : Partial<LehrerStammdaten>) => Promise<void>;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	const data = computed<LehrerStammdaten>(() => props.lehrerListeManager().daten());

	const strasse = computed(() => AdressenUtils.combineStrasse(data.value.strassenname || "", data.value.hausnummer || "", data.value.hausnummerZusatz || ""))

	const patchStrasse = (value: string ) => {
		if (value) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals?.[0] || value, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
		}
	}

	const wohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () =>data.value.wohnortID ? props.mapOrte.get(data.value.wohnortID) : undefined,
		set: (val) => void props.patch({ wohnortID: val?.id })
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if ((ortsteil.ort_id === null) || (ortsteil.ort_id === data.value.wohnortID))
				result.push(ortsteil);
		return result;
	});

	const ortsteilID = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => data.value.ortsteilID ? props.mapOrtsteile.get(data.value.ortsteilID) : undefined,
		set: (val) => void props.patch({ ortsteilID: val?.id ?? null })
	});

</script>
