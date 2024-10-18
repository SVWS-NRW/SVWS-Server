<template>
	<svws-ui-content-card title="Basisdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Name" :model-value="daten.name1" @change="name1=>patch({name1: name1 ?? undefined})" type="text" />
			<svws-ui-text-input placeholder="Namensergänzung" :model-value="daten.name2" @change="name2=>patch({name2: name2 ?? undefined})" type="text" />
			<svws-ui-select title="Beschäftigungsart" :model-value="beschaeftigungsart" :items="mapBeschaeftigungsarten" :item-text="i => i.text ?? ''" />
			<svws-ui-text-input placeholder="Branche" :model-value="daten.branche" @change="branche=>patch({branche: branche ?? undefined})" title="Branche" type="text" />
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
		patch: (data : Partial<BetriebStammdaten>) => Promise<void>;
	}>();

	const beschaeftigungsart = computed<KatalogEintrag | null>({
		get: () => (props.daten.adressArt === null) ? null : props.mapBeschaeftigungsarten.get(props.daten.adressArt) ?? null,
		set: (value) => void props.patch({ adressArt: value?.id})
	})

</script>
