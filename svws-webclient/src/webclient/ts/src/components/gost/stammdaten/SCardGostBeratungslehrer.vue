<template>
	<svws-ui-content-card :title="'Beratungslehrer'">
		<div class="content-wrapper">
			<div class="input-wrapper">Hier kommen die Beratungslehrer hin</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.gost;
</script>
