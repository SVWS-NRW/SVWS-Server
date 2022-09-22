<template>
	<svws-ui-content-card title="Besondere Merkmale für die Statistik">
		<div>
			<svws-ui-textarea-input
				v-model="inputMerkmale"
				placeholder="Merkmale"
				resizeable="vertical"
			/>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from "vue";

	const inputMerkmale = computed({
		get() {
			return "Hier werden später Statist-relevante Merkmale aufgelistet und sind veränderbar...";
			// return this.app.schulbesuchsdaten.daten.merkmale;
		},
		set(val) {
			val;
			// TODO es wird eine aufwendigere Komponente benötigt...
		}
	});
</script>
