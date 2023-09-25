<template>
	<svws-ui-content-card title="Basisdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Name" :model-value="daten.name1" @change="name1=>doPatch({name1})" type="text" />
			<svws-ui-text-input placeholder="Namensergänzung" :model-value="daten.name2" @change="name2=>doPatch({name2})" type="text" />
			<svws-ui-select title="Beschäftigungsart" :model-value="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="(i: KatalogEintrag) => i.text ?? ''" />
			<svws-ui-text-input placeholder="Branche" :model-value="daten.branche" @change="branche=>doPatch({branche})" title="Branche" type="text" />
		</svws-ui-input-wrapper>
		<svws-ui-spacing :size="2" v-if="$slots.default" />
		<slot />
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
