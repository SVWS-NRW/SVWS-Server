<template>
	<svws-ui-secondary-menu>
		<template #headline>Benutzerprofil</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { BenutzerprofilAuswahlProps } from "./SBenutzerprofilAuswahlProps";

	const props = defineProps<BenutzerprofilAuswahlProps>();

</script>
