<template>
	<svws-ui-secondary-menu>
		<template #headline>
			Schule
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<svws-ui-secondary-menu-navigation :tab-manager />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { SchuleAuswahlProps } from './SSchuleAuswahlProps';

	const props = defineProps<SchuleAuswahlProps>();

</script>
