<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="input-wrapper">
			<svws-ui-text-input placeholder="Name" :model-value="daten.name1" @blur="name1=>doPatch({name1})" type="text" />
			<svws-ui-multi-select title="Beschäftigungsart" :model-value="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text ?? ''" />
			<svws-ui-text-input placeholder="Namensergänzung" :model-value="daten.name2" @blur="name2=>doPatch({name2})" type="text" />
			<svws-ui-text-input placeholder="Branche" :model-value="daten.branche" @blur="branche=>doPatch({branche})" title="Branche" type="text" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { BetriebStammdaten, KatalogEintrag } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		daten: BetriebStammdaten;
		mapBeschaeftigungsarten: Map<number, KatalogEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<BetriebStammdaten>): void;
	}>()

	function doPatch(data: Partial<BetriebStammdaten>) {
		emit('patch', data);
	}

	const beschaeftigungsart = computed<KatalogEintrag | undefined>({
		get: () => (props.daten.adressArt === null) ? undefined : props.mapBeschaeftigungsarten.get(props.daten.adressArt),
		set: (value) => doPatch({ adressArt: value?.id})
	})

</script>