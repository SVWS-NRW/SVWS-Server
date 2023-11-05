<template>
	<div class="page--content">
		<s-card-lehrer-personaldaten-allgemein :lehrer-liste-manager="lehrerListeManager" :patch="patch" :patch-abschnittsdaten="patchAbschnittsdaten" />
		<s-card-lehrer-personaldaten-lehraemter :lehrer-liste-manager="lehrerListeManager" :patch="patch" :patch-abschnittsdaten="patchAbschnittsdaten" />
		<s-card-lehrer-personaldaten-beschaeftigung :akt-abschnitt="aktAbschnitt" :lehrer-liste-manager="lehrerListeManager" :patch="patch" :patch-abschnittsdaten="patchAbschnittsdaten" />
		<s-card-lehrer-personaldaten-anrechnung :akt-abschnitt="aktAbschnitt" :lehrer-liste-manager="lehrerListeManager" :patch="patch" :patch-abschnittsdaten="patchAbschnittsdaten" />
	</div>
</template>

<script setup lang="ts">

	import type { LehrerPersonaldatenProps } from './SLehrerPersonaldatenProps';

	const props = defineProps<LehrerPersonaldatenProps>();

</script>
