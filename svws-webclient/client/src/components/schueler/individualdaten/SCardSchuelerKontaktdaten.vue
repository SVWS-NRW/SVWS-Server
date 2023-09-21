<template>
	<svws-ui-content-card title="Wohnort und Kontaktdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Straße" :model-value="inputStrasse" @change="patchStrasse" type="text" span="full" />
			<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete statistics />
			<svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="mapOrtsteile" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Telefon" :model-value="data().telefon" @change="telefon=>doPatch({ telefon })" type="tel" />
			<svws-ui-text-input placeholder="Mobil oder Fax" :model-value="data().telefonMobil" @change="telefonMobil=>doPatch({ telefonMobil })" type="tel" />
			<svws-ui-text-input placeholder="Private E-Mail-Adresse" :model-value="data().emailPrivat" @change="emailPrivat=>doPatch({ emailPrivat })" type="email" verify-email />
			<svws-ui-text-input placeholder="Schulische E-Mail-Adresse" :model-value="data().emailSchule" @change="emailSchule=>doPatch({ emailSchule })" type="email" verify-email />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { OrtKatalogEintrag, OrtsteilKatalogEintrag, SchuelerStammdaten } from "@core";
	import { orte_filter, orte_sort, ortsteilFilter, ortsteilSort } from "~/utils/helfer";
	import { AdressenUtils } from "@core";
	import { computed } from "vue";


	const props = defineProps<{
		data: () => SchuelerStammdaten;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

	// const eingabeStrasseOk = ref(true);
	const inputStrasse = computed(()=>
		AdressenUtils.combineStrasse(props.data().strassenname || "", props.data().hausnummer || "", props.data().hausnummerZusatz || ""))

	const patchStrasse = (value: string ) => {
		if (value) {
			const vals = AdressenUtils.splitStrasse(value);
			doPatch({ strassenname: vals?.[0] || value, hausnummer: vals?.[1] || "", hausnummerZusatz: vals?.[2] || "" });
		}
	}

	const inputWohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () => {
			const id = props.data().wohnortID;
			return id === null ? undefined : props.mapOrte.get(id)
		},
		set: (value) => doPatch({ wohnortID: value === undefined ? null : value.id })
	});

	const inputOrtsteilID = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => {
			const id = props.data().ortsteilID;
			return id === null ? undefined : props.mapOrtsteile.get(id)
		},
		set: (value) => doPatch({ ortsteilID: value === undefined ? null : value.id })
	});

</script>
