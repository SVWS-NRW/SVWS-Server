<template>
	<svws-ui-content-card title="Adresse">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Straße / Hausnummer" :model-value="daten.strassenname" @blur="strassenname=>doPatch({strassenname})" type="text" />
			</div>
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Zusatz" :model-value="daten.hausnrzusatz" @blur="hausnrzusatz=>doPatch({hausnrzusatz})" type="text" />
			</div>
			<svws-ui-multi-select title="Wohnort" v-model="inputWohnortID" :items="mapOrte" :item-text="(i: OrtKatalogEintrag) => `${i.plz} ${i.ortsname}`" autocomplete />
			<!-- <svws-ui-multi-select title="Ortsteil" v-model="inputOrtsteilID" :items="mapOrtsteile" :item-text="(i: OrtsteilKatalogEintrag) => i.ortsteil ?? ''"
            :item-sort="ortsteilSort" :item-filter="ortsteilFilter" /> -->

			<svws-ui-text-input placeholder="1. Telefon-Nr." :model-value="daten.telefon1" @blur="telefon1=>doPatch({telefon1})" type="text" />
			<svws-ui-text-input placeholder="2. Telefon-Nr." :model-value="daten.telefon2" @blur="telefon2=>doPatch({telefon2})" type="text" />
			<svws-ui-text-input placeholder="Fax-Nr." :model-value="daten.fax" @blur="fax=>doPatch({fax})" type="text" />
			<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="daten.email" @blur="email=>doPatch({email})" type="email" verify-email />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { BetriebStammdaten, OrtKatalogEintrag } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		daten: BetriebStammdaten;
		mapOrte: Map<number, OrtKatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<BetriebStammdaten>): void;
	}>()

	function doPatch(data: Partial<BetriebStammdaten>) {
		emit('patch', data);
	}

	const inputWohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () =>props.daten.ort_id ? props.mapOrte.get(props.daten.ort_id) : undefined,
		set: (val) =>	doPatch({ ort_id : val?.id })
	});
</script>
