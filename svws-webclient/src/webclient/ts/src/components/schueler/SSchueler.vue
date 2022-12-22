/* eslint-disable vue/no-parsing-error */
<template>
	<Teleport to="#auswahl">
		<s-schueler-auswahl />
	</Teleport>
	<Teleport to="#app">
		<s-schueler-app />
	</Teleport>
	<Teleport to="#gruppenaktionen">
		<s-schueler-gruppenaktionen />
	</Teleport>
</template>

<script setup lang="ts">
</script>
