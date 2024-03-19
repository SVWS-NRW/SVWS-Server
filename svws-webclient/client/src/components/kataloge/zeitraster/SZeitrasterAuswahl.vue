<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span title="Zeitraster">Zeitraster</span>
			</nav>
		</template>
		<template #abschnitt>
			<span class="text-base font-bold opacity-50 select-none">{{ aktAbschnitt.schuljahr + "." + aktAbschnitt.abschnitt }}</span>
		</template>
		<template #header />
		<template #content>
			<div class="container">
				Übersicht Zeitraster
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { ZeitrasterAuswahlProps } from "./SZeitrasterAuswahlProps";

	const props = defineProps<ZeitrasterAuswahlProps>();

</script>
