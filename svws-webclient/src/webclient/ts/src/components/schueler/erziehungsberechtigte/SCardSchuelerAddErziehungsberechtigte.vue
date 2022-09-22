<template>
	<svws-ui-button>Erziehungsberechtigten hinzufügen</svws-ui-button>
</template>

<script setup lang="ts">
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;
</script>
