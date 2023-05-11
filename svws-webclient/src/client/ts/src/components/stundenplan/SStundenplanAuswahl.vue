<template>
	<svws-ui-secondary-menu>
		<template #headline>Stundenplan</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="secondary-menu--navigation">
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { StundenplanAuswahlProps } from "./SStundenplanAuswahlProps";

	const props = defineProps<StundenplanAuswahlProps>();

</script>
