<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Straße" :model-value="inputStrasse" @change="patchStrasse" type="text" span="full" />
			<svws-ui-select v-model="inputWohnortID" title="Wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<svws-ui-select v-model="inputOrtsteilID" title="Ortsteil" :items="mapOrtsteile" :item-sort="ortsteilSort" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" />
			<svws-ui-spacing />
			<svws-ui-text-input :model-value="data.telefon" @change="telefon=>doPatch({telefon})" type="tel" placeholder="Telefon" />
			<svws-ui-text-input :model-value="data.telefonMobil" @change="telefonMobil=>doPatch({telefonMobil})" type="tel" placeholder="Mobil oder Fax" />
			<svws-ui-text-input :model-value="data.emailPrivat" @change="emailPrivat=>doPatch({emailPrivat})" type="email" placeholder="Private E-Mail-Adresse" verify-email />
			<svws-ui-text-input :model-value="data.emailDienstlich" @change="emailDienstlich=>doPatch({emailDienstlich})" type="email" placeholder="Schulische E-Mail-Adresse" verify-email />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { orte_filter, orte_sort, ortsteilSort } from "~/utils/helfer";
	import { AdressenUtils } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		data: LehrerStammdaten;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<LehrerStammdaten>): void;
	}>()

	function doPatch(data: Partial<LehrerStammdaten>) {
		emit('patch', data);
	}
	const inputStrasse = computed(()=>
		AdressenUtils.combineStrasse(props.data.strassenname || "", props.data.hausnummer || "", props.data.hausnummerZusatz || ""))

	const patchStrasse = (value: string ) => {
		if (value) {
			const vals = AdressenUtils.splitStrasse(value);
			doPatch({ strassenname: vals?.[0] || value, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
		}
	}

	const inputWohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () =>props.data.wohnortID ? props.mapOrte.get(props.data.wohnortID) : undefined,
		set: (val) =>	doPatch({ wohnortID: val?.id })
	});

	const inputOrtsteilID = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => props.data.ortsteilID ? props.mapOrtsteile.get(props.data.ortsteilID) : undefined,
		set: (val) =>	doPatch({ ortsteilID: val?.id })
	});

</script>
