<template>
	<div v-show="visibleBenutzer" class="app-container">
		<s-card-benutzer-daten />
		<s-card-benutzer-kompetenzen />
	</div>
	<div v-show="visibleBenutzergruppe" class="app-container">
		<s-card-benutzergruppe-daten />
		<s-card-benutzergruppe-kompetenzen />
	</div>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";
	import { App } from "~/apps/BaseApp";

	const visibleBenutzer: ComputedRef<boolean> = computed(() => {
		return (App.apps.benutzer.auswahl.ausgewaehlt !== undefined);
	});

	const visibleBenutzergruppe: ComputedRef<boolean> = computed(() => {
		return (App.apps.benutzergruppe.auswahl.ausgewaehlt !== undefined);
	});

</script>
