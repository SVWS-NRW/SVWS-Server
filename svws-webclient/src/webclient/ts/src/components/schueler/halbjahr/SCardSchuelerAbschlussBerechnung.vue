<template>
	<svws-ui-content-card title="Abschluss Berechnung">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="" v-model="inputAbschlussBerechnung" resizeable="vertical" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

</script>
