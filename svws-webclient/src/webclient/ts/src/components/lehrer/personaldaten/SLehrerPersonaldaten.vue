<template>
	<div class="app-container">
		<s-card-lehrer-personaldaten-allgemein :personaldaten="personaldaten" />
		<s-card-lehrer-personaldaten-lehraemter :personaldaten="personaldaten" />
		<s-card-lehrer-personaldaten-beschaeftigung :personaldaten="personaldaten" />
		<s-card-lehrer-personaldaten-anrechnung :personaldaten="personaldaten" />
	</div>
</template>

<script setup lang="ts">

	import { DataLehrerPersonaldaten } from "~/apps/lehrer/DataLehrerPersonaldaten";

	const props = defineProps<{
		personaldaten: DataLehrerPersonaldaten;
	}>();

</script>
