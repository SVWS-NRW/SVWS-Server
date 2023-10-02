<template>
	<div class="h-full w-full grid gap-4 grid-cols-2">
		<s-schueler-lernabschnitt-halbjahr :manager="manager" :patch="patch" :schule="schule" />
		<s-schueler-lernabschnitt-zeugnis-abschluss :manager="manager" :patch="patch" :patch-bemerkungen="patchBemerkungen" />
		<s-schueler-lernabschnitt-abschluss-berechnung :manager="manager" :patch="patch" />
		<s-schueler-lernabschnitt-fehlzeiten :manager="manager" :patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerLernabschnittAllgemeinProps } from "./SSchuelerLernabschnittAllgemeinProps";

	const props = defineProps<SchuelerLernabschnittAllgemeinProps>();

</script>
