<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span class="line-clamp-2">Einstellungen</span>
		</template>
		<template #header />
		<template #content>
			<svws-ui-secondary-menu-navigation :tab-manager />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { EinstellungenAuswahlProps } from './EinstellungenAuswahlProps';

	const props = defineProps<EinstellungenAuswahlProps>();

</script>
