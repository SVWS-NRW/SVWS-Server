<template>
	<s-card-schueler-stundenplan v-if="visible" />
</template>

<script setup lang="ts">
import { computed, ComputedRef } from "vue";
import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.schueler;
const visible: ComputedRef<boolean> = computed<boolean>(() =>
	true
	// !!app.dataStundenplan?.daten
);

</script>