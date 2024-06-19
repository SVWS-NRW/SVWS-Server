<template>
	<div class="page--content">
		<s-lehrer-individualdaten-allgemein :lehrer-liste-manager :patch />
		<s-lehrer-individualdaten-kontakt :lehrer-liste-manager :patch :map-orte :map-ortsteile />
		<s-lehrer-individualdaten-leitungsfunktionen :lehrer-liste-manager />
	</div>
</template>

<script setup lang="ts">

	import type { LehrerIndividualdatenProps } from "./SLehrerIndividualdatenProps";

	const props = defineProps<LehrerIndividualdatenProps>();

</script>
