<template>
	<svws-ui-table clickable :clicked="halbjahr" @update:clicked="gotoHalbjahr" :columns="[{ key: 'kuerzel', label: 'Halbjahr' }]"
		:items="GostHalbjahr.values()" />
	<Teleport to=".svws-sub-nav-target" defer>
		<nav class="svws-ui-secondary-tabs">
			<svws-ui-tab-bar :tab-manager secondary>
				<template #badge="{ tab }">
					<template v-if="(tab.name === 'gost_klausurplanung_probleme') && kMan().hasFehlenddatenZuAbijahrUndHalbjahr(props.jahrgangsdaten!.abiturjahr, halbjahr)">
						<div class="font-bold text-white bg-error rounded-full shadow h-5 ml-1 -mt-3 px-1.5 pt-0.5" v-if="numErrors">{{ numErrors }}</div>
						<div class="font-bold text-black bg-yellow-200 rounded-full shadow h-5 ml-1 -mt-3 px-1.5 pt-0.5" v-if="numWarnings">{{ numWarnings }}</div>
					</template>
				</template>
			</svws-ui-tab-bar>
		</nav>
		<svws-ui-sub-nav />
	</Teleport>
</template>

<script setup lang="ts">

	import { GostHalbjahr } from "@core";
	import type { GostKlausurplanungAuswahlProps } from './SGostKlausurplanungAuswahlProps';
	import { computed } from 'vue';

	const props = defineProps<GostKlausurplanungAuswahlProps>();

	const numErrors = computed<number>(() => props.kMan().planungsfehlerGetAnzahlByHalbjahrAndQuartal(props.jahrgangsdaten!.abiturjahr, props.halbjahr, props.quartalsauswahl.value));
	const numWarnings = computed<number>(() => props.kMan().planungshinweiseGetAnzahlByHalbjahrAndQuartal(props.jahrgangsdaten!.abiturjahr, props.halbjahr, props.quartalsauswahl.value));

</script>