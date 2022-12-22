<template>
	<svws-ui-content-card title="Alle bisher besuchten Schulen">
		<div>
			<svws-ui-textarea-input placeholder="Schulen" v-model="inputAlleSchulen" resizeable="vertical" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	const inputAlleSchulen = computed({
		get() {
			return "Hier werden später alle bisher besuchten Schulen aufgelistet und sind veränderbar...";
			// return this.app.schulbesuchsdaten.daten.alleSchulen;
		},
		set(val) {
			val;
			// TODO es wird eine aufwendigere Komponente benötigt...
		}
	});

</script>
